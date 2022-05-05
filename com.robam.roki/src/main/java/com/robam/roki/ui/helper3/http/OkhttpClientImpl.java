package com.robam.roki.ui.helper3.http;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkhttpClientImpl implements IHttpClient{
    OkHttpClient client = new OkHttpClient.Builder()
            .build();

    /**
     * get请求
     *
     * @param request
     * @param froceCache
     */
    @Override
    public IResponse get(IRequest request, boolean froceCache) {
        /**
         * 解析参数  从BaseRequest，转化为OkHttp3需要的业务参数
         */
        request.setMethod(IRequest.GET);
        Map<String, String> header = request.getHeader();
        //创建Builder
        Request.Builder builder = new Request.Builder();
        for (String key : header.keySet()) {
            builder.header(key, header.get(key));//将设置的header信息，设置到Okhttp3的header中
        }
        //添加Url
        builder.url(request.getUrl());
        //构建Request
        Request okRequest = builder.build();
        return execute(okRequest);
    }

    @Override
    public IResponse post(IRequest request, boolean forceCache) {
        /**
         * 请求类型 在上一篇文章中说明过
         */
        MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, request.getBody().toString());
        /**
         * 解析业务参数
         */
        request.setMethod(IRequest.POST);
        Request.Builder builder = new Request.Builder();
        Map<String, String> header = request.getHeader();
        for (String key : header.keySet()) {
            builder.header(key, header.get(key));
        }
        Request okRequest = builder.url(request.getUrl())
                .post(body)
                .build();

        return execute(okRequest);


    }

    /**
     * 具体的执行过程
     * 注意：使用的同步请求
     */
    private IResponse execute(Request request) {
        BaseResponse baseResponse = new BaseResponse();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            //设置状态码
            baseResponse.setCode(response.code());
            //设置返回数据
            baseResponse.setData(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
            //异常信息  自定义一个异常信息(可配置)
//            baseResponse.setCode(baseResponse.UNKNOW_ERROR);
            baseResponse.setData(response.message());
        }

        return baseResponse;

    }


}
