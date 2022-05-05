package com.robam.roki.ui.page;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.events.OvenWorkFinishEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenMode;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Oven026PauseSettingDialog;
import com.robam.roki.ui.dialog.Oven075PauseSettingDialog;
import com.robam.roki.ui.dialog.OvenBroken026Dialog;
import com.robam.roki.ui.dialog.OvenCancelWorkDialog;
import com.robam.roki.utils.DialogUtil;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by rent on 2016/10/14.
 */

public class DeviceOven026WorkingPage extends BasePage {
    @InjectView(R.id.oven026_return)//返回
            ImageView oven026_return;
    @InjectView(R.id.oven026_setting)//更多
            TextView oven026_setting;
    @InjectView(R.id.oven026_working_img_pauseic1)
    ImageView oven026_working_img_pauseic1;
    @InjectView(R.id.oven026_working_img_pauseic2)
    ImageView oven026_working_img_pauseic2;
    @InjectView(R.id.oven026_working_img_pauseic3)
    ImageView oven026_working_img_pauseic3;
    @InjectView(R.id.oven026_working_tv_settemp)//设置温度
            TextView oven026_working_tv_settemp;
    @InjectView(R.id.oven026_working_tv_settemp_text)//温度文字
            TextView oven026_working_tv_settemp_text;
    @InjectView(R.id.oven026_working_tv_settime)//设置时间
            TextView oven026_working_tv_settime;
    @InjectView(R.id.oven026_working_tv_realtemp)//实时温度
            TextView oven026_working_tv_realtemp;
    @InjectView(R.id.oven026_working_tv_realtime)//实时时间
            TextView oven026_working_tv_realtime;
    @InjectView(R.id.oven026_working_fra_middle)//中间frame
            FrameLayout oven026_working_fra_middle;
    @InjectView(R.id.oven026_working_img_circle)//默认圆圈
            ImageView oven026_working_img_circle;
    @InjectView(R.id.oven026_working_ll_midcontent)//圆圈里内容
            LinearLayout oven026_working_ll_midcontent;
    @InjectView(R.id.oven026_working_tv_circleabove)//圆圈内容上半部分
            TextView oven026_working_tv_circleabove;
    @InjectView(R.id.oven026_working_img_circledown)//圆圈内容下半部分
            ImageView oven026_working_img_circledown;
    @InjectView(R.id.oven026_working_tv_circledown)//圆圈内容文字部分
            TextView oven026_working_tv_circledown;
    @InjectView(R.id.oven026_working_img_pause)//暂停背景
            ImageView oven026_working_img_pause;
    @InjectView(R.id.oven026_working_fra_light)//灯frame
            FrameLayout oven026_working_fra_light;
    @InjectView(R.id.oven026_working_img_light_circle)//灯圆圈
            ImageView oven026_working_img_light_circle;
    @InjectView(R.id.oven026_working_img_light)//灯图片
            ImageView oven026_working_img_light;
    @InjectView(R.id.oven026_working_fra_rotate)//旋转frame
            FrameLayout oven026_working_fra_rotate;
    @InjectView(R.id.oven026_working_img_rotate_circle)//旋转圆圈
            ImageView oven026_working_img_rotate_circle;
    @InjectView(R.id.oven026_working_img_rotate)//旋转图
            ImageView oven026_working_img_rotate;
    @InjectView(R.id.oven026_working_ll_switch)//开关
            LinearLayout oven026_working_ll_switch;
    @InjectView(R.id.oven026_working_img_switch)//开关图片
            ImageView oven026_working_img_switch;
    @InjectView(R.id.oven026_working_tv_switch)//开关文字
            TextView oven026_working_tv_switch;
    @InjectView(R.id.oven026_working_view1)//线条一
            View oven026_working_view1;
    @InjectView(R.id.oven026_working_fram_settempdown)//设置下温度linear
            FrameLayout oven026_working_fram_settempdown;
    @InjectView(R.id.oven026_working_tv_settempdown)//设置textview
            TextView oven026_working_tv_settempdown;
    @InjectView(R.id.oven026_working_view2)
    View oven026_working_view2;
    @InjectView(R.id.oven026_working_ll_realtempdown)
    LinearLayout oven026_working_ll_realtempdown;
    @InjectView(R.id.oven026_working_tv_realtempdown)
    TextView oven026_working_tv_realtempdown;
    @InjectView(R.id.oven026_working_view3)//线条三
            View oven026_working_view3;
    @InjectView(R.id.oven026_working_view4)//线条四
            View oven026_working_view4;
    @InjectView(R.id.oven026_working_ll_pause)
    LinearLayout oven026_working_ll_pause;

