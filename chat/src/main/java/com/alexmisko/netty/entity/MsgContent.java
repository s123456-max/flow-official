package com.alexmisko.netty.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgContent implements Serializable{
    private Integer type; // 类型
    private Long senderId; // 发送者id
    private Long receiverId; // 接受者id
    private String msg;  // 聊天内容
    private String msgId; // 用于消息的签收
}
