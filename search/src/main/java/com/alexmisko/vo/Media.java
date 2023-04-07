package com.alexmisko.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Media {

    private Long id;

    private Long videoId;
    
    private String url;
    
    private String type;
    
    private String digest;

    private String thumb;
}
