package com.alexmisko.vo;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavorMessageDetail {

    private Long videoId;

    private String type;
    
    private String content;

    private UserInfo userInfo;

    private Date createTime;

}
