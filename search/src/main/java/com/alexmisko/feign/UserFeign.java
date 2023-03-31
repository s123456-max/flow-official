package com.alexmisko.feign;

import com.alexmisko.vo.Result;
import com.alexmisko.vo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "AUTH")
public interface UserFeign {
    @GetMapping("/auth/user")
    public Result<User> getUser(@RequestParam("userId") Long userId);
}
