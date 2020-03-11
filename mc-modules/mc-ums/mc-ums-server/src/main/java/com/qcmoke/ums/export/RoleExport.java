package com.qcmoke.ums.export;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;


/**
 * @author qcmoke
 */
@Data
@Excel("角色信息表")
public class RoleExport {

    @ExcelField(value = "角色名称")
    private String roleName;

    @ExcelField(value = "角色中文名称")
    private String roleNameZh;

    @ExcelField(value = "角色描述")
    private String remark;

    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

}
