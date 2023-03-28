package com.alexmisko.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alexmisko.pojo.Tag;
import com.alexmisko.service.TagService;
import com.alexmisko.vo.Result;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class TagController {

    @Autowired
    private TagService tagService;
    
    @PostMapping("/tag/user")
    public Result<String> publishTag(@RequestBody List<Tag> tagList){
        log.info("标签列表：[{}]", tagList);
        // tagList.forEach(item -> {
        //     item.setVideoId(Long.valueOf("666"));
        // });
        tagService.saveBatch(tagList);
        return Result.success("添加标签成功！");
    }
}
