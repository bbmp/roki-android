package com.robam.roki.ui.page.device.hidkit;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.services.RestfulService;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.EventUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.HidKitStatusChangedEvent;
import com.robam.common.events.NewestVersionEvent;
import com.robam.common.events.TheUpgradeHidKitEvent;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceMoreBean;
import com.robam.roki.model.bean.HidKitUpdateBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.AbsMoreAdapter;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/6/7.
 */

public class HidKitDeviceMorePage extends BasePage {


    @InjectView(R.id.tb_title)
    TitleBar tbTitle;
    @InjectView(R.id.recyclerView_more)
    RecyclerView mRecyclerViewMore;
    private AbsMoreAdapter mMoreAdapter;
    public List<DeviceMoreBean> mDatas = new ArrayList<>();
    String mGuid;
    String versionNum;
    String mDesc;
    protected String mTitle;
    private IDevice mIDevice;

    private IRokiDialog dialog;
    private IRokiDialog downDialog;
    private IRokiDialog dialogFailed;
    private Timer timer;
    private TimerTask timerTask;
    private String KC306_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KC306.json";
    private String KM310_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KM310.json";
    private String KC306_fileName = "KC306.json";
    private String KM310_fileName = "KM310.json";
    //    private String downloadUrl = "https://roki-test.oss-cn-qingdao.aliyuncs.com/KC306.json";//测试环境
    private boolean connected;


    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (downDialog != null && downDialog.isShow()) {
                        downDialog.dismiss();
                    }
                    if (null != dialogFailed && !dialogFailed.isShow()) {
                        dialogFailed.setCanceledOnTouchOutside(false);
                        dialogFailed.setTitleText(R.string.dialog_update_failed);
                        dialogFailed.setContentText(R.string.dialog_update_content_failed);
                        dialogFailed.setCancelBtn(R.string.dialog_close_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LogUtils.i("20201110", "onClick:");
                                stopUpdateTask();
                                dialogFailed.dismiss();
                            }
                        });
                        dialogFailed.setOkBtn(R.string.dialog_try_again_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogFailed.dismiss();
                                stopUpdateTask();
//                                dealWithVersion();
                                if ((mIDevice != null && !mIDevice.isConnected()) || !connected) {
                                    ToastUtils.showShort(R.string.oven_dis_con);
                                    return;
                                }
                                ((AbsHidKit) mIDevice).setHidKitStatusCombined((short) 1, (short) 2, (short) 1, (short) 1, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
//                        mHandler.sendEmptyMessage(100);
                                        startUpdateTask();
                                        LogUtils.i("20201111", " onSuccess:");
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        stopUpdateTask();
                                        LogUtils.i("20201111", " onFailure:" + t);
                                    }
                                });
                            }
                        });
                        dialogFailed.show();
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        versionNum = bd == null ? null : bd.getString(PageArgumentKey.versionNum);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGuid == null) {
            return;
        }
        mIDevice = Plat.deviceService.lookupChild(mGuid);

        if (mIDevice.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), mIDevice.getDt() + ":更多页", null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_more, container, false);
        ButterKnife.inject(this, view);
        initData();
        initView();
        return view;
    }

    private void initView() {
        mMoreAdapter = new AbsMoreAdapter(cx, mDatas);
        mMoreAdapter.setOnItemRecycleClickLister(new AbsMoreAdapter.OnItemRecycleClick() {
            @Override
            public void onItemClick(View v, int position) {
                initItemClick(v, position);
            }
        });
        mRecyclerViewMore.setAdapter(mMoreAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewMore.setLayoutManager(linearLayoutManager);
        mRecyclerViewMore.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.VERTICAL_LIST));

        if (null == dialogFailed) {
            dialogFailed = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_UPDATE_FAILED);
        }
        tbTitle.setOnTitleBarListener(new OnTitleBarListener() {
            @Override
            public void onLeftClick(View view) {
                UIService.getInstance().popBack();
            }

            @Override
            public void onTitleClick(View view) {

            }

            @Override
            public void onRightClick(View view) {

            }
        });
    }

    private void initData() {

        DeviceMoreBean deviceMoreBean = new DeviceMoreBean();
        deviceMoreBean.setName(cx.getString(R.string.fan_dialog_setting_product_information));
        deviceMoreBean.setImageRes(R.mipmap.img_product_information);
        deviceMoreBean.setType(1);

        DeviceMoreBean deviceMoreBean1 = new DeviceMoreBean();
        deviceMoreBean1.setName(cx.getString(R.string.fan_dialog_setting_message_consulting));
        deviceMoreBean1.setImageRes(R.mipmap.img_message_consulting);
        deviceMoreBean1.setType(1);

        DeviceMoreBean deviceMoreBean2 = new DeviceMoreBean();
        deviceMoreBean2.setName(cx.getString(R.string.fan_dialog_setting_a_key_after_sale));
        deviceMoreBean2.setImageRes(R.mipmap.img_after_sales);
        deviceMoreBean2.setType(1);

        DeviceMoreBean deviceMoreBean3 = new DeviceMoreBean();
        deviceMoreBean3.setName(cx.getString(R.string.fan_dialog_setting_check_for_updates));
        deviceMoreBean3.setImageRes(R.mipmap.img_check_for_updates);
        deviceMoreBean3.setType(1);


        DeviceMoreBean deviceMoreBean4 = new DeviceMoreBean();
        deviceMoreBean4.setName(cx.getString(R.string.connected_to_the_internet_again));
        deviceMoreBean4.setImageRes(R.mipmap.img_connected_to_the_internet_again);
        deviceMoreBean4.setType(1);


        mDatas.add(deviceMoreBean);
        mDatas.add(deviceMoreBean1);
        mDatas.add(deviceMoreBean2);
        mDatas.add(deviceMoreBean3);
        mDatas.add(deviceMoreBean4);
    }

    public void initItemClick(View v, int position) {
        switch (position) {
            case 0:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd);

                break;
            case 1:
                UIService.getInstance().postPage(PageKey.Chat);

                break;
            case 2:
                callAfterSale();
                break;
            case 3:
                if ((mIDevice != null && !mIDevice.isConnected()) || !connected) {
                    ToastUtils.showShort(R.string.oven_dis_con);
                    return;
                }
                AbsHidKit defaultHidKit = Utils.getDefaultHidKit();
                int version = defaultHidKit.getVersion();
                if (versionNum != null && version >= Integer.parseInt(versionNum)) {
                    final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_UPDATE_COMPLETION);
                    dialogByType.setCanceledOnTouchOutside(false);
                    dialogByType.setContentText(R.string.dialog_newest_version);
                    dialogByType.show();
                    dialogByType.setOkBtn(R.string.dialog_affirm_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogByType.dismiss();
                        }
                    });
                } else {
                    EventUtils.postEvent(new NewestVersionEvent());
                    dealWithVersion();
                }
                break;

            case 4:
                Bundle b = new Bundle();
                b.putString("displayType", mGuid.substring(0, 5));
                if (mGuid.contains("KC306")) {
                    b.putString(PageArgumentKey.Guid, mGuid.substring(0, 5));
                } else {
                    b.putString(PageArgumentKey.Guid, mGuid);
                }
                UIService.getInstance().postPage(PageKey.WifiConnect, b);
                break;
            default:
                break;
        }

    }

    private void callAfterSale() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        dialog.setTitleText(R.string.after_sale_phone);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri uri = Uri.parse(String.format("tel:%s", cx.getString(R.string.after_sale_phone)));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }



    @Subscribe
    public void onEvent(TheUpgradeHidKitEvent event) {
        switch (event.the_upgrade_val) {
            case 0://完成
                stopUpdateTask();
                if (downDialog != null && downDialog.isShow()) {
                    downDialog.dismiss();
                }
                if (dialog != null && dialog.isShow()) {
                    dialog.dismiss();
                }
                break;
        }
    }

    @Subscribe
    public void onEvent(HidKitStatusChangedEvent event) {
        LogUtils.i("20220408","HidKitStatusChangedEvent:"+mIDevice.isConnected());
        if (mIDevice == null || !Objects.equal(mIDevice.getID(), event.pojo.getID()))
            return;
        mIDevice = (IDevice) event.pojo;
        connected = mIDevice.isConnected();

    }
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (event.device.getID().equals(mGuid)) {
            connected = event.isConnected;
            LogUtils.i("20220408","DeviceConnectionChangedEvent:"+connected);

        }
    }

    private void dealWithVersion() {
        String downloadUrl = "", fileName = "";
        if (mGuid.contains("KC306")) {
            downloadUrl = KC306_downloadUrl;
            fileName = KC306_fileName;
        } else if (mGuid.contains("KM310")) {
            downloadUrl = KM310_downloadUrl;
            fileName = KM310_fileName;
        }
        RestfulService.getInstance().downFile(downloadUrl, fileName, new Callback<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) {
                    String kc306Json = AlarmDataUtils.getFileFromSD(uri.getPath());
                    try {
                        HidKitUpdateBean hidKitUpdateBean = JsonUtils.json2Pojo(kc306Json, HidKitUpdateBean.class);
                        versionNum = hidKitUpdateBean.getVersion().getValue();
                        String getVersion = versionNum;
                        AbsHidKit defaultHidKit = Utils.getDefaultHidKit();
                        int version = defaultHidKit.getVersion();
                        if (getVersion != null && version >= Integer.parseInt(getVersion)) {
                            final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_UPDATE_COMPLETION);
                            dialogByType.setCanceledOnTouchOutside(false);
                            dialogByType.setContentText(R.string.dialog_newest_version);
                            dialogByType.show();
                            dialogByType.setOkBtn(R.string.dialog_affirm_text, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogByType.dismiss();
                                }
                            });
                            return;
                        }
                        final String desc = hidKitUpdateBean.getDesc().getValue();
                        mDesc = desc;
                        dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_DISCOVER_NEW_VERSION);
                        dialog.setContentText(mDesc);
                        dialog.setTitleText(R.string.dialog_discover_new_version);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelBtn(R.string.dialog_ignore_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                        dialog.setOkBtn(R.string.dialog_update_text, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                if (!mIDevice.isConnected()) {
                                    ToastUtils.showShort(R.string.oven_dis_con);
                                    return;
                                }
                                ((AbsHidKit) mIDevice).setHidKitStatusCombined((short) 1, (short) 2, (short) 1, (short) 1, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
//                        mHandler.sendEmptyMessage(100);
                                        startUpdateTask();
                                        LogUtils.i("20201111", " onSuccess:");
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        stopUpdateTask();
                                        LogUtils.i("20201111", " onFailure:" + t);
                                    }
                                });

                            }
                        });
                        dialog.show();
                    } catch (Exception e) {
                        ToastUtils.showShort("没有新版本");
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 开始更新任务
     */

    private void startUpdateTask() {
        downDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_DOWN_NEW_VERSION);
        downDialog.setTitleText(R.string.dialog_discover_new_version);
        downDialog.setContentText(mDesc);
        downDialog.setCanceledOnTouchOutside(false);
        downDialog.setCancelable(false);
        downDialog.show();
        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }
        if (timer != null) {
            timer.cancel();
        }
        if (timerTask != null) {
            timerTask.cancel();
        }
        timer = new Timer();
        timerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                if (downDialog != null && downDialog.isShow()) {
                    if (i >= 100) {
                        downDialog.setProgress(99);
                    } else {
                        downDialog.setProgress(i);
                    }
                    if (i >= 180) {
                        //三分钟算超时
                        mHandler.sendEmptyMessage(0);
                    } else {
                        i += 1;
                    }
                }
            }
        };

        timer.schedule(timerTask, 0, 600);

    }

    /**
     * 停止更新任务
     */
    private void stopUpdateTask() {

        if (null != timer) {
            timer.cancel();
            timer = null;
        }
        if (null != timerTask) {
            timerTask.cancel();
            timerTask = null;
        }
        if (downDialog != null && downDialog.isShow()) {
            downDialog.dismiss();
        }
        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }

    }

}
