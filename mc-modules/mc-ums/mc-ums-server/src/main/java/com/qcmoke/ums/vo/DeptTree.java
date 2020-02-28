package com.qcmoke.ums.vo;

import com.qcmoke.ums.entity.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qcmoke
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends Tree<Dept> {

    private Integer orderNum;
}
