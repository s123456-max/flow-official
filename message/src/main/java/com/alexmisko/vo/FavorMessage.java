package com.alexmisko.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavorMessage {

    private Long senderId;

    private Long receiverId;
    
    private FavorMessageDetail message;

}
