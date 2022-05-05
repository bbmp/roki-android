package com.robam.roki.ui.helper3.http;

public class BaseResponse implements IResponse{
    //响应码
    private int code;
    //响应数据
    private String data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getData() {
        return data;
    }
}
