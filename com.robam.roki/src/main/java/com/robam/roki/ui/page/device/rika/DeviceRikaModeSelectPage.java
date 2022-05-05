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
import com.j256.ormlite.stmt.query.In;
import com.legent.VoidCallback;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.bean.RikaFunctionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.RikaProModeAdapter;
import com.robam.roki.ui.dialog.RikaMultiStepDialog;
import com.robam.roki.ui.page.device.oven.MyGridView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DeviceRikaModeSelectPage extends BasePage {
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

    private String functionParams;
    private String multiParams;
    private List<RikaFunctionParams> deviceConfigurationFunctionsList;
    private List<String> modeNameList = new ArrayList<>();
    private IRokiDialog mRokiDialog;
    private RikaMultiStepDialog mRikaMultiDialog;
    private ArrayList<RikaFunctionParams.MultiParams> mMultiList = new ArrayList<>();
    private AbsRika mRika;
    private short mTemp;
    private short mTime;
    private String mViewBackgroundImg;

    private Short multiModel1;
    private Short multiModel2;
    private Short multiTemp1;
    private Short multiTemp2;
    private Short multiTime1;
    private Short multiTime2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_rika_mode_select, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        functionParams = bd == null ? null : bd.getString(PageArgumentKey.functionParams);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        initData();
        initView();
        initListener();
        return view;
    }

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

    private void initData() {
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonElements = jsonParser.parse(functionParams).getAsJsonArray();//获取JsonArray对象
        deviceConfigurationFunctionsList = new ArrayList<>();
        for (int i = 0; i < jsonElements.size(); i++) {
//            try {
//                JSONObject modeLists = new JSONObject(jsonElements.get(i).toString());
//                JSONArray modeList = modeLists.getJSONArray("modeList");
//                for (int j = 1; j < modeList.length(); j++) {
//                    JSONObject mode = new JSONObject(modeList.getJSONObject(j).getString("mode"));
//                    if (TextUtils.equals(mode.getString("title"), "多段")) {
//                        multiParams = mode.getString("params");
//                        JSONObject allMode = new JSONObject(multiParams);
//
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
            RikaFunctionParams params = gson.fromJson(jsonElements.get(i), RikaFunctionParams.class);//解析
            deviceConfigurationFunctionsList.add(params);
            modeNameList.add(params.name);
        }
        for (int i = 0; i < deviceConfigurationFunctionsList.size(); i++) {
            for (int j = 0; j < deviceConfigurationFunctionsList.get(i).modeList.size(); j++) {
                if (!TextUtils.equals("多段", deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.title)) {
                    RikaFunctionParams.MultiParams multiParams = new RikaFunctionParams.MultiParams();
                    multiParams.mode = deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.params.setMeum.value;
                    multiParams.modeName = deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.title;
                    multiParams.tempList = deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.params.workTemp.value;
                    multiParams.timeList = deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.params.workTime.value;
                    multiParams.defaultTemp = Short.parseShort(deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.params.workTempDefault.value);
                    multiParams.defaultTime = Short.parseShort(deviceConfigurationFunctionsList.get(i).modeList.get(j).mode.params.workTimeDefault.value);
                    mMultiList.add(multiParams);
                }
            }
        }
    }

    private void initView() {
        mTitle.setText(title);
        if (deviceConfigurationFunctionsList.size() <= 2) {
            mMode3.setVisibility(View.GONE);
            ovenGrid.setVisibility(View.GONE);
        }
        for (int i = 0; i < deviceConfigurationFunctionsList.size(); i++) {
            RikaProModeAdapter adapter = new RikaProModeAdapter(getActivity(), deviceConfigurationFunctionsList.get(i), "");
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
                RikaFunctionParams functions = deviceConfigurationFunctionsList.get(0);
                showDialog(functions, position);
            }
        });
        roastGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RikaFunctionParams functions = deviceConfigurationFunctionsList.get(1);
                showDialog(functions, position);
            }
        });
        ovenGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RikaFunctionParams functions = deviceConfigurationFunctionsList.get(2);
                if (TextUtils.equals(functions.modeList.get(position).mode.title, "多段")) {
                    showMultiDialog();
                    return;
                }
                showDialog(functions, position);
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

    private void showDialog(RikaFunctionParams functions, int index) {
        final String model = functions.modeList.get(index).mode.params.setMeum.value;
        List<Integer> tempList = TestDatas.createModeDataTemp(functions.modeList.get(index).mode.params.workTemp.value);
        List<Integer> timeList = TestDatas.createModeDataTime(functions.modeList.get(index).mode.params.workTime.value);
        int timeCount = 1;
        if (functions.modeList.get(index).mode.params.workTime.value.size() > 1) {
            timeCount = functions.modeList.get(index).mode.params.workTime.value.get(2);
        }
        //拿到时间温度的索引值
        int indexTemp = Short.parseShort(functions.modeList.get(index).mode.params.workTempDefault.value) - tempList.get(0);
        int indexTime = Short.parseShort(functions.modeList.get(index).mode.params.workTimeDefault.value) - timeList.get(0);
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
                        String content = contentFront.substring(0, contentFront.length() - 1);
                        mTime = Short.parseShort(content);
                    }
                },
                null,
                new OnItemSelectedListenerRear() {
                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        String content = contentRear.substring(0, contentRear.length() - 2);
                        mTemp = Short.parseShort(content);
                    }
                }
        );
        mRokiDialog.setOkBtn("开始工作", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRika == null) {
                    return;
                }
                if (mRika.steamOvenWorkStatus != RikaStatus.STEAMOVEN_ON) {
                    mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                            (short) 49, (short) 1, RikaStatus.STEAMOVEN_ON, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                            (short) 50, (short) 4, (short) Short.parseShort(model), mTemp, mTime, new VoidCallback() {
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
                            (short) 50, (short) 4, (short) Short.parseShort(model), mTemp, mTime, new VoidCallback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });
                }
                mRokiDialog.dismiss();
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

    private void showMultiDialog() {

        mRikaMultiDialog = new RikaMultiStepDialog(getActivity(), mMultiList);
        mRikaMultiDialog.setCanceledOnTouchOutside(true);
        mRikaMultiDialog.setListener(new RikaMultiStepDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(ArrayList<Integer> oneList, ArrayList<Integer> twoList) {
                for (int i = 0; i < oneList.size(); i++) {
                        multiModel1 = Short.parseShort(mMultiList.get(oneList.get(0)).mode);
                        List<Integer> tempList = TestDatas.createModeDataTemp(mMultiList.get(oneList.get(0)).tempList);
                        multiTemp1 = tempList.get(oneList.get(1)).shortValue();
                        List<Integer> timeList = TestDatas.createModeDataTime(mMultiList.get(oneList.get(0)).timeList);
                        multiTime1 = timeList.get(oneList.get(2)).shortValue();
                }
                for (int i = 0; i < twoList.size(); i++) {
                        multiModel2 = Short.parseShort(mMultiList.get(twoList.get(0)).mode);
                        List<Integer> tempList = TestDatas.createModeDataTemp(mMultiList.get(twoList.get(0)).tempList);
                        multiTemp2 = tempList.get(twoList.get(1)).shortValue();
                        List<Integer> timeList = TestDatas.createModeDataTime(mMultiList.get(twoList.get(0)).timeList);
                        multiTime2 = timeList.get(twoList.get(2)).shortValue();

                    if (mRika == null) {
                        return;
                    }
                    if (mRika.steamOvenWorkStatus != RikaStatus.STEAMOVEN_ON) {
                        mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAMOVEN_ON, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        mRika.setSteamOvenMultiStepCooking(MsgKeys.setRikaOvenMultiStep_Req, (short) 1,RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                                multiModel1, multiTemp1, multiTime1,
                                                multiModel2, multiTemp2, multiTime2,
                                                new VoidCallback() {
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
                        mRika.setSteamOvenMultiStepCooking(MsgKeys.setRikaOvenMultiStep_Req, (short) 1,RikaStatus.STEAMQVEN_CATEGORYCODE, (short) 2,
                                multiModel1, multiTemp1, multiTime1,
                                multiModel2, multiTemp2, multiTime2,
                                new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });
                    }
                }

            }
        });
        mRikaMultiDialog.showDialog(mRikaMultiDialog);
    }

}
