package com.example.apsdemo.admin.authority.system.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单管理(SysMenu)实体类
 *
 */
@Data
public class SysMenu implements Serializable {
    private static final long serialVersionUID = -91971017232365666L;

    private Integer menuId;
    /**
    * 父菜单ID，一级菜单为0
    */
    private Integer parentId;
    /**
    * 菜单名称
    */
    private String name;
    /**
     * 菜单编码
     */
    private String nameCode;
    /**
    * 菜单URL
    */
    private String url;
    /**
    * 授权(多个用逗号分隔，如：user:list,user:create)
    */
    private String perms;
    /**
    * 类型   0：目录   1：菜单   2：按钮
    */
    private Integer type;
    /**
    * 菜单图标
    */
    private String icon;
    /**
    * 排序
    */
    private Integer orderNum;
    /**
    * 创建时间
    */
    private Date createDate;
    /**
    * 修改时间
    */
    private Date updateDate;
    /**
     * 菜单状态：0：启用 1：禁用
     */
    private Boolean status;


}