    @InjectView(R.id.oven026_working_img_finish)//完成图标
            ImageView oven026_working_img_finish;
    @InjectView(R.id.oven026_working_tv_finish)//完成文字
            TextView oven026_working_tv_finish;
    short from;//来源 1 从主目录，0从菜谱控制
    String guid;
    AbsOven oven026;
    LayoutInflater inflater;
    View contentView;

    Animation circleRotate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? 0 : bd.getShort("from");

        oven026 = Plat.deviceService.lookupChild(guid);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_oven026_working,
                container, false);
        ButterKnife.inject(this, contentView);
        init();
        return contentView;
    }

    private void init() {
        if (guid.startsWith("RR016") || guid.startsWith("RR026") || guid.startsWith("RR075") || guid.startsWith("HK906"))
            oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    public void onEvent(OvenWorkFinishEvent event) {
        if (event.finish != 0) return;
        training_lock = true;
        oven026_working_tv_realtime.setText("0");
        stopAnimation();
        oven026_working_img_finish.setVisibility(View.VISIBLE);
        oven026_working_tv_finish.setVisibility(View.VISIBLE);
        oven026_working_img_pause.setVisibility(View.GONE);
        oven026_working_ll_midcontent.setVisibility(View.GONE);
        oven026_working_img_circle.setVisibility(View.GONE);
        if (!isRunningForeground()) {
            training_lock = false;
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    back();
                } catch (Exception e) {
                    LogUtils.i("20161109", "12312312");
                    e.printStackTrace();
                }
            }
        }, 2000);
    }

    public boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) cx.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return currentPackageName != null && currentPackageName.equals(getActivity().getPackageName());
    }

    private void workingReturn() {
        if (from == 1) {
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().popBack().popBack();
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (oven026 == null || !Objects.equal(oven026.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            if (isRunningForeground()) {
                if (closedialog != null && closedialog.isShow()) {
                    closedialog.dismiss();
                }
                if (oven075PauseSettingDialog != null && oven075PauseSettingDialog.isShowing()) {
                    oven075PauseSettingDialog.dismiss();
                }
                back();
            }

        }
    }

    private boolean training_lock = false;

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {

        if (oven026 == null || !Objects.equal(oven026.getID(), event.pojo.getID()) || timer != null || training_lock)
            return;
        if (oven026.status == OvenStatus.PreHeat) {
            if (oven075PauseSettingDialog != null && oven075PauseSettingDialog.isShowing()) {
                oven075PauseSettingDialog.dismiss();
            }
            setPreHeatMode();
        } else if (oven026.status == OvenStatus.Working) {
            if (oven075PauseSettingDialog != null && oven075PauseSettingDialog.isShowing()) {
                oven075PauseSettingDialog.dismiss();
            }
            setWorkMode();
        } else if (oven026.status == OvenStatus.Pause) {
            setPauseMolde();
        } else if (oven026.status == OvenStatus.Order) {
            setOrderMolde();
        } else if (oven026.status == OvenStatus.Wait || oven026.status == OvenStatus.Off || oven026.status == OvenStatus.On) {
            if (closedialog != null && closedialog.isShow()) {
                closedialog.dismiss();
            }
            if (oven075PauseSettingDialog != null && oven075PauseSettingDialog.isShowing()) {
                oven075PauseSettingDialog.dismiss();
            }
            if (isRunningForeground())
                back();
        } else if (oven026.status == OvenStatus.AlarmStatus) {
            //setPauseMolde();
            if (oven075PauseSettingDialog != null && oven075PauseSettingDialog.isShowing()) {
                oven075PauseSettingDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }

    }

    Timer timer;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return true;
    }

    @OnClick(R.id.oven026_return)
    public void OnClickReturn() {
        workingReturn();
    }


    @OnClick(R.id.oven026_working_ll_pause)
    public void onClickPauseSetting() {
        if ("RR075".equals(oven026.getDt())) {
            if (oven026.alarm != 255)
                return;
        }
        if (oven026_working_img_pauseic1.getVisibility() == View.VISIBLE) {
            if (oven026.autoMode == 0)
                Pause_TempAndTimeSet();
            else
                ToastUtils.show(R.string.device_steamOvenOne_name_model_not_content, Toast.LENGTH_SHORT);
        }
    }

    //暂停
    @OnClick(R.id.oven026_working_fra_middle)
    public void OnClickCircle() {

        if (oven026.status == OvenStatus.Working || oven026.status == OvenStatus.PreHeat) {
            oven026.setOvenStatus(OvenStatus.Pause, null);
        } else if (oven026.status == OvenStatus.Pause) {
            oven026.setOvenStatus(OvenStatus.Working, null);
        }
    }

    //灯的点击事件
    @OnClick(R.id.oven026_working_fra_light)
    public void OnClickLight() {
        if ("RR075".equals(oven026.getDt())) {
            if (oven026.alarm != 255) {
                return;
            }
        }

        if (oven026.light == 1) {
            oven026.setLightControl((short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    oven026_working_img_light_circle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
                    oven026_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_white);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            oven026.setLightControl((short) 1, new VoidCallback() {
                @Override
                public void onSuccess() {
                    oven026_working_img_light_circle.setImageResource(R.mipmap.ic_count_stove_on);
                    oven026_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_yellow);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

    @OnClick(R.id.oven026_working_fra_rotate)
    public void OnClickRotate() {
        if ("RR075".equals(oven026.getDt())) {
            if (oven026.alarm != 255) {
                return;
            }
            if (oven026.status == OvenStatus.Pause) {
                ToastUtils.show("不能操作", Toast.LENGTH_SHORT);
                return;
            }
        }
        LogUtils.out("OnClickRotate;;;" + oven026.revolve);
        if (oven026.revolve == 1) {
            oven026.setOvenSpitRotateLightControl((short) 0, oven026.light, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    oven026_working_img_rotate_circle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
                    oven026_working_img_rotate.setImageResource(R.mipmap.ic_oven026work_rotate_white);
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        } else {
            oven026.setOvenSpitRotateLightControl((short) 1, oven026.light, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    oven026_working_img_rotate_circle.setImageResource(R.mipmap.ic_count_stove_on);
                    oven026_working_img_rotate.setImageResource(R.mipmap.ic_oven026work_rotate_yellow);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

    IRokiDialog closedialog = null;

    //结束工作
    @OnClick(R.id.oven026_working_ll_switch)
    public void onClickSwitch() {
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                    if (oven026.status == OvenStatus.Order) {
                        oven026.setOvenStatus(OvenStatus.Off, null);
                    } else if (oven026.status == OvenStatus.AlarmStatus) {
                        oven026.setOvenStatus(OvenStatus.Off, null);
                    } else if (oven026.status == OvenStatus.Off) {

                    } else {
                        oven026.setOvenStatus(OvenStatus.On, null);
                    }
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

    /**
     * 预热模式
     */
    void setPreHeatMode() {
        TempTimeAreaChange();
        light_change();
        rotate_change();
        if (oven026PauseSettingDialog != null && oven026PauseSettingDialog.isShowing()) {//暂停弹出框取消
            oven026PauseSettingDialog.dismiss();
            oven026PauseSettingDialog = null;
        }
        oven026_working_img_pauseic1.setVisibility(View.GONE);
        oven026_working_img_pauseic2.setVisibility(View.GONE);
        oven026_working_img_pauseic3.setVisibility(View.GONE);

        oven026_working_img_circle.setVisibility(View.VISIBLE);
        oven026_working_img_pause.setVisibility(View.GONE);
        oven026_working_tv_settemp.setText(oven026.setTemp + "");//设置温度
        oven026_working_tv_settime.setText(oven026.setTime + "");//设置时间
        oven026_working_tv_realtemp.setText(oven026.temp + "");//温度
        if (oven026.time % 60 != 0)
            oven026_working_tv_realtime.setText((oven026.time / 60 + 1) + "");//时间
        else
            oven026_working_tv_realtime.setText((oven026.time / 60) + "");//时间
        startAnimation();
        oven026_working_img_circledown.setVisibility(View.VISIBLE);
        oven026_working_tv_circledown.setVisibility(View.GONE);
        oven026_working_img_circledown.setImageResource(R.mipmap.ic_oven026work_preheating);
        oven026_working_tv_circleabove.setText(cx.getString(R.string.device_preheating));
        oven026_working_img_rotate.setVisibility(View.VISIBLE);
        LogUtils.i("20180514", "guid：" + guid);
        if (oven026.runP == 9 || oven026.autoMode == 1 || oven026.autoMode == 2 || oven026.autoMode == 3 || oven026.autoMode == 4
                || oven026.autoMode == 5 || oven026.autoMode == 6 || oven026.autoMode == 7 || oven026.autoMode == 8 || oven026.autoMode == 9
                || oven026.autoMode == 10 || oven026.autoMode == 11 || oven026.autoMode == 12 || guid.startsWith("RR016") || guid.startsWith("HK906")) {
            oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
        } else {
            oven026_working_fra_rotate.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 工作模式
     */
    void setWorkMode() {
        TempTimeAreaChange();
        light_change();
        rotate_change();
        if (oven026PauseSettingDialog != null && oven026PauseSettingDialog.isShowing()) {//暂停弹出框取消
            oven026PauseSettingDialog.dismiss();
            oven026PauseSettingDialog = null;
        }
        oven026_working_img_pauseic1.setVisibility(View.GONE);
        oven026_working_img_pauseic2.setVisibility(View.GONE);
        oven026_working_img_pauseic3.setVisibility(View.GONE);

        oven026_working_img_circle.setVisibility(View.VISIBLE);
        oven026_working_img_pause.setVisibility(View.GONE);
        oven026_working_tv_settemp.setText(oven026.setTemp + "");//设置温度
        oven026_working_tv_settime.setText(oven026.setTime + "");//设置时间
        oven026_working_tv_realtemp.setText(oven026.temp + "");//温度
        if (oven026.time % 60 != 0)
            oven026_working_tv_realtime.setText((oven026.time / 60 + 1) + "");//时间
        else
            oven026_working_tv_realtime.setText((oven026.time / 60) + "");//时间
        startAnimation();
        oven026_working_img_rotate.setVisibility(View.VISIBLE);
        oven026_working_img_circledown.setVisibility(View.VISIBLE);
        oven026_working_tv_circledown.setVisibility(View.GONE);
        if (oven026.autoMode != OvenMode.None) {
            initAutoModel();
        } else {
            if (IRokiFamily.RR075.equals(oven026.getDt())) {
                initEXP075Model();
            } else {
                initExpModel();
            }
        }
    }


    /**
     * 暂停模式
     */
    void setPauseMolde() {
        TempTimeAreaChange();
        light_change();
        rotate_change();
        stopAnimation();
        oven026_working_img_pauseic1.setVisibility(View.VISIBLE);
        oven026_working_img_pauseic2.setVisibility(View.VISIBLE);
        oven026_working_img_pauseic3.setVisibility(View.VISIBLE);

        oven026_working_img_circle.setVisibility(View.GONE);
        oven026_working_img_pause.setVisibility(View.VISIBLE);
        oven026_working_tv_settemp.setText(oven026.setTemp + "");//设置温度
        oven026_working_tv_settime.setText(oven026.setTime + "");//设置时间
        oven026_working_tv_realtemp.setText(oven026.temp + "");//温度
        if (oven026.time % 60 != 0)
            oven026_working_tv_realtime.setText((oven026.time / 60 + 1) + "");//时间
        else
            oven026_working_tv_realtime.setText((oven026.time / 60) + "");//时间
        oven026_working_img_circledown.setVisibility(View.VISIBLE);
        oven026_working_tv_circledown.setVisibility(View.GONE);
        if (oven026.autoMode != 0) {
            initAutoModel();
        } else {
            if (IRokiFamily.RR075.equals(oven026.getDt())) {
                oven026_working_img_rotate.setVisibility(View.INVISIBLE);
                oven026_working_img_rotate_circle.setImageResource(R.mipmap.img_oven028_circle_kaocha);
                initEXP075Model();
            } else {
                oven026_working_img_rotate.setVisibility(View.VISIBLE);
                initExpModel();
            }
        }
    }

    /**
     * 预约模式
     */
    void setOrderMolde() {
        TempTimeAreaChange();
        light_change();
        rotate_change();
        if (oven026PauseSettingDialog != null && oven026PauseSettingDialog.isShowing()) {//暂停弹出框取消
            oven026PauseSettingDialog.dismiss();
            oven026PauseSettingDialog = null;
        }
        oven026_working_img_circle.setVisibility(View.VISIBLE);
        oven026_working_img_pause.setVisibility(View.GONE);
        oven026_working_tv_settemp.setText(oven026.setTemp + "");//设置温度
        oven026_working_tv_settime.setText(oven026.setTime + "");//设置时间
        oven026_working_tv_realtemp.setText(oven026.temp + "");//温度
        if (oven026.time % 60 != 0)
            oven026_working_tv_realtime.setText((oven026.time / 60 + 1) + "");//时间
        else
            oven026_working_tv_realtime.setText((oven026.time / 60) + "");//时间
        oven026_working_img_circledown.setVisibility(View.GONE);
        oven026_working_tv_circledown.setVisibility(View.VISIBLE);
        oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_order_start_time));
        if (oven026.orderTime_min < 10)
            oven026_working_tv_circledown.setText(oven026.orderTime_hour + ":0" + oven026.orderTime_min);
        else
            oven026_working_tv_circledown.setText(oven026.orderTime_hour + ":" + oven026.orderTime_min);

        if (oven026.runP == 9 || oven026.autoMode == 1 || oven026.autoMode == 2 || oven026.autoMode == 3 || oven026.autoMode == 4
                || oven026.autoMode == 5 || oven026.autoMode == 6 || oven026.autoMode == 7 || oven026.autoMode == 8 || oven026.autoMode == 9
                || oven026.autoMode == 10 || oven026.autoMode == 11 || oven026.autoMode == 12 || guid.startsWith("RR016")) {
            oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
        } else {
            oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
        }
        startAnimation();
    }


    void light_change() {
        if (oven026.light == 1) {
            oven026_working_img_light_circle.setImageResource(R.mipmap.ic_count_stove_on);
            oven026_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_yellow);
        } else {
            oven026_working_img_light_circle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            oven026_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_white);
        }
    }

    void rotate_change() {
        if (oven026.revolve == 1) {
            oven026_working_img_rotate_circle.setImageResource(R.mipmap.ic_count_stove_on);
            oven026_working_img_rotate.setImageResource(R.mipmap.ic_oven026work_rotate_yellow);
        } else {
            oven026_working_img_rotate_circle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            oven026_working_img_rotate.setImageResource(R.mipmap.ic_oven026work_rotate_white);
        }
    }

    void TempTimeAreaChange() {
        if (oven026.autoMode == OvenMode.None && oven026.runP == OvenMode.EXP) {//叛党当前是否为exp模式
            oven026_working_tv_settemp_text.setText(cx.getString(R.string.device_steamOvenOne_up_temp_text));
            oven026_working_view1.setVisibility(View.VISIBLE);
            oven026_working_fram_settempdown.setVisibility(View.VISIBLE);
            oven026_working_tv_settempdown.setText(oven026.setTempDownValue + "");
            oven026_working_view2.setVisibility(View.VISIBLE);
            oven026_working_ll_realtempdown.setVisibility(View.VISIBLE);
            oven026_working_tv_realtempdown.setText(oven026.currentTempDownValue + "");
            oven026_working_view3.setVisibility(View.GONE);
            oven026_working_view4.setVisibility(View.VISIBLE);
        } else {
            oven026_working_tv_settemp_text.setText(cx.getString(R.string.temp));
            oven026_working_view1.setVisibility(View.GONE);
            oven026_working_fram_settempdown.setVisibility(View.GONE);
            oven026_working_view2.setVisibility(View.GONE);
            oven026_working_ll_realtempdown.setVisibility(View.GONE);
            oven026_working_view3.setVisibility(View.VISIBLE);
            oven026_working_view4.setVisibility(View.GONE);
        }
    }


    void initEXP075Model() {
        switch (oven026.runP) {
            case 1://快热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fastheat_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kuaire));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 2://风焙烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fengbeikao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_fengpeikao));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 3://焙烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_beikao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_peikao));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 4://底加热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_dijiare_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_dijiare));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 6://风扇考
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_fengshankao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_fengshankao));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 7://烤烧
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_shaokao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kaoshao));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 8://强烤烧
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_qiangshaokao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao));
                oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 9://EXP
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_028ovenwork_exp_white);
                oven026_working_tv_circleabove.setText("专家模式");
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            default:
                break;

        }
    }

    void initExpModel() {
        switch (oven026.runP) {
            case 1://快热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_kuaire_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kuaire));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 2://风焙烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_fengbeikao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_fengpeikao));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 3://焙烤
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_beikao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_peikao));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 4://底加热
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_dijiare_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_dijiare));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 6://风扇考
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_fengshankao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_fengshankao));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 7://烤烧
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_shaokao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_kaoshao));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 8://强烤烧
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_qiangkaoshao_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_name_qiangshaokao));
                if (guid.startsWith("RR016")||guid.contains("HK906"))
                    oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                else
                    oven026_working_fra_rotate.setVisibility(View.VISIBLE);
                break;
            case 9://EXP
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_zhuanjia_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_experts));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;

        }
    }

    void initAutoModel() {
        switch (oven026.autoMode) {
            case 1://烤牛排
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_beef_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_niupai));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 2://烤面包
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_bread_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_mianbao));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 3://烤饼干
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_biscuits_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_binggan));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 4://烤鸡翅
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_chicken_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_jichi));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 5://烤蛋糕
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_cake_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_dangao));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 6://烤披萨
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_pizza_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_pisa));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 7://强虾
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_shrimp_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_xia));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 8://烤鱼
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_fish_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_yu));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 9://烤红薯
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_sweetpotato_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_hongshu));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 10://烤玉米
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_corn_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_yumi));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 11://烤五花肉
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_wuhuarou_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_oven_model_wuhuarou));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
            case 12://烤蔬菜
                oven026_working_img_circledown.setImageResource(R.mipmap.ic_026ovenwork_vegetables_white);
                oven026_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_shucai));
                oven026_working_fra_rotate.setVisibility(View.INVISIBLE);
                break;
        }
    }

    Oven026PauseSettingDialog oven026PauseSettingDialog;
    Oven075PauseSettingDialog oven075PauseSettingDialog;

    //时间调节
    private void Pause_TempAndTimeSet() {
        if (IRokiFamily.RR075.equals(oven026.getDt())) {
            oven075PauseSettingDialog = new Oven075PauseSettingDialog(cx, oven026.autoMode, oven026.runP, new Oven075PauseSettingDialog.PickListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm(Integer uptemp, Integer downtemp, Integer min) {
                    if (oven026.autoMode != 0) {
                        oven026.setOvenAutoRunMode(oven026.autoMode, Short.valueOf(String.valueOf(min)), null);
                    } else {
                        if (oven026.runP == OvenMode.EXP) {
                            oven026.setOvenRunMode(OvenMode.EXP, Short.valueOf(String.valueOf(min)), Short.valueOf(String.valueOf(uptemp)), (short) 0, (short) 0, (short) 0, (short) 1, Short.valueOf(String.valueOf(downtemp)), null);
                        } else {
                            oven026.setOvenRunMode(oven026.runP, Short.valueOf(String.valueOf(min)), Short.valueOf(String.valueOf(uptemp)), (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, null);
                        }
                    }
                }
            });
            Window win = oven075PauseSettingDialog.getWindow();
            win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
            WindowManager.LayoutParams wl = win.getAttributes();
            wl.width = WindowManager.LayoutParams.MATCH_PARENT;
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(wl);
            oven075PauseSettingDialog.show();
        } else {
            oven026PauseSettingDialog = new Oven026PauseSettingDialog(cx, oven026.autoMode, oven026.runP, new Oven026PauseSettingDialog.PickListener() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onConfirm(Integer uptemp, Integer downtemp, Integer min) {
                    if (oven026.autoMode != 0) {
                        oven026.setOvenAutoRunMode(oven026.autoMode, Short.valueOf(String.valueOf(min)), null);
                    } else {
                        if (oven026.runP == OvenMode.EXP) {
                            oven026.setOvenRunMode(OvenMode.EXP, Short.valueOf(String.valueOf(min)), Short.valueOf(String.valueOf(uptemp)), (short) 0, (short) 0, (short) 0, (short) 1, Short.valueOf(String.valueOf(downtemp)), null);
                        } else {
                            oven026.setOvenRunMode(oven026.runP, Short.valueOf(String.valueOf(min)), Short.valueOf(String.valueOf(uptemp)), (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, null);
                        }
                    }
                }
            });
            Window win = oven026PauseSettingDialog.getWindow();
            win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
            WindowManager.LayoutParams wl = win.getAttributes();
            wl.width = WindowManager.LayoutParams.MATCH_PARENT;
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(wl);
            oven026PauseSettingDialog.show();
        }


    }


    /**
     * 开启动画
     */
    void startAnimation() {
        if (circleRotate == null) {
            circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_oven_circle_rotate);
            LinearInterpolator lin = new LinearInterpolator();
            circleRotate.setInterpolator(lin);
            oven026_working_img_circle.startAnimation(circleRotate);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotate != null)
            circleRotate.cancel();
        circleRotate = null;
        oven026_working_img_circle.clearAnimation();
    }


    @Override
    public void onStart() {
        super.onStart();
        LogUtils.i("20161109", "onStart onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("20161109", "onResume onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i("20161109", "onPause onPause");
    }

    @Override
    public void onDestroyView() {
        LogUtils.i("20161109", "onDestroyView onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.i("20161109", "onDestroy onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtils.i("20161109", "onStop onStop");
        if (oven026PauseSettingDialog != null && oven026PauseSettingDialog.isShowing()) {//暂停弹出框取消
            oven026PauseSettingDialog.dismiss();
            oven026PauseSettingDialog = null;
        }
    }

    private void back() {
        if (from == 1) {
            // UIService.getInstance().returnHome();
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().popBack();
        }
    }
}
