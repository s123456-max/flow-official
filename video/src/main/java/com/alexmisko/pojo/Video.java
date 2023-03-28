package com.alexmisko.pojo;

import com.alexmisko.vo.Tag;
import com.alexmisko.vo.UserInfo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("flow_user_video")
public class Video {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String description;

    private String longitude;

    private String latitude;

    private String location;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private UserInfo userInfo;

    @TableField(exist = false)
    private List<Media> mediaList;

    @TableField(exist = false)
    private List<Tag> tagList;
}
