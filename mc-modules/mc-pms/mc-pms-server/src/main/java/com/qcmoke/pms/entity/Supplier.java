package com.qcmoke.pms.entity;

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
 * @since 2020-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_supplier")
public class Supplier implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 供应商编号
     */
    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商地址
     */
    private String address;

    /**
     * 开户银行
     */
    private String bank;

    /**
     * 银行账号
     */
    private Long bankAccount;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系人号码
     */
    private String linkTel;

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
     * 删除状态【0->未删除；1->已删除】
     */
    private Integer deleteStatus;


}
