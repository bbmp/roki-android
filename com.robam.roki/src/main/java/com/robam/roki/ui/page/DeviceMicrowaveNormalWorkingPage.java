package com.robam.roki.ui.page;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.MicroWaveStatusChangedEvent;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.microwave.MicroWaveM509;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.util.UtilDialog;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.BlackPromptDialog;
import com.robam.roki.ui.dialog.MicrowaveProfessionalDialog;
import com.robam.roki.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class DeviceMicrowaveNormalWorkingPage extends BasePage {
    @InjectView(R.id.mic_nw_txtTem)
    TextView mic_nw_txtTem;
    @InjectView(R.id.topleft_img)
    ImageView topleft_img;
    @InjectView(R.id.topleft_unit)
    TextView topleft_unit;
    @InjectView(R.id.mic_nw_txtCurrentWeight)//????????????
            TextView mic_nw_txtCurrentWeight;
    @InjectView(R.id.mic_nw_txtCurrentTime)//????????????
            TextView mic_nw_txtCurrentTime;
    @InjectView(R.id.mic_nw_rotate_img)//??????????????????
            ImageView mic_nw_rotate_img;
    @InjectView(R.id.mic_nw_middle_ll)//????????????linearlayout
            LinearLayout mic_nw_middle_ll;
    @InjectView(R.id.mic_nw_middle_ll_worktype1)//????????????1
            TextView mic_nw_middle_ll_worktype1;
    @InjectView(R.id.mic_nw_middle_ll_img)//????????????
            ImageView mic_nw_middle_ll_img;
    @InjectView(R.id.mic_nw_middle_ll_worktype2)//????????????2
            TextView mic_nw_middle_ll_worktype2;
    private Animation circleRotate;
    private MicroWaveM509 microWave;
    private short time;//?????????
    private final int Time_Refresh = 1;
    private final int Close = 2;
    private Timer timer;//?????????
    private boolean lock = true;//??????
    private boolean isFirst = true;//???????????????????????????
    private View contentView;
    private IRokiDialog gitingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        microWave = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_microwave_normal_working,
                container, false);
        ButterKnife.inject(this, contentView);
        initView();
        gitingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    private void initView() {
        Bundle bd = getArguments();
        String type = bd.getString(PageArgumentKey.MicroWavePageArg);
        if ("1".equals(type))//????????????
            setLeftTop();
        //InWorkingMode();
        if (MicrowaveProfessionalDialog.dlg != null && MicrowaveProfessionalDialog.dlg.isShowing()) {
            MicrowaveProfessionalDialog.dlg.dismiss();
            MicrowaveProfessionalDialog.dlg = null;
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Time_Refresh) {
                if (time == 0) {
                }
                short min = (short) (time / 60);
                short sec = (short) (time % 60);
                try {
                    if (sec < 10)
                        mic_nw_txtCurrentTime.setText(min + ":0" + sec);
                    else
                        mic_nw_txtCurrentTime.setText(min + ":" + sec);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (time == 0) {
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        return;
                    }
                    time--;
                }
            }else if (msg.what == Close){
                UIService.getInstance().returnHome();
            }
        }
    };

    /**
     * ?????????????????????
     */
    @OnClick(R.id.microwaveM509_normal_working_recipe)
    public void OnMicroM509Click() {
        //HomeRecipeView.recipeCategoryClick(DeviceType.RWBL);
        ToastUtils.show("???????????????????????????", Toast.LENGTH_SHORT);
    }

    /**
     * ??????????????? ??????
     */
    @OnClick(R.id.mic_nw_middle_ll)
    public void OnClickPauseOrRun() {
        if (lock) return;
        short cmd = 0;
        if (microWave.state == MicroWaveStatus.Run) {
            cmd = 3;
        } else if (microWave.state == MicroWaveStatus.Pause) {
            cmd = 2;
        } else {
            return;
        }
        final short temp = cmd;
        microWave.setMicroWaveState(cmd, new VoidCallback() {
            @Override
            public void onSuccess() {
                if (temp == 3)
                    InPauseMode();
                else
                    InWorkingMode();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("cmd", "onFailure");
            }
        });
    }

    /**
     * ????????????
     */
    @OnClick(R.id.mic_nw_linSwitch)
    public void OnClickEnd() {
        if (lock) return;
        short endStatus = 4;
        Log.i("end", endStatus + "");
        if (microWave.state == MicroWaveStatus.Run) {
            endStatus = 4;
        } else if (microWave.state == MicroWaveStatus.Pause) {
            endStatus = 1;
        }
        Log.i("end", endStatus + "");
        final short cmd = endStatus;
        final IRokiDialog closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedialog.dismiss();
                microWave.setMicroWaveState(cmd, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        handler.sendEmptyMessage(Close);
                    }
                    @Override
                    public void onFailure(Throwable t) {}
                });
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()){
                    closedialog.dismiss();
                }
            }
        });

    }

    /**
     * ??????
     */
    @OnClick(R.id.microwave_return)
    public void OnReturnClick() {
        UIService.getInstance().returnHome();
    }

    @Subscribe
    public void OnEvent(MicroWaveStatusChangedEvent event) {
        if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                || microWave.mode == MicroWaveModel.ComibineHeating)
            setLeftTop();
        switch (microWave.state) {
            case MicroWaveStatus.Run:
                InWorkingMode();
                break;
            case MicroWaveStatus.Pause:
                InPauseMode();
                break;
            case MicroWaveStatus.Alarm:
                break;
            case MicroWaveStatus.Wait:
                if (time == 0) {
                    InEndMode();
                    time = -100;
                    lock = true;
                } else if (time == -100) {//????????????
                    backToParent();
                } else {//????????????
                    backToParent();
                }
                break;
            case MicroWaveStatus.Setting://????????????
                backToParent();
                break;
            default:
                break;
        }
        lock = false;
        //0?????? 1??????
        if (microWave.doorState == 0) {
            if (gitingDialog != null && gitingDialog.isShow()) {
                gitingDialog.dismiss();
                gitingDialog = null;
            }
        } else if (microWave.doorState == 1) {
            if (gitingDialog != null && gitingDialog.isShow()) {
            } else {
                gitingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
                gitingDialog.setContentText(R.string.device_alarm_gating_content);
                gitingDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                gitingDialog.show();
            }
        }
    }

    private BlackPromptDialog blackPromptDialog = null;

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (microWave == null || !Objects.equal(microWave.getID(), event.device.getID()))
            return;
        backToParent();
        //getFragmentManager().popBackStack();
    }

    /**
     * ??????
     */
    @Subscribe
    public void onEvent(MicroWaveAlarmEvent event) {
        if (event.alarm == 0) return;
        if (BlackPromptDialog.dlg != null && BlackPromptDialog.dlg.isShowing()) return;
        if (microWave.state==3){
            microWave.setMicroWaveState((short)3, new VoidCallback() {
                @Override
                public void onSuccess() {
                    InPauseMode();
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.i("cmd", "onFailure");
                }
            });
        }

    }

    /**
     * ????????????
     */
    private void InWorkingMode() {
        mic_nw_txtCurrentWeight.setVisibility(View.VISIBLE);
        if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                || microWave.mode == MicroWaveModel.ComibineHeating) {
            mic_nw_txtCurrentWeight.setText(ChangeFire(microWave.power) + "");
        } else
            mic_nw_txtCurrentWeight.setText(microWave.weight + "");
        mic_nw_txtCurrentTime.setVisibility(View.VISIBLE);
        if (timer == null || (microWave.time - time > 30)) {
            if (timer != null) {
                timer.cancel();
                timer = null;//????????????
            }
            setTimeBySeconds();
        }

        mic_nw_middle_ll_worktype2.setVisibility(View.GONE);
        mic_nw_middle_ll_worktype1.setVisibility(View.VISIBLE);
        mic_nw_middle_ll_worktype1.setText(getModelText().string);
        mic_nw_middle_ll_img.setVisibility(View.VISIBLE);
        mic_nw_middle_ll_img.setImageResource(getModelText().res);
        mic_nw_rotate_img.setImageResource(R.mipmap.img_order_run);
        setAnimation();
    }

    /**
     * ????????????
     */
    private void InPauseMode() {
        if (timer != null) {
            timer.cancel();
            timer = null;//????????????
        }
        if (circleRotate != null) {
            circleRotate.cancel();
            circleRotate = null;
        }
        mic_nw_rotate_img.clearAnimation();
        mic_nw_rotate_img.setImageResource(R.mipmap.img_oven_pause);
        mic_nw_rotate_img.bringToFront();
        if (lock) {//?????????????????????????????????????????????????????????????????????????????????
            mic_nw_txtCurrentWeight.setVisibility(View.VISIBLE);
            if (microWave.mode == MicroWaveModel.Barbecue || microWave.mode == MicroWaveModel.MicroWave
                    || microWave.mode == MicroWaveModel.ComibineHeating)
                mic_nw_txtCurrentWeight.setText(ChangeFire(microWave.power) + "");
            else
                mic_nw_txtCurrentWeight.setText(microWave.weight + "");
            mic_nw_txtCurrentTime.setVisibility(View.VISIBLE);
            short min = (short) (microWave.time / 60);
            short sec = (short) (microWave.time % 60);
            if (sec < 10)
                mic_nw_txtCurrentTime.setText(min + ":0" + sec);
            else
                mic_nw_txtCurrentTime.setText(min + ":" + sec);
        }
    }

    /**
     * ????????????
     */
    private void InEndMode() {
        if (timer != null) {
            timer.cancel();
            timer = null;//????????????
        }
        if (circleRotate != null) {
            circleRotate.cancel();
            circleRotate = null;
        }
        mic_nw_rotate_img.clearAnimation();
        mic_nw_rotate_img.setImageResource(R.mipmap.img_steamoven_finish);
        mic_nw_middle_ll_worktype1.setVisibility(View.GONE);
        mic_nw_middle_ll_worktype2.setVisibility(View.VISIBLE);
        mic_nw_middle_ll_img.setVisibility(View.GONE);
    }

    /**
     * ?????????????????????
     */
    private void setLeftTop() {
        topleft_unit.setText("-");
        mic_nw_txtTem.setText("??????");
        topleft_img.setImageResource(R.mipmap.ic_device_microwave_linksetting_fire_write);
    }

    /**
     * ???????????????
     */
    private void setTimeBySeconds() {
        time = microWave.time;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(Time_Refresh);
            }
        }, 0, 1000);
    }

    /**
     * ????????????
     */
    private void setAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            mic_nw_rotate_img.startAnimation(circleRotate);
        }
    }

    /**
     * ????????????????????????
     */
    private ModelInfo getModelText() {
        ModelInfo info = new ModelInfo();
        int res = 0;
        switch (microWave.mode) {
            case MicroWaveModel.Meat:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_normal_working_meat_yellow;
                break;
            case MicroWaveModel.Checken:
                info.string = "?????????";
                info.res = R.mipmap.ic_device_microwave_working_checken;
                break;
            case MicroWaveModel.Kebab:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_kebab;
                break;
            case MicroWaveModel.Rice:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_rice;
                break;
            case MicroWaveModel.Porridge:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_porridge;
                break;
            case MicroWaveModel.Milk:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_baotang;
                break;
            case MicroWaveModel.Bread:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_bread;
                break;
            case MicroWaveModel.Vegetables:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_vegetables;
                break;
            case MicroWaveModel.Fish:
                info.string = "???";
                info.res = R.mipmap.ic_device_microwave_working_fish;
                break;
            case MicroWaveModel.Barbecue:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_barbecue;
                break;
            case MicroWaveModel.MicroWave:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_microwave;
                break;
            case MicroWaveModel.ComibineHeating:
                info.string = "????????????";
                info.res = R.mipmap.ic_device_microwave_working_microwave_combinedheating;
                break;
            case MicroWaveModel.HeatingAgain:
                info.string = "?????????";
                info.res = R.mipmap.ic_device_microwave_working_heatingagain;
                break;
            case MicroWaveModel.Unfreeze:
                info.string = "??????";
                info.res = R.mipmap.ic_device_microwave_working_unfreeze;
                break;
            default:
                break;
        }
        return info;
    }

    /**
     * ?????? ????????????????????????
     */
    private short ChangeFire(short value) {
        if (microWave.mode == MicroWaveModel.Barbecue) {
            switch (microWave.power) {
                case 7:
                    value = (short) 6;
                    break;
                case 8:
                    value = (short) 4;
                    break;
                case 9:
                    value = (short) 2;
                    break;
                default:
                    break;
            }
        } else if (microWave.mode == MicroWaveModel.ComibineHeating) {
            switch (microWave.power) {
                case 10:
                    value = (short) 6;
                    break;
                case 11:
                    value = (short) 4;
                    break;
                case 12:
                    value = (short) 2;
                    break;
                default:
                    break;
            }
        }
        return value;
    }

    private boolean shut = false;

    private void backToParent() {
        if (shut) return;
        if (UIService.getInstance().getPage(PageKey.DeviceMicrowave) != null) {
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().returnHome();
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, microWave.getID());
            UIService.getInstance().postPage(PageKey.DeviceMicrowave, bundle);
        }
        shut = true;
    }

    /**
     * ??????????????????????????????????????????????????????
     */
    class ModelInfo {
        String string;
        int res;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UIService.getInstance().returnHome();
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        ButterKnife.reset(this);
    }
}
