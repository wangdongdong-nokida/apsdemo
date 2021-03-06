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
    @Select("SELECT e.employeeid userId,e.employeename username,e.employeename name,e.password,e.emailAddress email,null as avatar,t.teamname teamName" +
            " FROM employee e left join team t on e.employee_teamid = t.teamid WHERE employeename = #{username}")
    @Results({
            @Result(property = "sysRoles", column = "userId", javaType = List.class,
                    many = @Many(select = "com.example.apsdemo.admin.authority.security.mapper.SysRoleMapper.findByUserId"))
    })
    SysUser findByUsername(String username);

    @Update("update employee set password = #{password} where employeeName = #{userName}")
    int updatePassword(@Param("userName") String userName, @Param("password") String password);

}
