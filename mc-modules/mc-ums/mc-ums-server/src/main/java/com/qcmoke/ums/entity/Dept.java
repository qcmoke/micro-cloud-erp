package com.qcmoke.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author qcmoke
 */
@Data
@TableName("t_dept")
public class Dept implements Serializable {

    private static final long serialVersionUID = -7790334862410409053L;

    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    @TableField(value = "parent_id")
    private Long parentId;

    @TableField(value = "dept_name")
    private String deptName;

    @TableField(value = "order_num")
    private Integer orderNum;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "modify_time")
    private Date modifyTime;

    private transient String createTimeFrom;

    private transient String createTimeTo;

}