package com.aliyun.vodplayerview.activity;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.VcPlayerLog;
import com.aliyun.vodplayer.R;
import com.aliyun.vodplayer.downloader.AliyunDownloadConfig;
import com.aliyun.vodplayer.downloader.AliyunDownloadInfoListener;
import com.aliyun.vodplayer.downloader.AliyunDownloadManager;
import com.aliyun.vodplayer.downloader.AliyunDownloadMediaInfo;
import com.aliyun.vodplayer.downloader.AliyunRefreshStsCallback;
import com.aliyun.vodplayer.media.AliyunLocalSource;
import com.aliyun.vodplayer.media.AliyunVidSts;
import com.aliyun.vodplayer.media.IAliyunVodPlayer;
import com.aliyun.vodplayerview.constants.PlayParameter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListAdapter;
import com.aliyun.vodplayerview.playlist.AlivcPlayListManager;
import com.aliyun.vodplayerview.playlist.AlivcVideoInfo;
import com.aliyun.vodplayerview.utils.Commen;
import com.aliyun.vodplayerview.utils.download.DownloadDBHelper;
import com.aliyun.vodplayerview.view.choice.AlivcShowMoreDialog;
import com.aliyun.vodplayerview.view.control.ControlView;
import com.aliyun.vodplayerview.view.download.DownloadDataProvider;
import com.aliyun.vodplayerview.utils.ScreenUtils;
import com.aliyun.vodplayerview.utils.VidStsUtil;
import com.aliyun.vodplayerview.view.download.AddDownloadView;
import com.aliyun.vodplayerview.view.download.AlivcDialog;
import com.aliyun.vodplayerview.view.download.AlivcDialog.onCancelOnclickListener;
import com.aliyun.vodplayerview.view.download.AlivcDialog.onConfirmClickListener;
import com.aliyun.vodplayerview.view.download.AlivcDownloadMediaInfo;
import com.aliyun.vodplayerview.view.download.DownloadChoiceDialog;
import com.aliyun.vodplayerview.view.download.DownloadView;
import com.aliyun.vodplayerview.view.download.DownloadView.OnDownloadViewListener;
import com.aliyun.vodplayerview.view.more.AliyunShowMoreValue;
import com.aliyun.vodplayerview.view.more.ShowMoreView;
import com.aliyun.vodplayerview.view.more.SpeedValue;
import com.aliyun.vodplayerview.view.tipsview.ErrorInfo;
import com.aliyun.vodplayerview.widget.AliyunScreenMode;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView.OnPlayerViewClickListener;
import com.aliyun.vodplayerview.widget.AliyunVodPlayerView.PlayViewType;

/**
 * ?????????????????????????????? Created by Mulberry on 2018/4/9.
 */
public class AliyunPlayerSkinActivity extends AppCompatActivity {

    private DownloadDBHelper dbHelper;
    private AliyunDownloadConfig config;
    private PlayerHandler playerHandler;
    private DownloadView dialogDownloadView;
    private AlivcShowMoreDialog showMoreDialog;
    private boolean isStrangePhone() {
        boolean strangePhone = "mx5".equalsIgnoreCase(Build.DEVICE)
            || "Redmi Note2".equalsIgnoreCase(Build.DEVICE)
            || "Z00A_1".equalsIgnoreCase(Build.DEVICE)
            || "hwH60-L02".equalsIgnoreCase(Build.DEVICE)
            || "hermes".equalsIgnoreCase(Build.DEVICE)
            || ("V4".equalsIgnoreCase(Build.DEVICE) && "Meitu".equalsIgnoreCase(Build.MANUFACTURER))
            || ("m1metal".equalsIgnoreCase(Build.DEVICE) && "Meizu".equalsIgnoreCase(Build.MANUFACTURER));

        VcPlayerLog.e("lfj1115 ", " Build.Device = " + Build.DEVICE + " , isStrange = " + strangePhone);
        return strangePhone;
    }

    private SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SS");
    private List<String> logStrs = new ArrayList<>();

    private AliyunScreenMode currentScreenMode = AliyunScreenMode.Small;
    private TextView tvLogs;
    private TextView tvTabLogs;
    private TextView tvTabDownloadVideo;
    private ImageView ivLogs;
    private ImageView ivDownloadVideo;
    private LinearLayout llClearLogs;
    private RelativeLayout rlLogsContent;
    private RelativeLayout rlDownloadManagerContent;
    private TextView tvVideoList;
    private ImageView ivVideoList;
    private RecyclerView recyclerView;
    private LinearLayout llVideoList;
    private TextView tvStartSetting;

    private DownloadView downloadView;
    private AliyunVodPlayerView mAliyunVodPlayerView = null;

    private DownloadDataProvider downloadDataProvider;
    private AliyunDownloadManager downloadManager;
    private AlivcPlayListAdapter alivcPlayListAdapter;

    private ArrayList<AlivcVideoInfo.Video> alivcVideoInfos;
    private ErrorInfo currentError = ErrorInfo.Normal;
    /**
     * ??????????????????????????????
     */
    private static final int CODE_REQUEST_SETTING = 1000;
    /**
     * ??????????????????????????????, 100???vid??????, 200???url??????
     */
    private static final int CODE_RESULT_TYPE_VID = 100;
    private static final int CODE_RESULT_TYPE_URL = 200;
    private static final String DEFAULT_URL = "http://player.alicdn.com/video/aliyunmedia.mp4";
    private static final String DEFAULT_VID = "6e783360c811449d8692b2117acc9212";
    /**
     * get StsToken stats
     */
    private boolean inRequest;

