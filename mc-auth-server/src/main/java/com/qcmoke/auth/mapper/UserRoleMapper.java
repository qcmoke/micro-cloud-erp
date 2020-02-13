package com.qcmoke.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.auth.entity.UserRole;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户角色关联表 Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
