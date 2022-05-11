package com.legent.services;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.legent.ContextIniter;
import com.legent.Helper;
import com.legent.utils.FileUtils;
import com.legent.utils.StreamUtils;
import com.legent.utils.api.AppUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.StorageUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.converter.JacksonConverter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestfulService extends AbsService {

    public final static int CONNECT_TIMEOUT =10;
    public final static int READ_TIMEOUT=10;
    public final static int WRITE_TIMEOUT=10;

    static final public String TAG = "rest";
    static private RestfulService instance = new RestfulService();

    synchronized static public RestfulService getInstance() {
        return instance;
    }

    String defaultHost;
    OkHttpClient client;
    boolean isSsl;
    private Retrofit retrofit;
    Map<String, RestAdapter> map = Maps.newHashMap();

    private RestfulService() {
        client = new OkHttpClient();
        // 设置okHttp 超时时间，add by liyuebiao 2016-12-14
        client.setReadTimeout(READ_TIMEOUT,TimeUnit.SECONDS);//设置读取超时时间
        client.setWriteTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//设置写的超时时间
        client.setConnectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS);//设置连接超时时间
        if (isSsl) {
            enableSSL(client);
        }
    }

    /**
     * 设置默认restful服务器
     *
     * @param host restful服务器。 形如 http://api.ismal.cn 或 http://api.ismal.cn:80 或
     *             http://api.ismal.cn:80/rest
     */
    public void setDefaultHost(String host) {
        Log.v(TAG, host);
        defaultHost = host;
        getAdapter(host);
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(defaultHost)
                .build();
    }

    public OkHttpClient getClient() {
        return client;
    }

    public <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
//        return createApi(defaultHost, clazz);
    }

    public <T> T createApi(String host, Class<T> clazz) {
        RestAdapter adapter = getAdapter(host);
        return adapter.create(clazz);
    }

    synchronized private RestAdapter getAdapter(String host) {
        if (map.containsKey(host)) {
            return map.get(host);
        } else {
            MyCookieManager ccm = new MyCookieManager(host);
            CookieHandler.setDefault(ccm);
//            ccm.setCookies(null);

            RestAdapter.Builder builder = new RestAdapter.Builder();

            ObjectMapper objMapper = new ObjectMapper();
            objMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            builder.setConverter(new JacksonConverter(objMapper));
            builder.setClient(new OkClient(client));
            builder.setEndpoint(host);
            builder.setRequestInterceptor(ccm.COOKIES_REQUEST_INTERCEPTOR);
            builder.setLog(restLog);
            builder.setLogLevel(AppUtils.isDebug(cx) ? RestAdapter.LogLevel.FULL
                    : RestAdapter.LogLevel.NONE);
            RestAdapter adapter = builder.build();

            map.put(host, adapter);
            return adapter;
        }
    }

    public void downFile(String url, final String fileName,
                         final com.legent.Callback<Uri> callback) {
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                Helper.onFailure(callback, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream in = response.body().byteStream();
                // Read the data from the stream

                String filePath = String.format("%s/%s",
                        StorageUtils.getCachPath(cx), fileName);

                FileUtils.writeFile(filePath, in);
                Uri uri = Uri.fromFile(new File(filePath));
                Helper.onSuccess(callback, uri);
            }
        });
    }

    private void enableSSL(OkHttpClient client) {
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (Exception e) {
            // The system has no TLS. Just give up.
            e.printStackTrace();
        }

    }


    // -------------------------------------------------------------------------------
    // RestAdapter.Log
    // -------------------------------------------------------------------------------

    private RestAdapter.Log restLog = new RestAdapter.Log() {
        @Override
        public void log(String msg) {
            String[] blacklist = {"Access-Control", "Cache-Control", "Connection", "Content-Type",
                    "Keep-Alive", "Pragma", "Server", "Vary", "X-Powered-By",
                    "Content-Length", "Date", "Transfer-Encoding", "OkHttp",
                    "---> END", "<--- HTTP 200", "<--- END", "status:200"};
            for (String bString : blacklist) {
                if (msg.startsWith(bString)) {
                    return;
                }
            }
            Log.d(TAG, msg);
        }
    };

    // -------------------------------------------------------------------------------
    // CustomCookieManager
    // -------------------------------------------------------------------------------

    class MyCookieManager extends CookieManager {

        private final String SET_COOKIE_KEY = "Set-Cookie";
        private final String SESSION_KEY = "JSESSIONID";

        String host;

        /**
         * Creates a new instance of this cookie manager accepting all cookies.
         */
        public MyCookieManager(String host) {
            super.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            this.host = host;
        }

        @Override
        public void put(URI uri, Map<String, List<String>> stringListMap)
                throws IOException {
            super.put(uri, stringListMap);
            if (stringListMap.get(SET_COOKIE_KEY) != null)
                for (String string : stringListMap.get(SET_COOKIE_KEY)) {
                    if (string.contains(SESSION_KEY)) {
                        setCookies(string);
                    }
                }
        }

        public final RequestInterceptor COOKIES_REQUEST_INTERCEPTOR = new RequestInterceptor() {

            private final String Cookie_Header = "Cookie";

            @Override
            public void intercept(RequestFacade request) {

                String cookies = getCookies();
                if (!Strings.isNullOrEmpty(cookies)) {
                    request.addHeader(Cookie_Header, cookies);
                }
            }
        };

        public void setCookies(String cookies) {
            PreferenceUtils.setString(getID(), cookies);
        }

        public String getCookies() {
            return PreferenceUtils.getString(getID(), null);
        }


        private String getID() {
            return String.format("%s:%s", host, "COOKIE");
        }
    }


    // -------------------------------------------------------------------------------
    //  static
    // -------------------------------------------------------------------------------

    static public void printJson(retrofit.client.Response res) {
        if (ContextIniter.context != null && !AppUtils.isDebug(ContextIniter.context))
            return;

        try {
            Log.i(TAG,
                    String.format("status:%s reason:%s\nurl:%s",
                            res.getStatus(), res.getReason(), res.getUrl()));
            if (res.getBody() == null)
                return;

            String json = StreamUtils.stream2String(res.getBody().in());
            Log.v(TAG, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void printError(RetrofitError e) {
        if (ContextIniter.context != null && !AppUtils.isDebug(ContextIniter.context))
            return;

        String err = String.format("url:%s\nerror:%s", e.getUrl(),
                e.getMessage());
        Log.w(TAG, err);
    }

}
