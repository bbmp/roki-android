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
import android.widget.RelativeLayout;
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
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.MicroWaveAlarmEvent;
import com.robam.common.events.MicroWaveStatusChangedEvent;
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
import com.robam.roki.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class DeviceMicrowaveLinkageWorkingPage extends BasePage {
    @InjectView(R.id.mic_nw_linked_ll_1)
    LinearLayout mic_nw_linked_ll_1;
    @InjectView(R.id.mic_nw_linked_txt_1)
    TextView mic_nw_linked_txt_1;
    @InjectView(R.id.mic_nw_linked_ll_2)
    LinearLayout mic_nw_linked_ll_2;
    @InjectView(R.id.mic_nw_linked_txt_2)
    TextView mic_nw_linked_txt_2;
    @InjectView(R.id.mic_nw_linked_ll_3)
    LinearLayout mic_nw_linked_ll_3;
    @InjectView(R.id.mic_nw_linked_txt_3)
    TextView mic_nw_linked_txt_3;
    @InjectView(R.id.mic_nw_linked_rotate_img)//运行圆圈
            ImageView mic_nw_linked_rotate_img;
    @InjectView(R.id.mic_nw_linked_middle_ll)//中间部分
            LinearLayout mic_nw_linked_middle_ll;
    @InjectView(R.id.mic_nw_linked_middle_ll_img)//圆圈内图片
            ImageView mic_nw_linked_middle_ll_img;
    @InjectView(R.id.mic_nw_linked_middle_ll_worktype1)//模式名称
            TextView mic_nw_linked_middle_ll_worktype1;
    @InjectView(R.id.mic_nw_linked_middle_ll_worktype2)//完成
            TextView mic_nw_linked_middle_ll_worktype2;
    @InjectView(R.id.mic_nw_linked_time)//时间
            TextView mic_nw_linked_time;
    @InjectView(R.id.mic_nw_linked_fire)//火力
            TextView mic_nw_linked_fire;
    MicroWaveM509 microWaveM509;
    private Animation circleRotate;//动画
    private short time;//倒计时
    private final int Time_Refresh = 1;
    private Timer timer;//定时器
    private boolean lock = true;//第一次轮训反馈之前 按钮锁定
    private View contentView;
    private IRokiDialog gitingDialog;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Time_Refresh) {
                if (time == 0) {

                }
                short min = (short) (time / 60);
                short sec = (short) (time % 60);
                Log.i("mic-timer", min + "::" + sec);
                if (sec < 10)
                    mic_nw_linked_time.setText(min + ":0" + sec);
                else
                    mic_nw_linked_time.setText(min + ":" + sec);

                if (time == 0) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    return;
                }
                time--;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        microWaveM509 = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_microwave_linkage_working,
                container, false);
        ButterKnife.inject(this, contentView);
        initView();
        return contentView;
    }

    private void initView() {
    }

    @OnClick(R.id.microwaveM509_linkage_working_recipe)
    public void OnClickMicroRecipe() {
        //HomeRecipeView.recipeCategoryClick(DeviceType.RWBL);
        ToastUtils.show("即将开放，敬请期待", Toast.LENGTH_SHORT);
    }

    /**
     * 暂停或运行 点击
     */
    @OnClick(R.id.mic_nw_linked_middle_ll)
    public void OnClickPauseOrRun() {
        if (lock) return;
        short cmd = 0;
        if (microWaveM509.state == MicroWaveStatus.Run) {
            cmd = 3;
        } else if (microWaveM509.state == MicroWaveStatus.Pause) {
            cmd = 2;
        } else {
            return;
        }
        final short temp = cmd;
        microWaveM509.setMicroWaveState(cmd, new VoidCallback() {
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
     * 结束操作
     */
    @OnClick(R.id.mic_nw_linked_linSwitch)
    public void OnClickEnd() {
        if (lock) return;
        short endStatus = 4;
        Log.i("end", endStatus + "");
        if (microWaveM509.state == MicroWaveStatus.Run) {
            endStatus = 4;
        } else if (microWaveM509.state == MicroWaveStatus.Pause) {
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
                microWaveM509.setMicroWaveState(cmd, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        backToParent();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
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
     * 返回
     */
    @OnClick(R.id.microwave_working_return)
    public void OnReturnClick() {
        UIService.getInstance().returnHome();
    }

    @Subscribe
    public void OnEvent(MicroWaveStatusChangedEvent event) {
        switch (microWaveM509.state) {
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
                    time = -100;
                    lock = true;
                    InEndMode();
                } else if (time == -100) {
                    backToParent();
                } else {
                    backToParent();
                }
                break;
            case MicroWaveStatus.Setting:
                backToParent();
                break;
            default:
                break;
        }
        lock = false;
        if (microWaveM509.doorState == 0) {
            if (gitingDialog != null && gitingDialog.isShow()) {
                gitingDialog.dismiss();
                gitingDialog = null;
            }
        } else if (microWaveM509.doorState == 1) {
            if (gitingDialog != null && gitingDialog.isShow()) {
            } else {
                gitingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
                gitingDialog.setContentText(R.string.device_alarm_gating_content);
                gitingDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
                gitingDialog.show();
            }
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (microWaveM509 == null || !Objects.equal(microWaveM509.getID(), event.device.getID()))
            return;
        if (!microWaveM509.isConnected()) {
            backToParent();
            //getFragmentManager().popBackStack();
        }
    }

    @Subscribe
    public void onEvent(MicroWaveAlarmEvent event) {
        if (event.alarm == 0) return;
        if (BlackPromptDialog.dlg != null && BlackPromptDialog.dlg.isShowing()) return;
        Dialog dialog = Helper.newBlackPromptDialog(cx, null, R.layout.dialog_microwave_door_notclosed);
        dialog.show();
    }

    /**
     * 工作模式
     */
    private void InWorkingMode() {
        ChangeRectangle(microWaveM509.step);

        mic_nw_linked_middle_ll_worktype2.setVisibility(View.GONE);
        mic_nw_linked_middle_ll_worktype1.setVisibility(View.VISIBLE);
        mic_nw_linked_middle_ll_worktype1.setText(getModelText().string);
        mic_nw_linked_middle_ll_img.setVisibility(View.VISIBLE);
        mic_nw_linked_middle_ll_img.setImageResource(getModelText().res);
        mic_nw_linked_rotate_img.setImageResource(R.mipmap.img_order_run);
        setAnimation();
        if (timer == null)
            setTimeBySeconds();
        mic_nw_linked_fire.setText(ChangeFire(microWaveM509.power) + "");
    }

    /**
     * 暂停模式
     */
    private void InPauseMode() {
        if (timer != null) {
            timer.cancel();
            timer = null;//时间暂停
        }
        if (circleRotate != null) {
            circleRotate.cancel();
            circleRotate = null;
        }
        mic_nw_linked_rotate_img.clearAnimation();
        mic_nw_linked_rotate_img.setImageResource(R.mipmap.img_oven_pause);
        mic_nw_linked_rotate_img.bringToFront();

        if (lock) {//首次进入页面轮训结果若是暂停状态，设置时间和重量或火力
            mic_nw_linked_fire.setVisibility(View.VISIBLE);
            mic_nw_linked_fire.setText(ChangeFire(microWaveM509.power) + "");
            mic_nw_linked_time.setVisibility(View.VISIBLE);
            short min = (short) (microWaveM509.time / 60);
            short sec = (short) (microWaveM509.time % 60);
            if (sec < 10)
                mic_nw_linked_time.setText(min + ":0" + sec);
            else
                mic_nw_linked_time.setText(min + ":" + sec);
        }
    }

    /**
     * 完成模式
     */
    private void InEndMode() {
        if (timer != null) {
            timer.cancel();
            timer = null;//时间暂停
        }
        if (circleRotate != null) {
            circleRotate.cancel();
            circleRotate = null;
        }
        mic_nw_linked_rotate_img.clearAnimation();
        mic_nw_linked_rotate_img.setImageResource(R.mipmap.img_steamoven_finish);
        mic_nw_linked_middle_ll_worktype1.setVisibility(View.GONE);
        mic_nw_linked_middle_ll_worktype2.setVisibility(View.VISIBLE);
        mic_nw_linked_middle_ll_img.setVisibility(View.GONE);
    }

    /**
     * 设置倒计时 ( 秒 )
     */
    private void setTimeBySeconds() {
        time = microWaveM509.time;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(Time_Refresh);
            }
        }, 0, 1000);
    }

    /**
     * 改变数字样式
     *
     * @param index
     */
    private void ChangeRectangle(short index) {
        switch (index) {
            case 1:
                RelativeLayout.LayoutParams rl1 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 60),
                        Utils.dip2px(cx, 50));
                rl1.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_1.setLayoutParams(rl1);
                mic_nw_linked_ll_1.setBackgroundDrawable(r.getDrawable(R.drawable.shape_yellow_rectangle));
                mic_nw_linked_txt_1.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 53),
                        Utils.dip2px(cx, 43)));
                mic_nw_linked_txt_1.setBackgroundDrawable(r.getDrawable(R.drawable.shape_yellow_microwave_linkage_num));
                RelativeLayout.LayoutParams rl2 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 50),
                        Utils.dip2px(cx, 40));
                rl2.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_2.setLayoutParams(rl2);
                mic_nw_linked_ll_2.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_rectangle));
                mic_nw_linked_txt_2.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 43),
                        Utils.dip2px(cx, 33)));
                mic_nw_linked_txt_2.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_microwave_linkage_num));
                RelativeLayout.LayoutParams rl3 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 50),
                        Utils.dip2px(cx, 40));
                rl3.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_3.setLayoutParams(rl3);
                mic_nw_linked_ll_3.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_rectangle));
                mic_nw_linked_txt_3.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 43),
                        Utils.dip2px(cx, 33)));
                mic_nw_linked_txt_3.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_microwave_linkage_num));
                break;
            case 2:
                RelativeLayout.LayoutParams rr2 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 60),
                        Utils.dip2px(cx, 50));
                rr2.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_2.setLayoutParams(rr2);
                mic_nw_linked_ll_2.setBackgroundDrawable(r.getDrawable(R.drawable.shape_yellow_rectangle));
                mic_nw_linked_txt_2.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 53),
                        Utils.dip2px(cx, 43)));
                mic_nw_linked_txt_2.setBackgroundDrawable(r.getDrawable(R.drawable.shape_yellow_microwave_linkage_num));

                RelativeLayout.LayoutParams rr1 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 50),
                        Utils.dip2px(cx, 40));
                rr1.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_1.setLayoutParams(rr1);
                mic_nw_linked_ll_1.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_rectangle));
                mic_nw_linked_txt_1.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 43),
                        Utils.dip2px(cx, 33)));
                mic_nw_linked_txt_1.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_microwave_linkage_num));

                RelativeLayout.LayoutParams rr3 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 50),
                        Utils.dip2px(cx, 40));
                rr3.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_3.setLayoutParams(rr3);
                mic_nw_linked_ll_3.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_rectangle));
                mic_nw_linked_txt_3.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 43),
                        Utils.dip2px(cx, 33)));
                mic_nw_linked_txt_3.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_microwave_linkage_num));
                break;
            case 3:
                RelativeLayout.LayoutParams rrr3 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 60),
                        Utils.dip2px(cx, 50));
                rrr3.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_3.setLayoutParams(rrr3);
                mic_nw_linked_ll_3.setBackgroundDrawable(r.getDrawable(R.drawable.shape_yellow_rectangle));
                mic_nw_linked_txt_3.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 53),
                        Utils.dip2px(cx, 43)));
                mic_nw_linked_txt_3.setBackgroundDrawable(r.getDrawable(R.drawable.shape_yellow_microwave_linkage_num));

                RelativeLayout.LayoutParams rrr1 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 50),
                        Utils.dip2px(cx, 40));
                rrr1.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_1.setLayoutParams(rrr1);
                mic_nw_linked_ll_1.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_rectangle));
                mic_nw_linked_txt_1.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 43),
                        Utils.dip2px(cx, 33)));
                mic_nw_linked_txt_1.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_microwave_linkage_num));
                RelativeLayout.LayoutParams rrr2 = new RelativeLayout.LayoutParams(Utils.dip2px(cx, 50),
                        Utils.dip2px(cx, 40));
                rrr2.addRule(RelativeLayout.CENTER_IN_PARENT);
                mic_nw_linked_ll_2.setLayoutParams(rrr2);
                mic_nw_linked_ll_2.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_rectangle));
                mic_nw_linked_txt_2.setLayoutParams(new LinearLayout.LayoutParams(Utils.dip2px(cx, 43),
                        Utils.dip2px(cx, 33)));
                mic_nw_linked_txt_2.setBackgroundDrawable(r.getDrawable(R.drawable.shape_gray_microwave_linkage_num));
                break;
            default:
                break;
        }
    }

    /**
     * 旋转动画
     */
    private void setAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            mic_nw_linked_rotate_img.startAnimation(circleRotate);
        }
    }

    /**
     * 获取模式对应文字和图片
     */
    private ModelInfo getModelText() {
        ModelInfo info = new ModelInfo();
        switch (microWaveM509.mode) {
            case MicroWaveModel.Barbecue:
                info.string = "烧烤";
                info.res = R.mipmap.ic_device_microwave_working_barbecue;
                break;
            case MicroWaveModel.MicroWave:
                info.string = "微波";
                info.res = R.mipmap.ic_device_microwave_working_microwave;
                break;
            case MicroWaveModel.ComibineHeating:
                info.string = "组合加热";
                info.res = R.mipmap.ic_device_microwave_working_microwave_combinedheating;
                break;
            default:
                break;
        }
        return info;
    }

    /**
     * 更改 烧烤和组合火力值
     */
    private short ChangeFire(short value) {
        if (microWaveM509.mode == MicroWaveModel.Barbecue) {
            switch (microWaveM509.power) {
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
        } else if (microWaveM509.mode == MicroWaveModel.ComibineHeating) {
            switch (microWaveM509.power) {
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

    private boolean shut;

    private void backToParent() {
        if (shut) return;
        if (UIService.getInstance().getPage(PageKey.DeviceMicrowave) != null) {
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().returnHome();
            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.Guid, microWaveM509.getID());
            UIService.getInstance().postPage(PageKey.DeviceMicrowave, bundle);
        }
        shut = true;
    }

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
