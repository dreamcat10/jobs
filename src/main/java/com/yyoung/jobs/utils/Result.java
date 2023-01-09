package com.yyoung.jobs.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -684979447075468480L;

    //状态码
    private Integer code;
    //错误信息
    private String msg;
    //数据
    private T data;

    //成功返回
    public static <T> Result<T> success( T data){
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "成功";
        result.data = data;

        return result;
    }

    //失败返回
    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<>();
        result.code = 500;
        result.msg = msg;
        result.data = null;

        return result;
    }



}
