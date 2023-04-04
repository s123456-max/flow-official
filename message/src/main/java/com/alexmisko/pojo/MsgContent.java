package com.alexmisko.pojo;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@TableName("flow_chat")
public class MsgContent{

    @TableId(type =  IdType.AUTO)
    private Long id;

    private String type; // 消息类型

    private Long senderId; // 发送者id

    private Long receiverId; // 接受者id

    private String message;  // 聊天内容

    private String status;  // 离线消息/在线消息

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
}
