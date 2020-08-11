package com.example.apsdemo.admin.authority.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import com.example.apsdemo.admin.common.api.CommonResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @GetMapping("/userInfo")
    public CommonResult<JSONObject> currentUser() {
        SysUser sysUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JSONObject jsonObject = JSONUtil.createObj().putOpt("name",sysUser.getUsername()).putOpt("avatar",sysUser.getAvatar());
        return CommonResult.success(jsonObject);
    }


}
