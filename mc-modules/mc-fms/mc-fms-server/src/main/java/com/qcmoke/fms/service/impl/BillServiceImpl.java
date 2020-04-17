package com.qcmoke.fms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.fms.dto.BillQuery;
import com.qcmoke.fms.entity.Account;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.mapper.BillMapper;
import com.qcmoke.fms.service.AccountService;
import com.qcmoke.fms.service.BillService;
import com.qcmoke.fms.vo.BillVo;
import com.qcmoke.fms.vo.StatisticsDataItemVo;
import com.qcmoke.fms.vo.StatisticsDataVo;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Slf4j
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private AccountService accountService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addBill(BillApiDto billApiDto) {

        String xid = RootContext.getXID();
        log.info("xid={}", xid);

        Double totalAmount = billApiDto.getTotalAmount();
        Long dealNum = billApiDto.getDealNum();
        PayType payType = billApiDto.getPayType();
        long accountId = Integer.toUnsignedLong(payType.value());
        DealType dealType = billApiDto.getDealType();
        int dealTypeValue = dealType.value();

        int count = this.count(new LambdaQueryWrapper<Bill>()
                .eq(Bill::getDealNum, dealNum)
                .eq(Bill::getType, dealTypeValue));
        if (count > 0) {
            throw new GlobalCommonException("该账单已经存在!");
        }

        Bill bill = new Bill();
        bill.setDealNum(dealNum);
        bill.setTotalAmount(totalAmount);
        bill.setAccountId(accountId);
        bill.setType(dealTypeValue);
        bill.setCreateTime(new Date());
        boolean flag = this.save(bill);
        if (!flag) {
            throw new GlobalCommonException("创建账单失败!");
        }

        //根据交易金额扣减或者添加相应的账余额
        Account account = accountService.getById(accountId);
        if (account == null) {
            throw new GlobalCommonException("不存在相应的账户");
        }
        account.setModifyTime(new Date());
        switch (dealType) {
            case SALE_OUT:
            case PURCHASE_OUT:
                double updateAmount = account.getAmount() - totalAmount;
                Double safetyAmount = account.getSafetyAmount();
                if (updateAmount < safetyAmount) {
                    throw new GlobalCommonException("该账户余额已低于账户的安全余额量！,请联系财务管理员充值");
                }
                account.setAmount(updateAmount);
                break;
            case SALE_IN:
            case PURCHASE_IN:
                account.setAmount(account.getAmount() + totalAmount);
                break;
            default:
                throw new GlobalCommonException("不支持该交易类型");
        }
        account.setModifyTime(new Date());
        flag = accountService.updateById(account);
        if (!flag) {
            throw new GlobalCommonException("修改账户余额失败!");
        }
    }

    @Override
    public IPage<BillVo> getPage(Page<Bill> page, BillQuery query) {
        return billMapper.getPage(page, query);
    }

    @Override
    public Result<StatisticsDataVo> statistics(Integer year) {
        List<StatisticsDataItemVo> incomeList = billMapper.statistics(year.toString(), true, false);
        List<StatisticsDataItemVo> outlayList = billMapper.statistics(year.toString(), false, false);
        List<StatisticsDataItemVo> orderCountList = billMapper.statistics(year.toString(), true, true);
        StatisticsDataVo statisticsDataVo = new StatisticsDataVo();
        List<Double> incomeRsList = incomeList.stream().map(StatisticsDataItemVo::getRs).collect(Collectors.toList());
        List<Double> outlayRsList = outlayList.stream().map(StatisticsDataItemVo::getRs).collect(Collectors.toList());
        List<Integer> orderCountRsList = new ArrayList<>();
        orderCountList.forEach(billVo -> {
            Double aDouble = billVo.getRs();
            int intValue = aDouble.intValue();
            orderCountRsList.add(intValue);
        });
        List<Double> profitRsList = new ArrayList<>();

        for (int i = 0; i < incomeRsList.size(); i++) {
            Double in = incomeRsList.get(i);
            Double out = outlayRsList.get(i);
            profitRsList.add(in - out);
        }
        statisticsDataVo.setTurnover(incomeRsList);
        statisticsDataVo.setProfit(profitRsList);
        statisticsDataVo.setOrderQuantity(orderCountRsList);
        return Result.ok(statisticsDataVo);
    }
}
