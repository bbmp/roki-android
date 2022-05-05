package com.robam.roki.ui.helper3.http;

import java.util.Map;

public interface IRequest {
    String POST = "POST";
    String GET = "GET";

    /**
     * 请求方式
     *
     * @param method
     */
    void setMethod(String method);

    /**
     * 请求头
     *
     * @param key
     * @param value
     */
    void setHeader(String key, String value);

    /**
     * 请求体
     *
     * @param key
     * @param value
     */
    void setBody(String key, String value);

    /**
     * 执行URL
     *
     * @return
     */
    String getUrl();
    /**
     *获取请求头部
     */
    Map<String,String> getHeader();

    /**
     * 提供执行库请求参数
     */

    Object getBody();

}
