package com.qcmoke.fms.api;

import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.dto.BillApiDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author qcmoke
 */
@RequestMapping("/bill")
public interface BillApi {

    @RequestMapping(value = "/addBill", method = RequestMethod.POST)
    Result<?> addBill(@RequestBody BillApiDto billApiDto);
}
