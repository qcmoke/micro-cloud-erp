package com.qcmoke.fms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.mapper.BillMapper;
import com.qcmoke.fms.service.BillService;
import com.qcmoke.fms.vo.BillVo;
import com.qcmoke.fms.vo.StatisticsDataItemVo;
import com.qcmoke.fms.vo.StatisticsDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    @Autowired
    private BillMapper billMapper;



    @Override
    public IPage<BillVo> getPage(Page<Bill> page, Bill bill) {
        return billMapper.getPage(page,bill);
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
