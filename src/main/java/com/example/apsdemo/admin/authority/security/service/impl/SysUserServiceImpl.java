package com.example.apsdemo.admin.authority.security.service.impl;

import com.example.apsdemo.admin.authority.security.mapper.SysUserMapper;
import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import com.example.apsdemo.admin.authority.security.service.SysUserService;
import org.springframework.stereotype.Service;


@Service
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;

    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public SysUser findByUsername(String username) {
        SysUser sysUser = sysUserMapper.findByUsername(username);
        return sysUser;
    }
}
