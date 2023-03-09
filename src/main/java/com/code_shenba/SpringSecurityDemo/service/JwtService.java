package com.code_shenba.SpringSecurityDemo.service;

import com.code_shenba.SpringSecurityDemo.entity.LoginRequest;
import com.code_shenba.SpringSecurityDemo.entity.User;
import com.code_shenba.SpringSecurityDemo.repository.UserRepository;
import com.code_shenba.SpringSecurityDemo.util.JwtUtil;
import com.code_shenba.SpringSecurityDemo.util.PasswordUtil;


//import org.apache.log4j.Logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class JwtService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<Map<String, Object>> login(LoginRequest request) throws Exception {
        try {
            Map<String, Object> response = new HashMap<String, Object>();
            if (null == request) {
                response.put("status", 0);
                response.put("error", "Invalid request payload.!");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Optional<User> userOptional = repository.findByUserName(request.getUserName());
            if (!userOptional.isPresent()) {
                response.put("status", 0);
                response.put("error", "Invalid username.!");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            User user = userOptional.get();
            String enPassword = PasswordUtil.getEncryptedPassword(request.getUserPassword());
            System.out.println(enPassword);

            if(!user.getUserName().equals(request.getUserName())) {
                response.put("error", "Invalid username.!");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (!user.getUserPassword().equals(enPassword)) {
                response.put("status", 0);
                response.put("error", "Password is wrong.!");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            final String token = jwtUtil.generateToken(user);
            response.put("User", user);
            response.put("status", 1);
            response.put("message", "Logged in successfully.!");
            response.put("jwt", token);

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch(Exception e) {
            logger.error(String.valueOf(e));
            return new ResponseEntity<Map<String, Object>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = repository.findByUserName(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        User user = userOptional.get();
        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),
                getAuthority());
    }

    private List<SimpleGrantedAuthority> getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

}
