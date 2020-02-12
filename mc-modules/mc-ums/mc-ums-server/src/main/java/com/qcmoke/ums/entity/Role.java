package com.qcmoke.ums.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_role")
public class Role implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 角色ID
     */
    @TableId(value = "rid", type = IdType.AUTO)
    private Long rid;

    /**
     * 角色名称
     */
    private String rname;

    /**
     * 角色中文名称
     */
    private String rnameZh;

    /**
     * 角色描述
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;


}
