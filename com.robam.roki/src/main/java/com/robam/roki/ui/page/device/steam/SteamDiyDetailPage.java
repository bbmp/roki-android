package com.robam.roki.ui.page.device.steam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.RTextView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by RuanWei on 2020/7/7.
 */

public class SteamDiyDetailPage extends BasePage {
    @InjectView(R.id.mode_back)
    ImageView mode_back;

    @InjectView(R.id.tv_title_name)
    TextView tvTitleName;

    @InjectView(R.id.tv_content)
    TextView tvContent;

    @InjectView(R.id.rt_mode)
    RTextView rtMode;

    @InjectView(R.id.rt_temp)
    RTextView rtTemp;

    @InjectView(R.id.rt_min)
    RTextView rtMin;

    @InjectView(R.id.rt_roate)
    RTextView rtRoate;

    @InjectView(R.id.tv_begin)
    TextView tvBegin;

    @InjectView(R.id.iv_edit)
    ImageView ivEdit;
    DiyCookbookList diyCookbookList;
    String mGuid;
    String needDescalingParams;
    private List<DeviceConfigurationFunctions> mDatas;
    AbsSteamoven steam;
    private Map<String, String> map;
    public static SteamDiyDetailPage instance = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_oven_bake_diy, container, false);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        diyCookbookList = (DiyCookbookList) bd.getSerializable(PageArgumentKey.Bean);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        steam = Plat.deviceService.lookupChild(mGuid);
        ButterKnife.inject(this, view);
        instance = this;
        initView();
        selectCode();
        return view;
    }

    private void initView() {
        rtRoate.setVisibility(View.INVISIBLE);
    }

    private void selectCode() {
        for (int i = 0; i < mDatas.size(); i++) {
            if ("diy".equals(mDatas.get(i).functionCode)) {
                String functionParams = mDatas.get(i).functionParams;
                try {
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject model = jsonObject.getJSONObject("model");
                    map = new HashMap<>();
                    Iterator<String> keys = model.keys();
                    while (keys.hasNext()) {
                        String next = keys.next();
                        String mode = model.getJSONObject(next).getString("value");
                        map.put(mode, next);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        initData();
    }

    private String temp;
    private String modeCode;
    private String minute;
    private String mCode;

    private void initData() {
        temp = diyCookbookList.temp;
        if(temp.contains("℃")){
            temp=temp.substring(0,temp.indexOf("℃"));
        }
        String name = diyCookbookList.name;
        modeCode = diyCookbookList.modeCode;
        LogUtils.i("20191113666", "modeCode::::" + modeCode);
        minute = diyCookbookList.minute;
        String cookbookDesc = diyCookbookList.cookbookDesc;

        for (Map.Entry<String, String> str : map.entrySet()) {
            if (str.getValue().equals(modeCode)) {
                mCode = str.getKey();
            }

        }


        tvTitleName.setText(name);
        tvContent.setText(cookbookDesc);
        rtMode.setText(mCode);
        rtTemp.setText(temp + "℃");
        rtMin.setText(minute + "分钟");


    }

    public void close() {
        UIService.getInstance().popBack();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.mode_back, R.id.iv_edit, R.id.tv_begin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mode_back:
                UIService.getInstance().popBack();
                break;
            case R.id.iv_edit:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                bd.putString(PageArgumentKey.title, "编辑菜谱");
                bd.putSerializable(PageArgumentKey.Bean, diyCookbookList);
                bd.putSerializable(PageArgumentKey.List, (Serializable) mDatas);
                UIService.getInstance().postPage(PageKey.SteamDiyEdit, bd);
                break;
            //开始烹饪
            case R.id.tv_begin:
                if (steam.descaleModeStageValue != 0||steam.WeatherDescalingValue == 1) {
                    descalingDialog();
                    return;
                }
                send((short)133,modeCode,Short.decode(temp),Short.decode(minute));
                break;
        }
    }


    private void send(final int cmd, final String mode, final int setTemp, final int setTime) {
        if (steam != null) {
            ToolUtils.logEvent(steam.getDt(), "开始蒸箱模式温度时间工作:" + mode + ":" + setTemp + ":" + setTime, "roki_设备");
        }
        if (steam.doorState == 0) {
            ToastUtils.show("门未关好，请先关好箱门", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.waterboxstate == 0) {
            ToastUtils.show(R.string.device_alarm_water_out, Toast.LENGTH_SHORT);
            return;
        }

        if (steam.status == SteamStatus.AlarmStatus) {
            ToastUtils.show("请先解除报警", Toast.LENGTH_SHORT);
            return;
        }
        if (steam.descaleModeStageValue != 0) {
            descalingDialog();
            return;
        }
        if (steam.status == SteamStatus.Off||steam.status == SteamStatus.Wait) {
            steam.setSteamStatus(SteamStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom(cmd, mode, setTime, setTemp);

                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            sendCom(cmd, mode, setTime, setTemp);

        }


    }


    private void sendCom(int cmd, String mode, int setTime, int setTemp) {
        steam.setSteamCookModule((short) cmd,
                Short.decode(mode), (short) setTemp, (short) setTime,
                (short) 0, (short) 0, (short) 0, (short) 0, (short) 0,
                (short) 0, (short) 0, (short) 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200704", t.getMessage());

                    }
                });
    }

    private void descalingDialog() {
        String descalingTitle = null;
        String descalingContent = null;
        String descalingButton = null;
        try {
            if (!"".equals(needDescalingParams)) {
                JSONObject jsonObject = new JSONObject(needDescalingParams);
                JSONObject needDescaling = jsonObject.getJSONObject("needDescaling");
                descalingTitle = needDescaling.getString("title");
                descalingContent = needDescaling.getString("content");
                descalingButton = needDescaling.getString("positiveButton");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        dialogByType.setTitleText(descalingTitle);
        dialogByType.setContentText(descalingContent);
        dialogByType.setOkBtn(descalingButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogByType != null) {
                    dialogByType.dismiss();
                }
            }
        });
        dialogByType.show();
    }
}
