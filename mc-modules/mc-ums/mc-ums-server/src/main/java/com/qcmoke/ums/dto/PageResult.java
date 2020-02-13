package com.qcmoke.ums.dto;

import lombok.Data;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class PageResult {
    private List<?> rows;
    private Integer total;
}