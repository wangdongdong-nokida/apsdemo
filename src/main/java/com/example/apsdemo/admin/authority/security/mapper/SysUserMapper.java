package com.example.apsdemo.admin.authority.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import org.apache.ibatis.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据登录的用户名名查询
     * @param username 账户名
     * @return
     */
    @Select("SELECT employeeid userId,employeename username,employeename name,password,emailAddress email,null as avatar" +
            " FROM employee WHERE employeename = #{username}")
    @Results({
            @Result(property = "sysRoles", column = "userId", javaType = List.class,
                    many = @Many(select = "com.example.apsdemo.admin.authority.security.mapper.SysRoleMapper.findByUserId"))
    })
    SysUser findByUsername(String username);

    @Update("update employee set password = #{password},USER_PWD = #{newPwd} where employeeName = #{userName}")
    int updatePassword(@Param("userName") String userName, @Param("password") String password, @Param("newPwd") String newPwd);

}
