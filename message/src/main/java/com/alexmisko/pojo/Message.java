package com.alexmisko.pojo;

import java.util.Date;

import com.alexmisko.vo.UserInfo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Message {

    @TableId(type =  IdType.AUTO)
    private Long id;

    private Long senderId; // 发送者id

    private Long receiverId; // 接受者id

    private String type;

    private String message;  // 通知内容

    private String status;

    private Long videoId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT")
    private Date createTime;

    @TableField(exist = false)
    private UserInfo userInfo;
}
