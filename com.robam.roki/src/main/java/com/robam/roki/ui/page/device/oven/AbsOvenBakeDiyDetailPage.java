package com.robam.roki.ui.page.device.oven;

import android.os.Bundle;
import android.os.Handler;
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
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.view.RTextView;

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
 * 烘烤diy详情页
 */

public class AbsOvenBakeDiyDetailPage extends BasePage {
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


    String mGuid;
    AbsOven oven;
    DiyCookbookList diyCookbookList;
    private String mCode;
    private String minute;
    private String temp;
    private int openRotate;
    private List<DeviceConfigurationFunctions> mDatas;
    public static AbsOvenBakeDiyDetailPage instance = null;
    private Map<String, String> map;
    private String modeCode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.abs_oven_bake_diy, container, false);
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        diyCookbookList = (DiyCookbookList) bd.getSerializable(PageArgumentKey.Bean);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        oven = Plat.deviceService.lookupChild(mGuid);
        ButterKnife.inject(this, view);
        instance = this;
        selectCode();

        return view;
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

    private void initData() {
        temp = diyCookbookList.temp;
        if(temp.contains("℃")){
            temp=temp.substring(0,temp.indexOf("℃"));
        }
        String name = diyCookbookList.name;
        modeCode = diyCookbookList.modeCode;
        minute = diyCookbookList.minute;
        openRotate = diyCookbookList.openRotate;
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
        if (openRotate == 0) {
            rtRoate.setText("旋转烤关");
        } else if (openRotate == 1) {
            rtRoate.setText("旋转烤开");
        }


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
                bd.putSerializable(PageArgumentKey.List, (Serializable) mDatas);
                UIService.getInstance().postPage(PageKey.AbsOvenBakeEdit, bd);
                break;
            //开始烹饪
            case R.id.tv_begin:
                if (oven.PlatInsertStatueValue == 0) {
                    send1((short) 154, modeCode, Short.decode(minute), Short.decode(temp));
                } else {
                    ToastUtils.show("当前模式不需要隔板，请先拆除隔板", Toast.LENGTH_SHORT);
                }

                break;
        }
    }


    public void send1(final int cmd, final String mode, final int setTime, final int setTemp) {
        if (oven.status == OvenStatus.Off) {
            oven.setOvenStatus(OvenStatus.On, new VoidCallback() {
                @Override
                public void onSuccess() {
                    sendCom((short) cmd, mode, setTime, setTemp);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            sendCom((short) cmd, mode, setTime, setTemp);
        }
    }


    private void sendCom(final int cmd, final String mode, final int setTime, final int setTemp) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                oven.setOvenBakeDIYMode((short) cmd, Short.decode(mode), (short) setTime, (short) setTemp, (short) openRotate, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack().popBack();
                    }
                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show("指令下发失败了哟,请重新下发", Toast.LENGTH_SHORT);
                    }
                });
            }
        }, 500);
    }


}
