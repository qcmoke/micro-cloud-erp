package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.pms.client.BillClient;
import com.qcmoke.pms.client.MaterialStockClient;
import com.qcmoke.pms.client.UserClient;
import com.qcmoke.pms.constant.OrderStatusEnum;
import com.qcmoke.pms.constant.PayStatusEnum;
import com.qcmoke.pms.constant.PayTypeEnum;
import com.qcmoke.pms.constant.TransferStockStatusEnum;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.entity.Supplier;
import com.qcmoke.pms.mapper.PurchaseOrderMasterMapper;
import com.qcmoke.pms.service.MaterialService;
import com.qcmoke.pms.service.PurchaseOrderDetailService;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import com.qcmoke.pms.service.SupplierService;
import com.qcmoke.pms.utils.UserClientUtil;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
import com.qcmoke.ums.vo.UserVo;
import com.qcmoke.wms.constant.ItemType;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Slf4j
@Service
public class PurchaseOrderMasterServiceImpl extends ServiceImpl<PurchaseOrderMasterMapper, PurchaseOrderMaster> implements PurchaseOrderMasterService {

    @Autowired
    private PurchaseOrderMasterMapper purchaseOrderMasterMapper;
    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;
    @Autowired
    private MaterialStockClient materialStockClient;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private BillClient billClient;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void successForInItemToStock(List<Long> masterIdList) {
        List<PurchaseOrderMaster> masterList = this.listByIds(masterIdList);
        if (CollectionUtils.isEmpty(masterList)) {
            throw new GlobalCommonException("不存在相关记录");
        }
        masterList.forEach(master -> {
            master.setModifyTime(new Date());
            master.setStatus(OrderStatusEnum.STOCKED.getStatus());
        });
        boolean flag = this.updateBatchById(masterList);
        if (!flag) {
            throw new GlobalCommonException("修改状态失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkCallBackForCreateStockPreReview(Long orderId, boolean isOk) {
        PurchaseOrderMaster master = this.getById(orderId);
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
    public void deleteByIdList(List<Long> masterIdList) {
        List<PurchaseOrderMaster> masterList = this.listByIds(masterIdList);
        boolean flag = masterList.stream()
                .anyMatch(master -> master.getPayStatus() == PayStatusEnum.PAID.value());
        if (flag) {
            throw new GlobalCommonException("包含已付款的订单，不允许删除！");
        }
        flag = this.removeByIds(masterIdList);
        if (!flag) {
            throw new GlobalCommonException("订单删除失败");
        }
        flag = purchaseOrderDetailService.remove(new LambdaQueryWrapper<PurchaseOrderDetail>().in(PurchaseOrderDetail::getMasterId, masterIdList));
        if (!flag) {
            throw new GlobalCommonException("订单明细删除失败");
        }
    }


    /**
     * 创建或修改采购订单
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrUpdatePurchaseOrder(PurchaseOrderMasterDto purchaseOrderMasterDto, Long currentUserId) {
        Long masterDtoId = purchaseOrderMasterDto.getMasterId();
        Long supplierId = purchaseOrderMasterDto.getSupplierId();
        Integer payType = purchaseOrderMasterDto.getPayType();
        Double freight = purchaseOrderMasterDto.getFreight();
        List<PurchaseOrderDetail> detailDtoList = purchaseOrderMasterDto.getPurchaseOrderDetailList();
        Boolean orderIsPaid = purchaseOrderMasterDto.getIsPayStatus();
        if (CollectionUtils.isEmpty(detailDtoList)
                || orderIsPaid == null
                || supplierId == null) {
            throw new GlobalCommonException("无效订单（detailDtoList、orderIsPaid supplierId are required）");
        }
        if (orderIsPaid) {
            if (payType == null) {
                throw new GlobalCommonException("未选择支付方式");
            }
            if (PayTypeEnum.notContains(payType)) {
                throw new GlobalCommonException("不支持该支付方式");
            }
            if (freight == null) {
                throw new GlobalCommonException("未填写运费金额");
            }
        }
        Supplier supplier = supplierService.getById(supplierId);
        if (supplier == null) {
            throw new GlobalCommonException("系统目前不存在该供应商");
        }
        boolean isCreateOrder = masterDtoId == null;
        if (!isCreateOrder) {
            PurchaseOrderMaster orderMaster = this.getById(masterDtoId);
            if (orderMaster == null) {
                throw new GlobalCommonException("不存在修改的订单");
            }
            if (orderMaster.getStatus() >= OrderStatusEnum.PASS.getStatus()) {
                throw new GlobalCommonException("该订单已审核通过，不允许修改！");
            }
        }

        /*计算订单总金额（从明细里获取物料id集合，并从物料表中获取物料的价格进行计算得到订单总能金额）*/
        List<Long> materialIds = detailDtoList.stream().map(PurchaseOrderDetail::getMaterialId).collect(Collectors.toList());
        List<Material> materialList = materialService.listByIds(materialIds);
        AtomicReference<Double> amountTotal = new AtomicReference<>(0.0);
        detailDtoList.forEach(detail -> {
            Long materialId = detail.getMaterialId();
            Material material = (Material) CollectionUtils.find(materialList, object -> ((Material) object).getMaterialId().equals(materialId));
            if (material != null) {
                amountTotal.updateAndGet(v -> v + material.getPrice() * detail.getCount());
            }
        });

        //更新订单主表
        PurchaseOrderMaster purchaseOrderMaster = BeanCopyUtil.copy(purchaseOrderMasterDto, PurchaseOrderMaster.class);
        if (isCreateOrder) {
            purchaseOrderMaster.setCreateTime(new Date()).setPurchaseDate(new Date());
        } else {
            purchaseOrderMaster.setModifyTime(new Date());
        }
        if (orderIsPaid) {
            purchaseOrderMaster.setPayStatus(PayStatusEnum.PAID.value());
        } else {
            purchaseOrderMaster.setPayStatus(PayStatusEnum.NO_PAID.value());
        }

        purchaseOrderMaster.setTotalAmount(amountTotal.get());
        purchaseOrderMaster.setOperatorId(OauthSecurityJwtUtil.getCurrentUserId());
        boolean flag = this.saveOrUpdate(purchaseOrderMaster);
        if (!flag) {
            throw new GlobalCommonException("创建或修改订单失败");
        }

        //更新相关明细
        purchaseOrderDetailService.remove(new LambdaQueryWrapper<PurchaseOrderDetail>().eq(PurchaseOrderDetail::getMasterId, masterDtoId));
        detailDtoList.forEach(detail -> {
            Long masterId = purchaseOrderMaster.getMasterId();
            detail.setCreateTime(new Date()).setMasterId(masterId);
            detail.setModifyTime(new Date()).setMasterId(purchaseOrderMaster.getMasterId());
        });
        flag = purchaseOrderDetailService.saveBatch(detailDtoList);
        if (!flag) {
            throw new GlobalCommonException("创建或修改采明细失败");
        }
    }


    private PageResult<PurchaseOrderMasterVo> buildPageResult(List<PurchaseOrderMasterVo> records, Long total) {
        Set<Long> idSet = records.stream().map(PurchaseOrderMasterVo::getOperatorId).filter(Objects::nonNull).collect(Collectors.toSet());
        List<UserVo> userVoList = UserClientUtil.getUserVoList(idSet, userClient);
        records.forEach(purchaseOrderMasterVo -> {
            Long operatorId = purchaseOrderMasterVo.getOperatorId();
            UserVo user = (UserVo) CollectionUtils.find(userVoList, object -> ((UserVo) object).getUserId().equals(operatorId));
            if (user != null) {
                purchaseOrderMasterVo.setOperator(user.getUsername());
            }
        });
        PageResult<PurchaseOrderMasterVo> pageResult = new PageResult<>();
        pageResult.setRows(records);
        pageResult.setTotal(total);
        return pageResult;
    }

    @Override
    public PageResult<PurchaseOrderMasterVo> pageForAddStock(Page<PurchaseOrderMaster> page, PurchaseOrderMaster purchaseOrderMaster) {
        IPage<PurchaseOrderMasterVo> iPage = purchaseOrderMasterMapper.selectPurchaseOrderMasterVoPageForAddStock(page);
        return buildPageResult(iPage.getRecords(), iPage.getTotal());
    }

    @Override
    public PageResult<PurchaseOrderMasterVo> getPage(Page<PurchaseOrderMaster> page, PurchaseOrderMaster purchaseOrderMaster) {
        IPage<PurchaseOrderMasterVo> iPage = purchaseOrderMasterMapper.selectPurchaseOrderMasterVoPage(page);
        return buildPageResult(iPage.getRecords(), iPage.getTotal());
    }

    /**
     * 生成出入库单和财务账单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferToStock(Long masterId, Long currentUserId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在对应的订单！");
        }
        if (orderMaster.getStatus() == null || orderMaster.getStatus() < OrderStatusEnum.PASS.getStatus()) {
            throw new GlobalCommonException("审核未通过不能移交仓库生成出入库单！");
        }
        if (TransferStockStatusEnum.ALREADY_TRANSFER.getStatus() == orderMaster.getTransferStockStatus()) {
            throw new GlobalCommonException("改订单已经移交仓库，不能重复！");
        }


        //将支出记录到财务账单
        Double orderAmountTotalValue = orderMaster.getTotalAmount();
        Double freight = orderMaster.getFreight();
        Double billAmountTotal = orderAmountTotalValue + orderAmountTotalValue + freight;
        BillApiDto billApiDto = new BillApiDto()
                .setDealNum(orderMaster.getMasterId())
                .setDealType(DealType.PURCHASE_OUT)
                .setPayType(PayType.valueOf(orderMaster.getPayType()))
                .setTotalAmount(billAmountTotal);
        Result<?> billResult = billClient.addBill(billApiDto);
        if (billResult.isError()) {
            throw new GlobalCommonException("支出记录到财务账单失败");
        }


        //将采购订单对应的明细物料加到移交明细单里
        List<PurchaseOrderDetail> details = purchaseOrderDetailService.list(new LambdaQueryWrapper<PurchaseOrderDetail>().eq(PurchaseOrderDetail::getMasterId, orderMaster.getMasterId()));
        List<StockItemDetailDto> stockItemDetailDtoList = new ArrayList<>();
        details.forEach(detail -> {
            StockItemDetailDto stockDto = new StockItemDetailDto();
            stockDto.setItemId(detail.getMaterialId());
            stockDto.setItemNum(detail.getCount());
            stockItemDetailDtoList.add(stockDto);
        });

        if (CollectionUtils.isEmpty(stockItemDetailDtoList)) {
            throw new GlobalCommonException("该订单没有可移交的物料！请注意审核订单明细！");
        }
        StockItemDto stockItemDto = new StockItemDto();
        stockItemDto.setOrderId(masterId);
        stockItemDto.setItemType(ItemType.MATERIAL);
        stockItemDto.setStockType(StockType.PURCHASE_IN);
        stockItemDto.setStockItemDetailDtoList(stockItemDetailDtoList);
        Result<?> result = materialStockClient.createStockPreReview(stockItemDto);
        if (HttpStatus.OK.value() != result.getStatus()) {
            throw new GlobalCommonException("移交失败！");
        }

        //修改订单为已移交
        orderMaster.setMasterId(masterId);
        orderMaster.setOperatorId(currentUserId);
        orderMaster.setTransferStockStatus(TransferStockStatusEnum.ALREADY_TRANSFER.getStatus());
        orderMaster.setStatus(OrderStatusEnum.NOT_STOCKED.getStatus());
        orderMaster.setModifyTime(new Date());
        boolean flag = this.updateById(orderMaster);
        if (!flag) {
            throw new GlobalCommonException("修改订单状态失败");
        }
    }


    private void checkPaid(PurchaseOrderMaster orderMaster) {
        if (orderMaster.getPayStatus() != PayStatusEnum.PAID.value()
                || orderMaster.getTotalAmount() == null
                || orderMaster.getFreight() == null) {
            throw new GlobalCommonException("该订单未支付完成！");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean toApplyCheck(Long masterId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("没有对应的记录");
        }
        checkPaid(orderMaster);
        return updateStatus(orderMaster, OrderStatusEnum.APPLY_BUT_NO_CHECK.getStatus());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean checkPass(Long masterId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("没有对应的记录");
        }
        checkPaid(orderMaster);
        return updateStatus(orderMaster, OrderStatusEnum.PASS.getStatus());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean checkFail(Long masterId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("没有对应的记录");
        }
        return updateStatus(orderMaster, OrderStatusEnum.NO_PASS.getStatus());
    }

    private boolean updateStatus(PurchaseOrderMaster orderMaster, int willStatus) {
        //如果是申请状态，那么状态只能改为不通过或者通过两个状态,而不能越级
        if (OrderStatusEnum.NO_APPLY.getStatus() == orderMaster.getStatus() && willStatus > OrderStatusEnum.PASS.getStatus()) {
            throw new GlobalCommonException("操作越级，非法！");
        }
        if (TransferStockStatusEnum.ALREADY_TRANSFER.getStatus() == orderMaster.getTransferStockStatus()) {
            throw new GlobalCommonException("改订单已经移交仓库，不能再进行修改！");
        }

        orderMaster.setModifyTime(new Date());
        orderMaster.setStatus(willStatus);
        orderMaster.setOperatorId(OauthSecurityJwtUtil.getCurrentUserId());
        orderMaster.setTransferStockStatus(TransferStockStatusEnum.NO_TRANSFER.getStatus());
        return this.updateById(orderMaster);
    }

}
