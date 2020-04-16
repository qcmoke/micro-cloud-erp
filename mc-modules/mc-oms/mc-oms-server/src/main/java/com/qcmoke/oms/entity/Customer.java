package com.qcmoke.oms.entity;

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
 * @since 2020-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_customer")
public class Customer implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 客户编号
     */
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    /**
     * 客户收货地址
     */
    private String address;

    /**
     * 开户行
     */
    private String bank;

    /**
     * 银行账户
     */
    private Long bankAccount;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系人电话
     */
    private String linkTel;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 联系电话
     */
    private String telPhone;

    /**
     * 邮编
     */
    private String postcode;

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
