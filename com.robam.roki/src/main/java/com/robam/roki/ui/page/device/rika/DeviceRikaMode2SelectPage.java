package com.robam.roki.ui.page.device.rika;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.RikaFunctionParams;
import com.robam.roki.model.bean.RikaSubParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.RikaProMode2Adapter;
import com.robam.roki.ui.adapter.RikaProModeAdapter;
import com.robam.roki.ui.dialog.RikaMultiStepDialog;
import com.robam.roki.ui.page.device.oven.MyGridView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceRikaMode2SelectPage extends BasePage {
    @InjectView(R.id.mgv_oven_mode)
    MyGridView ovenGrid;

    @InjectView(R.id.mgv_steam_mode)
    MyGridView steamGrid;

    @InjectView(R.id.mgv_roast_mode)
    MyGridView roastGrid;

    @InjectView(R.id.tv_title)
    TextView mTitle;

    @InjectView(R.id.tv_mode1)
    TextView mMode1;

    @InjectView(R.id.tv_mode2)
    TextView mMode2;

    @InjectView(R.id.tv_mode3)
    TextView mMode3;

    private List<String> modeNameList = new ArrayList<>();
    private IRokiDialog mRokiDialog;
    private RikaMultiStepDialog mRikaMultiDialog;
    private List<DeviceConfigurationFunctions> deviceConfigurationFunctionsList;
    private Gson gson;
    private short model;
    private short time;
    private short temp;
    private AbsRika mRika;
    private String mViewBackgroundImg;

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {

        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        short steamOvenWorkStatus = mRika.steamOvenWorkStatus;
        if (IRokiFamily.RIKAY.equals(mRika.getDp()) )
            if (steamOvenWorkStatus == RikaStatus.STEAMOVEN_RUN || steamOvenWorkStatus == RikaStatus.STEAMOVEN_PREHEAT) {
                if (mRokiDialog != null) {
                    mRokiDialog.dismiss();
                }
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.RIKA, mRika);
                bundle.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                UIService.getInstance().postPage(PageKey.DeviceRikaYWork, bundle);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_rika_mode_select, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        deviceConfigurationFunctionsList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        initData();
        initView();
        initListener();
        return view;
    }

    private void initData() {
        gson = new Gson();
        for (int i = 0; i < deviceConfigurationFunctionsList.size(); i++) {
            modeNameList.add(deviceConfigurationFunctionsList.get(i).functionName);
        }
    }

    private void initView() {
        mTitle.setText(title);
        boolean isClean = false;
        if (deviceConfigurationFunctionsList.size() <= 2) {
            mMode3.setVisibility(View.GONE);
            ovenGrid.setVisibility(View.GONE);
            isClean = true;
        }
        for (int i = 0; i < deviceConfigurationFunctionsList.size(); i++) {
            RikaProMode2Adapter adapter = new RikaProMode2Adapter(getActivity(), deviceConfigurationFunctionsList.get(i).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions, "", isClean);
            switch (i) {
                case 0:
                    steamGrid.setAdapter(adapter);
                    mMode1.setText(modeNameList.get(i));
                    break;
                case 1:
                    roastGrid.setAdapter(adapter);
                    mMode2.setText(modeNameList.get(i));
                    break;
                case 2:
                    ovenGrid.setAdapter(adapter);
                    mMode3.setText(modeNameList.get(i));
                    break;
            }
        }
        steamGrid.setVerticalSpacing(50);
        roastGrid.setVerticalSpacing(50);
        ovenGrid.setVerticalSpacing(50);
    }

    private void initListener() {
        steamGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = deviceConfigurationFunctionsList.get(0).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions.get(position);
                String funParams = deviceConfigurationFunctions.functionParams;
                try {
                    JSONObject params = new JSONObject(funParams);
                    String strParam = params.getString("params");
                    RikaSubParams param = gson.fromJson(strParam, RikaSubParams.class);
                    showDialog(param, position);
                } catch (Exception e) {
                    try {
                        JSONObject params = new JSONObject(funParams);
                        JSONObject strParam = new JSONObject(params.getString("params"));
                        final JSONObject model = new JSONObject(strParam.getString("model"));
                        final String modelValue = model.getString("value");
                        if (mRika == null) {
                            return;
                        }
                        if (mRika.steamOvenWorkStatus != RikaStatus.STEAMOVEN_ON) {
                            mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                                    (short) 49, (short) 1, RikaStatus.STEAMOVEN_ON, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                                    (short) 50, (short) 4, Short.parseShort(modelValue), (short) 255, Short.parseShort(modelValue) == 24? (short)10:(short) 255, new VoidCallback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onFailure(Throwable t) {
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                        } else {
                            mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                    (short) 50, (short) 4, Short.parseShort(modelValue), (short) 255, Short.parseShort(modelValue) == 24? (short)10:(short) 255, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                        }
                    } catch (Exception exception) {

                    }
                    e.printStackTrace();
                }

            }
        });
        roastGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DeviceConfigurationFunctions deviceConfigurationFunctions = deviceConfigurationFunctionsList.get(1).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions.get(position);
                String funParams = deviceConfigurationFunctions.functionParams;
                try {
                    JSONObject params = new JSONObject(funParams);
                    String strParam = params.getString("params");
                    RikaSubParams param = gson.fromJson(strParam, RikaSubParams.class);
                    if (TextUtils.equals(param.model.value,"19")) {
                        if (mRika == null) {
                            return;
                        }
                        if (mRika.steamOvenWorkStatus != RikaStatus.STEAMOVEN_ON) {
                            mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                                    (short) 49, (short) 1, RikaStatus.STEAMOVEN_ON, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {
                                            mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                                    (short) 50, (short) 4, (short) 19, (short) 60, (short) 100, new VoidCallback() {
                                                        @Override
                                                        public void onSuccess() {
                                                        }

                                                        @Override
                                                        public void onFailure(Throwable t) {
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                        } else {
                            mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                    (short) 50, (short) 4, (short) 19, (short) 60, (short) 100, new VoidCallback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                        }
                        return;
                    }
                    showDialog(param, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }



    private void showDialog(final RikaSubParams functions, int index) {
        List<Integer> tempList = TestDatas.createModeDataTemp(functions.workTemp.value);
        List<Integer> timeList = TestDatas.createModeDataTime(functions.workTime.value);
        int timeCount = 1;
        if (functions.workTime.value.size() > 1) {
            timeCount = functions.workTime.value.get(2);
        }
        //拿到时间温度的索引值
        int indexTemp = Short.parseShort(functions.workTempDefault.value) - tempList.get(0);
        int indexTime = Short.parseShort(functions.workTimeDefault.value) - timeList.get(0);
        indexTime = indexTime / timeCount;
        mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_17);
        mRokiDialog.setWheelViewData(
                HelperRikaData.getTempData2(tempList),
                null,
                HelperRikaData.getTimeData3(timeList, timeCount),
                "",
                false,
                indexTemp,
                0,
                indexTime,
                new OnItemSelectedListenerFrone() {
                    @Override
                    public void onItemSelectedFront(String contentFront) {
                        String tempStr = contentFront.substring(0, contentFront.length()-1);
                        temp = Short.parseShort(tempStr);
                        model = Short.parseShort(functions.model.value);
                    }
                },
                null,
                new OnItemSelectedListenerRear() {
                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        String timeStr = contentRear.substring(0, contentRear.length()-2);
                        time = Short.parseShort(timeStr);
                        model = Short.parseShort(functions.model.value);
                    }
                }
        );
        mRokiDialog.setOkBtn("开始工作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
                if (mRika == null) {
                    return;
                }
                if (mRika.steamOvenWorkStatus != RikaStatus.STEAMOVEN_ON) {
                    mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                            (short) 49, (short) 1, RikaStatus.STEAMOVEN_ON, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                            (short) 50, (short) 4, model, temp, time, new VoidCallback() {
                                                @Override
                                                public void onSuccess() {
                                                }

                                                @Override
                                                public void onFailure(Throwable t) {
                                                }
                                            });
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
                } else {
                    mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                            (short) 50, (short) 4, model, temp, time, new VoidCallback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
                }
            }
        });
        mRokiDialog.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });
        mRokiDialog.setCanceledOnTouchOutside(true);
        mRokiDialog.show();
    }

}
