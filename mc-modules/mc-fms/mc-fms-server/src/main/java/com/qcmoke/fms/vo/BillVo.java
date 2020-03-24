package com.qcmoke.fms.vo;

import com.qcmoke.fms.entity.Bill;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BillVo extends Bill {
    private String typeInfo;
    private String accountName;
    private String bankName;
    private String bankNum;
}
