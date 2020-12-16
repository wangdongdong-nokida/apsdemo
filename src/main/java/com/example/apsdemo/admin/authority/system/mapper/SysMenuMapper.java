package com.example.apsdemo.admin.authority.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.apsdemo.admin.authority.system.pojo.SysMenu;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("SELECT\n" +
            "\tm.* \n" +
            "FROM\n" +
            "\tsys_menu m\n" +
            "\tJOIN sys_role_menu rm ON m.menu_id = rm.menu_id \n" +
            "WHERE\n" +
            "\trm.role_id IN (${roleIds})" +
            "AND type IN (0,1) AND status = 0 " +
            "ORDER BY order_num")
    //@Query(value = "SELECT m FROM sys_menu m JOIN sys_role_menu rm ON m.menu_id = rm.menu_id  WHERE rm.role_id IN (:roleIds) AND type IN (0,1) AND status = 0  ORDER BY order_num")
    List<SysMenu> findByMenu(String roleIds);

}
