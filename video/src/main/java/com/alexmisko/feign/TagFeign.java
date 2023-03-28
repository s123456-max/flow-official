package com.alexmisko.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alexmisko.config.FeignConfig;
import com.alexmisko.vo.Result;
import com.alexmisko.vo.Tag;

@FeignClient(value = "TAG", configuration = FeignConfig.class)
public interface TagFeign {
    
    @PostMapping("/tag/user")
    public Result<String> publishTag(@RequestBody List<Tag> tagList);
}
