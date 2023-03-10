package com.alexmisko.controller;

import com.alexmisko.filter.AccessContext;
import com.alexmisko.pojo.UserInfo;
import com.alexmisko.service.UserInfoService;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;

    /**
     * 查询用户信息（用户操作）
     */
    @GetMapping("/userInfo/user")
    public Result<UserInfo> getUserInfo(){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        log.info("userId and username: [{}] [{}]", loginUserInfo.getId(), loginUserInfo.getUsername());
        return userInfoService.getByUserId(loginUserInfo.getId());
    }

    /**
     * 查询某个用户信息（管理员操作）
     */
    @GetMapping("/userInfo/admin/{id}")
    public Result<UserInfo> getAnyUserInfo(@PathVariable Long id){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        log.info("userId and username: [{}] [{}]", loginUserInfo.getId(), loginUserInfo.getUsername());
        return userInfoService.getByUserId(id);
    }
}
