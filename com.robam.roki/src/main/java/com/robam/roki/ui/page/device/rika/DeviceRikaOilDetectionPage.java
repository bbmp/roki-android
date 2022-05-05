package com.robam.roki.ui.page.device.rika;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.CirclePercentView;
import com.robam.roki.utils.DialogUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/9.
 * Rika油网检测
 */

public class DeviceRikaOilDetectionPage extends BasePage {

    AbsRika rika;
    String tag;
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
    @InjectView(R.id.cpv_oil_detection_percent)
    CirclePercentView mCpvOilDetectionPercent;
    @InjectView(R.id.tv_percent_num)
    TextView mTvPercentNum;
    @InjectView(R.id.ll_percent)
    LinearLayout mLlPercent;
    @InjectView(R.id.tv_oil_reset)
    TextView mTvOilReset;
    @InjectView(R.id.tv_oil_dismantling)
    TextView mTvOilDismantling;
    private int mCleaningUseTime;
    private final int Min_Max = 3600;
    private final int Hour_Max = 60;
    private int waitTime;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        rika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.Bean);
        title = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.title);
        mCleaningUseTime = bd == null ? null : (Integer) bd.getSerializable(PageArgumentKey.time);
        tag = bd == null ? null : (String) bd.getSerializable(PageArgumentKey.tag);
        mList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_rika_oil_detection, container, false);
        ButterKnife.inject(this, view);
        if ("other".equals(tag)) {
            waitTime = 3000;
        } else {
            waitTime = 0;
        }
        mTvOilDismantling.setVisibility(View.GONE);
        mTvOilReset.setVisibility(View.GONE);
        LogUtils.i("20180613", "title:" + title);
        oilDetectionScan();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        mTvOilDetection.setText(title);
        mTvOilDismantling.setText(mDisassembleName);
    }

    private void initData() {
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
                        LogUtils.i("20180613", "mDisassembleName:" + mDisassembleName);
                    }
                }
            }
        }
    }

//    @Subscribe
//    public void onEvent(RikaStatusChangedEvent event) {
//        if (rika == null || !Objects.equal(rika.getID(), event.pojo.getID()))
//            return;
//        mCleaningUseTime = rika.cleaningUseTime;
////        if (0 == rika.sterilDoorLockStatus && rika.sterilWorkStatus != RikaStatus.STERIL_ALARM){
////
////                IRokiDialog rokiDialog = RokiDialogFactory.createDialogByType(cx,DialogUtil.DIALOG_TYPE_09);
////                rokiDialog.setContentText(R.string.device_alarm_rika_E1_content);
////                rokiDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
////                rokiDialog.show();
////        }
//
//        LogUtils.i("20180625", "mCleaningUseTime:" + mCleaningUseTime);
//    }

    private void oilDetectionScan() {
        Glide.with(getActivity())
                .asGif()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .load(R.mipmap.oil_detection_sean)
                .into(mIvOilDetectionScan);
        mTvDetectionText.setText(R.string.rika_oil_Testing_for_you);
        Timer timer = new Timer();//实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
//                FragmentActivity activity = getActivity();
                //Activity activity = getActivity();
                FragmentActivity activity= getActivity();
                if (activity != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIvOilDetectionScan.setVisibility(View.GONE);
                            mLlPercent.setVisibility(View.VISIBLE);
                            mCpvOilDetectionPercent.setVisibility(View.VISIBLE);
                            drawCircleProgress();
                        }
                    });
                }
                this.cancel();
            }
        }, waitTime);//毫秒
    }

    private void drawCircleProgress() {
        int percent;
        double set = (3600d - rika.cleaningUseTime) / 3600d * 100d;
        double ceil = Math.ceil(set);
        if (ceil > 0) {
            percent = (int) ceil;
        } else {
            percent = 0;
        }
        mTvOilDismantling.setVisibility(View.VISIBLE);
        mTvOilReset.setVisibility(View.VISIBLE);
        if (percent > 50) {
            mTvDetectionText.setText(R.string.rika_oil_net_50);
        } else if (percent > 20) {
            mTvDetectionText.setText(R.string.rika_oil_net_20);
        } else if (percent > 5) {
            mTvDetectionText.setText(R.string.rika_oil_net_max05);
        } else {
            mTvDetectionText.setText(R.string.rika_oil_net_min05);
        }


        mTvPercentNum.setText(String.valueOf(percent));
        mCpvOilDetectionPercent.setPercent(percent);

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
    public void onMTvOilResetClicked() {

        final IRokiDialog closeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closeDialog.setTitleText(R.string.device_oil_detection_reset);
        closeDialog.setContentText(R.string.device_oil_detection_reset_desc);
        closeDialog.show();
        closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                closeDialog.dismiss();
                rika.setFanSwitchStatus((short) 1, RikaStatus.FAN_CATEGORYCODE, (short) 1, (short) 52, (short) 1,
                        RikaStatus.FAN_ON, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                rika.cleaningUseTime = 0;
                                drawCircleProgress();
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
        //油网拆洗
        Bundle bd = new Bundle();
        bd.putSerializable(PageArgumentKey.Bean, rika);
        bd.putSerializable(PageArgumentKey.title, mDisassembleName);
        bd.putSerializable(PageArgumentKey.Url, mDisassembleUrl);
        UIService.getInstance().postPage(PageKey.DeviceFanOilDetail, bd);

    }

}
