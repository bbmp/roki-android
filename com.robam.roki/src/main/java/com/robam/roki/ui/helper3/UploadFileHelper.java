package com.robam.roki.ui.helper3;

import static com.robam.common.RobamApp.BANNERURL;

import android.app.Application;
import android.os.Handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.utils.LogUtils;
import com.robam.common.io.cloud.IRokiRestService;
import com.robam.common.io.cloud.Reponses;
import com.robam.roki.ui.bean3.BannerBean;
import com.robam.roki.ui.helper3.http.BaseRequest;
import com.robam.roki.ui.helper3.http.IRequest;
import com.robam.roki.ui.helper3.http.IResponse;
import com.robam.roki.ui.helper3.http.OkhttpClientImpl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogRecord;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author r210190
 */
public class UploadFileHelper {
    static Handler handler = new Handler();

    public static void upload(String type,File file , Callback<Reponses.UploadRepones> call){
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String result = uploadFile(type,file);
                if (result != null){
                    call.onSuccess(new Gson().fromJson(result, Reponses.UploadRepones.class));
                }else {
                    call.onFailure(new Throwable());
                }
            }
        });
    }
    public static String uploadFile(String type,File file){
        String result = null ;
        String path = Plat.serverOpt.getRestfulBaseUrl() + IRokiRestService.upLoad;
//        String path = "http://develop.api.myroki.com:8081/rest/ops/api/file/upload";      //上传接口
        OkHttpClient client = getUnsafeOkHttpClient();          //设置跳过所有ssl认证

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8") , file);
        String name = file.getName();          //文件名称
        try {
            name = URLEncoder.encode(name, "UTF-8");                 //文件名称编码，防止出现中文乱码
        } catch (UnsupportedEncodingException e1) {
            //TODO
        }

        //定义请求体，前面三个为表单中的string类型参数，第四个为需要上传的文件
        String phone = Plat.accountService.getCurrentUser().getPhone2();
        MultipartBody mBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("type" , type)
                .addFormDataPart("phone" , phone)
                .addFormDataPart("platForm" , "android")
//                .addFormDataPart("type" , type)
                .addFormDataPart("file" , name , fileBody)
                .build();

        //设置为post请求，url后面为请求路径，header设置请求头（可以设置多个），post后面设置请求体
        Request request = new Request.Builder().url(path).post(mBody).build();
        //执行请求操作
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()){
                result = response.body().string();
                LogUtils.i("upload" , "获取到的响应信息为： "+result);
//                Reponses.UploadRepones uploadRepones = new Gson().fromJson(result, Reponses.UploadRepones.class);
            }else {

            }
            response.body().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result ;
    }


    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            // TODO Auto-generated method stub
                            return new X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(500, TimeUnit.MILLISECONDS)
                    .readTimeout(500, TimeUnit.MILLISECONDS);
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    // TODO Auto-generated method stub
                    return true;
                }
            });
            return builder.build();
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    public static void getBanner(Callback<BannerBean> call) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final OkhttpClientImpl httpClient = new OkhttpClientImpl();
                    String BannerUrl=BANNERURL;
//                    String BannerUrl = "http://develop.api.myroki.com/api-cook-manage/cook-manage-admin/api/ops/carousel/show";
//                    String BannerUrl = "https://cook.myroki.com/api-cook-manage/cook-manage-admin/api/ops/carousel/show";
//                    final IRequest request = new BaseRequest(testBannerUrl);
                    final IRequest request = new BaseRequest(BannerUrl);
                    IResponse response = httpClient.get(request, false);
                    LogUtils.i("banner", "response=" + response.getData());
                    if (response != null && response.getCode() == 200) {
                        if (response.getData() != null) {
                            BannerBean bannerBean = new Gson().fromJson(response.getData(), BannerBean.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    call.onSuccess(bannerBean);
                                }
                            });

                        } else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    call.onFailure(new Throwable());
                                }
                            });
                        }

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                call.onFailure(new Throwable());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.getMessage();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            call.onFailure(e);
                        }
                    });
                }
            }
        });
    }

    private static void success() {

    }
}
