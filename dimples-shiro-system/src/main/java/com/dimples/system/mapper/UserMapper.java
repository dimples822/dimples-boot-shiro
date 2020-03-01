package com.dimples.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dimples.system.dto.UserDetailDTO;
import com.dimples.system.po.User;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhongyj <1126834403@qq.com><br/>
 * @date 2020/2/21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据User对象插入数据
     *
     * @param record User
     * @return User
     */
    int insertSelective(User record);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return List<User>
     */
    List<User> findByUsername(@Param("username") String username);

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @return IPage
     */
    IPage<UserDetailDTO> findUserDetailPage(@Param("page") Page<UserDetailDTO> page, @Param("user") User user);

    /**
     * 根据用户名查询用户详细信息
     *
     * @param username String
     * @return UserDetailDTO
     */
    UserDetailDTO findUserDetailByName(String username);
}