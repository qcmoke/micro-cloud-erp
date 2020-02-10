package com.qcmoke.ums.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author qcmoke
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends Tree<Menu>{

    private String path;
    private String component;
    private String perms;
    private String icon;
    private String type;
    private Integer orderNum;
}
