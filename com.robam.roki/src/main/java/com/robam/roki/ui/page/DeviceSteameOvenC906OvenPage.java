package com.robam.roki.ui.page;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneModel;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.view.DeviceOvenC906ModeWheel;
import com.robam.roki.ui.view.TempC906WheelView;
import com.robam.roki.utils.DialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/15.
 */

public class DeviceSteameOvenC906OvenPage extends BasePage {


    @InjectView(R.id.img_back)
    ImageView imgBack;
    @InjectView(R.id.wv1_pattern)
    DeviceOvenC906ModeWheel wv1Pattern;
    @InjectView(R.id.wv2_temp)
    TempC906WheelView wv2Temp;
    @InjectView(R.id.wv3_time)
    TempC906WheelView wv3Time;
    @InjectView(R.id.ll_wheelView)
    LinearLayout llWheelView;
    @InjectView(R.id.btn_start)
    Button btnStart;
    @InjectView(R.id.tv_mode_name)
    TextView tvModeName;
    @InjectView(R.id.tv_mode_dec)
    TextView tvModeDec;
    @InjectView(R.id.ll_exp_down_temp)
    LinearLayout mLlExpDownTemp;
    @InjectView(R.id.wv4_temp_down)
    TempC906WheelView mWv4TempDown;
    @InjectView(R.id.tv_temp)
    TextView mTvTemp;
    @InjectView(R.id.iv_water)
    ImageView mIvWater;
    private AbsSteameOvenOne steameOvenC906;
    private short time;
    private short temp;
    private short tempDwon;
    private IRokiDialog mRokiToastDialog;


