package com.example.apsdemo.admin.authority.security.service;

import com.example.apsdemo.admin.authority.security.pojo.SysUser;


public interface SysUserService {

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return
     */
    SysUser findByUsername(String username);

    int updateUserPassword(String userName,String password);

}
