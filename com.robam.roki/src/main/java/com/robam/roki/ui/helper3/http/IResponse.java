package com.robam.roki.ui.helper3.http;

public interface IResponse {
    /**
     * 状态码
     * @return
     */
    int  getCode();
    /**
     * 数据
     */
    String  getData();
}
