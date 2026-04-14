package com.ng.runz.service.impl;

import com.ng.runz.exception.ResourceNotFoundException;
import com.ng.runz.model.UserPrincipal;
import com.ng.runz.model.Users;
import com.ng.runz.service.UserService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.ObjectMapper;

@Service
public class UserPrincipalServiceDetail implements UserDetailsService {

    private final ApplicationContext applicationContext;

    public UserPrincipalServiceDetail(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Users userFound = applicationContext.getBean(UserService.class).getUserByUsername(username);

            return new UserPrincipal(userFound,userFound.getRoles().stream().toList());
        } catch (BeansException | ResourceNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
