package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.client.MaterialStockClient;
import com.qcmoke.pms.client.UserClient;
import com.qcmoke.pms.constant.WarehouseStatusEnum;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.mapper.PurchaseOrderMasterMapper;
import com.qcmoke.pms.service.MaterialService;
import com.qcmoke.pms.service.PurchaseOrderDetailService;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import com.qcmoke.pms.utils.UserClientUtil;
import com.qcmoke.pms.vo.PurchaseOrderDetailVo;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
import com.qcmoke.ums.vo.UserVo;
import com.qcmoke.wms.dto.MaterialStockDto;
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
    public void removeByIds(String masterIds) {
        List<String> idList = WebUtil.parseIdStr2List(masterIds);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        List<PurchaseOrderMaster> masterList = this.list(new LambdaQueryWrapper<PurchaseOrderMaster>().in(PurchaseOrderMaster::getMasterId, idList));
        boolean flag = masterList.stream().anyMatch(master -> WarehouseStatusEnum.PASS_AND_STOCKED.getStatus() == master.getStatus());
        if (flag) {
            throw new GlobalCommonException("包含已入库的订单，不允许删除已入库的订单！");
        }
        flag = this.removeByIds(idList);
        if (!flag) {
            throw new GlobalCommonException("订单删除失败");
        }
        flag = purchaseOrderDetailService.remove(new LambdaQueryWrapper<PurchaseOrderDetail>().in(PurchaseOrderDetail::getMasterId, masterIds));
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


    public static void main(String[] args) {
        List<UserVo> userVos = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserVo userVo = new UserVo();
            userVo.setUserId(Integer.toUnsignedLong(i));
            userVos.add(userVo);
        }
        UserVo user = (UserVo) CollectionUtils.find(userVos, object -> {
            UserVo userVo = (UserVo) object;
            return userVo.getUserId() == 1L;
        });
        System.out.println(user);
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
     * 原料入库
     *
     * @param masterId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addMaterialToStock(Long masterId) {
        //修改订单为已审核已入库
        PurchaseOrderMaster purchaseOrderMaster = new PurchaseOrderMaster();
        purchaseOrderMaster.setMasterId(masterId);
        purchaseOrderMaster.setStatus(WarehouseStatusEnum.PASS_AND_STOCKED.getStatus());
        purchaseOrderMaster.setModifyTime(new Date());
        this.updateStatus(purchaseOrderMaster);


        //将采购订单对应的明细物料加入库存中
        List<PurchaseOrderDetailVo> details = purchaseOrderDetailService.getListByMasterId(masterId);
        List<MaterialStockDto> materialStockDtoList = new ArrayList<>();
        details.forEach(detail -> {
            MaterialStockDto stockDto = new MaterialStockDto();
            stockDto.setMaterialId(detail.getMaterialId());
            stockDto.setCount(detail.getCount());
            stockDto.setMaterialName(detail.getMaterialName());
            materialStockDtoList.add(stockDto);
        });

        if (CollectionUtils.isEmpty(materialStockDtoList)) {
            throw new GlobalCommonException("该订单没有可入库的原料！请注意审核订单明细！");
        }
        Result<?> result = materialStockClient.batchAddMaterialToStock(materialStockDtoList);
        if (HttpStatus.OK.value() != result.getStatus()) {
            throw new GlobalCommonException("入库失败！");
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
        return this.updateById(orderMaster);
    }


    private void verifyRequest(PurchaseOrderMaster orderMaster) {
        WarehouseStatusEnum[] values = WarehouseStatusEnum.values();
        List<WarehouseStatusEnum> enumList = Arrays.asList(values);
        boolean flag = enumList.stream().anyMatch(item -> item.getStatus() == orderMaster.getStatus());
        if (!flag) {
            throw new GlobalCommonException("状态码不符");
        }
    }

    private void verifyBusiness(PurchaseOrderMaster orderMaster, Integer status) {
        //已经入库的订单不能再改为之前的状态
        if (WarehouseStatusEnum.PASS_AND_STOCKED.getStatus() == orderMaster.getStatus()) {
            throw new GlobalCommonException("改订单已经入库，无需再审核！");
        }
        //如果是申请状态，那么状态只能改为不通过或者通过两个状态,而不能越级
        if (WarehouseStatusEnum.NO_APPLY.getStatus() == orderMaster.getStatus() && status > WarehouseStatusEnum.PASS_BUT_NO_STOCKED.getStatus()) {
            throw new GlobalCommonException("操作越级，非法！");
        }
        //审核不通过，不能入库
        if (WarehouseStatusEnum.NO_PASS.getStatus() == orderMaster.getStatus() && status == WarehouseStatusEnum.PASS_AND_STOCKED.getStatus()) {
            throw new GlobalCommonException("审核不通过，不能入库！");
        }
    }

}
