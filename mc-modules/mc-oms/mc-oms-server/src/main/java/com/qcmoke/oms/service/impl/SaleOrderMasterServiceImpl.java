package com.qcmoke.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.oms.client.BillClient;
import com.qcmoke.oms.client.ProductStockClient;
import com.qcmoke.oms.constant.*;
import com.qcmoke.oms.dto.ApplyForDeliveryDto;
import com.qcmoke.oms.dto.OrderMasterDto;
import com.qcmoke.oms.dto.SaleOrderMasterApiDto;
import com.qcmoke.oms.dto.SaleOrderMasterQuery;
import com.qcmoke.oms.entity.Product;
import com.qcmoke.oms.entity.SaleOrderDetail;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.entity.SaleRefund;
import com.qcmoke.oms.mapper.SaleOrderMasterMapper;
import com.qcmoke.oms.service.ProductService;
import com.qcmoke.oms.service.SaleOrderDetailService;
import com.qcmoke.oms.service.SaleOrderMasterService;
import com.qcmoke.oms.service.SaleRefundService;
import com.qcmoke.oms.vo.SaleOrderMasterVo;
import com.qcmoke.wms.constant.ItemType;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Service
public class SaleOrderMasterServiceImpl extends ServiceImpl<SaleOrderMasterMapper, SaleOrderMaster> implements SaleOrderMasterService {

    @Autowired
    private SaleOrderDetailService saleOrderDetailService;
    @Autowired
    private SaleOrderMasterService saleOrderMasterService;
    @Autowired
    private SaleOrderMasterMapper saleOrderMasterMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private SaleRefundService saleRefundService;

