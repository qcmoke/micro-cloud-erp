package com.qcmoke.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.oms.client.BillClient;
import com.qcmoke.oms.client.ProductStockClient;
import com.qcmoke.oms.constant.OrderStatusEnum;
import com.qcmoke.oms.entity.SaleOrderDetail;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.entity.SaleRefund;
import com.qcmoke.oms.mapper.SaleRefundMapper;
import com.qcmoke.oms.service.SaleOrderDetailService;
import com.qcmoke.oms.service.SaleOrderMasterService;
import com.qcmoke.oms.service.SaleRefundService;
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

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Service
public class SaleRefundServiceImpl extends ServiceImpl<SaleRefundMapper, SaleRefund> implements SaleRefundService {

    @Autowired
    private ProductStockClient productStockClient;
    @Autowired
    private BillClient billClient;
    @Autowired
    private SaleOrderMasterService saleOrderMasterService;
    @Autowired
    private SaleOrderDetailService saleOrderDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saleRefund(SaleRefund saleRefund) {
        Long masterId = saleRefund.getSaleOrderMasterId();
        SaleOrderMaster orderMaster = saleOrderMasterService.getById(masterId);
        if (orderMaster == null) {
            throw new GlobalCommonException("不存在该订单");
        }
        //生成退款单
        saleRefund.setCreateTime(new Date());
        boolean save = this.save(saleRefund);
        if (!save) {
            throw new GlobalCommonException("生成退货单失败");
        }

        //退款
        OrderStatusEnum statusEnum = OrderStatusEnum.resolve(orderMaster.getStatus());
        if (OrderStatusEnum.isMoreAndEqOther(statusEnum, OrderStatusEnum.PAID_NO_SHIPPED)) {
            Result<?> result = billClient.addBill(
                    new BillApiDto()
                            .setDealNum(saleRefund.getSaleOrderMasterId())
                            .setDealType(DealType.SALE_OUT)
                            .setPayType(PayType.resolve(saleRefund.getRefundChannel()))
                            .setTotalAmount(saleRefund.getTotalAmount()));
            if (result.isError()) {
                throw new GlobalCommonException(result.getMessage());
            }
        }

        //退货
        if (OrderStatusEnum.isMoreAndEqOther(statusEnum, OrderStatusEnum.SHIPPED)) {
            List<SaleOrderDetail> detailList = saleOrderDetailService.list(new LambdaQueryWrapper<SaleOrderDetail>().eq(SaleOrderDetail::getMasterId, masterId));
            if (CollectionUtils.isEmpty(detailList)) {
                throw new GlobalCommonException("不存在相关明细");
            }
            List<StockItemDetailDto> detailDtoList = new ArrayList<>();
            detailList.forEach(detail -> {
                StockItemDetailDto detailDto = new StockItemDetailDto();
                detailDto.setItemId(detail.getProductId());
                detailDto.setItemNum(detail.getCount());
                detailDtoList.add(detailDto);
            });
            //生成出库单
            StockItemDto stockItemDto = new StockItemDto()
                    .setStockType(StockType.SALE_IN)
                    .setItemType(ItemType.PRODUCT)
                    .setOrderId(masterId)
                    .setStockItemDetailDtoList(detailDtoList);
            Result<?> result = productStockClient.createStockPreReview(stockItemDto);
            if (HttpStatus.OK.value() != result.getStatus()) {
                throw new GlobalCommonException(result.getMessage());
            }
        }
    }
}
