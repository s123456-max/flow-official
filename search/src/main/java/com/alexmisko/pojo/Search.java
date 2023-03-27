package com.alexmisko.pojo;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
@Document(indexName = "search")
public class Search {

    @Id
    private Long id;

    @Field(type = FieldType.Long)
    private Long userId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String Description;

    @Field(type = FieldType.Text)
    private String label;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date createTime;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Date updateTime;
}
