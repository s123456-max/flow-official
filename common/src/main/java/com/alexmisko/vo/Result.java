package com.alexmisko.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    private String code;

    private String msg;

    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>("200","success",data);
    }
    public static <T> Result<T> success(String message, T data) {
        return new Result<>("200",message,data);
    }
    public static <T> Result<T> success(String message) {
        return new Result<>("200",message,null);
    }
    public static <T> Result<T>  fail(String code, String msg) {
        return new Result<>(code,msg,null);
    }
}