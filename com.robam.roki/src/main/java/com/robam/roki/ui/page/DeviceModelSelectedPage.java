package com.robam.roki.ui.page;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.constant.IPlatRokiFamily;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaModeName;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.FunctionParams;
import com.robam.roki.model.bean.RikaCleaningDisinfectionParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
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
 * 设备模式选择页面 蒸模式页面
 */

public class DeviceModelSelectedPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private DeviceModelAdapter mDeviceModelAdapter;
    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringTempList = new ArrayList<String>();
    List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    AbsRika mRika;

    private IRokiDialog mRokiDialog;
    private List<DeviceConfigurationFunctions> mDeviceSelectModelList;
    private int mSteamModel;
    String tag;
    String code;
    String mViewBackgroundImg;
    private DeviceConfigurationFunctions mFunctions;
    private IRokiDialog mRokiCleanDialog;
    private short mCmd;
    private short mNumberCatrgory;
    private short mCategoryCode;
    private short mArgumentNumber;
    private short mKey;
    private short mKeyLenght;
    private short mWorkModel;
    private short mWarmDishTemp;

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
                case 3:
                    setDeviceRunDataClean((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mRika == null) {
            return;
        }
    }

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        short steamWorkStatus = mRika.steamWorkStatus;
        short sterilWorkStatus = mRika.sterilWorkStatus;
        if (IRokiFamily.RIKAZ.equals(mRika.getDp())) {
            if (steamWorkStatus == RikaStatus.STEAM_RUN || steamWorkStatus == RikaStatus.STEAM_PREHEAT
                    || steamWorkStatus == RikaStatus.STEAM_STOP) {
                if (mRokiDialog != null && mRokiDialog.isShow()) {
                    mRokiDialog.dismiss();
                }
                UIService.getInstance().popBack();

                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.RIKA, mRika);
                bundle.putString(PageArgumentKey.tag, tag);
                bundle.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                UIService.getInstance().postPage(PageKey.DeviceRikaWork, bundle);
            }
        } else if (IRokiFamily.RIKAX.equals(mRika.getDp()))
            if (sterilWorkStatus != RikaStatus.STERIL_ON && sterilWorkStatus != RikaStatus.STERIL_OFF
                    && sterilWorkStatus != RikaStatus.STERIL_ALARM) {
                if (mRokiCleanDialog != null && mRokiCleanDialog.isShow()) {
                    mRokiCleanDialog.dismiss();
                }
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.RIKA, mRika);
                bundle.putString(PageArgumentKey.tag, tag);
                bundle.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                UIService.getInstance().postPage(PageKey.DeviceRikaXWork, bundle);

            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        Bundle bd = getArguments();
        mDeviceConfigurationFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        tag = bd == null ? null : bd.getString(PageArgumentKey.tag);
        code = bd == null ? null : bd.getString(PageArgumentKey.code);
        title = bd == null ? null : bd.getString(PageArgumentKey.title);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
        View view = inflater.inflate(R.layout.page_device_model_selected, container, false);
        ButterKnife.inject(this, view);
        initDatas(mDeviceConfigurationFunctions, code);
        return view;

    }

    private void initDatas(List<DeviceConfigurationFunctions> deviceConfiguration, String code) {
        Log.e("yidao", "initDatas: "+code );
        if (!TextUtils.isEmpty(code)) {
            for (int i = 0; i < deviceConfiguration.size(); i++) {
                if (TextUtils.equals(deviceConfiguration.get(i).functionCode,code)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                }
            }
        } else {
            for (int i = 0; i < deviceConfiguration.size(); i++) {
                if (RikaModeName.SMOKE_AIR_VOLUME.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.STEAMING_ROAST_MODE.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.LOCAL_COOKBOOK.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.CLEANING_AND_DISINFECTION_NEW.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.SMOKE_COOKER_STEAMING_ROAST_LINKAGE.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.STEAMING_MODE.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.TIME_REMINDING.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.OIL_NETWORK_DETECTION.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.SMOKE_COOKER_STEAMING_LINKAGE.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.STEAM_COOKBOOK.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.CLEANING_AND_DISINFECTION.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.SMOKE_STOVE_ELIMINATION_LINKAGE.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else if (RikaModeName.APPOINTMENT_DISINFECTION.equals(deviceConfiguration.get(i).functionCode)) {
                    mFunctions = deviceConfiguration.get(i);
                    mTvDeviceModelName.setText(deviceConfiguration.get(i).functionName);
                } else {
                    //TODo
                }
            }
        }
        mDeviceSelectModelList = mFunctions.subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
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
        LogUtils.i("20180528", "view:" + view.getTag().toString());
        try {
            if (mDeviceSelectModelList != null && mDeviceSelectModelList.size() > 0) {
                for (int i = 0; i < mDeviceSelectModelList.size(); i++) {
                    if (view.getTag().toString().equals(mDeviceSelectModelList.get(i).functionCode)) {
                        //快速保洁
                        //消毒
                        //烘干
                        if ("fastCleaning".equals(view.getTag().toString()) || "disinfection".equals(view.getTag().toString())
                                || "dry".equals(view.getTag().toString())) {
                            LogUtils.i("20180528", "functionParams:" + mDeviceSelectModelList.get(i).functionParams);
                            String functionParams = mDeviceSelectModelList.get(i).functionParams;
//                            RikaCleaningDisinfectionParams disinfectionParams =
//                                    DeviceJsonToBeanUtils.JsonToObject(functionParams, RikaCleaningDisinfectionParams.class);
                            RikaCleaningDisinfectionParams disinfectionParams = JsonUtils.json2Pojo(functionParams, RikaCleaningDisinfectionParams.class);
                            cleaningDisinfectionEvent(disinfectionParams);
                            //鲜嫩蒸
                            //营养蒸
                            //强力蒸
                        } else if ("freshSteamed".equals(view.getTag().toString()) ||
                                "nutritionSteaming".equals(view.getTag().toString())
                                || "strongSteam".equals(view.getTag().toString())) {

                            ArrayList<DeviceConfigurationFunctions> descList = Lists.newArrayList();
                            LogUtils.i("20180607", "msg:" + mDeviceSelectModelList.get(i).msg);
                            descList.clear();
                            descList.add(mDeviceSelectModelList.get(i));
                            List<DeviceConfigurationFunctions> functions = mDeviceSelectModelList.get(i).subView.
                                    subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                            String jsonParams = functions.get(0).functionParams;
//                            FunctionParams functionParams = DeviceJsonToBeanUtils.JsonToObject(jsonParams, FunctionParams.class);
                            FunctionParams functionParams = JsonUtils.json2Pojo(jsonParams, FunctionParams.class);
                            List<Integer> tempValue = functionParams.getParams().getWorkTemp().getValue();
                            List<Integer> timeValue = functionParams.getParams().getWorkTime().getValue();
                            int tempDefault = functionParams.getParams().getWorkTempDefault().getValue();
                            int timeDefault = functionParams.getParams().getWorkTimeDefault().getValue();
                            mSteamModel = functionParams.getParams().getSetMeum().getValue();
                            LogUtils.i("20180607", " mSteamModel:" + mSteamModel);
                            int interval = timeValue.get(timeValue.size() - 1);//获取间隔数
                            int indexTemp = tempDefault - tempValue.get(0);
                            int indexTime;
                            if (interval > 1) {
                                indexTime = (timeDefault - timeValue.get(0)) / interval;
                            } else {
                                indexTime = timeDefault - timeValue.get(0);
                            }


                            mRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_17);
                            mRokiDialog.setWheelViewData(HelperRikaData.getTempData(tempValue),
                                    null, HelperRikaData.getTimeData(timeValue), descList, false, indexTemp, 0, indexTime, new OnItemSelectedListenerFrone() {
                                        @Override
                                        public void onItemSelectedFront(String contentFront) {
                                            Message msg = mHandler.obtainMessage();
                                            msg.obj = contentFront;
                                            msg.what = 1;
                                            mHandler.sendMessage(msg);

                                        }
                                    }, null, new OnItemSelectedListenerRear() {
                                        @Override
                                        public void onItemSelectedRear(String contentRear) {
                                            Message msg = mHandler.obtainMessage();
                                            msg.obj = contentRear;
                                            msg.what = 2;
                                            mHandler.sendMessage(msg);
                                            LogUtils.i("20180409", " contentRear:" + contentRear);
                                        }
                                    });
                            mRokiDialog.show();
                        } else {
                            //TODo
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //保洁，消毒，命令
    private void cleaningDisinfectionEvent(RikaCleaningDisinfectionParams disinfectionParams) {
        mCmd = (short) disinfectionParams.getCmd();
        mNumberCatrgory = (short) disinfectionParams.getParams().getNumberCatrgory().getValue();
        mCategoryCode = (short) disinfectionParams.getParams().getCategoryCode().getValue();
        mArgumentNumber = (short) disinfectionParams.getParams().getArgumentNumber().getValue();
        mKey = (short) disinfectionParams.getParams().getKey().getValue();
        mKeyLenght = (short) disinfectionParams.getParams().getKeyLength().getValue();
        mWorkModel = (short) disinfectionParams.getParams().getWorkModel().getValue();
        List<Integer> value = disinfectionParams.getParams().getWorkTime().getValue();
        List<String> timeData = HelperRikaData.getTimeData(value);
        mWarmDishTemp = (short) disinfectionParams.getParams().getWarmDishTemp().getValue();

        LogUtils.i("20180821", " lockStatus:" + mRika.lockStatus);

        if (RikaStatus.STERIL_LOCK_ON == mRika.sterilLockStatus) {
            ToastUtils.showShort(R.string.device_is_lock);
            return;
        }

        mRokiCleanDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        mRokiCleanDialog.setWheelViewData(null, timeData, null, false, 0, 1, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message msg = mHandler.obtainMessage();
                msg.what = 3;
                msg.obj = contentCenter;
                mHandler.sendMessage(msg);
            }
        }, null);

        mRokiCleanDialog.show();
    }

    private void setDeviceRunData(String data) {

        if (data.contains(StringConstantsUtil.STRING_DEGREE_CENTIGRADE)) {
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringTempList.add(removetTempString);
        }
        if (data.contains(StringConstantsUtil.STRING_MINUTES)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringTimeList.add(removeTimeString);
        }
        mRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
                String temp = stringTempList.get(stringTempList.size() - 1);
                String time = stringTimeList.get(stringTimeList.size() - 1);
                final short newTemp = Short.parseShort(temp);
                final short newTime = Short.parseShort(time);
                if (IPlatRokiFamily.RIKAZ.equals(mRika.getDp())) {
                    if (mRika.steamWorkStatus == RikaStatus.STEAM_OFF) {
                        mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAM_ON, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        try {
                                            Thread.sleep(2500);
                                            LogUtils.i("20180409", "setSteamWorkStatus onSuccess STEAM_OFF:");
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } finally {
                                            OffRun(newTemp, newTime);

                                            if (mRika != null) {
                                                ToolUtils.logEvent(mRika.getDt(), "开始集成灶蒸箱蒸模式温度时间工作:" + mFunctions.functionName + ":" + newTemp + ":" + newTime, "roki_设备");
                                            }

                                        }

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                    }
                                });

                    } else {
                        mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                (short) 50, (short) 3, (short) mSteamModel, newTemp, newTime, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                        mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                                (short) 49, (short) 1, RikaStatus.STEAM_RUN, new VoidCallback() {
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
                    }
                }


            }
        });

        mRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRokiDialog.dismiss();
            }
        });

    }

    private void setDeviceRunDataClean(String obj) {
        short workTime = 0;
        if (obj.contains(StringConstantsUtil.STRING_MINUTES)) {
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(obj);
            workTime = Short.parseShort(removeTimeString);
        }

        final short finalWorkTime = workTime;
        mRokiCleanDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRokiCleanDialog.dismiss();
                final short sterilOrderTime = 4;
                if (mRika.sterilWorkStatus == RikaStatus.STERIL_OFF) {
                    mRika.setSterilizerWorkStatus(mCmd, mNumberCatrgory, mCategoryCode, mArgumentNumber, mKey
                            , mKeyLenght, RikaStatus.STERIL_ON, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } finally {
                                        //mWorkModel 快洁 2

                                        LogUtils.i("2019101511", "mCmd:::" + mCmd +
                                                "mNumberCatrgory:::" + mNumberCatrgory +
                                                "mCategoryCode:::" + mCategoryCode +
                                                "mArgumentNumber:::" + mArgumentNumber +
                                                "mKey:::" + mKey +
                                                "mKeyLenght:::" + mKeyLenght +
                                                "mWorkModel:::" + mWorkModel +
                                                "finalWorkTime:::" + finalWorkTime +
                                                "sterilOrderTime:::" + sterilOrderTime +
                                                "mWarmDishTemp:::" + mWarmDishTemp);
                                        mRika.setSterilizerWorkStatus(mCmd, mNumberCatrgory, mCategoryCode, mArgumentNumber, mKey
                                                , mKeyLenght, mWorkModel, finalWorkTime, sterilOrderTime, mWarmDishTemp, new VoidCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        IRokiDialog dialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_18);
                                                        dialog.setCanceledOnTouchOutside(false);
                                                        dialog.show();
                                                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                            @Override
                                                            public void onDismiss(DialogInterface dialog) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable t) {
                                                        LogUtils.i("20180529", " t:" + t.toString());
                                                    }
                                                });
                                    }

                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20180529", " t:" + t.toString());
                                }
                            });
                } else {
                    mRika.setSterilizerWorkStatus(mCmd, mNumberCatrgory, mCategoryCode, mArgumentNumber, mKey
                            , mKeyLenght, mWorkModel, finalWorkTime, sterilOrderTime, mWarmDishTemp, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_18);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.show();
                                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Throwable t) {
                                    LogUtils.i("20180529", " t:" + t.toString());
                                }
                            });
                }
            }
        });

        mRokiCleanDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRokiCleanDialog.dismiss();
            }
        });

    }

    private void OffRun(final short newTemp, final short newTime) {
        mRika.setSteamRunWorkModel((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                (short) 50, (short) 3, (short) mSteamModel, newTemp, newTime, new VoidCallback() {
                    @Override
                    public void onSuccess() {


                        mRika.setSteamWorkStatus((short) 1, RikaStatus.STEAM_CATEGORYCODE, (short) 1,
                                (short) 49, (short) 1, RikaStatus.STEAM_RUN, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
//                                        UIService.getInstance().popBack();
//                                        Bundle bundle = new Bundle();
//                                        bundle.putSerializable(PageArgumentKey.RIKA, mRika);
//                                        bundle.putString(PageArgumentKey.tag, tag);
//                                        bundle.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
//                                        UIService.getInstance().postPage(PageKey.DeviceRikaWork, bundle);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
//                                        OffRun(newTemp,newTime);
                                    }
                                });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20180417", "t:" + t);
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
