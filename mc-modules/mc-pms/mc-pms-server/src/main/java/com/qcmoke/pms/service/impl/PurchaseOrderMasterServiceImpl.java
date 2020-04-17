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
import com.qcmoke.pms.constant.*;
import com.qcmoke.pms.dto.PurchaseOrderMasterApiDto;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.dto.PurchaseOrderMasterQuery;
import com.qcmoke.pms.entity.*;
import com.qcmoke.pms.mapper.PurchaseOrderMasterMapper;
import com.qcmoke.pms.service.*;
import com.qcmoke.pms.utils.UserClientUtil;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
import com.qcmoke.ums.vo.UserVo;
import com.qcmoke.wms.constant.ItemType;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import io.seata.core.context.RootContext;
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
    private MaterialRefundService materialRefundService;
    @Autowired
    private UserClient userClient;
    @Autowired
    private BillClient billClient;


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

        String xid = RootContext.getXID();
        log.info("xid={}", xid);


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
            if (PurchaseCheckStatusEnum.PASS.value() == orderMaster.getPurchaseCheckStatus()) {
                throw new GlobalCommonException("该订单已经采购管理员审核通过，不允许修改！");
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
            PurchaseOrderMaster orderMaster = this.getById(masterDtoId);
            if (orderMaster == null) {
                throw new GlobalCommonException("不存在修改的订单");
            }
            if (PayStatusEnum.PAID.value() == orderMaster.getPayStatus()) {
                throw new GlobalCommonException("该订单已完成支付，不允许修改");
            }
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

        //将收入记录到财务账单
        if (orderIsPaid) {
            Double orderAmountTotalValue = amountTotal.get();
            Double billAmountTotal = orderAmountTotalValue + freight;
            BillApiDto billApiDto = new BillApiDto()
                    .setDealNum(purchaseOrderMaster.getMasterId())
                    .setDealType(DealType.PURCHASE_OUT)
                    .setPayType(PayType.valueOf(purchaseOrderMaster.getPayType()))
                    .setTotalAmount(billAmountTotal);
            Result<?> result = billClient.addBill(billApiDto);
            if (result.isError()) {
                throw new GlobalCommonException(result.getMessage());
            }
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
    public PageResult<PurchaseOrderMasterVo> getPage(Page<PurchaseOrderMaster> page, PurchaseOrderMasterQuery query) {
        IPage<PurchaseOrderMasterVo> iPage = purchaseOrderMasterMapper.selectPurchaseOrderMasterVoPage(page, query);
        return buildPageResult(iPage.getRecords(), iPage.getTotal());
    }

    /**
     * 生成出入库单
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void applyToStock(Long masterId, Long currentUserId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在对应的订单！");
        }
        if (PurchaseCheckStatusEnum.PASS.value() != orderMaster.getPurchaseCheckStatus()) {
            throw new GlobalCommonException("审核未通过不能移交仓库生成出入库单！");
        }
        if (StockCheckStatusEnum.APPLIED_BUT_NO_CHECK.getStatus() == orderMaster.getStockCheckStatus()) {
            throw new GlobalCommonException("改订单已经提交仓库，不能重复！");
        }


//        //将支出记录到财务账单
//        Double orderAmountTotalValue = orderMaster.getTotalAmount();
//        Double freight = orderMaster.getFreight();
//        Double billAmountTotal = orderAmountTotalValue + orderAmountTotalValue + freight;
//        BillApiDto billApiDto = new BillApiDto()
//                .setDealNum(orderMaster.getMasterId())
//                .setDealType(DealType.PURCHASE_OUT)
//                .setPayType(PayType.valueOf(orderMaster.getPayType()))
//                .setTotalAmount(billAmountTotal);
//        Result<?> billResult = billClient.addBill(billApiDto);
//        if (billResult.isError()) {
//            throw new GlobalCommonException("支出记录到财务账单失败,e=" + billResult.getMessage());
//        }


        //将采购订单对应的明细物料加到入库单明细单里
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
        stockItemDto.setDealId(masterId);
        stockItemDto.setItemType(ItemType.MATERIAL);
        stockItemDto.setStockType(StockType.PURCHASE_IN);
        stockItemDto.setStockItemDetailDtoList(stockItemDetailDtoList);
        Result<?> result = materialStockClient.createStockPreReview(stockItemDto);
        if (HttpStatus.OK.value() != result.getStatus()) {
            throw new GlobalCommonException(result.getMessage());
        }

        //修改订单为已提交仓库审核
        orderMaster.setMasterId(masterId);
        orderMaster.setOperatorId(currentUserId);
        orderMaster.setStockCheckStatus(StockCheckStatusEnum.APPLIED_BUT_NO_CHECK.getStatus());
        orderMaster.setInStatus(InStatusEnum.NOT_SHIPPED.value());
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
        if (PurchaseCheckStatusEnum.PASS.value() == orderMaster.getPurchaseCheckStatus()) {
            throw new GlobalCommonException("该订单已经审核通过，不能再申请");
        }
        return updateStatusForCheck(orderMaster, PurchaseCheckStatusEnum.APPLIED_BUT_NO_CHECK.value());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean checkPass(Long masterId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("没有对应的记录");
        }
        checkPaid(orderMaster);
        return updateStatusForCheck(orderMaster, PurchaseCheckStatusEnum.PASS.value());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean checkFail(Long masterId) {
        PurchaseOrderMaster orderMaster = this.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("没有对应的记录");
        }
        return updateStatusForCheck(orderMaster, PurchaseCheckStatusEnum.FAIL.value());
    }

    private boolean updateStatusForCheck(PurchaseOrderMaster orderMaster, int willStatus) {
        if (StockCheckStatusEnum.APPLIED_BUT_NO_CHECK.getStatus() == orderMaster.getStockCheckStatus()) {
            throw new GlobalCommonException("改订单已经提交仓库审核，不能再进行修改！");
        }

        orderMaster.setModifyTime(new Date());
        orderMaster.setPurchaseCheckStatus(willStatus);
        orderMaster.setOperatorId(OauthSecurityJwtUtil.getCurrentUserId());
        orderMaster.setStockCheckStatus(StockCheckStatusEnum.NO_APPLY.getStatus());
        return this.updateById(orderMaster);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void successForInItemToStock(List<Long> masterIdList) {
        List<PurchaseOrderMaster> masterList = this.listByIds(masterIdList);
        if (CollectionUtils.isEmpty(masterList)) {
            throw new GlobalCommonException("不存在相关记录");
        }
        masterList.forEach(master -> {
            master.setModifyTime(new Date());
            master.setInStatus(InStatusEnum.SHIPPED.value());
            master.setFinishStatus(FinishStatusEnum.FINISHED.value());
        });
        boolean flag = this.updateBatchById(masterList);
        if (!flag) {
            throw new GlobalCommonException("修改订单状态失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void successForOutItemFromStock(PurchaseOrderMasterApiDto purchaseOrderMasterApiDto) {
        Long masterId = purchaseOrderMasterApiDto.getMasterId();
        MaterialRefund refund = materialRefundService.getById(masterId);
        if (refund == null) {
            throw new GlobalCommonException("不存在相关退单记录");
        }
        refund.setStockOutStatus(2);
        refund.setModifyTime(new Date());
        boolean flag = materialRefundService.updateById(refund);
        if (!flag) {
            throw new GlobalCommonException("修改退单记录状态失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkCallBackForCreateStockItem(StockType stockType, Long orderId, boolean isOk) {
        if (StockType.PURCHASE_IN == stockType) {
            PurchaseOrderMaster master = this.getById(orderId);
            if (master == null) {
                throw new GlobalCommonException("不存在相关记录");
            }
            if (isOk) {
                master.setStockCheckStatus(StockCheckStatusEnum.PASS.getStatus());
            } else {
                master.setStockCheckStatus(StockCheckStatusEnum.FAIL.getStatus());
            }
            master.setModifyTime(new Date());
            boolean flag = this.updateById(master);
            if (!flag) {
                throw new GlobalCommonException("修改状态失败");
            }
        }

        if (StockType.PURCHASE_OUT == stockType) {
            MaterialRefund refund = materialRefundService.getById(orderId);
            if (refund == null) {
                throw new GlobalCommonException("不存在相关记录");
            }
            if (isOk) {
                refund.setStockCheckStatus(2);
            } else {
                refund.setStockCheckStatus(3);
            }
            refund.setModifyTime(new Date());
            boolean flag = materialRefundService.updateById(refund);
            if (!flag) {
                throw new GlobalCommonException("修改退单状态失败");
            }
        }

    }

}
