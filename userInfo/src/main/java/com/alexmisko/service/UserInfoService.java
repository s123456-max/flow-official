package com.alexmisko.service;

import com.alexmisko.pojo.UserInfo;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserInfoService extends IService<UserInfo> {
    Result<UserInfo> getByUserId(Long id);
}
