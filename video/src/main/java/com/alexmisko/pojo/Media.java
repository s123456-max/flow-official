package com.alexmisko.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("flow_video")
public class Media {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long videoId;
    
    private String url;
    
    private String type;
    
    private String digest;

    private String thumb;
}