    private Map<String, Short> modeKingMap = new HashMap<String, Short>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 906:
                    tvModeName.setText(wv1Pattern.getSelectedText());
                    if (cx.getString(R.string.device_steamOvenOne_name_kuaisuyure).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_kuaisuyure));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_kuaire).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_kuaire));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_fengpeikao).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_fengpeikao));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_peikao).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_peikao));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_fengshankao).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_fengshankao));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_kaoshao).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_kaoshao));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_qiangshaokao).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_qiangshaokao));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_jiankao).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_jiankao));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_dijiare).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_dijiare));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_exp).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_exp));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_guoshuhonggan).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_guoshuhonggan));
                    } else if (cx.getString(R.string.device_steamOvenOne_name_baowen).equals(wv1Pattern.getSelectedText())) {
                        tvModeDec.setText(cx.getString(R.string.device_steamOvenOne_describe_baowen));
                    }
                    break;

                case 0:
                    mLlExpDownTemp.setVisibility(View.VISIBLE);
                    mTvTemp.setText(R.string.device_steamOvenOne_up_temp_text);
                    mWv4TempDown.setVisibility(View.VISIBLE);
                    mWv4TempDown.setVisibility(View.GONE);
                    mWv4TempDown.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mLlExpDownTemp.setVisibility(View.GONE);
                    mWv4TempDown.setVisibility(View.GONE);
                    mWv4TempDown.setVisibility(View.VISIBLE);
                    mWv4TempDown.setVisibility(View.GONE);
                    mTvTemp.setText(R.string.device_steamOvenOne_temp_text);
                    break;
            }
        }
    };


    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        LogUtils.i("20171205", " worknStatus:" + steameOvenC906.worknStatus);
        if (steameOvenC906 == null || !Objects.equal(steameOvenC906.getID(), event.pojo.getID()))
            return;
        if (steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus ||
                steameOvenC906.powerOnStatus == SteamOvenOnePowerOnStatus.Pause || steameOvenC906.powerOnStatus
                == SteamOvenOnePowerOnStatus.Order || steameOvenC906.powerStatus == SteamOvenOnePowerStatus.Off) {
            UIService.getInstance().popBack();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_steame_oven_c906_oven, null, false);
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        steameOvenC906 = Plat.deviceService.lookupChild(guid);
        ButterKnife.inject(this, view);
        wv1Pattern.setOnSelectListener(modeWheelLitener);
        wv1Pattern.setDefault(1);
        initData();
        tvModeName.setText(wv1Pattern.getSelectedText());
        mRokiToastDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return view;
    }

    private void initData() {
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_kuaisuyure), SteamOvenOneModel.KUAISUYURE);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_kuaire), SteamOvenOneModel.KUAIRE);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_fengpeikao), SteamOvenOneModel.FENGPEIKAO);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_peikao), SteamOvenOneModel.PEIKAO);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_fengshankao), SteamOvenOneModel.FENGSHAIKAO);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_kaoshao), SteamOvenOneModel.SHAOKAO);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao), SteamOvenOneModel.QIANGSHAOKAO);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_jiankao), SteamOvenOneModel.JIANKAO);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_dijiare), SteamOvenOneModel.DIJIARE);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_exp), SteamOvenOneModel.EXP);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_guoshuhonggan), SteamOvenOneModel.GUOSHUHONGGAN);
        modeKingMap.put(cx.getString(R.string.device_steamOvenOne_name_baowen), SteamOvenOneModel.BAOWEN);
    }

    DeviceOvenC906ModeWheel.OnSelectListener modeWheelLitener = new DeviceOvenC906ModeWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            List<?> listModeTemperature = getListModeTemperature(item);
            List<?> listModeTime = getListModeTime(item);
            wv2Temp.setData(listModeTemperature);
            wv3Time.setData(listModeTime);
            LogUtils.i("20171025", "index:" + index + " item:" + item);
            if (item.equals(cx.getString(R.string.device_steamOvenOne_name_exp))) {
                mWv4TempDown.setData(listModeTemperature);
                handler.sendEmptyMessage(0);
            } else {
                handler.sendEmptyMessage(1);
            }
            int defDownTemp = 0;
            int defTemp = 0;
            int defTime = 0;

            if (index == SteamOvenOneModel.KUAISUYURE) {//快速预热
                defTemp = 130;
                defTime = 0;
            } else if (index == SteamOvenOneModel.KUAIRE) {//快热
                defTemp = 150;
                defTime = 29;
            } else if (index == SteamOvenOneModel.FENGPEIKAO) {//风焙烤
                defTemp = 150;
                defTime = 29;
            } else if (index == SteamOvenOneModel.PEIKAO) {//焙烤
                defTemp = 110;
                defTime = 29;
            } else if (index == SteamOvenOneModel.FENGSHAIKAO) {//风扇烤
                defTemp = 170;
                defTime = 29;
            } else if (index == SteamOvenOneModel.SHAOKAO) {//烤烧
                defTemp = 130;
                defTime = 29;
            } else if (index == SteamOvenOneModel.QIANGSHAOKAO) {//强烤烧
                defTemp = 130;
                defTime = 29;
            } else if (index == SteamOvenOneModel.JIANKAO) {//煎烤
                defTemp = 100;
                defTime = 29;
            } else if (index == SteamOvenOneModel.DIJIARE) {//底加热
                defTemp = 110;
                defTime = 29;
            } else if (index == SteamOvenOneModel.EXP) {//双温双控
                defTemp = 60;
                defTime = 19;
                defDownTemp = 90;
            } else if (index == SteamOvenOneModel.GUOSHUHONGGAN) {//果蔬烘干
                defTemp = 10;
                defTime = 29;
            } else if (index == SteamOvenOneModel.BAOWEN) {//保温
                defTemp = 20;
                defTime = 0;
            }
            handler.sendEmptyMessage(906);
            wv2Temp.setDefault(defTemp);
            if (cx.getString(R.string.device_steamOvenOne_name_exp).equals(item)) {
                mWv4TempDown.setDefault(defDownTemp);
                final int finalDefTemp = defTemp;
                wv2Temp.setOnSelectListener(new TempC906WheelView.OnSelectListener() {
                    @Override
                    public void endSelect(int index, Object item) {
                        List<?> downTempRange = getDownTempRange(item);
                        mWv4TempDown.setData(downTempRange);
                        mWv4TempDown.setDefault(0);
                    }

                    @Override
                    public void selecting(int index, Object item) {
                    }
                });
            }
            wv3Time.setDefault(defTime);


        }

        @Override
        public void selecting(int index, Object item) {

        }
    };


    //根据选中的上管温度设置下管温度范围
    protected List<?> getDownTempRange(Object item) {
        if (item == null) {
            return null;
        }
        List<String> list = Lists.newArrayList();
        String wv1SelectValue = (String) item;
        Integer integer = Integer.parseInt(wv1SelectValue);
        if (integer <= 120) {
            for (int i = 100; i <= integer + 20; i++) {
                list.add(i + "");
            }
            return list;
        }
        if (integer > 120 && integer <= 180) {

            for (int i = integer - 20; i <= integer + 20; i++) {
                list.add(i + "");
            }
            return list;
        }

        if (integer > 180 && integer <= 200) {
            for (int i = integer - 20; i <= integer + 20; i++) {
                if (i <= 200) {
                    list.add(i + "");
                }
            }
            return list;
        }

        if (integer >= 200) {
            for (int i = integer - 20; i <= 200; i++) {
                list.add(i + "");
            }
            return list;
        }
        return list;
    }

    /*设置各种模式温度范围*/
    protected List<?> getListModeTemperature(Object item) {
        List<String> list = Lists.newArrayList();
        String model = (String) item;

        if (model.equals(cx.getString(R.string.device_steamOvenOne_name_kuaire)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_fengpeikao)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_peikao)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_fengshankao))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_kaoshao)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_kuaisuyure)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_jiankao))) {
            for (int i = 50; i <= 230; i++) {
                list.add(i + "");
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_baowen))) {
            for (int i = 40; i <= 90; i++) {
                list.add(i + "");
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_guoshuhonggan))) {
            for (int i = 50; i <= 80; i++) {
                list.add(i + "");
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_exp))) {
            for (int i = 100; i <= 200; i++) {
                list.add(i + "");
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_dijiare))) {
            for (int i = 50; i <= 180; i++) {
                list.add(i + "");
            }
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getListModeTime(Object item) {
        List<String> list = Lists.newArrayList();
        String model = (String) item;
        if (model.equals(cx.getString(R.string.device_steamOvenOne_name_kuaire)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_fengpeikao))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_fengshankao))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_kaoshao))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_peikao))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_dijiare))
                || model.equals(cx.getString(R.string.device_steamOvenOne_name_exp)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_jiankao)) ||
                model.equals(cx.getString(R.string.device_steamOvenOne_name_guoshuhonggan))) {
            for (int i = 1; i <= 120; i++) {
                list.add(i + "");
            }
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_kuaisuyure))) {
            String i = cx.getString(R.string.device_c906_yiyi);
            list.add(i);
        } else if (model.equals(cx.getString(R.string.device_steamOvenOne_name_baowen))) {
//            int i = 120;
            String i = cx.getString(R.string.device_c906_yiyi);
            list.add(i);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.img_back, R.id.btn_start, R.id.iv_water})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                UIService.getInstance().popBack();
                break;
            case R.id.btn_start:
                UIService.getInstance().popBack();
                startRunModel();
                break;
            case R.id.iv_water:
                if (steameOvenC906.WaterStatus == 0) {
                    steameOvenC906.setSteameOvenOneWaterPop((short) 1, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
                break;
        }
    }

    private void startRunModel() {

        if (steameOvenC906 != null) {
            if (!steameOvenC906.isConnected()) {
                if (mRokiToastDialog != null) {
                    mRokiToastDialog.setContentText(R.string.device_connected);
                    mRokiToastDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                    mRokiToastDialog.show();
                }
                return;
            }
            short model = 0;
            if (cx.getString(R.string.device_steamOvenOne_name_kuaisuyure).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.KUAISUYURE;
            } else if (cx.getString(R.string.device_steamOvenOne_name_kuaire).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.KUAIRE;
            } else if (cx.getString(R.string.device_steamOvenOne_name_fengpeikao).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.FENGPEIKAO;
            } else if (cx.getString(R.string.device_steamOvenOne_name_peikao).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.PEIKAO;
            } else if (cx.getString(R.string.device_steamOvenOne_name_fengshankao).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.FENGSHAIKAO;
            } else if (cx.getString(R.string.device_steamOvenOne_name_kaoshao).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.SHAOKAO;
            } else if (cx.getString(R.string.device_steamOvenOne_name_qiangshaokao).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.QIANGSHAOKAO;
            } else if (cx.getString(R.string.device_steamOvenOne_name_jiankao).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.JIANKAO;
            } else if (cx.getString(R.string.device_steamOvenOne_name_dijiare).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.DIJIARE;
            } else if (cx.getString(R.string.device_steamOvenOne_name_exp).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.EXP;
            } else if (cx.getString(R.string.device_steamOvenOne_name_guoshuhonggan).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.GUOSHUHONGGAN;
            } else if (cx.getString(R.string.device_steamOvenOne_name_baowen).equals(wv1Pattern.getSelectedText())) {
                model = SteamOvenOneModel.BAOWEN;
            }
            try {

                if (model == SteamOvenOneModel.KUAISUYURE &&
                        cx.getString(R.string.device_c906_yiyi).equals(wv3Time.getSelectedText())) {
                    time = 90;
                } else if (model == SteamOvenOneModel.BAOWEN &&
                        cx.getString(R.string.device_c906_yiyi).equals(wv3Time.getSelectedText())) {
                    time = 120;
                } else {
                    time = Short.valueOf(wv3Time.getSelectedText());
                }
                temp = Short.valueOf(wv2Temp.getSelectedText());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (model != SteamOvenOneModel.EXP) {
                LogUtils.i("20171205", "model:" + model + " time:" + time + " temp:" + temp + " tempDwon:" + tempDwon);

                steameOvenC906.setSteameOvenOneRunMode(model, time, temp,
                        (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
//                                if (steameOvenC906.alarm == 0){
//                                    UIService.getInstance().popBack();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString(PageArgumentKey.Guid, steameOvenC906.getID());
//                                    bundle.putShort("from", (short) 1);
//                                    UIService.getInstance().postPage(PageKey.DeviceOvenC906Working, bundle);
//                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
            } else if (model == SteamOvenOneModel.EXP) {
                LogUtils.i("20171130", "EXP   model:" + model + "time:" + time + " temp:" + temp + "tempDwon:" + tempDwon);
                try {
                    tempDwon = Short.valueOf(mWv4TempDown.getSelectedText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                steameOvenC906.setSteameOvenOneRunMode(model, time, temp,
                        (short) 0, tempDwon, (short) 0, (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                if (steameOvenC906.alarm == 0) {
//                                    UIService.getInstance().popBack();
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString(PageArgumentKey.Guid, steameOvenC906.getID());
//                                    bundle.putShort("from", (short) 1);
//                                    UIService.getInstance().postPage(PageKey.DeviceSteameOvenC906ExpWorking, bundle);
                                }
                            }

                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });
            }

        }
    }


}
