package com.qcmoke.fms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.vo.BillVo;
import com.qcmoke.fms.vo.StatisticsDataVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
public interface BillService extends IService<Bill> {

    Result<StatisticsDataVo> statistics(Integer year);

    IPage<BillVo> getPage(Page<Bill> billPage, Bill bill);

    void addBill(BillApiDto billApiDto);
}
