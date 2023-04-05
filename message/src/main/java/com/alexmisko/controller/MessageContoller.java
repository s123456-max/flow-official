package com.alexmisko.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexmisko.dao.MessageMapper;
import com.alexmisko.feign.UserInfoFeign;
import com.alexmisko.filter.AccessContext;
import com.alexmisko.pojo.Message;
import com.alexmisko.pojo.MsgContent;
import com.alexmisko.service.MessageService;
import com.alexmisko.service.MsgContentService;
import com.alexmisko.vo.LoginUserInfo;
import com.alexmisko.vo.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MessageContoller {

    @Autowired
    private MsgContentService msgContentService;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserInfoFeign userInfoFeign;
    
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
    public Result<List<Message>> getNotification(){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", loginUserInfo.getId());
        queryWrapper.orderByDesc("create_time");
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        messageList.forEach(item -> {
            item.setUserInfo(userInfoFeign.getUserInfo(item.getSenderId()).getData());
        });
        return Result.success(messageList);
    }

    @GetMapping("message/reddot/user")
    public Result<String> isRedDotNotification(){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", loginUserInfo.getId()).eq("status", "offline").select("status").last("limit 1");
        Message message = messageMapper.selectOne(queryWrapper);
        if (message != null && message.getStatus().equals("offline")){
            return Result.success("yes");
        }else{
            return Result.success("no");
        }
    }

    @PutMapping("message/claim/user")
    public Result<Integer> claimNotification(){
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("receiver_id",loginUserInfo.getId()).set("status", "online");
        Integer rows = messageMapper.update(null, updateWrapper);
        return Result.success(rows);
    }
}
