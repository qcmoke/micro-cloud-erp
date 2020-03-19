package com.qcmoke.oms.controller;


import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.entity.Customer;
import com.qcmoke.oms.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    
    @GetMapping("/list")
    public Result<Customer> getCustomerList() {
        return Result.ok();
    }
}

