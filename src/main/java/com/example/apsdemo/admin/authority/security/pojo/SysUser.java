package com.example.apsdemo.admin.authority.security.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * (SysUser)实体类
 */
@Data
public class SysUser implements Serializable, UserDetails {
    private static final long serialVersionUID = -35688002901605844L;

    private String userId;
    /**
    * 用户名
    */
    private String username;
    /**
     * 昵称
     */
    private String name;
    /**
    * 密码
    */
    private String password;

    /**
    * 邮箱
    */
    private String email;
    /**
     * 用户头像地址
     */
    private String avatar;

    /**
     * 班组名称
     */
    private String teamName;
    /**
     * 当前用户的角色
     */
    private List<SysRole> sysRoles;

    public String getAvatar(){
        return "http://localhost:8080/avatar/admin.png";
    }

/*    *//**
     * 账户是否未过期，
     * true： (即未过期)
     * false：(即过期)
     *//*
    private boolean accountNonExpired;

    *//**
     * 指示用户是否被锁定或解锁。锁定的用户不能认证。
     * true：用户没有被锁定
     * false：否则锁定
     *//*
    private boolean accountNonLocked;

    *//**
     * 指示用户的凭据（密码）是否已过期
     * true：如果用户的凭据是有效的(即未过期)，
     * false：如果不再有效(即过期)
     *//*
    private boolean credentialsNonExpired;

    *//**
     * 指示用户是启用还是禁用。
     * true：用户已启用
     * false：用户已禁用
     *//*
    private boolean enabled;*/


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (SysRole role : getSysRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleSign()));
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}