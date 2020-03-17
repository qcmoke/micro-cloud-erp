package com.qcmoke.wms.api;


import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.dto.MaterialStockDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author qcmoke
 * @since 2020-03-12
 */
@RequestMapping("/materialStock")
public interface MaterialStockApi {

    @PostMapping
    Result<?> batchAddMaterialToStock(@RequestBody List<MaterialStockDto> materialStockDtoList);
}

