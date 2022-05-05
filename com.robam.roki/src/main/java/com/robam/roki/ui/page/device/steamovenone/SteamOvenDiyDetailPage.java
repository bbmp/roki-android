package com.robam.roki.ui.page.device.steamovenone;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.RTextView;
import com.robam.roki.utils.DialogUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 915蒸烤一体机 diy菜谱详情页
 */
public class SteamOvenDiyDetailPage extends BasePage {
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

    @InjectView(R.id.rt_temp1)
    RTextView rtTemp1;

    @InjectView(R.id.down_temp_layout)
    LinearLayout downTempLayout;

    @InjectView(R.id.rotate_layout)
    LinearLayout rotateLayout;


    @InjectView(R.id.rt_min)
    RTextView rtMin;

    @InjectView(R.id.rt_min1)
    RTextView rtMin1;

    @InjectView(R.id.tv_begin)
    TextView tvBegin;

    @InjectView(R.id.iv_edit)
    ImageView ivEdit;

    String mGuid;
    AbsSteameOvenOne absSteameOvenOne;
    DiyCookbookList diyCookbookList;
    private String minute;
    private String temp;
    private String downTemp;
    private List<DeviceOvenDiyParams> deviceList;
    private String needDescalingParams;
    public static SteamOvenDiyDetailPage instance = null;
    private String modeCode;
    private String name;
    String diyType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_steam_oven_diy, container, false);
        Bundle bd = getArguments();
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        diyCookbookList = (DiyCookbookList) bd.getSerializable(PageArgumentKey.Bean);
        deviceList = bd == null ? null : (List<DeviceOvenDiyParams>) bd.getSerializable(PageArgumentKey.List);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        absSteameOvenOne = Plat.deviceService.lookupChild(mGuid);
        diyType = bd == null ? null : bd.getString(PageArgumentKey.diyType);
        ButterKnife.inject(this, view);
        instance = this;
        initData();
        return view;
    }


    private void initData() {
        temp = diyCookbookList.temp;
        if(temp.contains("℃")){
            temp=temp.substring(0,temp.indexOf("℃"));
        }
        downTemp = diyCookbookList.tempDown == null? "0":diyCookbookList.tempDown;
        name = diyCookbookList.name;
        modeCode = diyCookbookList.modeCode;
        minute = diyCookbookList.minute;
        String cookbookDesc = diyCookbookList.cookbookDesc;
        tvTitleName.setText(name);
        tvContent.setText(cookbookDesc);
        rtMode.setText(code2Mode(modeCode));
        rtMin.setText(minute+"分钟");
        rtMin1.setText(minute+"分钟");
        if (!TextUtils.equals(downTemp, "0")) {
            downTempLayout.setVisibility(View.VISIBLE);
            rotateLayout.setVisibility(View.GONE);
            rtTemp1.setText("下：" + downTemp+"℃");
            rtTemp.setText("上：" + temp+"℃");
        } else {
            downTempLayout.setVisibility(View.GONE);
            rotateLayout.setVisibility(View.VISIBLE);
            rtTemp.setText(temp+"℃");
        }
    }


    private String code2Mode(String code) {
        String modeName = null;
        for (int i = 0; i < deviceList.size(); i++) {
            if (String.valueOf(deviceList.get(i).getCode()).equals(code)) {
                modeName = deviceList.get(i).getValue();
            }
        }
        return modeName;
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
                bd.putSerializable(PageArgumentKey.Bean, diyCookbookList);
                bd.putSerializable(PageArgumentKey.List, (Serializable) deviceList);
                bd.putSerializable(PageArgumentKey.title, title);
                bd.putSerializable(PageArgumentKey.diyType, diyType.equals("")?"0":"1");
                UIService.getInstance().postPage(PageKey.SteamOvenEdit, bd);
                break;
            //开始烹饪
            case R.id.tv_begin:
                if (absSteameOvenOne.doorStatusValue == 1) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (needShowDialog(code2Mode(modeCode))) {
                    descalingDialog();
                    return;
                }
                sendSteamOvenModel();
                break;
        }
    }


    //蒸、烤
    private void sendSteamOvenModel() {
        if (absSteameOvenOne.powerStatus == SteamOvenOnePowerStatus.Off
                ||absSteameOvenOne.powerStatus == SteamOvenOnePowerStatus.Wait) {
            absSteameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    absSteameOvenOne.setSteameOvenOneRunMode(Short.parseShort(modeCode), Short.parseShort(minute), Short.parseShort(temp), (short)0, Short.parseShort(downTemp), (short) 0, (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            LogUtils.i("202004143","onSuccess");
//                            UIService.getInstance().popBack().popBack();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("202004143",t.getMessage());
                        }
                    });

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("202004143",t.getMessage());
                }
            });
        } else {
            absSteameOvenOne.setSteameOvenOneRunMode(Short.parseShort(modeCode), Short.parseShort(minute), Short.parseShort(temp), (short)0, Short.parseShort(downTemp), (short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.i("202004143","onSuccess");
//                    UIService.getInstance().popBack().popBack();
//                    UIService.getInstance().popBack();
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("202004143",t.getMessage());
                }
            });
        }
    }

    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (absSteameOvenOne == null || !Objects.equal(absSteameOvenOne.getID(), event.pojo.getID()))
            return;
        absSteameOvenOne = (AbsSteameOvenOne) event.pojo;
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

    private boolean needShowDialog(String model) {
        return false;
    }
}
