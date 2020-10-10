package com.example.apsdemo.admin.authority.system.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.apsdemo.admin.authority.security.pojo.SysUser;
import com.example.apsdemo.admin.authority.security.service.SysUserService;
import com.example.apsdemo.admin.common.api.CommonResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/user")
public class SysUserController {

    @Service
    private SysUserService userService;

    @GetMapping("/userInfo")
    public CommonResult<JSONObject> currentUser() {
        SysUser sysUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        JSONObject jsonObject = JSONUtil.createObj().putOpt("name",sysUser.getUsername()).putOpt("avatar",sysUser.getAvatar());
        return CommonResult.success(jsonObject);
    }

    @GetMapping("/resetPwd")
    @Transactional(rollbackFor = RuntimeException.class)
    public String resetPwd(String userName,String newPwd) {
        InputStream error = null;
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = "{bcrypt}"+passwordEncoder.encode(newPwd);
            int result = userService.updateUserPassword(userName,password,newPwd);
            if(result > 0){
                StringBuffer command = new StringBuffer();
                command.append("net user ");
                command.append(userName);
                command.append(" "+newPwd);
                Process p = Runtime.getRuntime().exec(command.toString());
                error = p.getErrorStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(error));
                StringBuffer buffer = new StringBuffer();
                String s = "";
                while ((s = bufferedReader.readLine()) != null) {
                    buffer.append(s);
                }
                bufferedReader.close();
                p.waitFor();
                if (p.exitValue() != 0) {
                    return userName+":"+newPwd+","+buffer.toString();
                } else {
                    return "";
                }
            } else {
                return "Employee表更新失败";
            }
        } catch (Exception ex) {
            if (error != null) {
                try {
                    error.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ex.getMessage();
        }
    }


}