    private static String[] PERMISSIONS_STORAGE = {
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    /**
     * ??????tab
     */
    private int currentTab = TAB_VIDEO_LIST;
    private static final int TAB_VIDEO_LIST = 1;
    private static final int TAB_LOG = 2;
    private static final int TAB_DOWNLOAD_LIST = 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isStrangePhone()) {
            //            setTheme(R.style.ActTheme);
        } else {
            setTheme(R.style.NoActionTheme);
        }
        copyAssets();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.alivc_player_layout_skin);

        requestVidSts();
        dbHelper = DownloadDBHelper.getDownloadHelper(getApplicationContext(), 1);
        initAliyunPlayerView();
        initLogView();
        initDownloadView();
        initVideoListView();

    }

    private void copyAssets() {
        Commen.getInstance(getApplicationContext()).copyAssetsToSD("encrypt", "aliyun").setFileOperateCallback(

            new Commen.FileOperateCallback() {
                @Override
                public void onSuccess() {
                    config = new AliyunDownloadConfig();
                    config.setSecretImagePath(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/aliyun/encryptedApp.dat");
                    //        config.setDownloadPassword("123456789");
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save/");
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    config.setDownloadDir(file.getAbsolutePath());
                    //????????????????????????
                    config.setMaxNums(2);
                    // ??????AliyunDownloadManager??????
                    downloadManager = AliyunDownloadManager.getInstance(getApplicationContext());
                    downloadManager.setDownloadConfig(config);

                    downloadDataProvider = DownloadDataProvider.getSingleton(getApplicationContext());
                    // ??????sts??????
                    downloadManager.setRefreshStsCallback(new MyRefreshStsCallback());
                    // ?????????????????????
                    downloadManager.setDownloadInfoListener(new MyDownloadInfoListener(downloadView));
                    //
                    downloadViewSetting(downloadView);
                    Log.e("Test", "assets copyt success");
                }

                @Override
                public void onFailed(String error) {
                    Log.e("Test", "assets copyt error, msg:::::" + error);
                }
            });
    }

    private void initAliyunPlayerView() {
        mAliyunVodPlayerView = (AliyunVodPlayerView)findViewById(R.id.video_view);
        //??????????????????
        mAliyunVodPlayerView.setKeepScreenOn(true);
        PlayParameter.PLAY_PARAM_URL = DEFAULT_URL;
        String sdDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test_save_cache";
        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*??????, s */, 300 /*?????????MB*/);
        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue);
        //mAliyunVodPlayerView.setCirclePlay(true);
        mAliyunVodPlayerView.setAutoPlay(true);

        mAliyunVodPlayerView.setOnPreparedListener(new MyPrepareListener(this));
        mAliyunVodPlayerView.setNetConnectedListener(new MyNetConnectedListener(this));
        mAliyunVodPlayerView.setOnCompletionListener(new MyCompletionListener(this));
        mAliyunVodPlayerView.setOnFirstFrameStartListener(new MyFrameInfoListener(this));
        mAliyunVodPlayerView.setOnChangeQualityListener(new MyChangeQualityListener(this));
        mAliyunVodPlayerView.setOnStoppedListener(new MyStoppedListener(this));
        mAliyunVodPlayerView.setmOnPlayerViewClickListener(new MyPlayViewClickListener());
        mAliyunVodPlayerView.setOrientationChangeListener(new MyOrientationChangeListener(this));
        mAliyunVodPlayerView.setOnUrlTimeExpiredListener(new MyOnUrlTimeExpiredListener(this));
        mAliyunVodPlayerView.setOnShowMoreClickListener(new MyShowMoreClickLisener(this));
        mAliyunVodPlayerView.enableNativeLog();

    }

    /**
     * ??????sts
     */
    private void requestVidSts() {
        if (inRequest) {
            return;
        }
        inRequest = true;
        PlayParameter.PLAY_PARAM_VID = DEFAULT_VID;
        VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new MyStsListener(this));
    }

    /**
     * ????????????????????????
     */
    private void loadPlayList() {

        AlivcPlayListManager.getInstance().fetchPlayList(PlayParameter.PLAY_PARAM_AK_ID,
            PlayParameter.PLAY_PARAM_AK_SECRE,
            PlayParameter.PLAY_PARAM_SCU_TOKEN, new AlivcPlayListManager.PlayListListener() {
                @Override
                public void onPlayList(int code, final ArrayList<AlivcVideoInfo.Video> videos) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alivcVideoInfos.addAll(videos);
                            alivcPlayListAdapter.notifyDataSetChanged();
                        }
                    });
                }

            });
    }

    /**
     * init????????????tab
     */
    private void initVideoListView() {
        tvVideoList = (TextView) findViewById(R.id.tv_tab_video_list);
        ivVideoList = (ImageView) findViewById(R.id.iv_video_list);
        recyclerView = (RecyclerView) findViewById(R.id.video_list);
        llVideoList = (LinearLayout) findViewById(R.id.ll_video_list);
        tvStartSetting = (TextView) findViewById(R.id.tv_start_player);
        alivcVideoInfos = new ArrayList<AlivcVideoInfo.Video>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        alivcPlayListAdapter = new AlivcPlayListAdapter(this, alivcVideoInfos);

        ivVideoList.setActivated(true);
        llVideoList.setVisibility(View.VISIBLE);
        rlDownloadManagerContent.setVisibility(View.GONE);
        rlLogsContent.setVisibility(View.GONE);

        tvVideoList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTab = TAB_VIDEO_LIST;
                ivVideoList.setActivated(true);
                ivLogs.setActivated(false);
                ivDownloadVideo.setActivated(false);
                downloadView.changeDownloadEditState(false);
                llVideoList.setVisibility(View.VISIBLE);
                rlDownloadManagerContent.setVisibility(View.GONE);
                rlLogsContent.setVisibility(View.GONE);
            }
        });

        recyclerView.setAdapter(alivcPlayListAdapter);

        alivcPlayListAdapter.setOnVideoListItemClick(new AlivcPlayListAdapter.OnVideoListItemClick() {
            @Override
            public void onItemClick(int position) {
                PlayParameter.PLAY_PARAM_TYPE = "vidsts";
                // ??????????????????, ?????????????????????
                changePlaySource(position);
            }
        });

        // ??????vid???url????????????
        tvStartSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AliyunPlayerSkinActivity.this, AliyunPlayerSettingActivity.class);
                // ?????????, ?????????vid
                startActivityForResult(intent, CODE_REQUEST_SETTING);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_SETTING) {
            switch (resultCode) {
                case CODE_RESULT_TYPE_VID:
                    setPlaySource();
                    loadPlayList();
                    break;
                case CODE_RESULT_TYPE_URL:
                    setPlaySource();
                    loadPlayList();
                    break;

                default:
                    break;
            }

        }
    }

    private int currentVideoPosition;

    /**
     * ??????????????????
     *
     * @param position ??????????????????????????????????????????
     */
    private void changePlaySource(int position) {

        currentVideoPosition = position;

        AlivcVideoInfo.Video video = alivcVideoInfos.get(position);

        changePlayVidSource(video.getVideoId(), video.getTitle());
    }

    /**
     * ??????????????????
     *
     * @param url
     * @param title
     */
    private void changePlayLocalSource(String url, String title) {
        AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
        alsb.setSource(url);
        alsb.setTitle(title);
        AliyunLocalSource localSource = alsb.build();
        mAliyunVodPlayerView.setLocalSource(localSource);
    }

    /**
     * ????????????vid??????
     *
     * @param vid   ???????????????vid
     * @param title ???????????????title
     */
    private void changePlayVidSource(String vid, String title) {
        AliyunVidSts vidSts = new AliyunVidSts();
        vidSts.setVid(vid);
        vidSts.setAcId(PlayParameter.PLAY_PARAM_AK_ID);
        vidSts.setAkSceret(PlayParameter.PLAY_PARAM_AK_SECRE);
        vidSts.setSecurityToken(PlayParameter.PLAY_PARAM_SCU_TOKEN);
        vidSts.setTitle(title);
        mAliyunVodPlayerView.setVidSts(vidSts);
        downloadManager.prepareDownloadMedia(vidSts);
    }

    /**
     * init ??????tab
     */
    private void initLogView() {
        tvLogs = (TextView)findViewById(R.id.tv_logs);
        tvTabLogs = (TextView)findViewById(R.id.tv_tab_logs);
        ivLogs = (ImageView)findViewById(R.id.iv_logs);
        llClearLogs = (LinearLayout)findViewById(R.id.ll_clear_logs);
        rlLogsContent = (RelativeLayout)findViewById(R.id.rl_logs_content);

        //??????Tab???????????????
        ivLogs.setActivated(false);

        //????????????
        llClearLogs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                logStrs.clear();
                tvLogs.setText("");
            }
        });

        tvTabLogs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currentTab = TAB_LOG;
                // TODO: 2018/4/10 show logs contents view
                ivLogs.setActivated(true);
                ivDownloadVideo.setActivated(false);
                ivVideoList.setActivated(false);
                rlLogsContent.setVisibility(View.VISIBLE);
                downloadView.changeDownloadEditState(false);
                rlDownloadManagerContent.setVisibility(View.GONE);
                llVideoList.setVisibility(View.GONE);
            }
        });
    }

    /**
     * init??????(????????????)tab
     */
    private void initDownloadView() {
        tvTabDownloadVideo = (TextView)findViewById(R.id.tv_tab_download_video);
        ivDownloadVideo = (ImageView)findViewById(R.id.iv_download_video);
        rlDownloadManagerContent = (RelativeLayout)findViewById(R.id.rl_download_manager_content);
        downloadView = (DownloadView)findViewById(R.id.download_view);
        //????????????Tab???????????????
        ivDownloadVideo.setActivated(false);
        tvTabDownloadVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTab = TAB_DOWNLOAD_LIST;
                // TODO: 2018/4/10 show download content
                ivDownloadVideo.setActivated(true);
                ivLogs.setActivated(false);
                ivVideoList.setActivated(false);
                rlLogsContent.setVisibility(View.GONE);
                llVideoList.setVisibility(View.GONE);
                rlDownloadManagerContent.setVisibility(View.VISIBLE);
                //Drawable drawable = getResources().getDrawable(R.drawable.alivc_new_download);
                //drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                updateDownloadTaskTip();
            }
        });
    }

    /**
     * downloadView????????? ?????????????????????????????????????????????, ??????????????? ?????????????????????????????????, ?????????download dialog????????????????????????????????????downloadView, ??????????????????????????????????????????,
     * ???????????????????????? AliyunPlayerSkinActivity.class#showAddDownloadView(DownloadVie view)?????????
     *
     * @param downloadView
     */
    private void downloadViewSetting(final DownloadView downloadView) {
        downloadView.addAllDownloadMediaInfo(downloadDataProvider.getAllDownloadMediaInfo());

        downloadView.setOnDownloadViewListener(new OnDownloadViewListener() {
            @Override
            public void onStop(AliyunDownloadMediaInfo downloadMediaInfo) {
                downloadManager.stopDownloadMedia(downloadMediaInfo);
            }

            @Override
            public void onStart(AliyunDownloadMediaInfo downloadMediaInfo) {
                downloadManager.startDownloadMedia(downloadMediaInfo);
            }

            @Override
            public void onDeleteDownloadInfo(final ArrayList<AlivcDownloadMediaInfo> alivcDownloadMediaInfos) {
                // ???????????????dialog
                final AlivcDialog alivcDialog = new AlivcDialog(AliyunPlayerSkinActivity.this);
                alivcDialog.setDialogIcon(R.drawable.icon_delete_tips);
                alivcDialog.setMessage(getResources().getString(R.string.alivc_delete_confirm));
                alivcDialog.setOnConfirmclickListener(getResources().getString(R.string.alivc_dialog_sure),
                    new onConfirmClickListener() {
                        @Override
                        public void onConfirm() {
                            alivcDialog.dismiss();
                            if (alivcDownloadMediaInfos != null && alivcDownloadMediaInfos.size() > 0) {
                                downloadView.deleteDownloadInfo();
                                if (dialogDownloadView != null) {

                                    dialogDownloadView.deleteDownloadInfo();
                                }
                                downloadDataProvider.deleteAllDownloadInfo(alivcDownloadMediaInfos);
                            } else {
                                Toast.makeText(AliyunPlayerSkinActivity.this, "???????????????????????????...", Toast.LENGTH_SHORT)
                                    .show();
                            }
                        }
                    });
                alivcDialog.setOnCancelOnclickListener(getResources().getString(R.string.alivc_dialog_cancle),
                    new onCancelOnclickListener() {
                        @Override
                        public void onCancel() {
                            alivcDialog.dismiss();
                        }
                    });
                alivcDialog.show();
            }
        });

        downloadView.setOnDownloadedItemClickListener(new DownloadView.OnDownloadItemClickListener() {
            @Override
            public void onDownloadedItemClick(int positin) {

                ArrayList<AliyunDownloadMediaInfo> downloadedList = downloadDataProvider.getAllDownloadMediaInfo();
                // ?????????????????????????????????,  ??????????????????
                Collections.reverse(downloadedList);

                if (positin < 0) {
                    Toast.makeText(AliyunPlayerSkinActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ??????????????????????????????, ?????????????????????vid
                AliyunDownloadMediaInfo aliyunDownloadMediaInfo = downloadedList.get(positin);
                PlayParameter.PLAY_PARAM_TYPE = "localSource";
                PlayParameter.PLAY_PARAM_URL = aliyunDownloadMediaInfo.getSavePath();
                mAliyunVodPlayerView.updateScreenShow();
                changePlayLocalSource(PlayParameter.PLAY_PARAM_URL, aliyunDownloadMediaInfo.getTitle());
            }

            @Override
            public void onDownloadingItemClick(ArrayList<AlivcDownloadMediaInfo> infos, int position) {
                AlivcDownloadMediaInfo alivcInfo = infos.get(position);
                AliyunDownloadMediaInfo aliyunDownloadInfo = alivcInfo.getAliyunDownloadMediaInfo();
                AliyunDownloadMediaInfo.Status status = aliyunDownloadInfo.getStatus();
                if (status == AliyunDownloadMediaInfo.Status.Error || status == AliyunDownloadMediaInfo.Status.Wait) {
                    //downloadManager.removeDownloadMedia(aliyunDownloadInfo);
                    downloadManager.startDownloadMedia(aliyunDownloadInfo);
                }
            }

        });
    }

    public static class MyPrepareListener implements IAliyunVodPlayer.OnPreparedListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyPrepareListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onPrepared() {
            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onPrepared();
            }
        }
    }

    private void onPrepared() {
        logStrs.add(format.format(new Date()) + getString(R.string.log_prepare_success));

        for (String log : logStrs) {
            tvLogs.append(log + "\n");
        }
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(), R.string.toast_prepare_success,
            Toast.LENGTH_SHORT).show();
    }

    private static class MyCompletionListener implements IAliyunVodPlayer.OnCompletionListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyCompletionListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onCompletion() {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onCompletion();
            }
        }
    }

    private void onCompletion() {
        logStrs.add(format.format(new Date()) + getString(R.string.log_play_completion));
        for (String log : logStrs) {
            tvLogs.append(log + "\n");
        }
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(), R.string.toast_play_compleion,
            Toast.LENGTH_SHORT).show();

        // ????????????????????????, ?????????????????????
        onNext();
    }

    private void onNext() {
        if (currentError == ErrorInfo.UnConnectInternet) {
            // ???????????????????????????????????????
            // ????????????, ???????????????????????????, ??????????????????ErrorTipsView
            // ????????????, ?????????????????????, ??????Replay, ?????????????????????
            if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
                mAliyunVodPlayerView.showErrorTipView(4014, -1, "?????????????????????");
            }
            return;
        }

        currentVideoPosition++;
        if (currentVideoPosition >= alivcVideoInfos.size() - 1) {
            //????????????????????????????????????????????????????????????????????????????????????
            currentVideoPosition = 0;
        }

        AlivcVideoInfo.Video video = alivcVideoInfos.get(currentVideoPosition);
        if (video != null) {
            changePlayVidSource(video.getVideoId(), video.getTitle());
        }

    }

    private static class MyFrameInfoListener implements IAliyunVodPlayer.OnFirstFrameStartListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyFrameInfoListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onFirstFrameStart() {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onFirstFrameStart();
            }
        }
    }

    private void onFirstFrameStart() {
        Map<String, String> debugInfo = mAliyunVodPlayerView.getAllDebugInfo();
        long createPts = 0;
        if (debugInfo.get("create_player") != null) {
            String time = debugInfo.get("create_player");
            createPts = (long)Double.parseDouble(time);
            logStrs.add(format.format(new Date(createPts)) + getString(R.string.log_player_create_success));
        }
        if (debugInfo.get("open-url") != null) {
            String time = debugInfo.get("open-url");
            long openPts = (long)Double.parseDouble(time) + createPts;
            logStrs.add(format.format(new Date(openPts)) + getString(R.string.log_open_url_success));
        }
        if (debugInfo.get("find-stream") != null) {
            String time = debugInfo.get("find-stream");
            long findPts = (long)Double.parseDouble(time) + createPts;
            logStrs.add(format.format(new Date(findPts)) + getString(R.string.log_request_stream_success));
        }
        if (debugInfo.get("open-stream") != null) {
            String time = debugInfo.get("open-stream");
            long openPts = (long)Double.parseDouble(time) + createPts;
            logStrs.add(format.format(new Date(openPts)) + getString(R.string.log_start_open_stream));
        }
        logStrs.add(format.format(new Date()) + getString(R.string.log_first_frame_played));
        for (String log : logStrs) {
            tvLogs.append(log + "\n");
        }
    }

    private class MyPlayViewClickListener implements OnPlayerViewClickListener {
        @Override
        public void onClick(AliyunScreenMode screenMode, PlayViewType viewType) {
            // ???????????????Type???Download, ?????????Download?????????
            if (viewType == PlayViewType.Download) {
                showAddDownloadView(screenMode);
            }
        }
    }

    /**
     * ??????download ?????????
     *
     * @param screenMode
     */
    private void showAddDownloadView(AliyunScreenMode screenMode) {
        downloadDialog = new DownloadChoiceDialog(this, screenMode);
        final AddDownloadView contentView = new AddDownloadView(this, screenMode);
        contentView.onPrepared(aliyunDownloadMediaInfoList);
        contentView.setOnViewClickListener(viewClickListener);
        final View inflate = LayoutInflater.from(getApplicationContext()).inflate(
            R.layout.alivc_dialog_download_video, null, false);
        dialogDownloadView = inflate.findViewById(R.id.download_view);
        downloadDialog.setContentView(contentView);
        downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });
        downloadDialog.show();
        downloadDialog.setCanceledOnTouchOutside(true);

        if (screenMode == AliyunScreenMode.Full) {
            contentView.setOnShowVideoListLisener(new AddDownloadView.OnShowNativeVideoBtnClickListener() {
                @Override
                public void onShowVideo() {
                    downloadViewSetting(dialogDownloadView);
                    downloadDialog.setContentView(inflate);
                }
            });
        }
    }

    private Dialog downloadDialog = null;

    private AliyunDownloadMediaInfo aliyunDownloadMediaInfo;
    private long currentDownloadIndex = 0;
    /**
     * ???????????????????????????
     */
    private AddDownloadView.OnViewClickListener viewClickListener = new AddDownloadView.OnViewClickListener() {
        @Override
        public void onCancel() {
            if (downloadDialog != null) {
                downloadDialog.dismiss();
            }
        }

        @Override
        public void onDownload(AliyunDownloadMediaInfo info) {
            if (downloadDialog != null) {
                downloadDialog.dismiss();
            }

            aliyunDownloadMediaInfo = info;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                int permission = ContextCompat.checkSelfPermission(AliyunPlayerSkinActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(AliyunPlayerSkinActivity.this, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);

                } else {
                    addNewInfo(info);
                }
            } else {
                addNewInfo(info);
            }

        }
    };

    private void addNewInfo(AliyunDownloadMediaInfo info) {
        downloadManager.addDownloadMedia(info);
        downloadManager.startDownloadMedia(info);
        downloadView.addDownloadMediaInfo(info);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                addNewInfo(aliyunDownloadMediaInfo);
            } else {
                // Permission Denied
                Toast.makeText(AliyunPlayerSkinActivity.this, "??????sd???????????????, ????????????", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private class MyDownloadInfoListener implements AliyunDownloadInfoListener {

        private DownloadView downloadView;

        public MyDownloadInfoListener(DownloadView downloadView) {
            this.downloadView = downloadView;
        }

        @Override
        public void onPrepared(List<AliyunDownloadMediaInfo> infos) {
            Collections.sort(infos, new Comparator<AliyunDownloadMediaInfo>() {
                @Override
                public int compare(AliyunDownloadMediaInfo mediaInfo1, AliyunDownloadMediaInfo mediaInfo2) {
                    if (mediaInfo1.getSize() > mediaInfo2.getSize()) {
                        return 1;
                    }
                    if (mediaInfo1.getSize() < mediaInfo2.getSize()) {
                        return -1;
                    }

                    if (mediaInfo1.getSize() == mediaInfo2.getSize()) {
                        return 0;
                    }
                    return 0;
                }
            });
            onDownloadPrepared(infos);
        }

        @Override
        public void onStart(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onStart");
            Toast.makeText(AliyunPlayerSkinActivity.this, "start...download......", Toast.LENGTH_SHORT).show();
            //downloadView.addDownloadMediaInfo(info);
            //dbHelper.insert(info, DownloadDBHelper.DownloadState.STATE_DOWNLOADING);
            if (!downloadDataProvider.hasAdded(info)) {
                updateDownloadTaskTip();
                downloadDataProvider.addDownloadMediaInfo(info);
            }

        }

        @Override
        public void onProgress(AliyunDownloadMediaInfo info, int percent) {
            downloadView.updateInfo(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfo(info);
            }
            Log.e("Test", "download....progress....." + info.getProgress() + ",  " + percent);
            Log.d("yds100", "onProgress");
        }

        @Override
        public void onStop(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onStop");
            downloadView.updateInfo(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfo(info);
            }
            //dbHelper.update(info, DownloadDBHelper.DownloadState.STATE_PAUSE);
        }

        @Override
        public void onCompletion(AliyunDownloadMediaInfo info) {
            Log.d("yds100", "onCompletion");
            downloadView.updateInfoByComplete(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfoByComplete(info);
            }
            downloadDataProvider.addDownloadMediaInfo(info);
            //aliyunDownloadMediaInfoList.remove(info);
        }

        @Override
        public void onError(AliyunDownloadMediaInfo info, int code, String msg, String requestId) {
            Log.d("yds100", "onError" + msg);
            Log.e("Test","download...onError...msg:::" + msg + ", requestId:::" + requestId + ", code:::" + code);
            downloadView.updateInfoByError(info);
            if (dialogDownloadView != null) {
                dialogDownloadView.updateInfoByError(info);
            }
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(DOWNLOAD_ERROR_KEY, msg);
            message.setData(bundle);
            message.what = DOWNLOAD_ERROR;
            playerHandler = new PlayerHandler(AliyunPlayerSkinActivity.this);
            playerHandler.sendMessage(message);
        }

        @Override
        public void onWait(AliyunDownloadMediaInfo outMediaInfo) {
            Log.d("yds100", "onWait");
        }

        @Override
        public void onM3u8IndexUpdate(AliyunDownloadMediaInfo outMediaInfo, int index) {
            Log.d("yds100", "onM3u8IndexUpdate");
        }
    }

    List<AliyunDownloadMediaInfo> aliyunDownloadMediaInfoList;

    private void onDownloadPrepared(List<AliyunDownloadMediaInfo> infos) {
        aliyunDownloadMediaInfoList = new ArrayList<>();
        aliyunDownloadMediaInfoList.addAll(infos);
    }

    private static class MyChangeQualityListener implements IAliyunVodPlayer.OnChangeQualityListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyChangeQualityListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onChangeQualitySuccess(String finalQuality) {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualitySuccess(finalQuality);
            }
        }

        @Override
        public void onChangeQualityFail(int code, String msg) {
            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onChangeQualityFail(code, msg);
            }
        }
    }

    private void onChangeQualitySuccess(String finalQuality) {
        logStrs.add(format.format(new Date()) + getString(R.string.log_change_quality_success));
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(),
            getString(R.string.log_change_quality_success), Toast.LENGTH_SHORT).show();
    }

    void onChangeQualityFail(int code, String msg) {
        logStrs.add(format.format(new Date()) + getString(R.string.log_change_quality_fail) + " : " + msg);
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(),
            getString(R.string.log_change_quality_fail), Toast.LENGTH_SHORT).show();
    }

    private static class MyStoppedListener implements IAliyunVodPlayer.OnStoppedListener {

        private WeakReference<AliyunPlayerSkinActivity> activityWeakReference;

        public MyStoppedListener(AliyunPlayerSkinActivity skinActivity) {
            activityWeakReference = new WeakReference<AliyunPlayerSkinActivity>(skinActivity);
        }

        @Override
        public void onStopped() {

            AliyunPlayerSkinActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.onStopped();
            }
        }
    }

    private static class MyRefreshStsCallback implements AliyunRefreshStsCallback {

        @Override
        public AliyunVidSts refreshSts(String vid, String quality, String format, String title, boolean encript) {
            VcPlayerLog.d("refreshSts ", "refreshSts , vid = " + vid);
            //NOTE: ?????????????????????????????????????????????????????????????????????????????????????????????
            AliyunVidSts vidSts = VidStsUtil.getVidSts(vid);
            if (vidSts == null) {
                return null;
            } else {
                vidSts.setVid(vid);
                vidSts.setQuality(quality);
                vidSts.setTitle(title);
                return vidSts;
            }
        }
    }

    private void onStopped() {
        Toast.makeText(AliyunPlayerSkinActivity.this.getApplicationContext(), R.string.log_play_stopped,
            Toast.LENGTH_SHORT).show();
    }

    private void setPlaySource() {
        if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            AliyunLocalSource.AliyunLocalSourceBuilder alsb = new AliyunLocalSource.AliyunLocalSourceBuilder();
            alsb.setSource(PlayParameter.PLAY_PARAM_URL);
            Uri uri = Uri.parse(PlayParameter.PLAY_PARAM_URL);
            if ("rtmp".equals(uri.getScheme())) {
                alsb.setTitle("");
            }
            AliyunLocalSource localSource = alsb.build();
            mAliyunVodPlayerView.setLocalSource(localSource);

        } else if ("vidsts".equals(PlayParameter.PLAY_PARAM_TYPE)) {
            if (!inRequest) {
                AliyunVidSts vidSts = new AliyunVidSts();
                vidSts.setVid(PlayParameter.PLAY_PARAM_VID);
                vidSts.setAcId(PlayParameter.PLAY_PARAM_AK_ID);
                vidSts.setAkSceret(PlayParameter.PLAY_PARAM_AK_SECRE);
                vidSts.setSecurityToken(PlayParameter.PLAY_PARAM_SCU_TOKEN);
                if (mAliyunVodPlayerView != null) {
                    mAliyunVodPlayerView.setVidSts(vidSts);
                }
//                downloadManager.prepareDownloadMedia(vidSts);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updatePlayerViewMode();
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onStop();
        }

        if (downloadManager != null && downloadDataProvider != null) {
            downloadManager.stopDownloadMedias(downloadDataProvider.getAllDownloadMediaInfo());
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePlayerViewMode();
    }


    private void updateDownloadTaskTip() {
        if (currentTab != TAB_DOWNLOAD_LIST) {

            Drawable drawable = getResources().getDrawable(R.drawable.alivc_download_new_task);
            drawable.setBounds(0, 0, 20, 20);
            tvTabDownloadVideo.setCompoundDrawablePadding(-20);
            tvTabDownloadVideo.setCompoundDrawables(null, null,  drawable, null);
        } else {
            tvTabDownloadVideo.setCompoundDrawables(null, null, null, null);
        }
    }

    private void updatePlayerViewMode() {
        if (mAliyunVodPlayerView != null) {
            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //??????????????????
                //???????????????
                //                if (!isStrangePhone()) {
                //                    getSupportActionBar().show();
                //                }

                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

                //??????view????????????????????????
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams)mAliyunVodPlayerView
                    .getLayoutParams();
                aliVcVideoViewLayoutParams.height = (int)(ScreenUtils.getWidth(this) * 9.0f / 16);
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = getSupportActionBar().getHeight();
                //                }

            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                //??????????????????
                //???????????????
                if (!isStrangePhone()) {
                    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mAliyunVodPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }

                //??????view??????????????????
                LinearLayout.LayoutParams aliVcVideoViewLayoutParams = (LinearLayout.LayoutParams)mAliyunVodPlayerView
                    .getLayoutParams();
                aliVcVideoViewLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                aliVcVideoViewLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                //                if (!isStrangePhone()) {
                //                    aliVcVideoViewLayoutParams.topMargin = 0;
                //                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        if (mAliyunVodPlayerView != null) {
            mAliyunVodPlayerView.onDestroy();
            mAliyunVodPlayerView = null;
        }

        if (playerHandler != null) {
            playerHandler.removeMessages(DOWNLOAD_ERROR);
            playerHandler = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAliyunVodPlayerView != null) {
            boolean handler = mAliyunVodPlayerView.onKeyDown(keyCode, event);
            if (!handler) {
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //???????????????????????????????????????????????????????????????
        updatePlayerViewMode();
    }

    private static final int DOWNLOAD_ERROR = 1;
    private static final String DOWNLOAD_ERROR_KEY = "error_key";

    private static class PlayerHandler extends Handler {
        //???????????????AliyunPlayerSkinActivity,GC????????????????????????.
        private final WeakReference<AliyunPlayerSkinActivity> mActivty;

        public PlayerHandler(AliyunPlayerSkinActivity activity) {
            mActivty = new WeakReference<AliyunPlayerSkinActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AliyunPlayerSkinActivity activity = mActivty.get();
            super.handleMessage(msg);
            if (activity != null) {
                switch (msg.what) {
                    case DOWNLOAD_ERROR:
                        Toast.makeText(mActivty.get(), msg.getData().getString(DOWNLOAD_ERROR_KEY), Toast.LENGTH_LONG)
                            .show();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private static class MyStsListener implements VidStsUtil.OnStsResultListener {

        private WeakReference<AliyunPlayerSkinActivity> weakctivity;

        public MyStsListener(AliyunPlayerSkinActivity act) {
            weakctivity = new WeakReference<AliyunPlayerSkinActivity>(act);
        }

        @Override
        public void onSuccess(String vid, String akid, String akSecret, String token) {
            AliyunPlayerSkinActivity activity = weakctivity.get();
            if (activity != null) {
                activity.onStsSuccess(vid, akid, akSecret, token);
            }
        }

        @Override
        public void onFail() {
            AliyunPlayerSkinActivity activity = weakctivity.get();
            if (activity != null) {
                activity.onStsFail();
            }
        }
    }

    private void onStsFail() {

        Toast.makeText(getApplicationContext(), R.string.request_vidsts_fail, Toast.LENGTH_LONG).show();
        inRequest = false;
        //finish();
    }

    private void onStsSuccess(String mVid, String akid, String akSecret, String token) {

        PlayParameter.PLAY_PARAM_VID = mVid;
        PlayParameter.PLAY_PARAM_AK_ID = akid;
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token;

        inRequest = false;
        // ??????sts?????????, ??????????????????,???????????????
        setPlaySource();
        if (alivcVideoInfos != null) {
            alivcVideoInfos.clear();
        }
        loadPlayList();
    }

    private static class MyOrientationChangeListener implements AliyunVodPlayerView.OnOrientationChangeListener {

        private final WeakReference<AliyunPlayerSkinActivity> weakReference;

        public MyOrientationChangeListener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void orientationChange(boolean from, AliyunScreenMode currentMode) {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.hideDownloadDialog(from, currentMode);
            activity.hideShowMoreDialog(from, currentMode);
        }
    }

    private void hideShowMoreDialog(boolean from, AliyunScreenMode currentMode) {
        if (showMoreDialog != null) {
            if (currentMode == AliyunScreenMode.Small) {
                showMoreDialog.dismiss();
                currentScreenMode = currentMode;
            }
        }
    }

    private void hideDownloadDialog(boolean from, AliyunScreenMode currentMode) {

        if (downloadDialog != null) {
            if (currentScreenMode != currentMode) {
                downloadDialog.dismiss();
                currentScreenMode = currentMode;
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    private class MyNetConnectedListener implements AliyunVodPlayerView.NetConnectedListener {
        WeakReference<AliyunPlayerSkinActivity> weakReference;

        public MyNetConnectedListener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void onReNetConnected(boolean isReconnect) {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.onReNetConnected(isReconnect);
        }

        @Override
        public void onNetUnConnected() {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.onNetUnConnected();
        }
    }

    private void onNetUnConnected() {
        currentError = ErrorInfo.UnConnectInternet;
        if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
            downloadManager.stopDownloadMedias(aliyunDownloadMediaInfoList);
        }
    }

    private void onReNetConnected(boolean isReconnect) {
        if (isReconnect) {
            if (aliyunDownloadMediaInfoList != null && aliyunDownloadMediaInfoList.size() > 0) {
                int unCompleteDownload = 0;
                for (AliyunDownloadMediaInfo info : aliyunDownloadMediaInfoList) {
                    //downloadManager.startDownloadMedia(info);
                    if (info.getStatus() == AliyunDownloadMediaInfo.Status.Stop) {

                        unCompleteDownload++;
                    }
                }

                if (unCompleteDownload > 0) {
                    Toast.makeText(this, "????????????, ???????????????????????????...", Toast.LENGTH_SHORT).show();
                }
            }
            VidStsUtil.getVidSts(PlayParameter.PLAY_PARAM_VID, new MyStsListener(this));
        }
    }

    private static class MyOnUrlTimeExpiredListener implements IAliyunVodPlayer.OnUrlTimeExpiredListener {
        WeakReference<AliyunPlayerSkinActivity> weakReference;

        public MyOnUrlTimeExpiredListener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<AliyunPlayerSkinActivity>(activity);
        }

        @Override
        public void onUrlTimeExpired(String s, String s1) {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.onUrlTimeExpired(s, s1);
        }
    }

    private void onUrlTimeExpired(String oldVid, String oldQuality) {
        //requestVidSts();
        AliyunVidSts vidSts = VidStsUtil.getVidSts(oldVid);
        PlayParameter.PLAY_PARAM_VID = vidSts.getVid();
        PlayParameter.PLAY_PARAM_AK_SECRE = vidSts.getAkSceret();
        PlayParameter.PLAY_PARAM_AK_ID = vidSts.getAcId();
        PlayParameter.PLAY_PARAM_SCU_TOKEN = vidSts.getSecurityToken();

    }

    private static class MyShowMoreClickLisener implements ControlView.OnShowMoreClickListener {
        WeakReference<AliyunPlayerSkinActivity> weakReference;

        MyShowMoreClickLisener(AliyunPlayerSkinActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void showMore() {
            AliyunPlayerSkinActivity activity = weakReference.get();
            activity.showMore(activity);
        }
    }

    private void showMore(final AliyunPlayerSkinActivity activity) {
        showMoreDialog = new AlivcShowMoreDialog(activity);
        AliyunShowMoreValue moreValue = new AliyunShowMoreValue();
        moreValue.setSpeed(mAliyunVodPlayerView.getCurrentSpeed());
        moreValue.setVolume(mAliyunVodPlayerView.getCurrentVolume());
        moreValue.setScreenBrightness(mAliyunVodPlayerView.getCurrentScreenBrigtness());

        ShowMoreView showMoreView = new ShowMoreView(activity, moreValue);
        showMoreDialog.setContentView(showMoreView);
        showMoreDialog.show();
        showMoreView.setOnDownloadButtonClickListener(new ShowMoreView.OnDownloadButtonClickListener() {
            @Override
            public void onDownloadClick() {
                // ????????????
                showMoreDialog.dismiss();
                if ("localSource".equals(PlayParameter.PLAY_PARAM_TYPE)) {
                    Toast.makeText(activity, "Url?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                showAddDownloadView(AliyunScreenMode.Full);
            }
        });

        showMoreView.setOnScreenCastButtonClickListener(new ShowMoreView.OnScreenCastButtonClickListener() {
            @Override
            public void onScreenCastClick() {
                Toast.makeText(AliyunPlayerSkinActivity.this, "???????????????, ????????????...", Toast.LENGTH_SHORT).show();
            }
        });

        showMoreView.setOnBarrageButtonClickListener(new ShowMoreView.OnBarrageButtonClickListener() {
            @Override
            public void onBarrageClick() {
                Toast.makeText(AliyunPlayerSkinActivity.this, "???????????????, ????????????...", Toast.LENGTH_SHORT).show();
            }
        });

        showMoreView.setOnSpeedCheckedChangedListener(new ShowMoreView.OnSpeedCheckedChangedListener() {
            @Override
            public void onSpeedChanged(RadioGroup group, int checkedId) {
                // ??????????????????
                if (checkedId == R.id.rb_speed_normal) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.One);
                } else if (checkedId == R.id.rb_speed_onequartern) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.OneQuartern);
                } else if (checkedId == R.id.rb_speed_onehalf) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.OneHalf);
                } else if (checkedId == R.id.rb_speed_twice) {
                    mAliyunVodPlayerView.changeSpeed(SpeedValue.Twice);
                }

            }
        });

        // ??????seek
        showMoreView.setOnLightSeekChangeListener(new ShowMoreView.OnLightSeekChangeListener() {
            @Override
            public void onStart(SeekBar seekBar) {

            }

            @Override
            public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                mAliyunVodPlayerView.setCurrentScreenBrigtness(progress);
            }

            @Override
            public void onStop(SeekBar seekBar) {

            }
        });

        showMoreView.setOnVoiceSeekChangeListener(new ShowMoreView.OnVoiceSeekChangeListener() {
            @Override
            public void onStart(SeekBar seekBar) {

            }

            @Override
            public void onProgress(SeekBar seekBar, int progress, boolean fromUser) {
                mAliyunVodPlayerView.setCurrentVolume(progress);
            }

            @Override
            public void onStop(SeekBar seekBar) {

            }
        });

    }
}
