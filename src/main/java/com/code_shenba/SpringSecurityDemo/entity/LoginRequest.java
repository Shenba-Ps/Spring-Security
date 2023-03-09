package com.code_shenba.SpringSecurityDemo.entity;

import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String userPassword;
}
