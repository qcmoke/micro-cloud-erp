package com.qcmoke.ums.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class PageResult {
    private List<?> rows;
    private Number total;
}