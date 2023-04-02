package com.alexmisko.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResourceUtil{
    
    // 公共资源
    private static final String[] PUBLIC_PATH = {
        "/auth/signIn",
        "/auth/signUp",
        "/auth/smsCode/**"
    };

    // 用户资源
    private static final String[] USER_PATH = {
        "/video/user",
        "/comment/**",
        "/userInfo/user/**",
        "/video/chunk/user",
        "/video/flow/user",
        "/video/favor/user",
        "/follow/user"
    };

    // 管理员资源
    private static final String[] ADMIN_PATH = {
        "/userInfo/admin/**",
        "/video/admin/**"
    };

    private static final String AUTH_PUBLIC = "ROLE_PUBLIC";

    private static final String AUTH_USER = "ROLE_USER";

    private static final String AUTH_ADMIN = "ROLE_ADMIN";

    public List<String> getPath(String auth_role){
        List<String> path = new ArrayList<>();
        if(AUTH_PUBLIC.equals(auth_role)){
            log.info("获取公共权限！");
            Collections.addAll(path, PUBLIC_PATH);
        }
        if(AUTH_USER.equals(auth_role)){
            log.info("获取用户权限！");
            Collections.addAll(path, PUBLIC_PATH);
            Collections.addAll(path, USER_PATH);
        }
        if(AUTH_ADMIN.equals(auth_role)){
            log.info("获取管理员权限！");
            Collections.addAll(path, PUBLIC_PATH);
            Collections.addAll(path, USER_PATH);
            Collections.addAll(path, ADMIN_PATH);
        }
        return path;
    }

    public boolean isResourcePath(String requestPath, List<String> path){
        return path.stream().map(url -> url.replace("/**", "")).anyMatch(requestPath::startsWith);
    }
}