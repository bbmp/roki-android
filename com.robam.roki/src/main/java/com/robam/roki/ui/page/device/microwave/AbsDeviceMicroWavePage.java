package com.robam.roki.ui.page.device.microwave;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.FunctionMore;
import com.legent.plat.pojos.device.FunctionTop3;
import com.legent.plat.pojos.device.HideFunc;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveModeName;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.util.RecipeRequestIdentification;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.MicroWaveHotRiceParams;
import com.robam.roki.model.helper.HelperWaterData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.page.RecipeDetailPage;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.util.FanLockUtils.mGuid;

/**
 * Created by 14807 on 2018/7/23.
 * 微波炉
 */
public class AbsDeviceMicroWavePage<MicroWave extends AbsMicroWave>
        extends DeviceCatchFilePage {

    MicroWave mMicroWave;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;
    List<DeviceConfigurationFunctions> mainList = new ArrayList<>();
    List<DeviceConfigurationFunctions> otherList = new ArrayList<>();
    List<DeviceConfigurationFunctions> moreList = new ArrayList<>();
    List<FunctionTop3> top3s = new ArrayList<>();
    List<FunctionMore> mores = new ArrayList<>();
    @InjectView(R.id.contain)
    FrameLayout contain;
    AbsMicroWaveFirstView microWaveFirstView;
    AbsMicroWaveWorkingView mMicroWaveWorkingView;
    public HashMap<String, String> paramMap = new HashMap<>();
    public HashMap<String, DeviceConfigurationFunctions> paramMapMore = new HashMap<>();
    int from;
    private String mBackgroundImg;
    private boolean mCompleteSign = false;
    private IRokiDialog mHotRecipeDialog;
    private IRokiDialog mToTasteDialog;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 0:
                    setHotRecipeData((String) msg.obj);
                    break;
            }
        }
    };


    private void setHotRecipeData(String data) {
        if (data.contains(StringConstantsUtil.STRING_G)) {
            final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
            mHotRecipeDialog.setOkBtn(R.string.work_start, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotRecipeDialog.dismiss();
                    if (mMicroWave.doorState == 1) {
                        ToastUtils.showShort(R.string.device_alarm_rika_E1);
                        return;
                    }
                    mMicroWave.setMicroWaveKindsAndHeatCold(MicroWaveModel.HeatingAgain, Short.parseShort(removeString), new VoidCallback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMicroWave == null) {
            return;
        }
    }

    @Subscribe
    public void onEvent(MicroWaveStatusChangedEvent event) {
        if (mMicroWave == null || !Objects.equal(mMicroWave.getID(), event.pojo.getID()))
            return;
        mMicroWave = (MicroWave) event.pojo;
        if (mMicroWave.state == MicroWaveStatus.Wait
                || mMicroWave.state == MicroWaveStatus.Alarm
                || mMicroWave.state == MicroWaveStatus.Setting) {
            contain.getChildAt(0).setVisibility(View.VISIBLE);
            contain.getChildAt(1).setVisibility(View.INVISIBLE);

            mMicroWaveWorkingView.closeAllDialog();

        } else {
            if (mHotRecipeDialog != null && mHotRecipeDialog.isShow()) {
                mHotRecipeDialog.dismiss();
            }
            contain.getChildAt(1).setVisibility(View.VISIBLE);
            contain.getChildAt(0).setVisibility(View.INVISIBLE);
            mMicroWaveWorkingView.updateStatus(event.pojo);
        }
        lineShowChange();

    }

    private void lineShowChange() {
        if (!mMicroWave.isConnected()) {
            ToastUtils.showLong(R.string.device_new_connected);
            if (microWaveFirstView != null) {
                microWaveFirstView.disConnect(true);
            }
        } else {
            if (microWaveFirstView != null) {
                microWaveFirstView.disConnect(false);
            }
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (mMicroWave == null || !Objects.equal(mMicroWave.getID(), event.device.getID()))
            return;

        LogUtils.i("20181109", "connected:" + !event.isConnected);
        if (!event.isConnected) {
            contain.getChildAt(0).setVisibility(View.VISIBLE);
            contain.getChildAt(1).setVisibility(View.INVISIBLE);
            mMicroWaveWorkingView.updateStatus(mMicroWave);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        from = bd.getInt(PageArgumentKey.from);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_microwave, container, false);

        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void getTop3Data(final Reponses.DeviceResponse deviceResponse) {

        LogUtils.i("20180831", "mUserId:" + mUserId + " mGuid:" + mGuid + " mDc:" + mDc);

        CloudHelper.getLookUpCode(mUserId, mGuid, mDc, new Callback<Reponses.GetLookUpResponse>() {
            @Override
            public void onSuccess(Reponses.GetLookUpResponse getLookUpResponse) {
                top3s = getLookUpResponse.functionTop3s;
                mores = getLookUpResponse.functionMores;
                LogUtils.i("20180831", " top3s:" + top3s);
                LogUtils.i("20180831", " mores:" + mores.size());
                initViewData(deviceResponse);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void initViewData(Reponses.DeviceResponse deviceResponse) {
        if (mDevice instanceof AbsMicroWave) {
            mMicroWave = (MicroWave) mDevice;
        }
        try {
            if (deviceResponse != null) {
                mBackgroundImg = deviceResponse.viewBackgroundImg;
                if (mIvBg != null) {
                    Glide.with(cx).load(mBackgroundImg).into(mIvBg);
                }
                if (mTvDeviceModelName != null) {
                    mTvDeviceModelName.setText(deviceResponse.title);
                }
                MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
                mainList = mainFunc.deviceConfigurationFunctions;
                LogUtils.i("20180913", " mainList:" + mainList.size());
                if (mainList != null && mainList.size() > 0) {
                    moreList = mainList.get(mainList.size() - 1)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                }

                setParamMapMore();
                OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
                otherList = otherFunc.deviceConfigurationFunctions;
                setParamMap();
                setSortList();
                int size = mainList.size();
                LogUtils.i("20181109", " size:" + size);
                HideFunc hideFunc = deviceResponse.modelMap.hideFunc;
                List<DeviceConfigurationFunctions> hideFuncList = hideFunc.deviceConfigurationFunctions;
                microWaveFirstView = new AbsMicroWaveFirstView(cx, mainList, otherList);
                mMicroWaveWorkingView = new AbsMicroWaveWorkingView(cx, mMicroWave, hideFuncList);
                if (microWaveFirstView != null) {
                    contain.addView(microWaveFirstView);
                }
                if (mMicroWaveWorkingView != null) {
                    contain.addView(mMicroWaveWorkingView);
                }
                if (contain != null) {
                    if (from == 1) {
                        View view = contain.getChildAt(1);
                        if (view != null) {
                            view.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        View view = contain.getChildAt(0);
                        if (view != null) {
                            view.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                lineShowChange();
                if (microWaveFirstView != null) {
                    microWaveFirstView.setOnclickMainLister(new AbsMicroWaveFirstView.OnClickMian() {
                        @Override
                        public void onclickMain(String str) {
                            if (!mMicroWave.isConnected()) {
                                ToastUtils.showLong(R.string.device_connected);
                            } else {
                                clickMain(str);
                            }
                        }

                        @Override
                        public void onclickOther(String str) {
                            if (!mMicroWave.isConnected()) {
                                ToastUtils.showLong(R.string.device_connected);
                            } else {
                                clickOther(str);
                            }
                        }
                    });
                }
            }

        } catch (Exception e) {
            LogUtils.i("20180831", " e:" + e.toString());
            e.printStackTrace();
        }

    }

    @Override
    protected void setDataToView(Reponses.DeviceResponse deviceResponse) {
        getTop3Data(deviceResponse);
    }

    boolean falg = true;

    public void clickMain(String code) {
        LogUtils.i("20180913", " code:" + code);
        switch (code) {
            case MicroWaveModeName.more:
                if (falg) {
                    microWaveFirstView.setUpData(moreList);
                    falg = false;
                } else {
                    microWaveFirstView.removeMoreView();
                    falg = true;
                }
                if (mMicroWave != null) {
                    ToolUtils.logEvent(mMicroWave.getDt(), "点击微波炉场景功能:" + "更多", "roki_设备");
                }
                break;
            default:


                goRecipe(code);
                sendMul(code);
                break;
        }
    }

    public void clickOther(String code) {
        LogUtils.i("20181108", " code:" + code);
        try {
            switch (code) {
                case MicroWaveModeName.toTaste:
                    showToTaste();
                    break;
                case MicroWaveModeName.model:
                    LogUtils.i("20180925", " model");
                    Bundle modelBd = new Bundle();
                    String modelTitle = null;
                    if (otherList != null && otherList.size() > 0) {
                        for (int i = 0; i < otherList.size(); i++) {
                            if ("model".equals(otherList.get(i).functionCode)) {
                                modelTitle = otherList.get(i).functionName;
                                DeviceConfigurationFunctions functions = otherList.get(i);
                                List<DeviceConfigurationFunctions> modelList = functions
                                        .subView
                                        .subViewModelMap
                                        .subViewModelMapSubView
                                        .deviceConfigurationFunctions;
                                modelBd.putSerializable(PageArgumentKey.List, (Serializable) modelList);
                            }
                        }
                    }
                    modelBd.putSerializable(PageArgumentKey.Bean, mMicroWave);
                    modelBd.putString(PageArgumentKey.viewBackgroundImg, mBackgroundImg);
                    modelBd.putString(PageArgumentKey.Guid, mMicroWave.getID());
                    modelBd.putString(PageArgumentKey.tag, "select");
                    modelBd.putString(PageArgumentKey.title, modelTitle);
                    UIService.getInstance().postPage(PageKey.MicroWaveModelSelected, modelBd);
                    break;
                case MicroWaveModeName.microWaveAutoRecipes:

                    if (mMicroWave != null) {
                        ToolUtils.logEvent(mMicroWave.getDt(), "点击: 微波炉自动烹饪菜谱", "roki_设备");
                    }

                    String platformCode="";
                    for (int i = 0; i < otherList.size(); i++) {
                        if ((MicroWaveModeName.microWaveAutoRecipes).equals(otherList.get(i).functionCode)) {
                            String params = otherList.get(i).functionParams;
                            try {
                                if (params!=null&&!"".equals(params)) {
                                    JSONObject jsonObject=new JSONObject(params);
                                    platformCode = jsonObject.optString("platformCode");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Bundle autoRecipesBd = new Bundle();
                    autoRecipesBd.putString(PageArgumentKey.RecipeId, DeviceType.RWBL);
                    autoRecipesBd.putString(PageArgumentKey.platformCode, platformCode);
                    autoRecipesBd.putString(PageArgumentKey.Guid, mGuid);
                    UIService.getInstance().postPage(PageKey.RecipeCategoryList, autoRecipesBd);

                    break;
                case MicroWaveModeName.modelCommon:
                    Bundle modelCommonBd = new Bundle();
                    String title = null;
                    if (otherList != null && otherList.size() > 0) {
                        for (int i = 0; i < otherList.size(); i++) {
                            if ("modelCommon".equals(otherList.get(i).functionCode)) {
                                title = otherList.get(i).functionName;
                                DeviceConfigurationFunctions functions = otherList.get(i);
                                List<DeviceConfigurationFunctions> modelList = functions
                                        .subView
                                        .subViewModelMap
                                        .subViewModelMapSubView
                                        .deviceConfigurationFunctions;
                                modelCommonBd.putSerializable(PageArgumentKey.List, (Serializable) modelList);
                            }
                        }
                    }
                    modelCommonBd.putSerializable(PageArgumentKey.Bean, mMicroWave);
                    modelCommonBd.putString(PageArgumentKey.viewBackgroundImg, mBackgroundImg);
                    modelCommonBd.putString(PageArgumentKey.Guid, mMicroWave.getID());
                    modelCommonBd.putString(PageArgumentKey.tag, "select");
                    modelCommonBd.putString(PageArgumentKey.title, title);
                    UIService.getInstance().postPage(PageKey.MicroWaveModelSelected, modelCommonBd);

                    break;
                case MicroWaveModeName.hotMeal:
                    if (otherList != null && otherList.size() > 0) {
                        for (int i = 0; i < otherList.size(); i++)
                            if ("hotMeal".equals(otherList.get(i).functionCode)) {
                                DeviceConfigurationFunctions functions = otherList.get(i);
                                List<DeviceConfigurationFunctions> modelList = functions
                                        .subView
                                        .subViewModelMap
                                        .subViewModelMapSubView
                                        .deviceConfigurationFunctions;
                                if (modelList != null && modelList.size() > 0) {
                                    String functionParams = modelList.get(i).functionParams;
                                    LogUtils.i("20181108", "functionParams:" + functionParams);
                                    paramShow(functionParams);
                                }
                            }
                    }
                    break;
                default:
                    ToastUtils.show("即将开放，敬请期待", Toast.LENGTH_SHORT);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToTaste() {

        mToTasteDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_16);
        mToTasteDialog.setTitleText(R.string.is_delete_toast);
        mToTasteDialog.setContentText(R.string.micro_to_taste);
        mToTasteDialog.setOkBtn(R.string.micro_to_taste_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToTasteDialog.dismiss();
                if (mMicroWave.doorState == 1) {
                    ToastUtils.showShort(R.string.device_alarm_rika_E1);
                    return;
                }
                mMicroWave.setMicroWaveCleanAir(MicroWaveModel.MicroWave, (short) 180, (short) 3, 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        mToTasteDialog.show();


    }

    private void setSortList() {
        mainList.clear();
        moreList.clear();
        if (top3s != null && top3s.size() > 0) {
            for (int i = 0; i < top3s.size(); i++) {
                for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                    if (top3s.get(i).functionCode.equals(en.getKey())) {
                        mainList.add(en.getValue());
                    }
                }
            }
        }

        if (mores != null && mores.size() > 0) {
            for (int i = 0; i < mores.size(); i++) {
                for (Map.Entry<String, DeviceConfigurationFunctions> en : paramMapMore.entrySet()) {
                    if (mores.get(i).functionCode.equals(en.getKey())) {
                        moreList.add(en.getValue());
                    }
                }
            }
        }

    }

    private void setParamMapMore() {
        for (int i = 0; i < mainList.size() - 1; i++) {
            paramMapMore.put(mainList.get(i).functionCode, mainList.get(i));
        }
        for (int i = 0; i < moreList.size(); i++) {
            paramMapMore.put(moreList.get(i).functionCode, moreList.get(i));
        }
    }

    private void setParamMap() {
        for (int i = 0; i < otherList.size(); i++) {
            paramMap.put(otherList.get(i).functionCode, otherList.get(i).functionParams);
        }
    }

    //跳转到菜谱
    private void goRecipe(String code) {

        LogUtils.i("20180913", " code:" + code);
        try {
            String funParam = paramMapMore.get(code).functionParams;
            LogUtils.i("20180913", " funParam:" + funParam);
            JSONObject jsonObject = new JSONObject(funParam);
            long cookId = jsonObject.getLong("cookbookId");
            RecipeDetailPage.show(cookId, RecipeDetailPage.DeviceRecipePage, RecipeRequestIdentification.RECIPE_SORTED,mGuid);
            if (mMicroWave != null) {
                ToolUtils.logEvent(mMicroWave.getDt(), "点击微波炉场景功能:" + paramMapMore.get(code).functionName, "roki_设备");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //发送统计
    private void sendMul(String code) {
        CloudHelper.getReportCode(mUserId, mGuid, code, mDc, new Callback<Reponses.GetReportResponse>() {
            @Override
            public void onSuccess(Reponses.GetReportResponse getReportResponse) {
//                ToastUtils.show("发送统计成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
//                ToastUtils.show("发送统计失败", Toast.LENGTH_SHORT);
            }
        });
    }


    private void paramShow(String params) {

        try {
            MicroWaveHotRiceParams waveHotRiceParams = JsonUtils.json2Pojo(params, MicroWaveHotRiceParams.class);
            if (waveHotRiceParams != null) {
                List<Integer> timeList = waveHotRiceParams.getSetTime().getValue();
                List<Integer> numberList = waveHotRiceParams.getSetNumber().getValue();
                final String defaultNumber = waveHotRiceParams.getDefaultSetNumber().getValue();//200
                String defalutTime = waveHotRiceParams.getDefaultSetTime().getValue();
                final String numberCompany = waveHotRiceParams.getNumberCompany().getValue();//g

                List<String> hotRecipeData = HelperWaterData.getHotRecipeData(numberList, numberCompany);

                mHotRecipeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
                mHotRecipeDialog.setWheelViewData(null, hotRecipeData, null, false, 0, 0, 0, null,
                        new OnItemSelectedListenerCenter() {
                            @Override
                            public void onItemSelectedCenter(String contentCenter) {
                                Message message = mHandler.obtainMessage();
                                message.what = 0;
                                message.obj = contentCenter;
                                mHandler.sendMessage(message);
                                if (mMicroWave!=null) {
                                    ToolUtils.logEvent(mMicroWave.getDt(), "设置微波炉热饭热菜重量工作:" + defaultNumber + numberCompany, "roki_设备");
                                }

                            }
                        }, null);

                mHotRecipeDialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        if (mMicroWave!=null) {
            ToolUtils.logEvent(mMicroWave.getDt(), "点击:更多", "roki_设备");
        }
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, mGuid);
        UIService.getInstance().postPage(PageKey.AbsMicroWaveMore, bd);

    }

}
