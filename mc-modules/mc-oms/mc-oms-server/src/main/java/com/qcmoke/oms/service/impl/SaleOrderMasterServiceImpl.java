package com.qcmoke.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.client.ProductStockClient;
import com.qcmoke.oms.constant.OrderStatusEnum;
import com.qcmoke.oms.constant.TransferStockStatusEnum;
import com.qcmoke.oms.dto.ApplyForDeliveryDto;
import com.qcmoke.oms.dto.OrderMasterDto;
import com.qcmoke.oms.dto.SaleOrderMasterApiDto;
import com.qcmoke.oms.entity.Product;
import com.qcmoke.oms.entity.SaleOrderDetail;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.mapper.SaleOrderDetailMapper;
import com.qcmoke.oms.mapper.SaleOrderMasterMapper;
import com.qcmoke.oms.service.ProductService;
import com.qcmoke.oms.service.SaleOrderDetailService;
import com.qcmoke.oms.service.SaleOrderMasterService;
import com.qcmoke.oms.vo.SaleOrderMasterVo;
import com.qcmoke.wms.constant.ItemType;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
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
    private ProductStockClient productStockClient;
    @Autowired
    private SaleOrderDetailService saleOrderDetailService;
    @Autowired
    private SaleOrderMasterService saleOrderMasterService;
    @Autowired
    private SaleOrderMasterMapper saleOrderMasterMapper;
    @Autowired
    private SaleOrderDetailMapper saleOrderDetailMapper;
    @Autowired
    private ProductService productService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkCallBackForCreateStockPreReview(Long orderId, boolean isOk) {
        SaleOrderMaster master = this.getById(orderId);
        if (master == null) {
            throw new GlobalCommonException("不存在相关记录");
        }
        master.setModifyTime(new Date());
        if (isOk) {
            master.setTransferStockStatus(TransferStockStatusEnum.FINISH.getStatus());
        } else {
            master.setTransferStockStatus(TransferStockStatusEnum.FAIL.getStatus());
        }
        boolean flag = this.updateById(master);
        if (!flag) {
            throw new GlobalCommonException("修改状态失败");
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
        master.setStatus(OrderStatusEnum.SHIPPED.value());
        boolean flag = this.updateById(master);
        if (!flag) {
            throw new GlobalCommonException("修改状态失败");
        }
    }

    @Override
    public IPage<SaleOrderMasterVo> getPage(Page<SaleOrderMaster> page, OrderMasterDto orderMasterDto) {
        return saleOrderMasterMapper.getPage(page, orderMasterDto);
    }


    /**
     * 创建或修改订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrUpdateSaleOrder(OrderMasterDto orderMasterDto) {
        SaleOrderMaster saleOrderMaster = BeanCopyUtil.copy(orderMasterDto, SaleOrderMaster.class);
        List<SaleOrderDetail> dtoDetails = orderMasterDto.getDetails();
        Boolean isPayStatus = orderMasterDto.getIsPayStatus();
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
            OrderStatusEnum currentOrderStatus = OrderStatusEnum.valueOf(orderMaster.getStatus());
            if (OrderStatusEnum.isMoreAndEqOther(currentOrderStatus, OrderStatusEnum.PAID_NO_SHIPPED)) {
                throw new GlobalCommonException("该订单已完成支付，不允许修改");
            }
        }
        //计算总金额
        List<Product> productList = productService.listByIds(dtoDetails.stream().map(SaleOrderDetail::getProductId).collect(Collectors.toList()));
        AtomicReference<Double> amountTotal = new AtomicReference<>(0.0);
        dtoDetails.forEach(detail -> {
            Long productId = detail.getProductId();
            Double count = detail.getCount();
            if (productId == null || count == null) {
                throw new GlobalCommonException("详细存在无效记录");
            }
            Product product = (Product) CollectionUtils.find(productList, object -> ((Product) object).getProductId().equals(productId));
            amountTotal.updateAndGet(v -> v + product.getPrice() * count);
        });
        //创建订单主表
        if (isCreateOrder) {
            saleOrderMaster.setCreateTime(new Date()).setSaleDate(new Date());
        } else {
            saleOrderMaster.setModifyTime(new Date());
        }
        saleOrderMaster.setTotalAmount(amountTotal.get());
        if (isPayStatus) {
            saleOrderMaster.setStatus(OrderStatusEnum.PAID_NO_SHIPPED.value());
            saleOrderMaster.setPaymentTime(new Date());
        } else {
            saleOrderMaster.setStatus(OrderStatusEnum.NO_PAY.value());
        }
        boolean flag = saleOrderMasterService.saveOrUpdate(saleOrderMaster);
        if (!flag) {
            throw new GlobalCommonException("更新订单失败");
        }
        //更新相关明细
        saleOrderDetailMapper.delete(new LambdaQueryWrapper<SaleOrderDetail>().eq(SaleOrderDetail::getMasterId, masterDtoId));
        dtoDetails.forEach(detail -> {
            if (isCreateOrder) {
                Long masterId = saleOrderMaster.getMasterId();
                detail.setCreateTime(new Date()).setMasterId(masterId);
            } else {
                detail.setModifyTime(new Date()).setMasterId(masterDtoId);
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
        OrderStatusEnum currentOrderStatus = OrderStatusEnum.valueOf(orderMaster.getStatus());
        if (OrderStatusEnum.isLessAndEqOther(currentOrderStatus, OrderStatusEnum.NO_PAY)) {
            throw new GlobalCommonException("该订未支付,无法申请发货");
        }
        if (OrderStatusEnum.isMoreAndEqOther(currentOrderStatus, OrderStatusEnum.SHIPPED)) {
            throw new GlobalCommonException("该订已经发货或者完成，无需再申请发货");
        }
        createStockPreReview(masterId);
        orderMaster.setModifyTime(new Date());
        orderMaster.setTransferStockStatus(TransferStockStatusEnum.ALREADY_TRANSFER.getStatus());
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
                .setOrderId(masterId)
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
            OrderStatusEnum currentOrderStatusEnum = OrderStatusEnum.valueOf(master.getStatus());
            if (OrderStatusEnum.isMoreAndEqOther(currentOrderStatusEnum, OrderStatusEnum.PAID_NO_SHIPPED)) {
                throw new GlobalCommonException("包含已完成支付的订单，不允许删除！");
            }
        });
        boolean flag = this.removeByIds(idList);
        if (!flag) {
            throw new GlobalCommonException("订单删除失败");
        }
        flag = saleOrderDetailService.remove(new LambdaQueryWrapper<SaleOrderDetail>().in(SaleOrderDetail::getMasterId, idList));
        if (!flag) {
            throw new GlobalCommonException("订单明细删除失败");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserDelivery(SaleOrderMaster willUpdateOrderMaster, Boolean isReceived) {
        Long willUpdateMasterId = willUpdateOrderMaster.getMasterId();
        SaleOrderMaster orderMaster = saleOrderMasterService.getById(willUpdateMasterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在该订单！");
        }
        OrderStatusEnum currentOrderStatus = OrderStatusEnum.resolve(orderMaster.getStatus());
        OrderStatusEnum willOrderStatus = OrderStatusEnum.RECEIVED;
        if (OrderStatusEnum.isMoreAndEqOther(currentOrderStatus, OrderStatusEnum.FINISHED)) {
            throw new GlobalCommonException("该订已经完成，不能再修改！");
        }

        if (currentOrderStatus == OrderStatusEnum.RECEIVED) {
            throw new GlobalCommonException("该订单已经确认收货，无需重复操作！");
        }
        if (isReceived) {
            if (OrderStatusEnum.isLessAndEqOther(currentOrderStatus, OrderStatusEnum.PAID_NO_SHIPPED)) {
                throw new GlobalCommonException("该订单还未发货！");
            }
            willUpdateOrderMaster.setStatus(willOrderStatus.value());
        }
        willUpdateOrderMaster.setModifyTime(new Date());
        boolean flag = saleOrderMasterService.updateById(willUpdateOrderMaster);
        if (!flag) {
            throw new GlobalCommonException("修改失败！");
        }
    }
}
