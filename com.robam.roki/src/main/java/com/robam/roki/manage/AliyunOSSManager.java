package com.robam.roki.manage;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.robam.roki.MobApp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AliyunOSSManager {
    private static AliyunOSSManager mOSSUtils;
    private UploadListener listener;
    private static OSS oss;
    private static String BUCKET_NAME = "roki" ;
    /**
     * 单一实例
     */
    public static AliyunOSSManager getInstance() {

        if (mOSSUtils == null) {
            synchronized (AliyunOSSManager.class) {
                if (mOSSUtils == null) {
                    mOSSUtils = new AliyunOSSManager();
                }

            }
        }
        return mOSSUtils;
    }

    public AliyunOSSManager() {
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        String stsServer = "http://121.199.11.71:9002/rest/dms/api/oss/token";
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // connction time out default 15s
        conf.setSocketTimeout(15 * 1000); // socket timeout，default 15s
        conf.setMaxConcurrentRequest(5); // synchronous request number，default 5
        conf.setMaxErrorRetry(2); // retry，default 2
        OSSLog.enableLog(); //write local log file ,path is SDCard_path\OSSLog\logs.csv
//        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider("<StsToken.AccessKeyId>", "<StsToken.SecretKeyId>", "<StsToken.SecurityToken>");
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);

         oss = new OSSClient(MobApp.getInstance() , endpoint, credentialProvider, conf);
    }

    /**
     * 上传单个文件
     *
     * @param name
     * @param localPath
     */

    public String uploadFile(String name, String localPath) {
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME,  name, localPath);
        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);

            }
        });

        try {
            // 开始同步上传
            PutObjectResult result = oss.putObject(put);

            Log.d("uploadFile" , "upload: result=" + result);
            // 得到一个外网可访问的地址
            String url = oss.presignPublicObjectURL(BUCKET_NAME, name);
//            if (listener != null)
//                listener.onUpLoadComplete(url);
            // 格式打印输出
            Log.e("uploadFile" ,"--------------同步上传：" + url + "-----------");
            Log.d("uploadFile" , String.format("PublicObjectURL:%s", url));
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

    //上传多个文件
    public void upLoadMultipleFile(String name, String path) {
        // 构造上传请求。
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, name, path);

        // 异步上传时可以设置进度回调。
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d("PutObject", "UploadSuccess");
                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
                String url = oss.presignPublicObjectURL(BUCKET_NAME, name);

                Log.e("图片地址--------------"  , url + "-----------------");
                if (listener != null)
                    listener.onUpLoadComplete(url);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        // task.cancel(); // 可以取消任务。
        // task.waitUntilFinished(); // 等待上传完成。

    }


    public void setUpLoadListener(UploadListener listener) {
        this.listener = listener;
    }

    /**
     * 下载文件
     *
     * @param name
     */


    public void downLoadFile(String name) {
        // 构造下载文件请求。
        GetObjectRequest get = new GetObjectRequest(BUCKET_NAME, name);
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功。
                Log.d("asyncGetObject", "DownloadSuccess");
                Log.d("Content-Length", "" + result.getContentLength());

                InputStream inputStream = result.getObjectContent();
                byte[] buffer = new byte[2048];
                int len;

                try {
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 您可以在此处编写代码来处理下载的数据。

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            // GetObject请求成功，将返回GetObjectResult，其持有一个输入流的实例。返回的输入流，请自行处理。
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常。
                if (clientExcepion != null) {
                    // 本地异常，如网络异常等。
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常。
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        // task.cancel(); // 可以取消任务。
        // task.waitUntilFinished(); // 等待任务完成。
    }


    public interface UploadListener {

        void onUpLoadComplete(String url);

    }

    /**
     * 获取OSS上图片地址名
     * @return
     */
    public static String getOSSImgName(String workGuid , String name){
        String imgPath =    "device/local/img/" + workGuid + File.separator + name + ".jpg";
        return imgPath ;
    }


    /**
     * 获取OSS上图片地址名
     * @return
     */
    public static String getOSSVoiceName(String workGuid , String name){
        String imgPath =    "device/local/voice/" + workGuid + File.separator + name + ".mp3";
        return imgPath ;
    }

    /**
     * 获取OSS上视频地址名
     * @return
     */
    public  static String getOSSVideoName( String workGuid){
        String imgPath =    "device/local/video/" + workGuid + ".mp4";
        return imgPath ;
    }
}
