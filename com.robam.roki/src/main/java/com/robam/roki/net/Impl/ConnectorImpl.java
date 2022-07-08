package com.robam.roki.net.Impl;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.legent.plat.Plat;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.net.Connector;
import com.robam.roki.net.JsonParse;
import com.robam.roki.net.JsonParseFactory;
import com.robam.roki.net.OnRequestCallback;
import com.robam.roki.net.model.ResponseInfo;
import com.robam.roki.request.bean.FlavouringBean;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ConnectorImpl implements Connector {
    private static Connector mConnector;
    private JsonParse mJsonParse;
    private  OkHttpClient mFileOkHttpClient;
    private  OkHttpClient mLongOkHttpClient;
    private  OkHttpClient mOkHttpClient;
    private  Context mContext;

    private Handler mHandler;
    private final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private ConnectorImpl(Context paramContext)
    {
        try
        {
            this.mContext = paramContext.getApplicationContext();
//            SSLSocketFactory[] mSSLSocketFactory = new SSLSocketFactory[1];
//            X509TrustManager[] arrayOfX509TrustManager = new X509TrustManager[1];
//            SSLManager.init(paramContext, arrayOfX509TrustManager);
            Cache localCache = new Cache(getCacheDir(this.mContext), 10485760L);
            this.mOkHttpClient = new OkHttpClient.Builder().connectTimeout(15000L, TimeUnit.MILLISECONDS).writeTimeout(15000L, TimeUnit.MILLISECONDS).readTimeout(15000L, TimeUnit.MILLISECONDS)
                    .cache(localCache).build();
            this.mLongOkHttpClient = new OkHttpClient.Builder().connectTimeout(600000L, TimeUnit.MILLISECONDS)
                    .writeTimeout(600000L, TimeUnit.MILLISECONDS).readTimeout(600000L, TimeUnit.MILLISECONDS)
                    .cache(localCache).build();
            this.mFileOkHttpClient = new OkHttpClient.Builder().connectTimeout(600000L, TimeUnit.MILLISECONDS)
                    .writeTimeout(600000L, TimeUnit.MILLISECONDS).readTimeout(600000L, TimeUnit.MILLISECONDS)
                    .cache(localCache).build();
            this.mJsonParse = JsonParseFactory.getInstance();
            this.mHandler = new Handler(Looper.getMainLooper());
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private File getCacheDir(Context mContext) {
        String path;
        if (Environment.getExternalStorageState().equals("mounted")) {
            path = mContext.getExternalCacheDir().getPath();
        } else {
            path = mContext.getCacheDir().getPath();
        }
        return new File(path, "okHttpCacheFile");
    }

    public static Connector getConnector(Context paramContext)
    {
        try
        {
            if (mConnector == null) {
                mConnector = new ConnectorImpl(paramContext);
            }
            return mConnector;
        }
        finally {}
    }


    @Override
    public int doPost(int requestCode, String url,
                      Object paramClass,  Class<?> paramType, OnRequestCallback paramOnRequestCallback
            , Class<?> responseInfoClass) {
        return post(requestCode,url,paramClass,paramType,paramOnRequestCallback,false);
    }

    @Override
    public int doGet(int requestCode, String url, Map<String,Object> request, Class<?> paramType,
                     OnRequestCallback paramOnRequestCallback, Class<?> responseInfoClass) {
        return get(requestCode,url,request,paramType,paramOnRequestCallback,responseInfoClass);
    }

    @Override
    public int doFilePost(int requestCode, String url, Map<String,Object> request, Class<?> paramType,
                          OnRequestCallback mOnRequestCallback, Class<?> responseInfoClass) {
        return uploadFile(requestCode,url,request,paramType,mOnRequestCallback,responseInfoClass);
    }

    @Override
    public int doDel(int requestCode, String url, Map<String, Object> request, Class<?> paramType, OnRequestCallback paramOnRequestCallback, Class<?> responseInfoClass) {
        return del(requestCode,url,request,paramType,paramOnRequestCallback,responseInfoClass);
    }

    private int uploadFile(int requestCode, String url, Map<String,Object> request, Class<?> paramType
            , OnRequestCallback OnRequestCallback, Class<?> responseInfoClass){

        File mFile=new File(request.get("path").toString());
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",mFile.getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                mFile))
                .addFormDataPart("type","1")
                .build();
        Request request1 = new Request.Builder()
                .url(Plat.serverOpt.getRestfulBaseUrl().replace("3","4")+url)
                .method("POST", body)
                .build();
//        Response response = client.newCall(request).execute();
        mLongOkHttpClient.newCall(request1).enqueue(new HttpCallback(
                requestCode, responseInfoClass,
                paramType, OnRequestCallback));



        return requestCode;

    }


    private int get(int requestCode, String url, Map<String,Object> requestParam, Class<?> paramType,OnRequestCallback paramOnRequestCallback
            , Class<?> responseInfoClass){

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.tag(paramOnRequestCallback.getClass().getSimpleName());
       Request request = requestBuilder.url(Plat.serverOpt.getRestfulBaseUrl()+url).build();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        urlBuilder=setGetRequest(urlBuilder,requestParam);
        requestBuilder.url(urlBuilder.build());
        mLongOkHttpClient.newCall(requestBuilder.build()).enqueue(new HttpCallback(requestCode, responseInfoClass,
                paramType, paramOnRequestCallback));
        return requestCode;

    }

    private int del(int requestCode, String url, Map<String,Object> requestParam, Class<?> paramType,OnRequestCallback paramOnRequestCallback
            , Class<?> responseInfoClass){

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.tag(paramOnRequestCallback.getClass().getSimpleName());
        FormBody body = new FormBody.Builder().build();
        requestBuilder.delete(body);
        Request request = requestBuilder.url(Plat.serverOpt.getRestfulBaseUrl()+url).build();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        urlBuilder=setGetRequest(urlBuilder,requestParam);
        requestBuilder.url(urlBuilder.build());
        mLongOkHttpClient.newCall(requestBuilder.build()).enqueue(new HttpCallback(requestCode, responseInfoClass,
                paramType, paramOnRequestCallback));
        return requestCode;

    }

    private HttpUrl.Builder setGetRequest(HttpUrl.Builder requestBuilder, Map<String, Object> params) {
        for(Map.Entry<String, Object> param : params.entrySet()) {
            requestBuilder.addQueryParameter(param.getKey(),param.getValue().toString());
        }
        Log.e(TAG,"Request:"+requestBuilder.toString());
         return requestBuilder;

    }

    private Request.Builder setRequest(Request.Builder paramBuilder,String paramRequest)


    {
            Log.e(TAG,"Request:"+paramRequest);
            paramBuilder.addHeader("content-type", "application/json");
            paramBuilder.post(RequestBody.create(MEDIA_TYPE_JSON, paramRequest));
//        paramBuilder.addQueryParameter();
            return paramBuilder;
        }

    private int post(int requestCode, String url,
                     Object paramClass,  Class<?> paramType, OnRequestCallback paramOnRequestCallback,boolean isLong)
    {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.tag(paramOnRequestCallback.getClass().getSimpleName());
        Log.e(TAG,"url"+Plat.serverOpt.getRestfulBaseUrl() +url);
        requestBuilder.url(Plat.serverOpt.getRestfulBaseUrl() +url);

        String json=mJsonParse.toJson(paramClass);
        Log.e(TAG,"request:"+json);
        requestBuilder=setRequest(requestBuilder,json);
        if (isLong)
        {
            this.mLongOkHttpClient.newCall(requestBuilder.build()).enqueue(new HttpCallback(requestCode, paramClass, paramType, paramOnRequestCallback));
            return requestCode;
        }
        this.mOkHttpClient.newCall(requestBuilder.build()).enqueue(new HttpCallback(requestCode, paramClass, paramType, paramOnRequestCallback));
        return requestCode;
    }



    private static final String TAG = "ConnectorImpl";
    private class HttpCallback
            implements Callback
    {
        private OnRequestCallback callback;
        private  Class<?> dataType;
        private int requestCode;
        private int requestId;


        public HttpCallback(int requestId, Object paramClass, Class<?> paramType, OnRequestCallback paramOnRequestCallback)
        {
            this.requestId = requestId;
            this.requestCode = 1;

            this.dataType = paramType;

            this.callback = paramOnRequestCallback;
        }

        private String getSuccessJson(String paramString)
        {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("{\n  \"data\": {\n    \"token\": \"");
            localStringBuilder.append(paramString);
            localStringBuilder.append("\"\n  },\n  \"msg\": \"成功\",\n  \"success\": true\n}");
            return localStringBuilder.toString();
        }

        public void onFailure(Call paramCal1l, IOException paramIOException)
        {

            Log.e(TAG,"onFailure");
            if (!paramCal1l.isCanceled())
            {
                if (this.callback == null) {
                    return;
                }
                String res = ConnectorImpl.this.mContext.getString(R.string.base_req_failed);
                int i=0;
                if ((paramIOException instanceof SocketTimeoutException)) {
                    res = ConnectorImpl.this.mContext.getString(R.string.base_req_timeout);

                }
                ConnectorImpl.this.mHandler.post(
                        new ConnectorImpl.UIRunnable(
                                 this.requestId, this.requestCode,res, String.valueOf(i), this.callback, true));
                return;
            }
        }


        public void onResponse(Call paramCall, @NonNull okhttp3.Response response) throws IOException {

            String repos=response.body().string();
            Log.e(TAG,"Response"+repos);
            if (response.isSuccessful()&&!TextUtils.isEmpty(repos)){


                Object mObject= JSONObject.parse(repos);



                    if (mObject instanceof JSONObject) {
                        JSONObject jsonObject1 = JSONObject.parseObject(repos);
                        String rc = jsonObject1.getString("rc");
                        String msg = jsonObject1.getString("msg");
                        Log.e("测试", rc + "----" + msg);
                        if (rc != null && Integer.parseInt(rc) == 0) {
                            Object object = mJsonParse.fromJson(repos, dataType);
                            ConnectorImpl.this.mHandler.post(
                                    new ConnectorImpl.UIRunnable(
                                            this.requestId, this.requestCode, "res", object, this.callback, false));
                        } else {
                            if (jsonObject1.getString("userId")!=null&&jsonObject1.getString("nickname")!=null){
                                Object object = mJsonParse.fromJson(repos, dataType);
                                ConnectorImpl.this.mHandler.post(
                                        new ConnectorImpl.UIRunnable(
                                                this.requestId, this.requestCode, "res", object, this.callback, false));
                            }else {
                                ConnectorImpl.this.mHandler.post(
                                        new ConnectorImpl.UIRunnable(
                                                this.requestId, this.requestCode, msg, Integer.getInteger(rc), this.callback, true));
                            }

                        }
                    } else if (mObject instanceof JSONArray) {
//                    Class<?> paramClass=String.class;
                        Object object = JSON.parseArray(repos, dataType);
                        ConnectorImpl.this.mHandler.post(
                                new ConnectorImpl.UIRunnable(
                                        this.requestId, this.requestCode, "res", object, this.callback, false));
                    }


            }else{
                ConnectorImpl.this.mHandler.post(
                        new ConnectorImpl.UIRunnable(
                                this.requestId, this.requestCode,"请求失败", null, this.callback, true));
            }



        }
    }
    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private String getFieldValueByFieldName( Object object,String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return  (String)field.get(object);
        } catch (Exception e) {

            return null;
        }
    }




    private class UIRunnable
            implements Runnable
    {
        private OnRequestCallback callback;
        private Object data;
        private boolean isError;
        private int requestCode;
        private int requestId;
        private String responseCode;

        public UIRunnable(int requestId, int requestCode, String responseCode, Object data,
                          OnRequestCallback paramOnRequestCallback, boolean paramBoolean)
        {
            this.requestId = requestId;
            this.requestCode = requestCode;
            this.responseCode = responseCode;
            this.data = data;
            this.callback = paramOnRequestCallback;
            this.isError = paramBoolean;
        }

        public void run()
        {
            Log.e(TAG,"run");
            Object Localdata = this.data;
            if ((Localdata != null) && ((Localdata instanceof String))) {
                Localdata = Localdata.toString();
            } else {
                Localdata = null;
            }
            if (!this.isError)
            {
                Log.e(TAG,"onSuccess");
                if (callback!=null) {
                    this.callback.onSuccess(this.requestId, this.requestCode, this.data);
                }
                return;
            }
            Log.e(TAG,"onFailure");
            if (callback!=null) {
                this.callback.onFailure(this.requestId, this.requestCode, this.responseCode, Localdata);
            }
        }
    }
}
