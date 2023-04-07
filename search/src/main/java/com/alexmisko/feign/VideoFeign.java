package com.alexmisko.feign;

import com.alexmisko.config.FeignConfig;
import com.alexmisko.vo.Result;
import com.alexmisko.vo.UserInfo;
import com.alexmisko.vo.Video;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "VIDEO", configuration = FeignConfig.class)
public interface VideoFeign {
    @GetMapping("/video/home/user/{id}")
    public Result<Video> getHomeVideoOne(@PathVariable Long id);
}
