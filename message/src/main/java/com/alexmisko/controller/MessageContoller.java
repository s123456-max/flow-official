package com.alexmisko.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexmisko.dao.MessageMapper;
import com.alexmisko.pojo.Message;
import com.alexmisko.pojo.MsgContent;
import com.alexmisko.service.MsgContentService;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@RestController
public class MessageContoller {

    @Autowired
    private MsgContentService msgContentService;

    @Autowired
    private MessageMapper messageMapper;
    
    @GetMapping("message/user")
    public Result<IPage<MsgContent>> getPageMessage(Long senderId, Long receiverId, Long page, Long num){
        QueryWrapper<MsgContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender_id", senderId).eq("receiver_id", receiverId);
        queryWrapper.or();
        queryWrapper.eq("receiver_id", senderId).eq("sender_id", receiverId);
        queryWrapper.orderByDesc("create_time");
        IPage<MsgContent> iPage = new Page<>(page, num);
        IPage<MsgContent> iPageMsgContent = msgContentService.page(iPage, queryWrapper);
        List<MsgContent> MsgContentList =  iPageMsgContent.getRecords();
        iPageMsgContent.setRecords(MsgContentList);
        return Result.success(iPageMsgContent);
    }

    @GetMapping("message/notification/user")
    public Result<List<Message>> getNotification(Long receiverId){
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", receiverId);
        return Result.success(messageMapper.selectList(queryWrapper));
    }
}
