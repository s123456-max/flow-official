package com.alexmisko.service;

import java.util.List;

import com.alexmisko.pojo.Tag;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TagService extends IService<Tag>{
    Result<List<Tag>> getTagList(Long videoId);
}
