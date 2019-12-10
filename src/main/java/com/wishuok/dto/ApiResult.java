package com.wishuok.dto;

public class ApiResult {
    private Boolean isSuccess;

    private int code;

    private String message;

    private Object data;

    public void setIsSuccess(Boolean isSuccess){
        this.isSuccess = isSuccess;
    }
    public Boolean getIsSuccess(){
        return this.isSuccess;
    }
    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setData(Object data){
        this.data = data;
    }
    public Object getData(){
        return this.data;
    }

    public static ApiResult Fail(int code, String msg){
        ApiResult ret = new ApiResult();
        ret.setCode(code);
        ret.setIsSuccess(false);
        ret.setMessage(msg);
        return ret;
    }

    public static ApiResult Success(String msg, Object data){
        ApiResult ret = new ApiResult();
        ret.setCode(0);
        ret.setIsSuccess(true);
        ret.setMessage(msg);
        ret.setData(data);
        return ret;
    }

    public static ApiResult Success(String msg){
        return Success(msg, null);
    }
}
