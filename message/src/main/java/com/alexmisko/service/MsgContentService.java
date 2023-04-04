package com.alexmisko.service;


import com.alexmisko.pojo.MsgContent;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MsgContentService extends IService<MsgContent>{
    void storeChatMessage(MsgContent msgContent);
}
