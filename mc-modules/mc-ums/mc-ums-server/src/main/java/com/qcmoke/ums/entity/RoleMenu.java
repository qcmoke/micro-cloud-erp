package com.qcmoke.ums.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * @author qcmoke
 */
@TableName("t_role_menu")
@Data
public class RoleMenu implements Serializable {
    @TableField(value = "rid")
    private Long rid;
    @TableField(value = "mid")
    private Long mid;
}