package com.qcmoke.fms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.mapper.BillMapper;
import com.qcmoke.fms.service.BillService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

}
