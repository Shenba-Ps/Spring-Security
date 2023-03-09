package com.code_shenba.SpringSecurityDemo.controller;

import com.code_shenba.SpringSecurityDemo.entity.LoginRequest;
import com.code_shenba.SpringSecurityDemo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping({"/api/v1/auth"})
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({"/login"})
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) throws Exception {
        return jwtService.login(request);
    }
}
