package com.robam.roki.ui.page.device.fan;

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
 * 烟机油杯检测
 */

public class DeviceFanOilCupPage extends BasePage {

    AbsFan fan;
    List<DeviceConfigurationFunctions> mList;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_oil_cup)
    TextView mTvOilCup;
    @InjectView(R.id.iv_oil_cup_scan)
    ImageView mIvOilCupScan;
    @InjectView(R.id.tv_cup_text)
    TextView mTvCupText;
    @InjectView(R.id.iv_oil_cup_percent)
    ImageView mIvOilCupPercent;
    @InjectView(R.id.tv_oil_cup_desc)
    TextView mTvOilCupDesc;
    @InjectView(R.id.tv_oil_reset)
    TextView mTvOilReset;
    @InjectView(R.id.tv_oil_dismantling)
    TextView mTvOilDismantling;
    private short mClean;
    private String mDisassembleUrl;//拆卸url
    private String mDisassembleName;
    private String mTag;
    private String dismantling;
    private int waitTime;

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        LogUtils.i("20180613", "mClean:" + mClean);
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID())) return;
        mClean = event.pojo.oilCup;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        fan = bd == null ? null : (AbsFan) bd.getSerializable(PageArgumentKey.Bean);
        title = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        mDisassembleUrl = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.Url);
        mDisassembleName = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.dismantling);
        mTag = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.tag);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fan_oil_cup, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fan == null) {
            return;
        }
        if (fan.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), fan.getDt() + ":油杯检测页", null);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ("other".equals(mTag)) {
            waitTime = 3000;
        } else {
            waitTime = 0;
            mClean = 1;
        }
        oilCupScan();
        initData();
        mTvOilCup.setText(title);
        mTvOilDismantling.setText(mDisassembleName);
    }

    private void initData() {
        if (mList == null || mList.size() == 0) return;
        for (int i = 0; i < mList.size(); i++) {
            if ("oilCupworkDetection".equals(mList.get(i).functionCode)) {
                List<DeviceConfigurationFunctions> oilcupSubViewList = mList.get(i)
                        .subView
                        .subViewModelMap
                        .subViewModelMapSubView
                        .deviceConfigurationFunctions;
                if (oilcupSubViewList == null || oilcupSubViewList.size() == 0) return;
                for (int j = 0; j < oilcupSubViewList.size(); j++) {
                    if ("oilCupDismant".equals(oilcupSubViewList.get(j).functionCode)) {
                        mDisassembleName = oilcupSubViewList.get(j).functionName;
                        mDisassembleUrl = oilcupSubViewList.get(j).subViewName;
                        LogUtils.i("20180613", "mDisassembleUrl:" + mDisassembleUrl);
                        LogUtils.i("20180613", "mDisassembleName:" + mDisassembleName);
                    }
                }
            }
        }
    }

    //净化检测
    private void oilCupScan() {

        if (mIvOilCupScan == null) return;
        Glide.with(this)
                .asGif()
                .load(R.mipmap.oil_detection_sean)
                .fitCenter()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mIvOilCupScan);
        mTvCupText.setText(R.string.fan_oil_detection_text);
        mTvCupText.setTextColor(0xff5d5d5d);
        Timer timer = new Timer();//实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIvOilCupScan.setVisibility(View.GONE);
                            mIvOilCupPercent.setVisibility(View.VISIBLE);
                            mTvOilCupDesc.setVisibility(View.VISIBLE);
                            mTvCupText.setTextColor(0xfffed268);
                            if (0 == mClean || !fan.isConnected()) {
                                mTvOilCupDesc.setText(R.string.fan_oil_detection_desc_good);
                                mIvOilCupPercent.setImageDrawable(cx.getResources().getDrawable(R.drawable.shape_oval_yellow));
                                mTvCupText.setText(R.string.fan_oil_cup_text_good);
                            } else {
                                mTvOilCupDesc.setText(R.string.fan_oil_detection_desc_bad);
                                mIvOilCupPercent.setImageDrawable(cx.getResources().getDrawable(R.drawable.shape_oval_gray));
                                mTvCupText.setText(R.string.fan_oil_cup_text_bad);
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
        closeDialog.setTitleText(R.string.device_oil_cup_reset);
        closeDialog.setContentText(R.string.device_oil_cup_reset_desc);
        closeDialog.show();
        closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog.dismiss();
                fan.clearOilCupAramTime(new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (mClean == 0) {
                            ToastUtils.showShort(R.string.fan_oil_cup_clean_not_reset);
                            return;
                        }
                        mIvOilCupScan.setVisibility(View.GONE);
                        mIvOilCupPercent.setVisibility(View.VISIBLE);
                        mTvOilCupDesc.setVisibility(View.VISIBLE);
                        mTvCupText.setTextColor(0xfffed268);
                        mTvOilCupDesc.setText(R.string.fan_oil_detection_desc_good);
                        mIvOilCupPercent.setImageDrawable(cx.getResources().getDrawable(R.drawable.shape_oval_yellow));
                        mTvCupText.setText(R.string.fan_oil_cup_text_good);
                        ToastUtils.showShort(R.string.fan_oil_cup_clean_reset_succeed);
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
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.Bean, fan);
        bd.putSerializable(PageArgumentKey.Url, mDisassembleUrl);
        bd.putSerializable(PageArgumentKey.title, mDisassembleName);
        UIService.getInstance().postPage(PageKey.DeviceFanOilDetail, bd);
    }

}
