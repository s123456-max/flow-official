package com.alexmisko.pojo;
import lombok.Data;

@Data
public class MsgContent{
    private String type; // 消息类型
    private Long senderId; // 发送者id
    private Long receiverId; // 接受者id
    private String message;  // 聊天内容
}
