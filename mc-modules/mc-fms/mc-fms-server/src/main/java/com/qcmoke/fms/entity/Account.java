package com.qcmoke.fms.entity;

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
 * 
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_account")
public class Account implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 账户id
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Integer accountId;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 交易平台名称(支付宝，微信，银行)
     */
    private String bankName;

    /**
     * 交易平台账号或者银行卡号
     */
    private Integer bankNum;

    /**
     * 余额
     */
    private String amount;

    /**
     * 是否默认(1:是；2:否)
     */
    private Integer isDefault;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 删除状态
     */
    private Integer deleteStatus;


}
