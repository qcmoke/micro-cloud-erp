package com.qcmoke.oms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.oms.client.BillClient;
import com.qcmoke.oms.client.ProductStockClient;
import com.qcmoke.oms.constant.OutStatusEnum;
import com.qcmoke.oms.constant.PayStatusEnum;
import com.qcmoke.oms.entity.SaleOrderDetail;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.entity.SaleRefund;
import com.qcmoke.oms.mapper.SaleRefundMapper;
import com.qcmoke.oms.service.SaleOrderDetailService;
import com.qcmoke.oms.service.SaleOrderMasterService;
import com.qcmoke.oms.service.SaleRefundService;
import com.qcmoke.oms.vo.SaleRefundVo;
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
    @Autowired
    private SaleRefundMapper saleRefundMapper;

    @Override
    public IPage<SaleRefundVo> getPage(Page<SaleRefund> page, SaleRefund saleRefund) {
        return saleRefundMapper.getPage(page, saleRefund);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void successForInItemToStock(List<Long> orderList) {
        List<SaleRefund> saleRefundList = this.listByIds(orderList);
        if (CollectionUtils.isEmpty(saleRefundList)) {
            throw new GlobalCommonException("不存在相关的退单记录");
        }
        saleRefundList.forEach(saleRefund -> {
            saleRefund.setModifyTime(new Date());
            saleRefund.setStockInStatus(2);
        });
        boolean flag = this.updateBatchById(saleRefundList);
        if (!flag) {
            throw new GlobalCommonException("更新退单状态失败");
        }

    }


    @GlobalTransactional(rollbackFor = Exception.class)
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
        if (PayStatusEnum.PAID.value() == orderMaster.getPayStatus()) {
            Result<?> result = billClient.addBill(
                    new BillApiDto()
                            .setDealNum(saleRefund.getRefundId())
                            .setDealType(DealType.SALE_OUT)
                            .setPayType(PayType.resolve(saleRefund.getRefundChannel()))
                            .setTotalAmount(saleRefund.getTotalAmount()));
            if (result.isError()) {
                throw new GlobalCommonException(result.getMessage());
            }
        }

        //退货
        if (OutStatusEnum.SHIPPED.value() == orderMaster.getOutStatus()) {
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
                    .setDealId(saleRefund.getRefundId())
                    .setStockItemDetailDtoList(detailDtoList);
            Result<?> result = productStockClient.createStockPreReview(stockItemDto);
            if (HttpStatus.OK.value() != result.getStatus()) {
                throw new GlobalCommonException(result.getMessage());
            }
        }
    }
}
