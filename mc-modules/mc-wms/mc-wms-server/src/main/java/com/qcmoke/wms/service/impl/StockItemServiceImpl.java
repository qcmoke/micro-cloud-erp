package com.qcmoke.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.client.PurchaseOrderMasterClient;
import com.qcmoke.wms.client.SaleOrderMasterClient;
import com.qcmoke.wms.constant.CheckStatusEnum;
import com.qcmoke.wms.constant.FinishStatusEnum;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.mapper.StockItemMapper;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.vo.StockItemVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
@Service
public class StockItemServiceImpl extends ServiceImpl<StockItemMapper, StockItem> implements StockItemService {

    @Autowired
    private StockItemMapper stockItemMapper;
    @Autowired
    private SaleOrderMasterClient saleOrderMasterClient;
    @Autowired
    private PurchaseOrderMasterClient purchaseOrderMasterClient;

    @Override
    public IPage<StockItemVo> getPage(Page<StockItem> page, StockItem stockItemDto) {
        return stockItemMapper.getPage(page, stockItemDto);
    }


    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkPass(Long stockItemId) {
        StockItem stockItem = stockItemMapper.selectById(stockItemId);
        if (FinishStatusEnum.FINISHED.value() == stockItem.getFinishStatus()) {
            throw new GlobalCommonException("该订单的货物已经完成出入库！");
        }
        stockItem.setStockItemId(stockItemId)
                .setCheckStatus(CheckStatusEnum.PASS.value())
                .setModifyTime(new Date())
                .setAdminId(OauthSecurityJwtUtil.getCurrentUserId());
        boolean flag = this.updateById(stockItem);
        if (!flag) {
            throw new GlobalCommonException("更新出入单失败");
        }
        StockType stockType = StockType.valueOf(stockItem.getStockType());
        Long orderId = stockItem.getDealId();
        Result<?> result;
        switch (stockType) {
            case SALE_IN:
            case SALE_OUT:
                result = saleOrderMasterClient.checkCallBackForCreateStockItem(stockType, orderId, true);
                if (result.isError()) {
                    throw new GlobalCommonException("通知oms失败,e=" + result.getMessage());
                }
                break;
            case PURCHASE_IN:
            case PURCHASE_OUT:
                result = purchaseOrderMasterClient.checkCallBackForCreateStockItem(stockType, orderId, true);
                if (result.isError()) {
                    throw new GlobalCommonException("通知pms失败,e=" + result.getMessage());
                }
                break;
            default:
                throw new GlobalCommonException("暂不支持此操作！");
        }
    }


    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkFail(Long stockItemId) {
        StockItem stockItem = stockItemMapper.selectById(stockItemId);
        stockItem.setStockItemId(stockItemId)
                .setCheckStatus(CheckStatusEnum.NO_PASS.value())
                .setModifyTime(new Date())
                .setAdminId(OauthSecurityJwtUtil.getCurrentUserId());
        boolean flag = this.updateById(stockItem);
        if (!flag) {
            throw new GlobalCommonException("更新出入单失败");
        }
        StockType stockType = StockType.valueOf(stockItem.getStockType());
        Result<?> result;
        switch (stockType) {
            case SALE_IN:
            case SALE_OUT:
                result = saleOrderMasterClient.checkCallBackForCreateStockItem(stockType, stockItem.getDealId(), false);
                if (result.isError()) {
                    throw new GlobalCommonException("通知oms失败,e=" + result.getMessage());
                }
                break;
            case PURCHASE_IN:
            case PURCHASE_OUT:
                result = purchaseOrderMasterClient.checkCallBackForCreateStockItem(stockType, stockItem.getDealId(), false);
                if (result.isError()) {
                    throw new GlobalCommonException("通知pms失败,e=" + result.getMessage());
                }
                break;
            default:
                throw new GlobalCommonException("暂不支持此操作！");
        }
    }
}
