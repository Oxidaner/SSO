package com.sso.sso_java.pojo;


import lombok.Data;

@Data
public class ResultData<T> {
    private int code;
    private String message;
    private T data;
    
    public static <T> ResultData<T> success(T data,String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(1);
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> fail(T data,String message) {
        ResultData<T> resultData = new ResultData<>();
        resultData.setCode(0);
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }
}
