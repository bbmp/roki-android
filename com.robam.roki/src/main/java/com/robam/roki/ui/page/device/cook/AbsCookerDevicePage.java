package com.robam.roki.ui.page.device.cook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.FunctionMore;
import com.legent.plat.pojos.device.FunctionTop3;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.speech.SpeechManager;
import com.robam.common.events.CookerParamReportEvent;
import com.robam.common.pojos.PayLoadKuF;
import com.robam.common.pojos.device.Oven.OvenModeName;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.services.StoreService;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.device.oven.AbsOvenFirstView;
import com.robam.roki.utils.DataUtils;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by Dell on 2018/6/4.
 * 智能灶
 */

public abstract class AbsCookerDevicePage extends BasePage {

    @InjectView(R.id.iv_bg)
    ImageView ivBg;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.iv_device_switch)
    ImageView ivDeviceSwitch;
    @InjectView(R.id.iv_device_more)
    ImageView ivDeviceMore;
    @InjectView(R.id.add_view)
    FrameLayout addView;
    @InjectView(R.id.cook_offline)
    LinearLayout coolerOffLine;

    View viewItem;

    String mGuid;
    AbsCooker absCooker;
    List<String> stepList = new ArrayList<>();
    String recipeId;
    public int from;
    long userId = Plat.accountService.getCurrentUserId();


    CookerWorkingView cookerWorkingView;
    DeviceCookerRecipeView deviceCookerRecipeView;
    CookerItemShowView cookItemDisplayView;

    String dt;
    String dc;
    List<DeviceConfigurationFunctions> backFunc;
    List<DeviceConfigurationFunctions> mainList = new ArrayList<>();
    List<DeviceConfigurationFunctions> otherList;


    IRokiDialog closedialog = null;
    List<FunctionTop3> top3s;
    List<FunctionMore> mores;
    String version = null;

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            tvDeviceModelName.setText(name);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        recipeId = bd == null ? null : bd.getString(PageArgumentKey.RecipeId);
        from = bd.getInt(PageArgumentKey.From);
        LogUtils.i("201806013", "recipeId" + recipeId);
        absCooker = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.abs_device_cooker, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (absCooker == null) {
            return;
        }
        if (absCooker.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), absCooker.getDt(), null);
        }

    }

    public void initView() {
        if (absCooker != null) {
            dt = absCooker.getDt();
            dc = absCooker.getDc();
        }
        SpeechManager.getInstance().init(Plat.app);
        CloudHelper.getCookerCode3(userId, mGuid, dc, new Callback<Reponses.GetLookUpResponse>() {
            @Override
            public void onSuccess(Reponses.GetLookUpResponse getLookUpResponse) {
                LogUtils.i("20180727", "getLookUpResponse:" + getLookUpResponse.toString());
                top3s = getLookUpResponse.functionTop3s;
                mores = getLookUpResponse.functionMores;

                //version = PreferenceUtils.getString(PageArgumentKey.DeviceVersion+dt,null);
                version = DataUtils.readJson(cx, "version" + dt);
                if (version == null) {
                    getDataMethod();
                } else {
                    getVersionMethod();
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


        Plat.deviceService.getDeviceByParams(userId, dt, dc, new Callback<com.legent.plat.io.cloud.Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(com.legent.plat.io.cloud.Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getVersionMethod() {
        CloudHelper.getCheck(dt, version, new Callback<Reponses.GetCheckResponse>() {
            @Override
            public void onSuccess(Reponses.GetCheckResponse getCheckResponse) {
                if (getCheckResponse == null) return;

                if (!getCheckResponse.isLast) {//false是最新
                    LogUtils.i("20180815", "有更新了");
                    getDataMethod();
                } else {//不是最新的不需要去服务器请求加载本地文件
                    String ovenData = DataUtils.readJson(cx, "Cooker" + dt);
                    Reponses.DeviceResponse deviceOven = null;
                    try {
                        deviceOven = JsonUtils.json2Pojo(ovenData, Reponses.DeviceResponse.class);
                        setData(deviceOven);
                        /*Message msg = Message.obtain();
                        msg.what = 1;
                        msg.obj = deviceOven;
                        handler.sendMessage(msg);*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void getDataMethod() {
        LogUtils.i("20190215", "here is run");
        Plat.deviceService.getDeviceByParams(userId, dt, dc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;
                // PreferenceUtils.setString(PageArgumentKey.DeviceVersion+dt,deviceResponse.version);
                DataUtils.writeJson(cx, deviceResponse.version, "version" + dt, false);
                DataUtils.writeJson(cx, deviceResponse.toString(), "Cooker" + dt, false);
                setData(deviceResponse);
                /*try {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = deviceResponse;
                    handler.sendMessage(msg);
                } catch (NullPointerException e) {
                    LogUtils.i("20180725","error::"+e.getMessage());
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public AbsOvenFirstView ovenFirstView;
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();
    List<DeviceConfigurationFunctions> tempList = new ArrayList<>();
    List<DeviceConfigurationFunctions> cookerItem = new ArrayList<>();
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    public HashMap<String, String> paramMap = new HashMap<>();

    private void setData(Reponses.DeviceResponse deviceResponse) {
        LogUtils.i("20180619", "deviceResponse::" + deviceResponse.toString());
        try {
            String backgroundImg = deviceResponse.viewBackgroundImg;
            Glide.with(cx)
                    .asBitmap()
                    .load(backgroundImg)
                    .fitCenter().into(ivBg);
//            tvDeviceModelName.setText(deviceResponse.title);

            tvDeviceModelName.setText((absCooker.getName() == null || absCooker.getName().equals(absCooker.getCategoryName())) ? absCooker.getDispalyType() : absCooker.getName());
            MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
            tempList = mainFunc.deviceConfigurationFunctions;
            if (tempList.size() > 1) {
                moreList = tempList.get(tempList.size() - 1).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
            }
            for (int i = 0; i < tempList.size(); i++) {
                if ("moreHelpModel".equals(tempList.get(i).functionCode)) {
                    cookerItem.add(tempList.get(i));
                } else {
                    mainList.add(tempList.get(i));
                }
            }
            setParamMapMore();
            setParamMap();
            BackgroundFunc backgroundFunc = deviceResponse.modelMap.backgroundFunc;
            backFunc = backgroundFunc.deviceConfigurationFunctions;
            OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
            otherList = otherFunc.deviceConfigurationFunctions;
            LogUtils.i("20190215", "top::" + top3s.size());
            if (top3s.size() != 0) {
                setSortList();
            }
            ovenFirstView = new AbsOvenFirstView(cx, mainList, cookerItem);

            ovenFirstView.setOnclickMainLister(new AbsOvenFirstView.OnClickMian() {

                @Override
                public void onclickMain(String str) {
                    clickMain(str);
                }

                @Override
                public void onclickOther(String str) {
                    clickOther(str);
                }
            });

            cookerWorkingView = new CookerWorkingView(cx, absCooker, otherList);
            addView.addView(ovenFirstView);
            addView.addView(cookerWorkingView);
            addView.getChildAt(0).setVisibility(View.INVISIBLE);
            addView.getChildAt(1).setVisibility(View.INVISIBLE);
            if (from == 10) {
                coolerOffLine.setVisibility(View.VISIBLE);
                addView.getChildAt(0).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            LogUtils.i("20190128", "e::" + e.getCause());
            e.printStackTrace();
        }

    }

    private void setSortList() {
        mainList.clear();
        moreList.clear();
        for (int i = 0; i < top3s.size(); i++) {
            for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                if (top3s.get(i).functionCode.equals(en.getKey())) {
                    mainList.add(en.getValue());
                }
            }
        }

        for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
            if ("more".equals(en.getKey())) {
                mainList.add(en.getValue());
            }
        }

        for (int i = 0; i < mores.size(); i++) {
            for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                if (mores.get(i).functionCode.equals(en.getKey())) {
                    moreList.add(en.getValue());
                }
            }
        }
    }

    boolean falg = true;

    String recipeName;

    protected void clickMain(String code) {
        LogUtils.i("20190215", "code::" + code);
        switch (code) {
            case OvenModeName.more:
                if (falg) {
                    ovenFirstView.setUpData(moreList);
                    falg = false;
                } else {
                    ovenFirstView.removeMoreView();
                    falg = true;
                }
                break;
            default:


                goRecipe(code);
                //发送统计
                sendMul(code);
                break;
        }
    }

    private void sendMul(String code) {
        CloudHelper.getReportCode(userId, mGuid, code, dc, new Callback<Reponses.GetReportResponse>() {

            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
                // ToastUtils.show("发送统计成功",Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    //跳转到菜谱
    private void goRecipe(String code) {
        try {
            String funParam = paramMapMore.get(code).functionParams;
            String functionName = paramMapMore.get(code).functionName;
            JSONObject jsonObject = new JSONObject(funParam);
            long cookId = jsonObject.getLong("cookbookId");
            if (absCooker != null) {
                ToolUtils.logEvent(absCooker.getDt(), functionName, "roki_设备");
            }

            Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, mGuid);
            bd.putString(PageArgumentKey.RecipeId, String.valueOf(cookId));
            UIService.getInstance().postPage(PageKey.DeviceDetailCooker, bd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void clickOther(String str) {
        onItemClickShow(str);
    }


    private void setParamMapMore() {
        paramMapMore.clear();
        for (int i = 0; i < mainList.size(); i++) {
            LogUtils.i("20190215", "fff::" + mainList.get(i).functionCode);
            paramMapMore.put(mainList.get(i).functionCode, mainList.get(i));
        }
        if (moreList != null) {
            for (int i = 0; i < moreList.size(); i++) {
                paramMapMore.put(moreList.get(i).functionCode, moreList.get(i));
            }
        }

    }

    private void setParamMap() {
        for (int i = 0; i < cookerItem.size(); i++) {
            paramMap.put(cookerItem.get(i).functionCode, cookerItem.get(i).functionParams);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    StoreService ss = StoreService.getInstance();
    PayLoadKuF payLoadKuF;


    private void onItemClickShow(String str) {
        switch (str) {
            case "moreHelpModel":
                /*if(absCooker.powerStatus==0){
                    ToastUtils.show("请先在产品上开机",Toast.LENGTH_SHORT);
                    return;
                }*/
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, absCooker.getID());
                UIService.getInstance().postPage(PageKey.AbsCookerHelper, bd);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        cookerWorkingView = null;
        deviceCookerRecipeView = null;
        cookItemDisplayView = null;
    }


    @Subscribe
    public void onEvent(CookerParamReportEvent event) {
        if (event.param == 10) {
            ToastUtils.show("烹饪完成", Toast.LENGTH_SHORT);
        } else if (event.param == 6) {
            ToastUtils.show("未检测到您的操作，即将自动进入下一步", Toast.LENGTH_SHORT);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_device_switch, R.id.iv_device_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().returnHome();
                break;
            case R.id.iv_device_switch:
                if (absCooker.powerStatus == 0) {
                    ToastUtils.show("已关机", Toast.LENGTH_SHORT);
                    //  sendOffCommand((short)1);
                    if (absCooker != null) {
                        ToolUtils.logEvent(absCooker.getDt(), "关机", "roki_设备");
                    }
                } else {

                    ToolUtils.logEvent(absCooker.getDt(), "开机", "roki_设备");
                    isOff();
                }

                break;
            case R.id.iv_device_more:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceMoreCook, bd);
                break;
        }
    }


    private void isOff() {
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(getString(R.string.close_new));
        closedialog.setContentText(getString(R.string.oven_off_tip));
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    sendOffCommand((short) 0);
                }
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });

    }

    //下发关机指令
    private void sendOffCommand(final short state) {
        absCooker.setCookerWorkStatus(state, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (state == 0) {
                    //ToastUtils.show("设备关闭成功", Toast.LENGTH_SHORT);

                } else {
                    ToastUtils.show("设备开机成功", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("设备关闭失败，请重新下发", Toast.LENGTH_SHORT);
            }
        });
    }

}
