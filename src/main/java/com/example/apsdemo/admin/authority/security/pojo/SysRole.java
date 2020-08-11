package com.example.apsdemo.admin.authority.security.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色(SysRole)实体类
 */
@Data
public class SysRole implements Serializable {
    private static final long serialVersionUID = 327983183656538503L;

    private String roleId;
    /**
    * 角色名称
    */
    private String roleName;
    /**
    * 角色标识
    */
    private String roleSign;
    /**
    * 备注
    */
    private String remark;
    /**
    * 创建用户id
    */
    private String userIdCreate;
    /**
    * 创建时间
    */
    private Date createDate;
    /**
    * 修改时间
    */
    private Date updateDate;

}