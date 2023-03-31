package com.alexmisko.pojo;


import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.alexmisko.vo.User;

import lombok.Data;

@Data
@Document(indexName = "search")
public class Search {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text)
    private String username;

    @Field(type = FieldType.Text)
    private String tags;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date createTime;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date updateTime;
}
