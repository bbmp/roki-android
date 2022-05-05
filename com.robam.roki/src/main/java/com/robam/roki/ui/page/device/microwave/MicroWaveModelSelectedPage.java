package com.robam.roki.ui.page.device.microwave;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.ModelCommonParams;
import com.robam.roki.model.helper.HelperMicroWaveData;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.adapter.DeviceModelAdapter;
import com.robam.roki.ui.dialog.MicrowaveSettingDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by 14807 on 2018/2/6.
<<<<<<< HEAD
 * 微波炉模式选择
=======
 * 微波炉专业模式 微模式
>>>>>>> master-ap
 */
public class MicroWaveModelSelectedPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private DeviceModelAdapter mDeviceModelAdapter;
    String mGuid;
    String mTitle;
    private List<DeviceConfigurationFunctions> mDeviceSelectModelList;
    AbsMicroWave mMicroWave;
    private String mUnit;
    private IRokiDialog mIRokiDialog;
    private short mModel;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setDeviceData((String) msg.obj);
                    break;
            }
        }
    };
    private MicrowaveSettingDialog mMicrowaveSettingDialog;


    @Override
    public void onResume() {
        super.onResume();
        if (mMicroWave==null) {
            return;
        }
    }

    @Subscribe
    public void onEvent(MicroWaveStatusChangedEvent event) {
        if (mMicroWave == null || !Objects.equal(mMicroWave.getID(), event.pojo.getID()))
            return;

        if (mMicroWave.state == MicroWaveStatus.Run || mMicroWave.state == MicroWaveStatus.Alarm
                || mMicroWave.state == MicroWaveStatus.Pause) {
            LogUtils.i("20180928", "state:" + mMicroWave.state + " dt:" + mMicroWave.getDt());

            if (mMicrowaveSettingDialog != null && mMicrowaveSettingDialog.isShowing()) {
                mMicrowaveSettingDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }
    }

    private void setDeviceData(String data) {
        String dataStr = null;
        if (data.contains(mUnit)) {
            dataStr = RemoveManOrsymbolUtil.getRemoveString(data);
        }

        final int weigth = Integer.parseInt(dataStr);
        mIRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIRokiDialog != null && mIRokiDialog.isShow()) {
                    mIRokiDialog.dismiss();
                    if (mMicroWave.doorState == 1) {
                        ToastUtils.showShort(R.string.device_alarm_rika_E1);
                        return;
                    }
                    mMicroWave.setMicroWaveKindsAndHeatCold(mModel, (short) weigth, new VoidCallback() {
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
        mIRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mDeviceSelectModelList = bd == null ? null : (List<DeviceConfigurationFunctions>) bd.getSerializable(PageArgumentKey.List);
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mTitle = bd == null ? null : bd.getString(PageArgumentKey.title);
        mMicroWave = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.page_device_model_selected, container, false);
        ButterKnife.inject(this, view);
        mTvDeviceModelName.setText(mTitle);
        LogUtils.i("20180925", "onCreateView");
        initData();
        return view;
    }

    private void initData() {
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
        try {
            if (mDeviceSelectModelList != null && mDeviceSelectModelList.size() > 0) {
                //                    }
                for (int i = 0; i < mDeviceSelectModelList.size(); i++)
                    if (view.getTag().toString().equals(mDeviceSelectModelList.get(i).functionCode)) {
                        ArrayList<DeviceConfigurationFunctions> descList = Lists.newArrayList();
                        descList.clear();
                        descList.add(mDeviceSelectModelList.get(i));
                        String functionCode = mDeviceSelectModelList.get(i).functionCode;
                        LogUtils.i("20180928", "functionCode:" + functionCode);
                        if (functionCode.equals("microwave")) {
                            initCode(MicroWaveModel.MicroWave);
                        } else if (functionCode.equals("barbecue")) {
                            initCode(MicroWaveModel.Barbecue);
                        } else if (functionCode.equals("combination")) {
                            initCode(MicroWaveModel.ComibineHeating);
                        } else {
                            initDialogCode(mDeviceSelectModelList.get(i));
                        }
                    } else {

                    }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initDialogCode(DeviceConfigurationFunctions functionCode) {
        String functionParams = functionCode.functionParams;
        LogUtils.i("20180929", "functionParams:" + functionParams);
        try {
            ModelCommonParams modelCommonParams = JsonUtils.json2Pojo(functionParams, ModelCommonParams.class);
            String cmd = modelCommonParams.getCmd();
            mModel = Short.parseShort(modelCommonParams.getModel());
            mUnit = modelCommonParams.getNumberCompany().getValue();
            String defaultValue = modelCommonParams.getDefaultSetNumber().getValue();
            List<Integer> valueList = modelCommonParams.getSetNumber().getValue();
            List<String> listData = HelperMicroWaveData.getListData(valueList, mUnit);
            int index = Integer.parseInt(defaultValue) - 1;
            LogUtils.i("20180929", " index:" + index);
            mIRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
            mIRokiDialog.setWheelViewData(null, listData, null, false, 0, index, 0, null, new OnItemSelectedListenerCenter() {
                @Override
                public void onItemSelectedCenter(String contentCenter) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = contentCenter;
                    mHandler.sendMessage(msg);
                }
            }, null);
            mIRokiDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String MicroWaveModelName;
    private void initCode(final int model) {
        mMicrowaveSettingDialog = Helper.newMicrowaveSettingDialog(cx, new Callback2<MicroWaveWheelMsg>() {
            @Override
            public void onCompleted(final MicroWaveWheelMsg microWaveWheelMsg) {
                LogUtils.i("20180928", "Fire:" + microWaveWheelMsg.getFire());
                if (microWaveWheelMsg.getModel() == MicroWaveModel.Barbecue) {
                    switch (microWaveWheelMsg.getFire()) {
                        case 6:
                            microWaveWheelMsg.setFire((short) 7);
                            break;
                        case 4:
                            microWaveWheelMsg.setFire((short) 8);
                            break;
                        case 2:
                            microWaveWheelMsg.setFire((short) 9);
                            break;
                        default:
                            break;
                    }
                } else if (microWaveWheelMsg.getModel() == MicroWaveModel.ComibineHeating) {
                    switch (microWaveWheelMsg.getFire()) {
                        case 6:
                            microWaveWheelMsg.setFire((short) 10);
                            break;
                        case 4:
                            microWaveWheelMsg.setFire((short) 11);
                            break;
                        case 2:
                            microWaveWheelMsg.setFire((short) 12);
                            break;
                        default:
                            break;
                    }
                }
                LogUtils.i("20180928", "mMicroWave:" + mMicroWave.getDt() + " mode:" + mMicroWave.mode);
                if (mMicroWave.doorState == 1) {
                    ToastUtils.showShort(R.string.device_alarm_rika_E1);
                    return;
                }

                LogUtils.i("20190805-8937", "model:" + model + "time:" + microWaveWheelMsg.getTime() + "fire：" + microWaveWheelMsg.getFire());
                mMicroWave.setMicroWaveProModeHeat((short) model, microWaveWheelMsg.getTime(), microWaveWheelMsg.getFire(), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("20190928", "onSuccess:");
                        switch (microWaveWheelMsg.getModel()) {
                            case MicroWaveModel.ComibineHeating:
                                MicroWaveModelName="组合加热";
                                break;
                            case MicroWaveModel.MicroWave:
                                MicroWaveModelName="微波";
                                break;
                            case MicroWaveModel.Barbecue:
                                MicroWaveModelName="烧烤";
                                break;
                        }

                        if (mMicroWave!=null) {
                            ToolUtils.logEvent(mMicroWave.getDt(), "开始微波炉模式火力时间工作:"+MicroWaveModelName+":" + microWaveWheelMsg.getFire() + ":" + microWaveWheelMsg.getTime(), "roki_设备");
                        }

                    }



                    @Override
                    public void onFailure(Throwable t) {
                        LogUtils.i("20190928", "t:" + t);
                    }
                });
            }
        }, (short) model, mMicroWave);

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
