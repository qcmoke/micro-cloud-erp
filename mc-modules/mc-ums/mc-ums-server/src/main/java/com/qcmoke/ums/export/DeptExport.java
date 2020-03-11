package com.qcmoke.ums.export;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

/**
 * @author qcmoke
 */
@Data
@Excel("部门信息表")
public class DeptExport {

    @ExcelField(value = "部门名称")
    private String deptName;

    @TableField(value = "ORDER_NUM")
    private Integer orderNum;


    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;
}
