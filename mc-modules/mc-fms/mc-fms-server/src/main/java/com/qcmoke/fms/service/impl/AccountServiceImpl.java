package com.qcmoke.fms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.fms.entity.Account;
import com.qcmoke.fms.mapper.AccountMapper;
import com.qcmoke.fms.service.AccountService;
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
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
