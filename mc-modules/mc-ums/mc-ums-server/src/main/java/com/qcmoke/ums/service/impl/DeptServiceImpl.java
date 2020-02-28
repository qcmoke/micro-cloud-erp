package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.ums.vo.DeptTree;
import com.qcmoke.ums.vo.PageResult;
import com.qcmoke.ums.vo.Tree;
import com.qcmoke.ums.entity.Dept;
import com.qcmoke.ums.mapper.DeptMapper;
import com.qcmoke.ums.service.DeptService;
import com.qcmoke.ums.utils.SqlUtil;
import com.qcmoke.ums.utils.TreeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public PageResult queryDeptList(PageQuery pageQuery, Dept dept) {
        PageResult pageResult = new PageResult();
        try {
            QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotBlank(dept.getDeptName())) {
                queryWrapper.lambda().like(Dept::getDeptName, dept.getDeptName());
            }
            if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo())) {
                queryWrapper.lambda()
                        .ge(Dept::getCreateTime, dept.getCreateTimeFrom())
                        .le(Dept::getCreateTime, dept.getCreateTimeTo());
            }
            SqlUtil.handleWrapperSort(pageQuery, queryWrapper, "orderNum", PageQuery.ORDER_ASC, true);
            List<Dept> deptList = baseMapper.selectList(queryWrapper);
            List<DeptTree> trees = new ArrayList<>();
            buildTrees(trees, deptList);
            List<? extends Tree<?>> deptTree = TreeUtil.build(trees);
            pageResult.setRows(deptTree);
            pageResult.setTotal(deptList.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            pageResult.setRows(null);
            pageResult.setTotal(0);
        }
        return pageResult;
    }

    private void buildTrees(List<DeptTree> trees, List<Dept> deptList) {
        deptList.forEach(dept -> {
            DeptTree tree = new DeptTree();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setLabel(dept.getDeptName());
            tree.setOrderNum(dept.getOrderNum());
            trees.add(tree);
        });
    }

}
