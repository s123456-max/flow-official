package com.alexmisko.netty.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MsgContent{
    private String type; // 消息类型
    private Long senderId; // 发送者id
    private Long receiverId; // 接受者id
    private String message;  // 聊天内容
    private String status;  // 离线消息/在线消息
}
