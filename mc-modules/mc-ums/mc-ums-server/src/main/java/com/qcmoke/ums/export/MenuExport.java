package com.qcmoke.ums.export;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.util.Date;

@Data
@Excel("菜单信息表")
public class MenuExport {

    /**
     * 菜单/按钮名称
     */
    @ExcelField(value = "名称")
    private String menuName;

    /**
     * 菜单URL
     */
    @ExcelField(value = "URL")
    private String path;

    /**
     * 对应 Vue组件
     */
    @ExcelField(value = "对应Vue组件")
    private String component;

    /**
     * 权限标识
     */
    @ExcelField(value = "权限")
    private String perms;

    /**
     * 图标
     */
    @ExcelField(value = "图标")
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    @ExcelField(value = "类型", writeConverterExp = "0=按钮,1=菜单")
    private String type;


    /**
     * 创建时间
     */
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    /**
     * 修改时间
     */
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;
}
