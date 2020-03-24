package com.qcmoke.pms.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.vo.MaterialRefundVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface MaterialRefundService extends IService<MaterialRefund> {

    PageResult<MaterialRefundVo> getPage(Page<MaterialRefund> page, MaterialRefund materialDto);

    void createRefuse(MaterialRefund materialRefund);
}
