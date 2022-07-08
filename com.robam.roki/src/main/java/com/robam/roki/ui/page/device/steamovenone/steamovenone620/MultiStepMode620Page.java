package com.robam.roki.ui.page.device.steamovenone.steamovenone620;

import android.graphics.Color;
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
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.SteamOvenBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.TimeAdapter;
import com.robam.roki.ui.adapter.TimeViewHolder;
import com.robam.roki.ui.wheel.WheelView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.JsonUtils;
import com.robam.roki.utils.TestDatas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * c915
 * 蒸烤一体机多段模式
 */
public class MultiStepMode620Page extends BasePage {
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.wv1_mode_name)
    WheelView wv1ModeName;
    @InjectView(R.id.wv1_temp)
    WheelView wv1Temp;
    @InjectView(R.id.wv1_minute)
    WheelView wv1Minute;

    @InjectView(R.id.wv2_mode_name)
    WheelView wv2ModeName;
    @InjectView(R.id.wv2_temp)
    WheelView wv2Temp;
    @InjectView(R.id.wv2_minute)
    WheelView wv2Minute;

    @InjectView(R.id.ll_add)
    LinearLayout llAdd;
    @InjectView(R.id.ll_two)
    LinearLayout llTwo;

    @InjectView(R.id.tv_begin_work)
    TextView tvBeginWork;

    String guid;
    AbsSteameOvenOneNew steameOvenOne;
    List<DeviceConfigurationFunctions> mDatas;
    private long userId;
    private List<Integer> keyList;
    String needDescalingParams;

    int indexSelectMode = 0;
    int indexSelectTemp;
    int indexSelectTime;
    int indexSelectMode2 = -1;
    int indexSelectTemp2;
    int indexSelectTime2;
    private List<SteamOvenBean> beanList;
    private TimeAdapter tempAdapter;
    private TimeAdapter timeAdapter;
    private List<Integer> temp;
    private List<Integer> time;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_multi_step_mode, container, false);
        ButterKnife.inject(this, view);
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenOne = Plat.deviceService.lookupChild(guid);
        mDatas = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        needDescalingParams = bd == null ? null : bd.getString(PageArgumentKey.descaling);
        userId = Plat.accountService.getCurrentUserId();
        initData();
        initView();
        return view;
    }


    private void initData() {

        try {
            for (int i = 0; i < mDatas.size(); i++) {
                if ("ddModel".equals(mDatas.get(i).functionCode)) {
                    String functionParams = mDatas.get(i).functionParams;
                    JSONObject jsonObject = new JSONObject(functionParams);
                    JSONObject param = jsonObject.getJSONObject("param");
                    Map json2Map = JsonUtils.getJson2Map(param.toString());
                    keyList = new ArrayList<>();
                    Set<String> strings = json2Map.keySet();
                    for (String str : strings) {
                        if (StringUtils.isNumber(str)) {
                            keyList.add(Integer.valueOf(str));
                        } else {
                            keyList.add(999);
                        }
                    }
                    LogUtils.i("20200427888", "keyList1:::" + keyList.toString());
                    Collections.sort(keyList);
                    LogUtils.i("20200427888", "keyList2:::" + keyList.toString());

                    if (beanList != null) {
                        beanList.clear();
                    }
                    beanList = new ArrayList<>();
                    for (int i1 = 0; i1 < keyList.size(); i1++) {
                        Integer integer = keyList.get(i1);
                        SteamOvenBean steamOvenBean = new SteamOvenBean();
                        JSONObject jsonOb = param.getJSONObject(String.valueOf(integer));
                        String model = jsonOb.getJSONObject("model").getString("value");
                        JSONArray setTemp = jsonOb.getJSONObject("setTemp").getJSONArray("value");
                        JSONArray setTime = jsonOb.getJSONObject("setTime").getJSONArray("value");
                        String defaultSetTemp = jsonOb.getJSONObject("defaultSetTemp").getString("value");
                        String defaultSetTime = jsonOb.getJSONObject("defaultSetTime").getString("value");
                        String id = jsonOb.getJSONObject("id").getString("value");
                        steamOvenBean.setId(Integer.parseInt("".equals(id) ? "0" : id));
                        steamOvenBean.setCode(String.valueOf(integer));
                        steamOvenBean.setModeName(model);
                        steamOvenBean.setTemp(array2List(setTemp));
                        steamOvenBean.setTime(array2List(setTime));
                        steamOvenBean.setDefaultTemp(defaultSetTemp);
                        steamOvenBean.setDefaultTime(defaultSetTime);
                        beanList.add(steamOvenBean);
                    }
                    LogUtils.i("20200427888", "beanList:::" + beanList.toString());
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private List<SteamOvenBean> search(List<SteamOvenBean> steamOvenBeans) {
        Collections.sort(steamOvenBeans, (o1, o2) -> {
            if (o1.getId() > o2.getId()) {
                return 1;
            }
            if (o1.getId() == o2.getId()) {
                return 0;
            }
            return -1;
        });
        return steamOvenBeans;
    }

    private void initView() {
        tvTitle.setText(title);
        DiyParamAdapter diyParamAdapter = new DiyParamAdapter(search(beanList));
        wv1ModeName.setAdapter(diyParamAdapter);
        wv1ModeName.setDefaultPosition(indexSelectMode);
        temp = beanList.get(indexSelectMode).getTemp();
        time = beanList.get(indexSelectMode).getTime();
        setDefaultPosition();
        tempAdapter = new TimeAdapter<Integer>(temp, "℃");
        wv1Temp.setAdapter(tempAdapter);
        timeAdapter = new TimeAdapter<Integer>(time, "分钟");
        wv1Minute.setAdapter(timeAdapter);

        wv1ModeName.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode = index;
                temp = beanList.get(indexSelectMode).getTemp();
                time = beanList.get(indexSelectMode).getTime();
                setDefaultPosition();
                tempAdapter = new TimeAdapter<Integer>(temp, "℃");
                wv1Temp.setAdapter(tempAdapter);

                timeAdapter = new TimeAdapter<Integer>(time, "分钟");
                wv1Minute.setAdapter(timeAdapter);


            }
        });

        wv1Temp.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTemp = index;
            }
        });
        wv1Minute.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime = index;

            }
        });

    }

    private void setDefaultPosition() {
        String defaultTemp = beanList.get(indexSelectMode).getDefaultTemp();
        for (int i = 0; i < temp.size(); i++) {
            if (defaultTemp.equals(temp.get(i).toString())) {
                indexSelectTemp = i;
                wv1Temp.setDefaultPosition(indexSelectTemp);

            }
        }

        String defaultTime = beanList.get(indexSelectMode).getDefaultTime();
        for (int i = 0; i < time.size(); i++) {
            if (defaultTime.equals(time.get(i).toString())) {
                indexSelectTime = i;
                wv1Minute.setDefaultPosition(indexSelectTime);

            }
        }
    }

    private void setDefaultPosition2() {
        String defaultTemp = beanList.get(indexSelectMode2).getDefaultTemp();
        for (int i = 0; i < temp.size(); i++) {
            if (defaultTemp.equals(temp.get(i).toString())) {
                indexSelectTemp2 = i;
                wv2Temp.setDefaultPosition(indexSelectTemp2);

            }
        }

        String defaultTime = beanList.get(indexSelectMode2).getDefaultTime();
        for (int i = 0; i < time.size(); i++) {
            if (defaultTime.equals(time.get(i).toString())) {
                indexSelectTime2 = i;
                wv2Minute.setDefaultPosition(indexSelectTime2);

            }
        }
    }


    private List<Integer> array2List(JSONArray jsonArray) {
        List<Integer> templist = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            Integer tem = null;
            try {
                tem = (Integer) jsonArray.get(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            templist.add(tem);
        }
        List<Integer> list = TestDatas.createModeDataTemp(templist);

        return list;
    }


    @OnClick({R.id.iv_back, R.id.ll_add, R.id.tv_begin_work})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.ll_add:
                llTwo.setVisibility(View.VISIBLE);
                llAdd.setVisibility(View.GONE);
                initSecondView();
                break;
            //开始工作
            case R.id.tv_begin_work:
                if (steameOvenOne.doorStatusValue == 1) {
                    ToastUtils.show("门未关好，请检查并确认关好门", Toast.LENGTH_SHORT);
                    return;
                }
                if (indexSelectMode2 == -1) {
                    String mode1 = beanList.get(indexSelectMode).getCode();
                    Integer temp1 = beanList.get(indexSelectMode).getTemp().get(indexSelectTemp);
                    Integer time1 = beanList.get(indexSelectMode).getTime().get(indexSelectTime);
                    LogUtils.i("20200427", "mode1:::" + mode1 + "  temp1:::" + temp1 + "  time1:::" + time1);
                    if (needShowDialog(beanList.get(indexSelectMode).getModeName())) {
                        descalingDialog();
                        return;
                    }
                    sendCommand((short) 2, (short) 1, mode1, temp1, time1, "0", (short) 0, (short) 0);
                } else {
                    Integer temp1 = beanList.get(indexSelectMode).getTemp().get(indexSelectTemp);
                    Integer time1 = beanList.get(indexSelectMode).getTime().get(indexSelectTime);
                    Integer temp2 = beanList.get(indexSelectMode2).getTemp().get(indexSelectTemp2);
                    Integer time2 = beanList.get(indexSelectMode2).getTime().get(indexSelectTime2);
                    String mode1 = beanList.get(indexSelectMode).getCode();
                    String mode2 = beanList.get(indexSelectMode2).getCode();
                    if (temp1 == 0 && time1 == 0 && temp2 == 0 && time2 == 0 && "".equals(mode1) && "".equals(mode2)) {
                        return;
                    }
                    if (needShowDialog(beanList.get(indexSelectMode).getModeName()) || needShowDialog(beanList.get(indexSelectMode2).getModeName())) {
                        descalingDialog();
                        return;
                    }
                    sendCommand((short) 3, (short) 2, mode1, temp1, time1, mode2, temp2, time2);
                }

                break;
        }
    }

    private void sendCommand(final short arg, final short totalNumber, final String mode1, final int temp1, final int time1, final String mode2, final int temp2, final int time2) {
        if (steameOvenOne.powerState == SteamOvenOnePowerStatus.Off ||
                steameOvenOne.powerState == SteamOvenOnePowerStatus.Wait
                ) {
            steameOvenOne.setSteameOvenStatus_on(new VoidCallback() {
                @Override
                public void onSuccess() {
                    setMultiStep(arg, totalNumber, mode1, (short) temp1, (short) time1, mode2, (short) temp2, (short) time2);
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200319", "开机：" + t.getMessage());
                }
            });
        } else {
            setMultiStep(arg, totalNumber, mode1, (short) temp1, (short) time1, mode2, (short) temp2, (short) time2);

        }

    }

    private void setMultiStep(short arg, short totalNumbers, String mode1, short temp1, short time1, String mode2, short temp2, short time2) {
        steameOvenOne.setSteamOvenOneMultiStepMode(arg, totalNumbers, Short.parseShort(mode1), temp1,
                time1, Short.parseShort(mode2), temp2, time2, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20200408999999", t.getMessage());

                    }
                });
    }


    private void initSecondView() {
        indexSelectMode2 = 0;
        DiyParamAdapter diyParamAdapter = new DiyParamAdapter(beanList);
        wv2ModeName.setAdapter(diyParamAdapter);
        wv2ModeName.setDefaultPosition(indexSelectMode2);


        temp = beanList.get(indexSelectMode2).getTemp();
        time = beanList.get(indexSelectMode2).getTime();

        setDefaultPosition2();

        tempAdapter = new TimeAdapter<Integer>(temp, "℃");
        wv2Temp.setAdapter(tempAdapter);
        timeAdapter = new TimeAdapter<Integer>(time, "分钟");
        wv2Minute.setAdapter(timeAdapter);

        wv2ModeName.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectMode2 = index;
                temp = beanList.get(indexSelectMode2).getTemp();
                time = beanList.get(indexSelectMode2).getTime();
                setDefaultPosition2();
                tempAdapter = new TimeAdapter<Integer>(temp, "℃");
                wv2Temp.setAdapter(tempAdapter);

                timeAdapter = new TimeAdapter<Integer>(time, "分钟");
                wv2Minute.setAdapter(timeAdapter);


            }
        });

        wv2Temp.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTemp2 = index;
            }
        });
        wv2Minute.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexSelectTime2 = index;

            }
        });
    }

    class DiyParamAdapter extends WheelView.WheelAdapter<TimeViewHolder> {
        private List<SteamOvenBean> deviceList;

        public DiyParamAdapter(List<SteamOvenBean> deviceList) {
            this.deviceList = deviceList;
        }

        private int pos = 0;


        @Override
        public int getItemCount() {
            return (deviceList != null && deviceList.size() > 0) ? deviceList.size() : 0;
        }

        @Override
        public TimeViewHolder onCreateViewHolder(LayoutInflater inflater, int viewType) {
            return new TimeViewHolder(inflater.inflate(R.layout.timeview_item, null, false));
        }

        @Override
        public void onBindViewHolder(TimeViewHolder holder, int position) {
            String model = deviceList.get(position).getModeName();
            holder.tv.setText(model);
            if (position == pos) {
                holder.tv.setTextColor(Color.parseColor("#000000"));
            } else {
                holder.tv.setTextColor(Color.GRAY);
            }

        }

        @Override
        public void getSelect(int pos) {
            this.pos = pos;
            notifyDataSetChanged();

        }
    }

    //开门事件
    @Subscribe
    public void onEvent(SteamOvenOpenDoorSteamEvent event) {
        if (steameOvenOne == null || !Objects.equal(steameOvenOne.getID(), event.pojo.getID()))
            return;
        steameOvenOne = (AbsSteameOvenOneNew) event.pojo;
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
        if (TextUtils.equals(model, cx.getString(R.string.device_steamOvenOne_name_fengpeikao))) {
            return false;
        }
        if (!TextUtils.equals(model, cx.getString(R.string.device_steamOvenOne_name_fengpeikao)) && (steameOvenOne.weatherDescalingValue != 0)) {
            return true;
        }
        if (steameOvenOne.weatherDescalingValue == 0) {
            return false;
        }
        return true;
    }
}