    @Autowired
    private ProductStockClient productStockClient;
    @Autowired
    private BillClient billClient;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkCallBackForCreateStockItem(StockType stockType, Long orderId, boolean isOk) {
        //销售发货
        if (StockType.SALE_OUT == stockType) {
            SaleOrderMaster master = this.getById(orderId);
            if (master == null) {
                throw new GlobalCommonException("不存在销售订单相关记录");
            }
            master.setModifyTime(new Date());
            if (isOk) {
                master.setStockCheckStatus(StockCheckStatusEnum.FINISH.getStatus());
            } else {
                master.setStockCheckStatus(StockCheckStatusEnum.FAIL.getStatus());
            }
            boolean flag = this.updateById(master);
            if (!flag) {
                throw new GlobalCommonException("修改状态失败");
            }
        }

        //销售退货
        if (StockType.SALE_IN.value() == stockType.value()) {
            SaleRefund saleRefund = saleRefundService.getById(orderId);
            if (saleRefund == null) {
                throw new GlobalCommonException("不存在销售退单相关记录");
            }
            if (isOk) {
                saleRefund.setStockCheckStatus(2);
            } else {
                saleRefund.setStockCheckStatus(3);
            }
            saleRefund.setModifyTime(new Date());
            boolean flag = saleRefundService.updateById(saleRefund);
            if (!flag) {
                throw new GlobalCommonException("修改状态失败");
            }
        }

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void successForOutItemFromStock(SaleOrderMasterApiDto orderMasterDto) {
        SaleOrderMaster master = this.getById(orderMasterDto.getMasterId());
        if (master == null) {
            throw new GlobalCommonException("不存在相关记录");
        }
        master.setModifyTime(new Date());
        master.setDeliveryTime(new Date());
        master.setDeliverySn(orderMasterDto.getDeliverySn());
        master.setDeliveryChannel(orderMasterDto.getDeliveryChannel());
        master.setOutStatus(OutStatusEnum.SHIPPED.value());
        boolean flag = this.updateById(master);
        if (!flag) {
            throw new GlobalCommonException("修改状态失败");
        }
    }

    @Override
    public IPage<SaleOrderMasterVo> getPage(Page<SaleOrderMaster> page, SaleOrderMasterQuery query) {
        return saleOrderMasterMapper.getPage(page, query);
    }


    /**
     * 创建或修改订单
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrUpdateSaleOrder(OrderMasterDto orderMasterDto) {
        SaleOrderMaster saleOrderMaster = BeanCopyUtil.copy(orderMasterDto, SaleOrderMaster.class);
        List<SaleOrderDetail> dtoDetails = orderMasterDto.getDetails();
        Boolean isPayStatus = orderMasterDto.getIsPayStatus();
        Double freightAmount = orderMasterDto.getFreightAmount();
        Long customerId = saleOrderMaster.getCustomerId();
        Long masterDtoId = saleOrderMaster.getMasterId();
        boolean isCreateOrder = masterDtoId == null;
        if (CollectionUtils.isEmpty(dtoDetails)
                || customerId == null
                || isPayStatus == null) {
            throw new GlobalCommonException("无效订单");
        }
        if (!isCreateOrder) {
            SaleOrderMaster orderMaster = this.getById(masterDtoId);
            if (orderMaster == null) {
                throw new GlobalCommonException("不存在修改的订单");
            }
            if (PayStatusEnum.PAID.value() == orderMaster.getPayStatus()) {
                throw new GlobalCommonException("该订单已完成支付，不允许修改");
            }
        }
        //计算总金额
        List<Product> productList = productService.listByIds(dtoDetails.stream().map(SaleOrderDetail::getProductId).collect(Collectors.toList()));
        AtomicReference<Double> orderAmountTotal = new AtomicReference<>(0.0);
        dtoDetails.forEach(detail -> {
            Long productId = detail.getProductId();
            Double count = detail.getCount();
            if (productId == null || count == null) {
                throw new GlobalCommonException("详细存在无效记录");
            }
            Product product = (Product) CollectionUtils.find(productList, object -> ((Product) object).getProductId().equals(productId));
            orderAmountTotal.updateAndGet(v -> v + product.getPrice() * count);
        });
        //创建订单主表
        if (isCreateOrder) {
            saleOrderMaster.setCreateTime(new Date()).setSaleDate(new Date());
        } else {
            saleOrderMaster.setModifyTime(new Date());
        }
        saleOrderMaster.setTotalAmount(orderAmountTotal.get());
        if (isPayStatus) {
            if (freightAmount == null) {
                throw new GlobalCommonException("运费为空！");
            }
            saleOrderMaster.setPayStatus(PayStatusEnum.PAID.value());
            saleOrderMaster.setPaymentTime(new Date());
        } else {
            saleOrderMaster.setPayStatus(PayStatusEnum.NO_PAID.value());
        }
        boolean flag = saleOrderMasterService.saveOrUpdate(saleOrderMaster);
        if (!flag) {
            throw new GlobalCommonException("更新订单失败");
        }

        if (isPayStatus) {
            Double orderAmountTotalValue = orderAmountTotal.get();
            Double billAmountTotal = orderAmountTotalValue + freightAmount;
            //将收入记录到财务账单
            BillApiDto billApiDto = new BillApiDto()
                    .setDealNum(saleOrderMaster.getMasterId())
                    .setDealType(DealType.SALE_IN)
                    .setPayType(PayType.valueOf(saleOrderMaster.getPayType()))
                    .setTotalAmount(billAmountTotal);
            Result<?> result = billClient.addBill(billApiDto);
            if (result.isError()) {
                throw new GlobalCommonException("收入记录到财务账单失败");
            }
        }

        //更新相关明细
        saleOrderDetailService.remove(new LambdaQueryWrapper<SaleOrderDetail>().eq(SaleOrderDetail::getMasterId, masterDtoId));
        dtoDetails.forEach(detail -> {
            if (isCreateOrder) {
                Long masterId = saleOrderMaster.getMasterId();
                detail
                        .setCreateTime(new Date())
                        .setMasterId(masterId);
            } else {
                detail
                        .setModifyTime(new Date())
                        .setMasterId(masterDtoId);
            }
        });
        flag = saleOrderDetailService.saveBatch(dtoDetails);
        if (!flag) {
            throw new GlobalCommonException("更新明细失败");
        }
    }


    /**
     * 提交发货申请单
     */
    @Override
    public void applyForDelivery(ApplyForDeliveryDto applyForDeliveryDto) {
        Long masterId = applyForDeliveryDto.getMasterId();
        SaleOrderMaster orderMaster = saleOrderMasterService.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在该订单");
        }
        if (PayStatusEnum.PAID.value() != orderMaster.getPayStatus()) {
            throw new GlobalCommonException("该订未支付,无法申请发货");
        }
        if (OutStatusEnum.SHIPPED.value() == orderMaster.getOutStatus()) {
            throw new GlobalCommonException("该订已经发货完成，无需再申请发货");
        }
        createStockPreReview(masterId);
        orderMaster.setModifyTime(new Date());
        orderMaster.setStockCheckStatus(StockCheckStatusEnum.ALREADY_TRANSFER.getStatus());
        boolean flag = saleOrderMasterService.updateById(orderMaster);
        if (!flag) {
            throw new GlobalCommonException("申请发货失败！");
        }
    }


