package com.alexmisko.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class Message {

    @TableId(type =  IdType.AUTO)
    private Long id;

    private Long senderId; // 发送者id

    private Long receiverId; // 接受者id

    private String type;

    private String message;  // 通知内容

    private Long videoId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
