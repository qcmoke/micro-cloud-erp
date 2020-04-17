package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.pms.client.BillClient;
import com.qcmoke.pms.client.MaterialStockClient;
import com.qcmoke.pms.constant.InStatusEnum;
import com.qcmoke.pms.constant.PayStatusEnum;
import com.qcmoke.pms.dto.MaterialRefundQuery;
import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.mapper.MaterialRefundMapper;
import com.qcmoke.pms.service.MaterialRefundService;
import com.qcmoke.pms.service.PurchaseOrderDetailService;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import com.qcmoke.pms.vo.MaterialRefundVo;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Service
public class MaterialRefundServiceImpl extends ServiceImpl<MaterialRefundMapper, MaterialRefund> implements MaterialRefundService {

    @Autowired
    private MaterialRefundMapper materialRefundMapper;
    @Autowired
    private MaterialStockClient materialStockClient;
    @Autowired
    private BillClient billClient;
    @Autowired
    private PurchaseOrderMasterService purchaseOrderMasterService;
    @Autowired
    private PurchaseOrderDetailService purchaseOrderDetailService;

    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createRefund(MaterialRefund materialRefund) {
        Long masterId = materialRefund.getPurchaseOrderMasterId();
        PurchaseOrderMaster orderMaster = purchaseOrderMasterService.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在该订单");
        }

        //生成退款单
        materialRefund.setCreateTime(new Date());
        materialRefund.setRefundDate(new Date());
        boolean save = this.save(materialRefund);
        if (!save) {
            throw new GlobalCommonException("生成退货单失败");
        }

        if (InStatusEnum.SHIPPED.value() == orderMaster.getInStatus()) {
            //退货
            List<PurchaseOrderDetail> detailList = purchaseOrderDetailService.list(new LambdaQueryWrapper<PurchaseOrderDetail>().eq(PurchaseOrderDetail::getMasterId, masterId));
            if (CollectionUtils.isEmpty(detailList)) {
                throw new GlobalCommonException("不存在相关明细");
            }
            List<StockItemDetailDto> detailDtoList = new ArrayList<>();
            detailList.forEach(detail -> {
                StockItemDetailDto detailDto = new StockItemDetailDto();
                detailDto.setItemId(detail.getMaterialId());
                detailDto.setItemNum(detail.getCount());
                detailDtoList.add(detailDto);
            });
            //生成出库单
            StockItemDto stockItemDto = new StockItemDto()
                    .setStockType(StockType.PURCHASE_OUT)
                    .setItemType(ItemType.MATERIAL)
                    .setDealId(materialRefund.getRefundId())
                    .setStockItemDetailDtoList(detailDtoList);
            Result<?> result = materialStockClient.createStockPreReview(stockItemDto);
            if (HttpStatus.OK.value() != result.getStatus()) {
                throw new GlobalCommonException(result.getMessage());
            }
        }

        if (orderMaster.getPayStatus() == PayStatusEnum.PAID.value()) {
            Integer refundChannel = materialRefund.getRefundChannel();
            Double totalAmount = materialRefund.getTotalAmount();
            if (refundChannel == null || totalAmount == null) {
                throw new GlobalCommonException("该订单已经支付，请输入退订金额和退款渠道！");
            }

            //采购退款
            BillApiDto billApiDto = new BillApiDto()
                    .setDealNum(materialRefund.getRefundId())
                    .setDealType(DealType.PURCHASE_IN)
                    .setPayType(PayType.valueOf(refundChannel))
                    .setTotalAmount(totalAmount);
            Result<?> result = billClient.addBill(billApiDto);
            if (result.isError()) {
                throw new GlobalCommonException(result.getMessage());
            }
        }
    }

    @Override
    public PageResult<MaterialRefundVo> getPage(Page<MaterialRefund> page, MaterialRefundQuery query) {
        IPage<MaterialRefundVo> iPage = materialRefundMapper.getPage(page, query);
        PageResult<MaterialRefundVo> pageResult = new PageResult<>();
        pageResult.setRows(iPage.getRecords());
        pageResult.setTotal(iPage.getTotal());
        return pageResult;
    }
}
