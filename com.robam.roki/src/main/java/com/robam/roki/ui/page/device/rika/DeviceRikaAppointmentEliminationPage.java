package com.robam.roki.ui.page.device.rika;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.QureyData;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.RikaStatusChangedEvent;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.fan.IFan;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.model.bean.RikaAppointmentParams;
import com.robam.roki.model.helper.HelperRikaData;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author by lixin, Email lx86@myroki.com, Date on 2018/12/13.
 * PS: 预约消毒Fragment页面.
 */
public class DeviceRikaAppointmentEliminationPage extends BasePage {


    AbsRika mRika;
    List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.tv_model)
    TextView mTvModel;
    @InjectView(R.id.tv_time)
    TextView mTvTime;
    @InjectView(R.id.tv_time_later)
    TextView mTvTimeLater;
    DeviceConfigurationFunctions mFunctions;
    @InjectView(R.id.tv_btn)
    TextView mTvBtn;
    private String mWorkModelDef;
    private String mWaitTimeDef;
    private String mWorkTimeDef;
    List<String> modelNameList = new ArrayList();
    private IRokiDialog mModelDialog;
    private RikaAppointmentParams mParams;
    private List<String> mDisinfectionWorkTimeList;
    private List<String> mDryWorkTimeList;
    private List<String> mQuickWorkTimeList;
    private List<String> mDisinfectionWaitTimeList;
    private List<String> mDryWaitTimeList;
    private List<String> mQuickWaitTimeList;
    private String tag;
    private String mViewBackgroundImg;
    private String mWorkModelName;
    private String mDisinfection;
    private String mDry;
    private String mQuick;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 1:
                    setDeviceModel((String) msg.obj);
                    break;
                case 2:
                    setDeviceModelWorkTime((String) msg.obj);
                    break;
                case 3:
                    setDeviceModelWaitTime((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bd = getArguments();
        mDeviceConfigurationFunctions = bd == null ? null : (List<DeviceConfigurationFunctions>)
                bd.getSerializable(PageArgumentKey.List);
        mRika = bd == null ? null : (AbsRika) bd.getSerializable(PageArgumentKey.RIKA);
        tag = bd == null ? null : bd.getString(PageArgumentKey.tag);
        mViewBackgroundImg = bd == null ? null : bd.getString(PageArgumentKey.viewBackgroundImg);
    }

    @Subscribe
    public void onEvent(RikaStatusChangedEvent event) {
        if (mRika == null || !Objects.equal(mRika.getID(), event.pojo.getID()))
            return;
        mRika = event.pojo;

        if (IRokiFamily.RIKAX.equals(mRika.getDp())) {
            if (event.pojo.sterilWorkStatus != RikaStatus.STERIL_ON && event.pojo.sterilWorkStatus != RikaStatus.STERIL_OFF
                    && event.pojo.sterilWorkStatus != RikaStatus.STERIL_ALARM) {
                if (mModelDialog != null && mModelDialog.isShow()) {
                    mModelDialog.dismiss();
                }
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PageArgumentKey.RIKA, mRika);
                bundle.putString(PageArgumentKey.tag, tag);
                bundle.putString(PageArgumentKey.viewBackgroundImg, mViewBackgroundImg);
                UIService.getInstance().postPage(PageKey.DeviceRikaXWork, bundle);

            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_rika_appointment_elimination, container, false);
        ButterKnife.inject(this, view);
        LogUtils.i("20181221", " mDry:" + mDry);
        initData(mDeviceConfigurationFunctions);
        return view;
    }

    private void initData(List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {

        try {
            for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                if ("appointmentDisinfection".equals(deviceConfigurationFunctions.get(i).functionCode)) {
                    mFunctions = deviceConfigurationFunctions.get(i);
                    mTvDeviceModelName.setText(mFunctions.subView.title);
                }
            }
            List<DeviceConfigurationFunctions> appointmentList = mFunctions.subView
                    .subViewModelMap
                    .subViewModelMapSubView
                    .deviceConfigurationFunctions;

            for (int i = 0; i < appointmentList.size(); i++) {
                if ("orderDisinfection".equals(appointmentList.get(i).functionCode)) {
                    String functionParams = appointmentList.get(i).functionParams;
                    mParams = JsonUtils.json2Pojo(functionParams, RikaAppointmentParams.class);
                    getData();
                    CloudHelper.queryRecord(Plat.accountService.getCurrentUserId() ,mRika.getGuid().getGuid(), "1", new Callback<Reponses.QueryResponse>() {
                        @Override
                        public void onSuccess(Reponses.QueryResponse queryResponse) {
                            if (mTvModel == null || mTvTime == null || mTvTimeLater == null) return;
                            List<QureyData> payload = queryResponse.payload;
                            LogUtils.i("20190115", "payload:" + payload.size());
                            if (payload != null && payload.size() > 0) {
                                mWorkModelDef = payload.get(0).mode;
                                mWorkTimeDef = payload.get(0).workTime;
                                mWaitTimeDef = payload.get(0).orderTime;
                                if (!TextUtils.isEmpty(mWorkModelDef) || !TextUtils.isEmpty(mWorkTimeDef) || !TextUtils.isEmpty(mWaitTimeDef)) {
                                    if (mWorkModelDef.equals("11")) {
                                        mTvModel.setText(mParams.getParams().getDisinfection().getWorkModelName().getValue());
                                    } else if (mWorkModelDef.equals("12")) {
                                        mTvModel.setText(mParams.getParams().getDry().getWorkModelName().getValue());
                                    } else if (mWorkModelDef.equals("13")) {
                                        mTvModel.setText(mParams.getParams().getQuickCleaning().getWorkModelName().getValue());
                                    } else {
                                        mTvModel.setText(mWorkModelName);
                                    }
                                    mTvTime.setText(mWorkTimeDef + cx.getString(R.string.minute));
                                    mTvTimeLater.setText(mWaitTimeDef + cx.getString(R.string.hour_later));
                                } else {
                                    getData();
                                }
                            } else {
                                if (!TextUtils.isEmpty(mWorkModelDef) || !TextUtils.isEmpty(mWorkTimeDef) || !TextUtils.isEmpty(mWaitTimeDef)) {
                                    if (mWorkModelDef.equals("11")) {
                                        mTvModel.setText(mParams.getParams().getDisinfection().getWorkModelName().getValue());
                                    } else if (mWorkModelDef.equals("12")) {
                                        mTvModel.setText(mParams.getParams().getDry().getWorkModelName().getValue());
                                    } else if (mWorkModelDef.equals("13")) {
                                        mTvModel.setText(mParams.getParams().getQuickCleaning().getWorkModelName().getValue());
                                    } else {
                                        mTvModel.setText(mWorkModelName);
                                    }
                                    mTvTime.setText(mWorkTimeDef + cx.getString(R.string.minute));
                                    mTvTimeLater.setText(mWaitTimeDef + cx.getString(R.string.hour_later));
                                } else {
                                    getData();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20190115", "t:" + t.toString());
                            if (mTvModel == null || mTvTime == null || mTvTimeLater == null) return;
                            if (!TextUtils.isEmpty(mWorkModelDef) || !TextUtils.isEmpty(mWorkTimeDef) || !TextUtils.isEmpty(mWaitTimeDef)) {
                                if (mWorkModelDef.equals("11")) {
                                    mTvModel.setText(mParams.getParams().getDisinfection().getWorkModelName().getValue());
                                } else if (mWorkModelDef.equals("12")) {
                                    mTvModel.setText(mParams.getParams().getDry().getWorkModelName().getValue());
                                } else if (mWorkModelDef.equals("13")) {
                                    mTvModel.setText(mParams.getParams().getQuickCleaning().getWorkModelName().getValue());
                                } else {
                                    mTvModel.setText(mWorkModelName);
                                }
                                mTvTime.setText(mWorkTimeDef + cx.getString(R.string.minute));
                                mTvTimeLater.setText(mWaitTimeDef + cx.getString(R.string.hour_later));
                            } else {
                                getData();
                            }
                        }
                    });

//                    mWorkModelDef = PreferenceUtils.getString("model" + mRika.getID(), null);
//                    mWorkTimeDef = PreferenceUtils.getString("workTime" + mRika.getID(), null);
//                    mWaitTimeDef = PreferenceUtils.getString("waitTime" + mRika.getID(), null);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getData() {
        mWorkModelName = mParams.getParamsDef().getDisinfection().getWorkModelName().getValue();
        mWorkModelDef = mParams.getParamsDef().getDisinfection().getWorkModel().getValue();
        mWaitTimeDef = mParams.getParamsDef().getDisinfection().getWaitTimeDef().getValue();
        mWorkTimeDef = mParams.getParamsDef().getDisinfection().getWorkTimeDef().getValue();
        mDisinfection = mParams.getParams().getDisinfection().getWorkModelName().getValue();
        mDry = mParams.getParams().getDry().getWorkModelName().getValue();
        mQuick = mParams.getParams().getQuickCleaning().getWorkModelName().getValue();
        modelNameList.clear();
        modelNameList.add(mDisinfection);
        modelNameList.add(mDry);
        modelNameList.add(mQuick);
        if (mWorkModelDef.equals("11")) {
            mTvModel.setText(mParams.getParams().getDisinfection().getWorkModelName().getValue());
        } else if (mWorkModelDef.equals("12")) {
            mTvModel.setText(mParams.getParams().getDry().getWorkModelName().getValue());
        } else if (mWorkModelDef.equals("13")) {
            mTvModel.setText(mParams.getParams().getQuickCleaning().getWorkModelName().getValue());
        } else {
            mTvModel.setText(mWorkModelName);
        }
        mTvTime.setText(mWorkTimeDef + cx.getString(R.string.minute));
        mTvTimeLater.setText(mWaitTimeDef + cx.getString(R.string.hour_later));

        List<Integer> disinfectionWorkTimeList = mParams.getParams().getDisinfection().getWorkTime().getValue();
        mDisinfectionWorkTimeList = HelperRikaData.getTimeData(disinfectionWorkTimeList);
        List<Integer> dryWorkTimeList = mParams.getParams().getDry().getWorkTime().getValue();
        mDryWorkTimeList = HelperRikaData.getTimeData(dryWorkTimeList);
        List<Integer> quickWorkTimeList = mParams.getParams().getQuickCleaning().getWorkTime().getValue();
        mQuickWorkTimeList = HelperRikaData.getTimeData(quickWorkTimeList);

        List<Integer> disinfectionWaitTimeList = mParams.getParams().getDisinfection().getWaitTime().getValue();
        mDisinfectionWaitTimeList = HelperRikaData.getTimeDataHour(disinfectionWaitTimeList);
        List<Integer> dryWaitTimeList = mParams.getParams().getDry().getWaitTime().getValue();
        mDryWaitTimeList = HelperRikaData.getTimeDataHour(dryWaitTimeList);
        List<Integer> quickWaitTimeList = mParams.getParams().getQuickCleaning().getWaitTime().getValue();
        mQuickWaitTimeList = HelperRikaData.getTimeDataHour(quickWaitTimeList);
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

    @OnClick(R.id.tv_model)
    public void onMTvModelClicked() {
        if (modelNameList == null || modelNameList.size() == 0) return;
        LogUtils.i("20181213", " modelNameList:" + modelNameList.size());
        mModelDialog = null;
        if (mModelDialog == null) {
            mModelDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        }
        mModelDialog.setWheelViewData(null, modelNameList, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                msg.obj = contentCenter;
                mHandler.sendMessage(msg);
            }
        }, null);

        mModelDialog.show();

    }

    //设置模式
    private void setDeviceModel(final String model) {

        LogUtils.i("20181213", " model:" + model);
        LogUtils.i("20181213", " mModelDialog:" + mModelDialog);
        if (mModelDialog == null) return;
        mModelDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModelDialog.dismiss();
                mWorkModelDef = model;
                mTvModel.setText(model);
                if (model.equals(mDisinfection)) {
                    String disinfectionWorkTimeDef = mParams.getParams().getDisinfection().getWorkTimeDef().getValue();
                    mTvTime.setText(disinfectionWorkTimeDef + cx.getString(R.string.minute));
                    mWorkTimeDef = disinfectionWorkTimeDef;
                    String disinfectionWaitTimeDef = mParams.getParams().getDisinfection().getWaitTimeDef().getValue();
                    mTvTimeLater.setText(disinfectionWaitTimeDef + cx.getString(R.string.hour_later));
                    mWaitTimeDef = disinfectionWaitTimeDef;
                } else if (model.equals(mDry)) {
                    String dryWorkTimeDef = mParams.getParams().getDry().getWorkTimeDef().getValue();
                    mTvTime.setText(dryWorkTimeDef + cx.getString(R.string.minute));
                    mWorkTimeDef = dryWorkTimeDef;
                    String dryWaitTimeDef = mParams.getParams().getDry().getWaitTimeDef().getValue();
                    mTvTimeLater.setText(dryWaitTimeDef + cx.getString(R.string.hour_later));
                    mWaitTimeDef = dryWaitTimeDef;
                } else if (model.equals(mQuick)) {
                    String quickWorkTimeDef = mParams.getParams().getQuickCleaning().getWorkTimeDef().getValue();
                    mTvTime.setText(quickWorkTimeDef + cx.getString(R.string.minute));
                    mWorkTimeDef = quickWorkTimeDef;
                    String quickWaitTimeDef = mParams.getParams().getQuickCleaning().getWaitTimeDef().getValue();
                    mTvTimeLater.setText(quickWaitTimeDef + cx.getString(R.string.hour_later));
                    mWaitTimeDef = quickWaitTimeDef;
                }


            }
        });

        mModelDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModelDialog != null && mModelDialog.isShow()) {
                    mModelDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.tv_time)
    public void onMTvTimeClicked() {

        String contentModel = mTvModel.getText().toString();
        Log.i("20181221", " contentModel:" + contentModel);
        Log.i("20181221", " mDisinfection:" + mDisinfection);
        Log.i("20181221", " mDry:" + mDry);
        Log.i("20181221", " mQuick:" + mQuick);
        if (mModelDialog != null) {
            mModelDialog = null;
        }
        if (mModelDialog == null) {
            mModelDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        }


        if (contentModel.equals(mDisinfection)) {
            mModelDialog.setWheelViewData(null, mDisinfectionWorkTimeList, null, false, 0, 1, 0,
                    null, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 2;
                            msg.obj = contentCenter;
                            mHandler.sendMessage(msg);
                        }
                    }, null);
            mModelDialog.show();
        } else if (contentModel.equals(mDry)) {
            mModelDialog.setWheelViewData(null, mDryWorkTimeList, null, false, 0, 1, 0,
                    null, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 2;
                            msg.obj = contentCenter;
                            mHandler.sendMessage(msg);
                        }
                    }, null);
            mModelDialog.show();
        } else if (contentModel.equals(mQuick)) {
            mModelDialog.setWheelViewData(null, mQuickWorkTimeList, null, false, 0, 0, 0,
                    null, new OnItemSelectedListenerCenter() {
                        @Override
                        public void onItemSelectedCenter(String contentCenter) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = 2;
                            msg.obj = contentCenter;
                            mHandler.sendMessage(msg);
                        }
                    }, null);
            mModelDialog.show();
        }

    }

    //设置模式工作分钟
    private void setDeviceModelWorkTime(String obj) {
        if (TextUtils.isEmpty(obj)) return;
        final String workTime = RemoveManOrsymbolUtil.getRemoveString(obj);
        mModelDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModelDialog.dismiss();
                mWorkTimeDef = workTime;
                mTvTime.setText(workTime + cx.getString(R.string.minute));
            }
        });
        mModelDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModelDialog != null && mModelDialog.isShow()) {
                    mModelDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.tv_time_later)
    public void onMTvTimeLaterClicked() {
        String contentModel = mTvModel.getText().toString();
        LogUtils.i("20181220", " contentModel:" + contentModel);
        LogUtils.i("20181220", " mDisinfection:" + mDisinfection);
        LogUtils.i("20181220", " mDry:" + mDry);
        LogUtils.i("20181220", " mQuick:" + mQuick);
        if (mModelDialog != null) {
            mModelDialog = null;
        }
        if (mModelDialog == null) {
            mModelDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        }
        if (contentModel.equals(mDisinfection)) {
            mModelDialog.setWheelViewData(null, mDisinfectionWaitTimeList, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 3;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);
            mModelDialog.show();
        } else if (contentModel.equals(mDry)) {
            mModelDialog.setWheelViewData(null, mDryWaitTimeList, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 3;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);
            mModelDialog.show();
        } else if (contentModel.equals(mQuick)) {
            mModelDialog.setWheelViewData(null, mQuickWaitTimeList, null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 3;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);
            mModelDialog.show();
        }


    }

    //设置预约时间
    private void setDeviceModelWaitTime(String obj) {
        if (TextUtils.isEmpty(obj)) return;
        final String waitTime = RemoveManOrsymbolUtil.getRemoveString(obj);
        mModelDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mModelDialog.dismiss();
                mWaitTimeDef = waitTime;
                mTvTimeLater.setText(waitTime + cx.getString(R.string.hour_later));
            }
        });
        mModelDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModelDialog != null && mModelDialog.isShow()) {
                    mModelDialog.dismiss();
                }
            }
        });
    }

    @OnClick(R.id.tv_btn)
    public void onMTvBtnClicked() {
        if (mRika == null) return;

        String disinfection = mParams.getParams().getDisinfection().getWorkModelName().getValue();
        String dry = mParams.getParams().getDry().getWorkModelName().getValue();
        String quick = mParams.getParams().getQuickCleaning().getWorkModelName().getValue();
        LogUtils.i("20190109", "mWorkModelDef:" + mWorkModelDef + " disinfection:" + disinfection + " dry:" + dry + " quick:" + quick);
        if (mWorkModelDef.equals(disinfection)) {
            mWorkModelDef = "11";
        } else if (mWorkModelDef.equals(dry)) {
            mWorkModelDef = "12";
        } else if (mWorkModelDef.equals(quick)) {
            mWorkModelDef = "13";
        }
        if (mRika.sterilWorkStatus == RikaStatus.STERIL_OFF) {

            mRika.setSterilizerWorkStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1,
                    RikaStatus.STERIL_CATEGORYCODE, (short) 1, (short) 49, (short) 6, RikaStatus.STERIL_ON, (short) 0, (short) 0, (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                final short n = 0;
                                mRika.setSterilizerWorkStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1,
                                        RikaStatus.STERIL_CATEGORYCODE, (short) 1, (short) 49, (short) 6,
                                        Short.parseShort(mWorkModelDef), Short.parseShort(mWorkTimeDef),
                                        Short.parseShort(mWaitTimeDef), n, new VoidCallback() {
                                            @Override
                                            public void onSuccess() {
                                                submitHistory(Plat.accountService.getCurrentUserId(), mWorkModelDef, mWorkTimeDef, mWaitTimeDef);
//                                                PreferenceUtils.setString("model" + mRika.getID(), mWorkModelDef);
//                                                PreferenceUtils.setString("workTime" + mRika.getID(), mWorkTimeDef);
//                                                PreferenceUtils.setString("waitTime" + mRika.getID(), mWaitTimeDef);
                                                IRokiDialog dialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_18);
                                                dialog.setCanceledOnTouchOutside(false);
                                                dialog.show();
                                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                    @Override
                                                    public void onDismiss(DialogInterface dialog) {
//
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                            }
                                        }

                                );
                            }

                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });


        } else {
            final short n = 0;
            mRika.setSterilizerWorkStatus(MsgKeys.setDeviceRunStatus_Req, (short) 1,
                    RikaStatus.STERIL_CATEGORYCODE, (short) 1, (short) 49, (short) 6,
                    Short.parseShort(mWorkModelDef), Short.parseShort(mWorkTimeDef),
                    Short.parseShort(mWaitTimeDef), n, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            submitHistory(Plat.accountService.getCurrentUserId(), mWorkModelDef, mWorkTimeDef, mWaitTimeDef);
//                            PreferenceUtils.setString("model" + mRika.getID(), mWorkModelDef);
//                            PreferenceUtils.setString("workTime" + mRika.getID(), mWorkTimeDef);
//                            PreferenceUtils.setString("waitTime" + mRika.getID(), mWaitTimeDef);
                            IRokiDialog dialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_18);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
//
                                }
                            });
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    }

            );
        }


    }

    /**
     * 提交工作历史
     *
     * @param model    模式
     * @param workTime 工作时间
     * @param waitTime 预约时间
     */
    private void submitHistory(long userId, String model, String workTime, String waitTime) {

        CloudHelper.submitRecord(userId, mRika.getGuid().getGuid(), model, workTime, waitTime, new Callback<Reponses.SubmitResponse>() {
            @Override
            public void onSuccess(Reponses.SubmitResponse submitResponse) {
                LogUtils.i("20190115", "submitResponse:" + submitResponse.msg);
            }

            @Override
            public void onFailure(Throwable t) {
                LogUtils.i("20190115", "submitResponse:" + t.toString());
            }
        });
    }
}
