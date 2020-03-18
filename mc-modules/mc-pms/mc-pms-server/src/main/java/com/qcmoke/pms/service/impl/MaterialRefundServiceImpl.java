package com.qcmoke.pms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.pms.client.UserClient;
import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.mapper.MaterialRefundMapper;
import com.qcmoke.pms.service.MaterialRefundService;
import com.qcmoke.pms.utils.UserClientUtil;
import com.qcmoke.pms.vo.MaterialRefundVo;
import com.qcmoke.ums.vo.UserVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Service
public class MaterialRefundServiceImpl extends ServiceImpl<MaterialRefundMapper, MaterialRefund> implements MaterialRefundService {


    @Autowired
    private MaterialRefundMapper materialRefundMapper;
    @Autowired
    private UserClient userClient;

    @Override
    public PageResult<MaterialRefundVo> getPage(Page<MaterialRefund> page, MaterialRefund materialDto) {
        IPage<MaterialRefundVo> iPage = materialRefundMapper.getPage(page, null);
        List<MaterialRefundVo> records = iPage.getRecords();
        return buildPageResult(records, iPage.getTotal());
    }

    private PageResult<MaterialRefundVo> buildPageResult(List<MaterialRefundVo> records, long total) {
        if (CollectionUtils.isNotEmpty(records)) {
            Set<Long> queryUserIdSet = records.stream().map(MaterialRefundVo::getCheckUserId).filter(Objects::nonNull).collect(Collectors.toSet());
            Set<Long> createUserIdSet = records.stream().map(MaterialRefundVo::getCreateUserId).filter(Objects::nonNull).collect(Collectors.toSet());
            queryUserIdSet.addAll(createUserIdSet);
            List<UserVo> userVoList = UserClientUtil.getUserVoList(queryUserIdSet, userClient);
            if (CollectionUtils.isNotEmpty(userVoList)) {
                records.forEach(vo -> {
                    UserVo checkUser = (UserVo) CollectionUtils.find(userVoList, object -> ((UserVo) object).getUserId().equals(vo.getCheckUserId()));
                    if (checkUser != null) {
                        vo.setCheckUser(checkUser);
                    }
                    UserVo createUser = (UserVo) CollectionUtils.find(userVoList, object -> ((UserVo) object).getUserId().equals(vo.getCreateUserId()));
                    if (createUser != null) {
                        vo.setCreateUser(createUser);
                    }
                    //如果退单没有设置金额，那么总金额设置为订单主表的总金额
                    if (vo.getTotalAmount() == null || vo.getTotalAmount() == 0) {
                        PurchaseOrderMaster master = vo.getPurchaseOrderMaster();
                        if (master != null) {
                            vo.setTotalAmount(master.getTotalAmount());
                        }

                    }
                });
            }
        }

        PageResult<MaterialRefundVo> pageResult = new PageResult<>();
        pageResult.setRows(records);
        pageResult.setTotal(total);
        return pageResult;
    }
}
