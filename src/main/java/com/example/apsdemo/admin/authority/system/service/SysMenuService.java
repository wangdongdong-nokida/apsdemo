package com.example.apsdemo.admin.authority.system.service;

import cn.hutool.core.lang.tree.Tree;

import java.util.List;

public interface SysMenuService {

    /**
     * 查询当前用户
     * @return 菜单数据
     */
    List<Tree<Integer>> findByMenu();

}
