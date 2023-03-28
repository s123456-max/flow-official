package com.alexmisko.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("flow_video_tag")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long videoId;

    private String tag;
    
}
