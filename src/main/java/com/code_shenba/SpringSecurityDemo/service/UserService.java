package com.code_shenba.SpringSecurityDemo.service;

import com.code_shenba.SpringSecurityDemo.entity.User;
import com.code_shenba.SpringSecurityDemo.repository.UserRepository;
import com.code_shenba.SpringSecurityDemo.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user) {
        user.setUserPassword(PasswordUtil.getEncryptedPassword(user.getUserPassword()));
        return repository.save(user);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