    /**
     * @param masterId 已经验证存在的主表id
     */
    private void createStockPreReview(Long masterId) {
        List<SaleOrderDetail> details = saleOrderDetailService.list(new LambdaQueryWrapper<SaleOrderDetail>().eq(SaleOrderDetail::getMasterId, masterId));
        if (CollectionUtils.isEmpty(details)) {
            throw new GlobalCommonException("该订单不存在明细");
        }
        List<StockItemDetailDto> itemDetailDtoList = new ArrayList<>();
        details.forEach(detail -> {
            StockItemDetailDto stockItemDetailDto = new StockItemDetailDto();
            stockItemDetailDto.setItemId(detail.getProductId());
            stockItemDetailDto.setItemNum(detail.getCount());
            stockItemDetailDto.setStockItemId(masterId);
            itemDetailDtoList.add(stockItemDetailDto);
        });
        //生成库存出库预审核表
        StockItemDto stockItemDto = new StockItemDto()
                .setStockType(StockType.SALE_OUT)
                .setItemType(ItemType.PRODUCT)
                .setDealId(masterId)
                .setStockItemDetailDtoList(itemDetailDtoList);
        Result<?> result = productStockClient.createStockPreReview(stockItemDto);
        if (HttpStatus.OK.value() != result.getStatus()) {
            throw new GlobalCommonException(result.getMessage());
        }
    }

    /**
     * 只有未支付的订单才能删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdList(List<Long> idList) {
        List<SaleOrderMaster> masterList = this.list(new LambdaQueryWrapper<SaleOrderMaster>().in(SaleOrderMaster::getMasterId, idList));
        if (CollectionUtils.isEmpty(masterList)) {
            throw new GlobalCommonException("不存在要删除的订单！");
        }
        masterList.forEach(master -> {
            if (PayStatusEnum.PAID.value() == master.getPayStatus()) {
                throw new GlobalCommonException("包含已完成支付的订单，不允许删除！");
            }
        });
        //删除主表记录
        boolean flag = this.removeByIds(idList);
        if (!flag) {
            throw new GlobalCommonException("订单删除失败");
        }
        //删除明细记录
        flag = saleOrderDetailService.remove(new LambdaQueryWrapper<SaleOrderDetail>().in(SaleOrderDetail::getMasterId, idList));
        if (!flag) {
            throw new GlobalCommonException("订单明细删除失败");
        }
    }


    /**
     * 确认收货处理
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void confirmUserReceipt(SaleOrderMaster willUpdateOrderMaster, Boolean isReceived) {
        SaleOrderMaster orderMaster = saleOrderMasterService.getById(willUpdateOrderMaster.getMasterId());
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在该订单！");
        }
        if (FinishStatusEnum.FINISHED.value() == orderMaster.getFinishStatus()) {
            throw new GlobalCommonException("该订已经完成，不能再修改！");
        }

        if (ReceiveStatusEnum.FINISHED.value() == orderMaster.getReceiveStatus()) {
            throw new GlobalCommonException("该订单已经确认收货，无需重复操作！");
        }
        if (isReceived) {
            if (OutStatusEnum.SHIPPED.value() != orderMaster.getOutStatus()) {
                throw new GlobalCommonException("该订单还未发货！");
            }
            willUpdateOrderMaster.setReceiveStatus(ReceiveStatusEnum.FINISHED.value());
            willUpdateOrderMaster.setFinishStatus(FinishStatusEnum.FINISHED.value());
        }
        willUpdateOrderMaster.setModifyTime(new Date());
        boolean flag = saleOrderMasterService.updateById(willUpdateOrderMaster);
        if (!flag) {
            throw new GlobalCommonException("修改失败！");
        }
    }
}
