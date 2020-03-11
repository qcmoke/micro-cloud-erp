package com.qcmoke.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.ums.vo.UserDetailVo;
import com.qcmoke.ums.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名获取用户详细信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */
    @Select("   SELECT" +
            "       u.*,GROUP_CONCAT(r.role_name) roleNames" +
            "   FROM t_user u" +
            "   LEFT JOIN t_user_role ur ON (u.user_id = ur.user_id)" +
            "   LEFT JOIN t_role r ON r.role_id = ur.role_id" +
            "   WHERE  " +
            "       u.username = #{username}" +
            "   GROUP BY " +
            "       u.username,u.user_id,u.email,u.mobile,u.password, u.status,u.create_time,u.sex,u.dept_id,u.last_login_time,u.modify_time,u.description,u.avatar")
    UserDetailVo selectUserDetailByUsername(@Param("username") String username);


    @Select("<script>" +
            "   SELECT " +
            "       u.user_id userId," +
            "       u.username," +
            "       u.email," +
            "       u.mobile," +
            "       u.status," +
            "       u.create_time createTime," +
            "       u.sex sex," +
            "       u.dept_id deptId," +
            "       u.last_login_time lastLoginTime," +
            "       u.modify_time modifyTime," +
            "       u.description," +
            "       u.avatar," +
            "       d.dept_name deptName," +
            "       GROUP_CONCAT(r.role_id) roleId," +
            "       GROUP_CONCAT(r.role_name) roleName" +
            "   FROM t_user u" +
            "   LEFT JOIN t_dept d ON (u.dept_id = d.dept_id)" +
            "   LEFT JOIN t_user_role ur ON (u.user_id = ur.user_id)" +
            "   LEFT JOIN t_role r ON r.role_id = ur.role_id" +
            "   GROUP BY u.username,u.user_id,u.email,u.mobile,u.status,u.create_time,u.sex,u.dept_id,u.last_login_time,u.modify_time,u.description,u.avatar" +
            "</script>")
    IPage<Map<String, Object>> getPage(Page<?> page, User user);
}
