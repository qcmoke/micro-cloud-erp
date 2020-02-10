package com.qcmoke.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("t_role")
public class Role implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;

    @TableId(value = "rid", type = IdType.AUTO)
    private Long roleId;

    @TableField(value = "rname")
    private String rname;

    @TableField(value = "remark")
    private String remark;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "modify_time")
    private Date modifyTime;

    private transient String menuIds;

}