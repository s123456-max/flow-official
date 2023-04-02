package com.alexmisko.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

@Data
public class Follow {

    @TableId(type =  IdType.AUTO)
    private Long id;

    private Long follower;

    private Long emperor;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
}