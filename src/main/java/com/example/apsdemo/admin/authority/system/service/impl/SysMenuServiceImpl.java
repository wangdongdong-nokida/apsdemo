package com.example.apsdemo.admin.authority.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import com.example.apsdemo.admin.authority.security.mapper.SysRoleMapper;
import com.example.apsdemo.admin.authority.security.pojo.SysRole;
import com.example.apsdemo.admin.authority.system.mapper.SysMenuMapper;
import com.example.apsdemo.admin.authority.system.pojo.SysMenu;
import com.example.apsdemo.admin.authority.system.service.SysMenuService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    private final SysRoleMapper sysRoleMapper;

    public SysMenuServiceImpl(SysMenuMapper sysMenuMapper, SysRoleMapper sysRoleMapper) {
        this.sysMenuMapper = sysMenuMapper;
        this.sysRoleMapper = sysRoleMapper;
    }


    @Override
    public List<Tree<Integer>> findByMenu() {
        // 获取登录成功的用户信息
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 获取当前用户所拥有的角色标识，并用逗号连接
        String roleSigns = userDetails.getAuthorities().stream().map(authority -> "'" + authority + "'").collect(Collectors.joining(","));
        // 查询出当前用户所拥有的角色信息

        List<SysRole> sysRoleList = sysRoleMapper.findByRoleSign(roleSigns);
        // 获取角色id并用逗号连接如："0,1,2,3,4,5,6"
        String roleIds = sysRoleList.stream().map(sysRole -> "'" + String.valueOf(sysRole.getRoleId()) + "'").collect(Collectors.joining(","));
        // 根据角色信息查询出当前用户所拥有的菜单
        List<SysMenu> sysMenus = sysMenuMapper.findByMenu(roleIds);
        return buildTreeMenu(sysMenus);
    }

    /**
     * 生成树状菜单，根据【ant desgin pro】要求的格式生成
     * @param sysMenus 菜单数据
     * @return 树状菜单
     */
    private List<Tree<Integer>> buildTreeMenu(List<SysMenu> sysMenus) {
        // 构建node列表
        List<TreeNode<Integer>> nodeList = CollUtil.newArrayList();
        for (SysMenu menu : sysMenus) {
            // 添加扩展属性
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("icon", menu.getIcon());
            nodeList.add(new TreeNode<>(menu.getMenuId(), menu.getParentId(), menu.getNameCode(), menu.getUrl()).setExtra(extraMap));
        }

        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名 都要默认值的
        treeNodeConfig.setWeightKey("path");
        // 最大递归深度
        treeNodeConfig.setDeep(3);

        //转换器
        return TreeUtil.build(nodeList, 0, treeNodeConfig,
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    // 扩展属性 ...
                    tree.putExtra("icon", treeNode.getExtra().get("icon"));
                });
    }
}
