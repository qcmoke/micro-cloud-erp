package com.qcmoke.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author MrBird
 */
@Data
@TableName("t_role")
public class Role implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;

    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Long roleId;

    @TableField(value = "ROLE_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    private String roleName;

    @TableField(value = "REMARK")
    @Size(max = 50, message = "{noMoreThan}")
    private String remark;

    @TableField(value = "CREATE_TIME")
    private Date createTime;

    @TableField(value = "MODIFY_TIME")
    private Date modifyTime;

    private transient String menuIds;

}