package com.example.apsdemo.admin.authority.security.jwt.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * description: UserLogin <br>
 */
@Data
public class UserLogin implements Serializable {

    private String userName;
    private String password;
    private String type;

}
