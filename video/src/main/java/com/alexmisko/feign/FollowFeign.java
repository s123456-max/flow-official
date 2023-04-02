package com.alexmisko.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alexmisko.config.FeignConfig;
import com.alexmisko.vo.Result;

@FeignClient(value = "FOLLOW", configuration = FeignConfig.class)
public interface FollowFeign {
    @GetMapping("follow/user")
    public Result<String> isRelationship(@RequestParam("follower") Long follower, @RequestParam("emperor") Long emperor);
}
