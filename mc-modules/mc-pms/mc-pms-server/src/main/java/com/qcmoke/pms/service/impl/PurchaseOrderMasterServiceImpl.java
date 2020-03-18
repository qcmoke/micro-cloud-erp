package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.client.MaterialStockClient;
import com.qcmoke.pms.client.UserClient;
import com.qcmoke.pms.constant.CheckStatusEnum;
import com.qcmoke.pms.constant.TransferStockStatusEnum;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.mapper.PurchaseOrderMasterMapper;
import com.qcmoke.pms.service.MaterialService;
import com.qcmoke.pms.service.PurchaseOrderDetailService;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import com.qcmoke.pms.utils.UserClientUtil;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
import com.qcmoke.ums.vo.UserVo;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
    private UserClient userClient;
    @Autowired
    private MaterialService materialService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdList(List<Long> idList) {
        List<PurchaseOrderMaster> masterList = this.list(new LambdaQueryWrapper<PurchaseOrderMaster>().in(PurchaseOrderMaster::getMasterId, idList));
        boolean flag = masterList.stream().anyMatch(master -> master.getTransferStockStatus() != null && master.getTransferStockStatus() >= TransferStockStatusEnum.ALREADY_TRANSFER.getStatus());
        if (flag) {
            throw new GlobalCommonException("订单包含已移交仓库的物料，不允许删除！");
        }
        flag = this.removeByIds(idList);
        if (!flag) {
            throw new GlobalCommonException("订单删除失败");
        }
        flag = purchaseOrderDetailService.remove(new LambdaQueryWrapper<PurchaseOrderDetail>().in(PurchaseOrderDetail::getMasterId, idList));
        if (!flag) {
            throw new GlobalCommonException("订单明细删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrUpdatePurchaseOrder(PurchaseOrderMasterDto purchaseOrderMasterDto, Long currentUserId) {
        List<PurchaseOrderDetail> detailDtoList = purchaseOrderMasterDto.getPurchaseOrderDetailList();
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

        /*保存订单主表*/
        //通过dto的id查询订单主表确定新建还是修改
        PurchaseOrderMaster master = null;
        Long masterId = purchaseOrderMasterDto.getMasterId();
        if (masterId != null) {
            master = this.getById(masterId);
            if (master.getStatus() == CheckStatusEnum.PASS.getStatus()) {
                throw new GlobalCommonException("该订单已被审核通过，不允许修改！");
            }
            if (master.getTransferStockStatus() == TransferStockStatusEnum.ALREADY_TRANSFER.getStatus()) {
                throw new GlobalCommonException("订单包含已移交仓库的物料，不允许修改！");
            }
        }
        if (master == null) {
            master = new PurchaseOrderMaster();
            master.setCreateTime(new Date());
            master.setPurchaseDate(new Date());
        } else {
            master = new PurchaseOrderMaster();
            master.setModifyTime(new Date());
        }
        master.setTotalAmount(amountTotal.get());
        master.setOperatorId(currentUserId);
        if (purchaseOrderMasterDto.getMasterId() != null) {
            master.setMasterId(purchaseOrderMasterDto.getMasterId());
        }
        if (purchaseOrderMasterDto.getSupplierId() != null) {
            master.setSupplierId(purchaseOrderMasterDto.getSupplierId());
        }
        if (purchaseOrderMasterDto.getFreight() != null) {
            master.setFreight(purchaseOrderMasterDto.getFreight());
        }
        if (purchaseOrderMasterDto.getFreightIsPaid() != null) {
            if (purchaseOrderMasterDto.getFreightIsPaid()) {
                master.setFreightPayStatus(2);
            } else {
                master.setFreightPayStatus(1);
            }
        }
        if (purchaseOrderMasterDto.getOrderIsPaid() != null) {
            if (purchaseOrderMasterDto.getOrderIsPaid()) {
                master.setPayStatus(2);
            } else {
                master.setPayStatus(1);
            }
        }
        if (purchaseOrderMasterDto.getPayType() != null) {
            master.setPayType(purchaseOrderMasterDto.getPayType());
        }
        if (StringUtils.isNoneBlank(purchaseOrderMasterDto.getRemark())) {
            master.setRemark(purchaseOrderMasterDto.getRemark());
        }
        boolean flag = this.saveOrUpdate(master);
        if (!flag) {
            throw new GlobalCommonException("保存订单主表失败");
        }

        //添加或修改库存明细
        purchaseOrderDetailService.remove(new LambdaQueryWrapper<PurchaseOrderDetail>().eq(PurchaseOrderDetail::getMasterId, masterId));
        PurchaseOrderMaster finalMaster = master;
        detailDtoList.forEach(detailDto -> {
            detailDto.setCreateTime(new Date());
            detailDto.setMasterId(finalMaster.getMasterId());
        });
        flag = purchaseOrderDetailService.saveOrUpdateBatch(detailDtoList);
        if (!flag) {
            throw new GlobalCommonException("新建订单明细失败");
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
     * 原料转交仓库审核
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void transferToStock(Long masterId, Long currentUserId) {
        PurchaseOrderMaster master = this.getById(masterId);
        if (master == null) {
            throw new GlobalCommonException("为找到对应的订单！");
        }
        if (master.getStatus() == null || master.getStatus() < CheckStatusEnum.PASS.getStatus()) {
            throw new GlobalCommonException("审核未通过不能移交！");
        }
        if (TransferStockStatusEnum.ALREADY_TRANSFER.getStatus() == master.getTransferStockStatus()) {
            throw new GlobalCommonException("改订单已经移交仓库，不能重复！");
        }

        //将采购订单对应的明细物料加到移交明细单里
        List<PurchaseOrderDetail> details = purchaseOrderDetailService.list(new LambdaQueryWrapper<PurchaseOrderDetail>().eq(PurchaseOrderDetail::getMasterId, master.getMasterId()));
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
        stockItemDto.setStockItemDetailDtoList(stockItemDetailDtoList);
        Result<?> result = materialStockClient.transferToStock(stockItemDto);
        if (HttpStatus.OK.value() != result.getStatus()) {
            throw new GlobalCommonException("移交失败！");
        }

        //修改订单为已移交
        master.setMasterId(masterId);
        master.setOperatorId(currentUserId);
        master.setTransferStockStatus(TransferStockStatusEnum.ALREADY_TRANSFER.getStatus());
        master.setModifyTime(new Date());
        boolean flag = this.updateById(master);
        if (!flag) {
            throw new GlobalCommonException("修改订单状态失败");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateStatus(PurchaseOrderMaster purchaseOrderMasterDto) {
        verifyRequest(purchaseOrderMasterDto);
        PurchaseOrderMaster orderMaster = this.getById(purchaseOrderMasterDto.getMasterId());
        if (orderMaster == null) {
            throw new GlobalCommonException("没有对应的记录");
        }
        orderMaster.setModifyTime(new Date());
        Integer status = purchaseOrderMasterDto.getStatus();
        verifyBusiness(orderMaster, status);
        orderMaster.setStatus(status);
        orderMaster.setTransferStockStatus(TransferStockStatusEnum.NO_TRANSFER.getStatus());
        return this.updateById(orderMaster);
    }


    private void verifyRequest(PurchaseOrderMaster orderMasterDto) {
        Integer status = orderMasterDto.getStatus();
        if (status == null) {
            throw new GlobalCommonException("状态码不符");
        }
        List<CheckStatusEnum> enumList = Arrays.asList(CheckStatusEnum.values());
        boolean flag = enumList.stream().anyMatch(item -> item.getStatus() == status);
        if (!flag) {
            throw new GlobalCommonException("状态码不符");
        }
    }

    private void verifyBusiness(PurchaseOrderMaster orderMaster, Integer status) {
        //如果是申请状态，那么状态只能改为不通过或者通过两个状态,而不能越级
        if (CheckStatusEnum.NO_APPLY.getStatus() == orderMaster.getStatus() && status > CheckStatusEnum.PASS.getStatus()) {
            throw new GlobalCommonException("操作越级，非法！");
        }
        if (TransferStockStatusEnum.ALREADY_TRANSFER.getStatus() == orderMaster.getTransferStockStatus()) {
            throw new GlobalCommonException("改订单已经移交仓库，不能再进行修改！");
        }
    }

}
