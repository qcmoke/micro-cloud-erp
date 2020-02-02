package com.qcmoke.system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;


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
