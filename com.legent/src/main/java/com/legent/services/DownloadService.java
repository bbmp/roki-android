package com.legent.services;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.events.ActivityResultOnPageEvent;
import com.legent.utils.api.PackageUtils;
import com.legent.utils.api.PermissionUtils;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class DownloadService {

    private final static String SubPath = Environment.DIRECTORY_DOWNLOADS;

    public interface DownloadListener {
        void onDownloadCompleted(Uri uri);
    }

    static public DownloadTask newFileDownloadTask(Context cx, String key,
                                                   String url, DownloadListener listener) {
        return new DownloadTask(cx, key, url, listener);
    }

    static public AppDownloadTask newAppDownloadTask(Context cx, String key,
                                                     String url) {

        return new AppDownloadTask(cx, key, url);
    }

    static public class AppDownloadTask extends DownloadTask {

        public AppDownloadTask(Context cx, String key, String downUrl) {
            super(cx, key, downUrl, null);
        }

        @Override
        protected void onFinished(Uri uri) {
            if (uri == null)
                return;
            PackageUtils.installApk(cx, uri);
        }
    }

    static public class DownloadTask extends BroadcastReceiver {

        Context cx;
        DownloadManager dm;
        SharedPreferences prefs;

        String key;
        String downUrl;

        boolean hasPermit;
        DownloadListener listener;

        public DownloadTask(Context cx, String key, String downUrl,
                            DownloadListener listener) {
            this.cx = cx;
            this.key = key;
            this.downUrl = downUrl;
            this.listener = listener;

            dm = (DownloadManager) cx
                    .getSystemService(Context.DOWNLOAD_SERVICE);
            prefs = PreferenceManager.getDefaultSharedPreferences(cx);

            hasPermit = PermissionUtils.checkPermission(cx,
                    PermissionUtils.ACCESS_DOWNLOAD_MANAGER
            );
        }

        @Override
        public void onReceive(Context cx, Intent intent) {
            File targetApkFile = null;
            // ???????????????????????????id??????????????????????????????????????????????????????????????????????????????????????????
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if (prefs.getLong(key, 0) == id) {
                if (intent.getAction().equals(
                        DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    cx.unregisterReceiver(this);
                    if (Build.VERSION.SDK_INT >= 23) {
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(id);
                        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
                        Cursor cur = dm.query(query);
                        if (cur != null) {
                            if (cur.moveToFirst()) {
                                String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                                if (!TextUtils.isEmpty(uriString)) {
                                    targetApkFile = new File(Uri.parse(uriString).getPath());
                                }
                            }
                            cur.close();
                        }
                        onFinished(Uri.fromFile(targetApkFile));
                    } else {
                        Uri uri = dm.getUriForDownloadedFile(id);
                        onFinished(uri);
                    }
                }
            }
        }

        protected void onFinished(Uri uri) {
            if (listener != null) {
                listener.onDownloadCompleted(uri);
            }
        }

        public void download(String fileName) throws Exception {
            download(fileName, null);
        }

        public void download(String fileName, String titleOnNotification)
                throws Exception {

            if (hasPermit)
                download(fileName, titleOnNotification, null);
            else
                downloadByHttp(fileName);
        }

        public void download(String fileName, String titleOnNotification,
                             String descriptionOnNotification) throws Exception {

            Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
            if (materialDialog == null) {
                materialDialog = new ProgressDialog(cx);
                materialDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                materialDialog.setTitle("??????????????????");
                materialDialog.setMessage("?????????...");
                materialDialog.setProgress(0);
                materialDialog.show();
            }

            String url = downUrl;
            Uri uri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(Request.NETWORK_MOBILE
                    | Request.NETWORK_WIFI);

            request.allowScanningByMediaScanner();
            request.setAllowedOverRoaming(true);
            // ??????????????????
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            String mimeString = mimeTypeMap
                    .getMimeTypeFromExtension(MimeTypeMap
                            .getFileExtensionFromUrl(url));
            request.setMimeType(mimeString);
            // ?????????????????????
            request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
            // request.setShowRunningNotification(true);// ?????????true?????????false????????????
            request.setVisibleInDownloadsUi(true);

            request.setTitle(titleOnNotification);
            request.setDescription(descriptionOnNotification);

            // sdcard???????????????download?????????
            request.setDestinationInExternalPublicDir(SubPath, fileName);

            cx.registerReceiver(this, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));

            dm = (DownloadManager) cx
                    .getSystemService(Context.DOWNLOAD_SERVICE);

            try {
                long id = dm.enqueue(request);
                lastDownloadId = id;
                // ??????id
                prefs = PreferenceManager.getDefaultSharedPreferences(cx);
                prefs.edit().putLong(key, id).commit();

                //10.???????????????????????????????????????
                downloadObserver = new DownloadChangeObserver(null);
                cx.getContentResolver().registerContentObserver(CONTENT_URI, true, downloadObserver);

            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

        private DownloadChangeObserver downloadObserver;
        private long lastDownloadId = 0;
        //private MaterialDialog materialDialog;
        private ProgressDialog materialDialog;


        //????????????????????????
        class DownloadChangeObserver extends ContentObserver {

            public DownloadChangeObserver(Handler handler) {
                super(handler);
            }

            @Override
            public void onChange(boolean selfChange) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(lastDownloadId);
                DownloadManager dManager = (DownloadManager) cx.getSystemService(Context.DOWNLOAD_SERVICE);
                final Cursor cursor = dManager.query(query);
                if (cursor != null && cursor.moveToFirst()) {
                    final int totalColumn = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                    final int currentColumn = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                    int totalSize = cursor.getInt(totalColumn);
                    int currentSize = cursor.getInt(currentColumn);
                    float percent = (float) currentSize / (float) totalSize;
                    final Integer progress = Math.round(percent * 100);
                    materialDialog.setProgress(progress);
                    if (progress == 100) {
                        materialDialog.dismiss();
                    }
                }
            }
        }


        private void downloadByHttp(String fileName) {

            RestfulService.getInstance().downFile(downUrl, fileName,
                    new Callback<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            onFinished(uri);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });

        }
    }








}
