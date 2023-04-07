package com.alexmisko.vo;

import com.alexmisko.vo.Tag;
import com.alexmisko.vo.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {

    private Long id;

    private Long userId;

    private String description;

    private String longitude;

    private String latitude;

    private String location;
    
    private Long favorNum;

    private Date createTime;

    private Date updateTime;

    private UserInfo userInfo;

    private List<Media> mediaList;

    private List<Tag> tagList;

    private String isFavor;

    private String isFollow;
}
