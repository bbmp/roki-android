package com.robam.roki.ui.helper3.http;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class BaseRequest implements IRequest{
    //默认请求POST方式
    private String method = POST;
    private String url;
    private Map<String, String> header;
    private Map<String, String> body;

    public BaseRequest(String url) {
        //初始化公共参数  例如：公共的头部信息
        this.url = url;
        header = new HashMap<>();
        body = new HashMap<>();
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setHeader(String key, String value) {
        header.put(key, value);
    }

    @Override
    public void setBody(String key, String value) {
        body.put(key, value);
    }

    @Override
    public String getUrl() {
        //可配置 ，是具体不同的url做不同的转换
        return url;
    }

    @Override
    public Map<String, String> getHeader() {
        return header;
    }

    @Override
    public Object getBody() {
        if (body != null) {
            return new Gson().toJson(body, HashMap.class);
        } else {
            return "{}";//可配置
        }

    }
}
