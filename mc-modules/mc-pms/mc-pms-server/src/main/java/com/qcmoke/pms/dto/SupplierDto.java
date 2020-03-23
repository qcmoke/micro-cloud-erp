package com.qcmoke.pms.dto;

import lombok.Data;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class SupplierDto {
    /**
     * 供应商编号
     */
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

    private List<Long> materialIds;
}
