package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.SteamOvenModelFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.DeviceModelAdapter;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/6.
 */
public class SteamOvenModelSelected620Page extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private DeviceModelAdapter mDeviceModelAdapter;
    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringTempList = new ArrayList<String>();
    String mGuid;
    String mTitle;
    String functionName;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setDeviceRunData((String) msg.obj);
                    break;
                case 2:
                    setDeviceRunData((String) msg.obj);
                    break;
            }
        }
    };
    private IRokiDialog mRokiDialog;
    private List<DeviceConfigurationFunctions> mDeviceSelectModelList;
    private int mSteamModel;
    AbsSteameOvenOneNew mSteamOvenOne;

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (mSteamOvenOne == null || !Objects.equal(mSteamOvenOne.getID(), event.pojo.getID()))
            return;

        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Pause
                || mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.Order
                || mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) {
            if (mRokiDialog != null && mRokiDialog.isShow()) {
                mRokiDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        Bundle bd = getArguments();
        mDeviceSelectModelList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        mSteamOvenOne = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.page_device_model_selected, container, false);
        ButterKnife.inject(this, view);
        mTvDeviceModelName.setText(mTitle);
        initData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSteamOvenOne == null)
            return;
        switch (mTitle) {
            case "蒸模式":
                MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), mSteamOvenOne.getDt() + ":蒸模式页", null);
                break;
            case "烤模式":
                MobApp.getmFirebaseAnalytics().setCurrentScreen(getActivity(), mSteamOvenOne.getDt() + ":烤模式页", null);
                break;
        }

    }

    private void initData() {

        mDeviceModelAdapter = new DeviceModelAdapter(cx, mDeviceSelectModelList, new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                modelSelectItemEvent(view);
            }
        });
        mRecyclerView.setAdapter(mDeviceModelAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(cx, 4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(gridLayoutManager);

    }

    //模式选择参数写入
    private void modelSelectItemEvent(View view) {
        if (mSteamOvenOne.powerOnStatus == SteamOvenOnePowerOnStatus.AlarmStatus) {
            ToastUtils.showShort(R.string.device_alarm_close_content);
            return;
        }
        try {
            if (mDeviceSelectModelList != null && mDeviceSelectModelList.size() > 0) {
                //                    }
                for (int i = 0; i < mDeviceSelectModelList.size(); i++) {
                    if (view.getTag().toString().equals(mDeviceSelectModelList.get(i).functionCode)) {
                        functionName = mDeviceSelectModelList.get(i).functionName;
//                        if ("freshSteamed".equals(view.getTag().toString()) || "nutritionSteaming".equals(view.getTag().toString())
//                                || "strongSteam".equals(view.getTag().toString()) || "fermentation".equals(view.getTag().toString())) {
                        ArrayList<DeviceConfigurationFunctions> descList = Lists.newArrayList();
                        descList.clear();
                        descList.add(mDeviceSelectModelList.get(i));
                        String freshSteamedParams = mDeviceSelectModelList.get(i).functionParams;
                        SteamOvenModelFunctionParams functionParams = JsonUtils.json2Pojo(freshSteamedParams, SteamOvenModelFunctionParams.class);
                        mSteamModel = Short.parseShort(functionParams.getParam().getModel().getValue());
                        String tempDefaultValue = functionParams.getParam().getDefaultSetTemp().getValue();
                        String timeDefaultValue = functionParams.getParam().getDefaultSetTime().getValue();
                        short newDefaultTemp = (Short.parseShort(tempDefaultValue));
                        short newDefaultTime = (Short.parseShort(timeDefaultValue));
                        List<Integer> setTempList = functionParams.getParam().getSetTemp().getValue();
                        List<Integer> setTimeList = functionParams.getParam().getSetTime().getValue();
                        //拿到时间温度的索引值
                        int indexTemp = newDefaultTemp - setTempList.get(0);
                        int indexTime = newDefaultTime - setTimeList.get(0);

                        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_17);
                        mRokiDialog.setWheelViewData(
                                HelperRikaData.getTempData(setTempList),
                                null,
                                HelperRikaData.getTimeData(setTimeList),
                                descList,
                                false,
                                indexTemp,
                                0,
                                indexTime,
                                contentFront -> {
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = contentFront;
                                    msg.what = 1;
                                    mHandler.sendMessage(msg);

                                },
                                null,
                                contentRear -> {
                                    Message msg = mHandler.obtainMessage();
                                    msg.obj = contentRear;
                                    msg.what = 2;
                                    mHandler.sendMessage(msg);
                                });
                        mRokiDialog.show();
                    } else {
                        //TODo
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setDeviceRunData(String data) {
        LogUtils.i("20180731", " data:" + data);
        if (data.contains(StringConstantsUtil.STRING_DEGREE_CENTIGRADE)) {
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringTempList.add(removetTempString);
        }
        if (data.contains(StringConstantsUtil.STRING_MINUTES)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringTimeList.add(removeTimeString);
        }
        mRokiDialog.setOkBtn(R.string.work_start, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
                String temp = stringTempList.get(stringTempList.size() - 1);
                String time = stringTimeList.get(stringTimeList.size() - 1);
                LogUtils.i("20180731", " temp:" + temp + " time:" + time);
                final short newTemp = Short.parseShort(temp);
                final int newTime = Integer.parseInt(time);
                if (mSteamOvenOne!=null) {
                    ToolUtils.logEvent(mSteamOvenOne.getDt(), "开始一体机"+mTitle+"温度时间工作:" + functionName + ":" + newTemp + ":" + newTime, "roki_设备");
                }
                assert mSteamOvenOne != null;
                mSteamOvenOne.setSteameOvenOneRunMode((short) mSteamModel, newTime, newTemp, (short) 0,  (short) 0,new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });

        mRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });

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


}
