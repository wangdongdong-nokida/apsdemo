package com.example.apsdemo.admin.authority.system.controller;

import cn.hutool.core.lang.tree.Tree;
import com.example.apsdemo.admin.authority.system.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理
 */
@Api(tags = "系统菜单管理相关接口")
@RestController
@RequestMapping("/user")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    public SysMenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @ApiOperation(value = "查询并生成树状菜单接口")
    @GetMapping("/menu")
    public List<Tree<Integer>> menu() {
        List<Tree<Integer>> tree = sysMenuService.findByMenu();
        return tree;
    }


    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.matches("12345678aA","$2a$10$VVyorGQFXMkZalZkWFmHX.Dtgzg666e3pAJlB5egSLRG8b1lcya0i"));
        System.out.println(passwordEncoder.encode("12345678aA"));
    }


}
