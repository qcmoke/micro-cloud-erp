package com.qcmoke.fms.api;

import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.dto.BillApiDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qcmoke
 */
@RequestMapping("/bill")
public interface BillApi {

    @PostMapping(value = "/addBill", consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<?> addBill(@RequestBody BillApiDto billApiDto);
}
