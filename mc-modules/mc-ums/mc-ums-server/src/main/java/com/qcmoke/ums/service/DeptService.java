package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.ums.vo.PageResult;
import com.qcmoke.ums.entity.Dept;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Repository
public interface DeptService extends IService<Dept> {

    PageResult queryDeptList(PageQuery pageQuery, Dept dept);
}
