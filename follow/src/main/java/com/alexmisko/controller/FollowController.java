package com.alexmisko.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alexmisko.config.ConditionException;
import com.alexmisko.filter.AccessContext;
import com.alexmisko.pojo.Follow;
import com.alexmisko.service.FollowService;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@RestController
public class FollowController {

    @Autowired
    FollowService followService;

    @GetMapping("follow/user")
    public Result<String> isRelationship(@RequestParam("follower") Long follower, @RequestParam("emperor") Long emperor){
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower", follower).eq("emperor", emperor);
        if(followService.getOne(queryWrapper) == null){
            return Result.success("no");
        }else{
            return Result.success("yes");
        }
    }

    @PostMapping("follow/user")
    public Result<String> followEmperor(Follow follow){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        Long userId = loginUserInfo.getId();
        if(userId.equals(follow.getEmperor())){
            throw new ConditionException("不能关注自己！");
        }
        QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("follower", userId).eq("emperor", follow.getEmperor());
        if (followService.getOne(queryWrapper) != null){
            followService.remove(queryWrapper);
            return Result.success("取消关注成功！");
        }else{
            follow.setFollower(userId);
            followService.save(follow);
            return Result.success("关注成功！");
        }
    }
}
