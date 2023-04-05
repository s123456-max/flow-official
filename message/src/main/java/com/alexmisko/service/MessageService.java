package com.alexmisko.service;

import com.alexmisko.pojo.Message;
import com.baomidou.mybatisplus.extension.service.IService;

public interface MessageService extends IService<Message>{
    void storeMessage(Message message);
}
