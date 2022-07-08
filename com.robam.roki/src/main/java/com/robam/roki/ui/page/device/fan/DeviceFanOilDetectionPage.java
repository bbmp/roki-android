package com.robam.roki.ui.page.device.fan;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/5/9.
 * 烟机油网检测
 */

public class DeviceFanOilDetectionPage extends BasePage {

    AbsFan fan;
    List<DeviceConfigurationFunctions> mList;
    String mDisassembleUrl;
    String mDisassembleName;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_oil_detection)
    TextView mTvOilDetection;
    @InjectView(R.id.iv_oil_detection_scan)
    ImageView mIvOilDetectionScan;
    @InjectView(R.id.tv_detection_text)
    TextView mTvDetectionText;
    @InjectView(R.id.iv_oil_detection_percent)
    ImageView mIvOilDetectionPercent;
    @InjectView(R.id.tv_oil_detection_desc)
    TextView mTvOilDetectionDesc;
    @InjectView(R.id.tv_oil_reset)
    TextView mTvOilReset;
    @InjectView(R.id.tv_oil_dismantling)
    TextView mTvOilDismantling;
    private boolean mClean = true;
    private String mTag;
    private int waitTime;

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        LogUtils.i("20180510", "mClean:" + mClean);
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID())) return;
        mClean = event.pojo.clean;
        LogUtils.i("DeviceFanOilDetectionPage" ,"-------78");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        LogUtils.i("DeviceFanOilDetectionPage" ,"-------85");
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        title = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        mDisassembleName = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.dismantling);
        mDisassembleUrl = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Url);
        mTag = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.tag);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        LogUtils.i("DeviceFanOilDetectionPage" ,"-------92");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_oil_detection, container, false);
        ButterKnife.inject(this, view);
        LogUtils.i("DeviceFanOilDetectionPage" ,"-------97");
        if ("other".equals(mTag)) {
            waitTime = 3000;
        } else {
            waitTime = 0;
            mClean = true;
        }
        oilDetectionScan();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fan==null) {
            return;
        }
        if (fan.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), fan.getDt() + ":油网检测页", null);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mTvOilDetection.setText(title);
        mTvOilDismantling.setText(mDisassembleName);
    }

    private void initData() {
        LogUtils.i("DeviceFanOilDetectionPage" ,"-------130");
        if (mList == null || mList.size() == 0) return;
        for (int i = 0; i < mList.size(); i++) {
            if ("oilNetworkDetection".equals(mList.get(i).functionCode)) {
                List<DeviceConfigurationFunctions> oilcupSubViewList = mList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                if (oilcupSubViewList == null || oilcupSubViewList.size() == 0) return;
                for (int j = 0; j < oilcupSubViewList.size(); j++) {
                    if ("oilNetDismant".equals(oilcupSubViewList.get(j).functionCode)) {
                        mDisassembleName = oilcupSubViewList.get(j).functionName;
                        mDisassembleUrl = oilcupSubViewList.get(j).subViewName;
                        LogUtils.i("20180613", "mDisassembleUrl:" + mDisassembleUrl);
                    }
                }
            }
        }

        LogUtils.i("DeviceFanOilDetectionPage" ,"-------149");
    }

    //净化检测
    private void oilDetectionScan() {
        if (mIvOilDetectionScan == null) return;
        Glide.with(this)
                .asGif()
                .load(R.mipmap.oil_detection_sean)
                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mIvOilDetectionScan);
        mClean = fan.clean;
        mTvDetectionText.setText(R.string.fan_oil_detection_text);
        mTvDetectionText.setTextColor(0xff5d5d5d);
        Timer timer = new Timer();//实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIvOilDetectionScan.setVisibility(View.GONE);
                            mIvOilDetectionPercent.setVisibility(View.VISIBLE);
                            mTvOilDetectionDesc.setVisibility(View.VISIBLE);
                            mTvDetectionText.setTextColor(0xfffed268);
                            if (!mClean || !fan.isConnected()) {
                                mTvOilDetectionDesc.setText(R.string.fan_oil_detection_desc_good);
                                mIvOilDetectionPercent.setImageDrawable(cx.getResources().getDrawable(R.drawable.shape_oval_yellow));
                                mTvDetectionText.setText(R.string.fan_oil_detection_text_good);
                            } else {
                                mTvOilDetectionDesc.setText(R.string.fan_oil_detection_desc_bad);
                                mIvOilDetectionPercent.setImageDrawable(cx.getResources().getDrawable(R.drawable.shape_oval_gray));
                                mTvDetectionText.setText(R.string.fan_oil_detection_text_bad);
                            }
                        }
                    });

                    this.cancel();
                }
            }
        }, waitTime);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


    @OnClick(R.id.tv_oil_reset)
    public void onMIvOilDetectionPercentClicked() {

        final IRokiDialog closeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closeDialog.setTitleText(R.string.device_oil_detection_reset);
        closeDialog.setContentText(R.string.device_oil_detection_reset_desc);
        closeDialog.show();
        closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog.dismiss();
                if (!mClean) {
                    ToastUtils.showShort(R.string.fan_oil_detection_clean_not_reset);
                    return;
                }
                fan.restFanCleanTime(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mIvOilDetectionScan.setVisibility(View.GONE);
                        mIvOilDetectionPercent.setVisibility(View.VISIBLE);
                        mTvOilDetectionDesc.setVisibility(View.VISIBLE);
                        mTvDetectionText.setTextColor(0xfffed268);
                        mTvOilDetectionDesc.setText(R.string.fan_oil_detection_desc_good);
                        mIvOilDetectionPercent.setImageDrawable(cx.getResources().getDrawable(R.drawable.shape_oval_yellow));
                        mTvDetectionText.setText(R.string.fan_oil_detection_text_good);
                        //ToastUtils.showShort(R.string.fan_oil_cup_clean_reset_succeed);
                        ToastUtils.showShort(R.string.fan_oil_detection_clean_reset_succeed);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        closeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closeDialog.isShow()) {
                    closeDialog.dismiss();
                }
            }
        });

    }

    @OnClick(R.id.tv_oil_dismantling)
    public void onMTvOilDismantlingClicked() {
        if (fan == null) return;
        //油网拆洗
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.title, mDisassembleName);
        bd.putSerializable(PageArgumentKey.Url, mDisassembleUrl);
        UIService.getInstance().postPage(PageKey.DeviceFanOilDetail, bd);
    }
}
