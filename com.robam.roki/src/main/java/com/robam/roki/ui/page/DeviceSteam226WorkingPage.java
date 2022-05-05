package com.robam.roki.ui.page;

import android.app.ActivityManager;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamFinishEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamMode;
import com.robam.common.pojos.device.Steamoven.SteamMode026;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static com.robam.common.pojos.device.Steamoven.SteamMode026.VERGETABLE;

/**
 * Created by rent on 2016/10/14.
 */

public class DeviceSteam226WorkingPage extends BasePage {
    @InjectView(R.id.steam226_title)//标题
            TextView steam226_title;
    @InjectView(R.id.steam226_return)//返回
            ImageView steam226_return;
    @InjectView(R.id.steam226_setting)//更多
            TextView steam226_setting;
    @InjectView(R.id.steam226_working_img_pauseic1)//暂停1
            ImageView steam226_working_img_pauseic1;
    @InjectView(R.id.steam226_working_img_pauseic2)//暂停2
            ImageView steam226_working_img_pauseic2;
    @InjectView(R.id.steam226_working_tv_settemp)//设置温度
            TextView steam226_working_tv_settemp;
    @InjectView(R.id.steam226_working_tv_settime)//设置时间
            TextView steam226_working_tv_settime;
    @InjectView(R.id.steam226_working_tv_realtemp)//实时温度
            TextView steam226_working_tv_realtemp;
    @InjectView(R.id.steam226_working_tv_realtime)//实时时间
            TextView steam226_working_tv_realtime;
    @InjectView(R.id.steam226_working_fra_middle)//中间frame
            FrameLayout steam226_working_fra_middle;
    @InjectView(R.id.steam226_working_img_circle)//默认圆圈
            ImageView steam226_working_img_circle;
    @InjectView(R.id.steam226_working_ll_midcontent)//圆圈里内容
            LinearLayout steam226_working_ll_midcontent;
    @InjectView(R.id.steam226_working_tv_circleabove)//圆圈内容上半部分
            TextView steam226_working_tv_circleabove;
    @InjectView(R.id.steam226_working_img_circledown)//圆圈内容下半部分
            ImageView steam226_working_img_circledown;
    @InjectView(R.id.steam226_working_tv_circledown)//圆圈内容文字部分
            TextView steam226_working_tv_circledown;
    @InjectView(R.id.steam226_working_img_pause)//暂停背景
            ImageView steam226_working_img_pause;
    @InjectView(R.id.steam226_working_fra_light)//灯frame
            FrameLayout steam226_working_fra_light;
    @InjectView(R.id.steam226_working_img_light_circle)//灯圆圈
            ImageView steam226_working_img_light_circle;
    @InjectView(R.id.steam226_working_img_light)//灯图片
            ImageView steam226_working_img_light;
    @InjectView(R.id.steam226_working_ll_switch)//开关
            LinearLayout steam226_working_ll_switch;
    @InjectView(R.id.steam226_working_img_switch)//开关图片
            ImageView steam226_working_img_switch;
    @InjectView(R.id.steam226_working_tv_switch)//开关文字
            TextView steam226_working_tv_switch;
    @InjectView(R.id.steam226_working_view1)//线条一
            View steam226_working_view1;
    TextView steam226_working_tv_realtempdown;
    @InjectView(R.id.steam226_working_view4)//线条四
            View steam226_working_view4;
    @InjectView(R.id.steam226_working_ll_pause)
    LinearLayout steam226_working_ll_pause;

    @InjectView(R.id.steam226_working_img_finish)//完成图标
            ImageView steam226_working_img_finish;
    @InjectView(R.id.steam226_working_tv_finish)//完成文字
            TextView steam226_working_tv_finish;
    short from;//来源 1 从主目录，0从菜谱控制
    String guid;
    AbsSteamoven steam226;
    LayoutInflater inflater;
    View contentView;
    Animation circleRotate;
    IRokiDialog iRokiDialog = null;
    IRokiDialog iRokidialogAlarm = null;
    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringTempList = new ArrayList<String>();
    private IRokiDialog dialogByType;
    int signDialog = 1;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    setDeviceRunData((String) msg.obj);
                break;
                case 2:
                    setDeviceRunData((String) msg.obj);
                break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        from = bd == null ? 0 : bd.getShort("from");
        LogUtils.i("20170307","guid:"+guid+" "+from);
        steam226 = Plat.deviceService.lookupChild(guid);
        LogUtils.i("20170307","steam226:"+steam226);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_steam226_working,
                container, false);
        ButterKnife.inject(this, contentView);
        iRokidialogAlarm = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);

        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        initView();
        return contentView;
    }

    void initView() {
      /*  if (guid != null && guid.startsWith(IRokiFamily.RS226)) {
            steam226_title.setText(cx.getString(R.string.device_steam226_name));
        }else{
            steam226_title.setText(cx.getString(R.string.device_steam275_name));
        }*/
    }

    @Subscribe
    public void onEvent(SteamFinishEvent event) {
        LogUtils.i("20180316","event11::"+event.alarmId);
        if ("RS226".equals(steam226.getDt())||"HS906".equals(steam226.getDt())){
            if (event.alarmId != 0) return;
        }
       // if (event.alarmId != 0) retur
            // n;
        training_lock = true;
        steam226_working_tv_realtime.setText("0");
        stopAnimation();
        steam226_working_img_finish.setVisibility(View.VISIBLE);
        steam226_working_tv_finish.setVisibility(View.VISIBLE);
        steam226_working_img_pause.setVisibility(View.GONE);
        steam226_working_ll_midcontent.setVisibility(View.GONE);
        steam226_working_img_circle.setVisibility(View.GONE);
        if (!isRunningForeground()) {
            training_lock = false;
            return;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                back();
            }
        }, 2500);
    }

    public boolean isRunningForeground() {
        ActivityManager am = (ActivityManager) cx.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        return currentPackageName != null && currentPackageName.equals(getActivity().getPackageName());
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steam226 == null || !Objects.equal(steam226.getID(), event.device.getID()))
            return;
        if (!event.isConnected && isRunningForeground()) {
            if (closedialog!=null&&closedialog.isShow()){
                closedialog.dismiss();
            }
            if (iRokiDialog!=null&&iRokiDialog.isShow()){
                iRokiDialog.dismiss();
            }
            back();
        }
    }

    private boolean training_lock = false;

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        LogUtils.i("20180303","ev::"+event.pojo.getID()+"timer::"+timer);
        LogUtils.i("20180303","steam226::"+steam226+" "+(!Objects.equal(steam226.getID(), event.pojo.getID()))+
        " "+(timer != null)+" "+training_lock+"mm：："+(steam226 == null || !Objects.equal(steam226.getID(), event.pojo.getID()) || timer != null || training_lock));
        if (steam226 == null || !Objects.equal(steam226.getID(), event.pojo.getID()) || timer != null || training_lock)
            return;

        LogUtils.i("20180303","event-status::"+event.pojo.status);
        if (event.pojo.waterboxstate==0){
            if (iRokiDialog!=null&&iRokiDialog.isShow()){
                iRokiDialog.dismiss();
            }
        }
        switch (event.pojo.status){
            case SteamStatus.PreHeat:
            case SteamStatus.Working:
                LogUtils.i("20180303","working-preheat");
                if (iRokiDialog!=null&&iRokiDialog.isShow()){
                    iRokiDialog.dismiss();
                }
                signDialog = 0;
                setWorkMode();
                break;
            case SteamStatus.Pause:
                signDialog += 1;
                setPauseMolde();
               break;
            case SteamStatus.Order:
                setOrderMolde();
                break;
            case SteamStatus.Wait:
            case SteamStatus.Off:
            case SteamStatus.On:
                LogUtils.i("20180303","islll::"+isRunningForeground());
                if (closedialog!=null&&closedialog.isShow()){
                    closedialog.dismiss();
                }
                if (iRokiDialog!=null&&iRokiDialog.isShow()){
                    iRokiDialog.dismiss();
                }
                if (isRunningForeground())
                    back();
                break;
            case SteamStatus.AlarmStatus:
                signDialog += 1;
                setPauseMolde();
                break;
            default:
                break;
        }
        LogUtils.i("20180303","ev::ppp"+event.pojo.status+"   "+steam226.status);
      /*  if (steam226.status == SteamStatus.Working || steam226.status == SteamStatus.PreHeat) {
            signDialog = 0;
            setWorkMode();
        } else if (steam226.status == SteamStatus.Pause) {
            signDialog += 1;
            setPauseMolde();
        } else if (steam226.status == SteamStatus.Order) {
            setOrderMolde();
        } else if (steam226.status == SteamStatus.Wait || steam226.status == SteamStatus.Off || steam226.status == SteamStatus.On) {
            if (closedialog.isShow()){
                closedialog.dismiss();
            }
            LogUtils.i("20180303","ev::"+event.pojo.status);

            if (isRunningForeground())
                back();
        } else if (steam226.status == SteamStatus.AlarmStatus) {
            signDialog += 1;
            setPauseMolde();
        }*/
        if (steam226.status != SteamStatus.AlarmStatus) {
            if (iRokidialogAlarm != null && iRokidialogAlarm.isShow())
                iRokidialogAlarm.dismiss();
           // steam226_working_img_wateryeild.setImageDrawable(r.getDrawable(R.mipmap.ic_steam226_water_write));
        }
    }

    Timer timer;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return true;
    }

    @OnClick(R.id.steam226_return)
    public void OnClickReturn() {
        workingReturn();
    }

    private void workingReturn(){
        if (from==1){
            UIService.getInstance().popBack();
        }else{
            UIService.getInstance().popBack().popBack();
        }
    }

    //暂停时间调整
    @OnClick(R.id.steam226_working_ll_pause)
    public void onClickPauseSetting() {
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (checkWaterOut_Dialog()) return;
        if (steam226_working_img_pauseic1.getVisibility() == View.VISIBLE) {
            Pause_TempAndTimeSet();
        }
    }

    //点击暂停
    @OnClick(R.id.steam226_working_fra_middle)
    public void OnClickCircle() {
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (checkWaterOut_Dialog()){
            ToastUtils.show("水箱已弹出",Toast.LENGTH_SHORT);
            return;
        }
        if (steam226.status == OvenStatus.Working || steam226.status == OvenStatus.PreHeat) {
            steam226.setSteamStatus(OvenStatus.Pause, null);
        } else if (steam226.status == OvenStatus.Pause) {
            steam226.setSteamStatus(OvenStatus.Working, null);
        }
    }


    @OnClick(R.id.steam226_working_fra_light)
    public void OnClickLight() {
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if ("RS226".equals(steam226.getDt())){
            if (checkWaterOut_Dialog()) return;
        }
        if (steam226.steamLight == 1) {
            steam226.setSteamLight((short) 0, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    steam226_working_img_light_circle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
                    steam226_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_white);
                }
                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            steam226.setSteamLight((short) 1, (short) 0, new VoidCallback() {
                @Override
                public void onSuccess() {
                    steam226_working_img_light_circle.setImageResource(R.mipmap.ic_count_stove_on);
                    steam226_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_yellow);
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }

     IRokiDialog closedialog = null;
    @OnClick(R.id.steam226_working_ll_switch)
    public void onClickSwitch() {
        if (!checkConnection()) return;
//        if (!alarmStatus(steam226.alarm)) return;
        if ("RS226".equals(steam226.getDt())){
            if (checkWaterOut_Dialog()) return;
        }
        closedialog= RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (closedialog.isShow()){
                    closedialog.dismiss();
                    if (steam226.status == SteamStatus.Order) {
                        steam226.setSteamStatus(OvenStatus.Off, null);
                    } else {
                        steam226.setSteamStatus(OvenStatus.On, null);
                    }
                }
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
     * 工作模式
     */
    void setWorkMode() {
        checkWaterOut_Dialog();
        light_change();
        steam226_working_img_pauseic1.setVisibility(View.GONE);
        steam226_working_img_pauseic2.setVisibility(View.GONE);
        steam226_working_img_circle.setVisibility(View.VISIBLE);
        steam226_working_img_pause.setVisibility(View.GONE);
        steam226_working_tv_settemp.setText(steam226.tempSet + "");//设置温度
        steam226_working_tv_settime.setText(steam226.timeSet + "");//设置时间
        steam226_working_tv_realtemp.setText(steam226.temp + "");//温度
        if (steam226.time % 60 != 0)
            steam226_working_tv_realtime.setText((steam226.time / 60 + 1) + "");//时间
        else
            steam226_working_tv_realtime.setText((steam226.time / 60) + "");//时间
        startAnimation();
        steam226_working_img_circledown.setVisibility(View.VISIBLE);
        steam226_working_tv_circledown.setVisibility(View.GONE);
        if (steam226.mode==0&&steam226.status == SteamStatus.PreHeat){
            steam226_working_img_circledown.setImageResource(R.mipmap.ic_oven026work_preheating);
            steam226_working_tv_circleabove.setVisibility(View.VISIBLE);
            steam226_working_tv_circleabove.setText(cx.getString(R.string.device_preheating));
            steam226_working_fra_middle.setClickable(false);
        }else if (steam226.status == SteamStatus.PreHeat) {
            steam226_working_img_circledown.setImageResource(R.mipmap.ic_oven026work_preheating);
            steam226_working_tv_circleabove.setVisibility(View.VISIBLE);
            steam226_working_tv_circleabove.setText(cx.getString(R.string.device_preheating));
        } else {
            if (IRokiFamily.RS275.equals(steam226.getDt())){
                init275Model();
            }else{
                initModel();
            }
        }

    }


    /**
     * 暂停模式
     */
    void setPauseMolde() {
        checkWaterOut_Dialog();
        light_change();
        stopAnimation();
        steam226_working_img_pauseic1.setVisibility(View.VISIBLE);
        steam226_working_img_pauseic2.setVisibility(View.VISIBLE);
        steam226_working_img_circle.setVisibility(View.GONE);
        steam226_working_img_pause.setVisibility(View.VISIBLE);
        steam226_working_img_pause.bringToFront();
        steam226_working_tv_settemp.setText(steam226.tempSet + "");//设置温度
        steam226_working_tv_settime.setText(steam226.timeSet + "");//设置时间
        steam226_working_tv_realtemp.setText(steam226.temp + "");//温度
        if (steam226.time % 60 != 0)
            steam226_working_tv_realtime.setText((steam226.time / 60 + 1) + "");//时间
        else
            steam226_working_tv_realtime.setText((steam226.time / 60) + "");//时间
        steam226_working_img_circledown.setVisibility(View.VISIBLE);
        steam226_working_tv_circledown.setVisibility(View.GONE);
        if (steam226.mode==0&& steam226.status==SteamStatus.Pause){
            steam226_working_img_pauseic1.setVisibility(View.GONE);
            steam226_working_img_pauseic2.setVisibility(View.GONE);
        }
        if (steam226.status == SteamStatus.PreHeat) {
            steam226_working_img_circledown.setImageResource(R.mipmap.ic_oven026work_preheating);
            steam226_working_tv_circleabove.setVisibility(View.VISIBLE);
            steam226_working_tv_circleabove.setText(cx.getString(R.string.device_preheating));
        } else {
            if (IRokiFamily.RS275.equals(steam226.getDt())){
                init275Model();
            }else{
                initModel();
            }

        }
    }

    /**
     * 预约模式
     */
    void setOrderMolde() {
        checkWaterOut_Dialog();
        light_change();
        steam226_working_img_circle.setVisibility(View.VISIBLE);
        steam226_working_img_pause.setVisibility(View.GONE);
        steam226_working_tv_settemp.setText(steam226.tempSet + "");//设置温度
        steam226_working_tv_settime.setText(steam226.timeSet + "");//设置时间
        steam226_working_tv_realtemp.setText(steam226.temp + "");//温度
        if (steam226.time % 60 != 0)
            steam226_working_tv_realtime.setText((steam226.time / 60 + 1) + "");//时间
        else
            steam226_working_tv_realtime.setText((steam226.time / 60) + "");//时间
        steam226_working_img_circledown.setVisibility(View.GONE);
        steam226_working_tv_circledown.setVisibility(View.VISIBLE);
        steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steamOvenOne_order_start_time));
        if (steam226.orderTime_min < 10)
            steam226_working_tv_circledown.setText(steam226.orderTime_hour + ":0" + steam226.orderTime_min);
        else
            steam226_working_tv_circledown.setText(steam226.orderTime_hour + ":" + steam226.orderTime_min);
        startAnimation();
    }


    void light_change() {
        if (steam226.steamLight == 1) {
            steam226_working_img_light_circle.setImageResource(R.mipmap.ic_count_stove_on);
            steam226_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_yellow);
        } else {
            steam226_working_img_light_circle.setImageResource(R.mipmap.img_steamoven_circle_open_small);
            steam226_working_img_light.setImageResource(R.mipmap.ic_fan8700_light_white);
        }
    }


    void init275Model(){
        switch (steam226.mode) {
            case 3://鱼
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_work_yu);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_yulei));
                break;
            case 4://蛋
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_work_dan);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_danlei));
                break;
            case 7://蔬菜
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_work_shucai);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_shucai));
                break;

            case 11://面食
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_work275_mianshi);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_mianshi));
                break;

            case 13://强蒸
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_275_suzheng_work);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_suzheng));
                break;
            case 14://杀菌
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906steam_work_shajun_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_shajun));
                break;
            case 20://275除垢
                steam226_working_img_circledown.setImageResource(R.mipmap.device_steam_228_clean);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.clean));
                break;
            case 9://275解冻
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_906steam_work_jiedong_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.unfreeze));
                break;
            default:
                steam226_working_tv_circleabove.setVisibility(View.GONE);
                steam226_working_tv_circledown.setVisibility(View.VISIBLE);
                steam226_working_tv_circledown.setText(cx.getString(R.string.device_steam_model_zidingyi));
                steam226_working_img_circledown.setVisibility(View.GONE);
                steam226_working_fra_middle.setClickable(false);
                break;
        }
    }

    void initModel() {
        LogUtils.i("20180108","steam226:::"+steam226.mode);
        switch (steam226.mode) {
            case 3://鱼
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_fish_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_yulei));
                break;
            case 4://蛋
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_egg_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_danlei));
                break;
            case 6://蹄筋
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_tendon_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_tijin));
                break;
            case 7://蔬菜
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_vegetable_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_shucai));
                break;
            case 10://五花肉
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_meat_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_roulei));
                break;
            case 11://馒头
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_pastry_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_mianshi));
                break;
            case 12://米饭
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_rice_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_mifan));
                break;
            case 13://强蒸
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_strongsteam_working_white);
                if ("RS275".equals(steam226.getDt())){
                    steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_suzheng));
                }else{
                    steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_qianglizheng));
                }
                break;
            case 14://杀菌
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_sterilization_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.device_steam_model_shajun));
                break;
            case 20://275除垢
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_sterilization_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.clean));
                break;
            case 9://275解冻
                steam226_working_img_circledown.setImageResource(R.mipmap.ic_steam226_sterilization_working_white);
                steam226_working_tv_circleabove.setText(cx.getString(R.string.unfreeze));
                break;
            default:
                    steam226_working_tv_circleabove.setVisibility(View.GONE);
                    steam226_working_tv_circledown.setVisibility(View.VISIBLE);
                    steam226_working_tv_circledown.setText(cx.getString(R.string.device_steam_model_zidingyi));
                    steam226_working_img_circledown.setVisibility(View.GONE);
                    steam226_working_fra_middle.setClickable(false);
                break;
        }
    }

    /*设置各种模式温度范围*/
    protected List<String> getList2(int index) {
        List<String> list = Lists.newArrayList();
        if (index == SteamMode026.FISH || index == SteamMode026.EGG || index == SteamMode026.VERGETABLE ) {
            for (int i = 85; i <= 100; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (index == SteamMode026.TENDON || index == SteamMode026.MEAT|| index == SteamMode026.RICE) {
            for (int i = 90; i <= 100; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (index == SteamMode026.STEAMEDBREAD) {
            for (int i = 35; i <= 100; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (index == SteamMode026.STRONGSTEAM) {
            list.add(105+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        } else if (index == SteamMode026.STERILIZATION) {
            list.add(100+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        }
        return list;
    }

    /*设置各种模式时间范围*/
    protected List<String> getList3(int index) {
        List<String> list = Lists.newArrayList();
        if (index == SteamMode026.FISH || index == SteamMode026.EGG || index == VERGETABLE ||
                index == SteamMode026.STEAMEDBREAD||index == SteamMode026.RICE || index == SteamMode026.STRONGSTEAM
        ||index == SteamMode026.MEAT || index == SteamMode026.STERILIZATION) {
            for (int i = 5; i <= 90; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else if (index == SteamMode026.TENDON ) {
            for (int i = 5; i <= 180; i++) {
                list.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        }
        return list;
    }

    /*275设置各种模式温度范围*/
    protected List<String> getList4(int index) {
        List<String> list = Lists.newArrayList();
        if (index == SteamMode026.FISH || index == SteamMode026.EGG || index == SteamMode026.VERGETABLE ) {
            for (int i = 85; i <= 100; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if ( index == SteamMode026.Clean) {
            for (int i = 90; i <= 100; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (index == SteamMode026.STEAMEDBREAD) {
            for (int i = 35; i <= 100; i++) {
                list.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (index == SteamMode026.STRONGSTEAM) {
            list.add(105+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        } else if (index == SteamMode026.STERILIZATION) {
            list.add(100+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        } else if (index == SteamMode026.UNFREESE){
            for (int i = 55; i <=65 ; i++) {
                list.add(i+StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }
        return list;
    }

    /*275设置各种模式时间范围*/
    protected List<String> getList5(int index) {
        List<String> list = Lists.newArrayList();
        if (index == SteamMode026.FISH || index == SteamMode026.EGG || index == VERGETABLE ||
                index == SteamMode026.STEAMEDBREAD || index == SteamMode026.STERILIZATION
                 || index == SteamMode026.STERILIZATION) {
            for (int i = 1; i <= 90; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else if (index == SteamMode026.STRONGSTEAM) {
            for (int i = 1; i <= 90; i++) {
                list.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        }else if (index == SteamMode026.UNFREESE||index == SteamMode026.Clean){
            for (int i = 1; i <=90; i++) {
                list.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        }
        return list;
    }

    public void initModeData(List<String> list1, int index1, List<String> list2, int index2) {
        if (list1 == null || list2 == null) return;
        iRokiDialog.setWheelViewData(list1, null, list2, false, index1, 0, index2, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                Message msg = mHandler.obtainMessage();
                msg.obj = contentFront;
                msg.what = 1;
                mHandler.sendMessage(msg);

            }
        },null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message msg = mHandler.obtainMessage();
                msg.obj = contentRear;
                msg.what = 2;
                mHandler.sendMessage(msg);
            }

        });

        iRokiDialog.setCanceledOnTouchOutside(true);
        iRokiDialog.show();
    }

    /**
     * 设置运行数据
     * @param str
     */
    private void setDeviceRunData(String str) {
        if (str.contains(StringConstantsUtil.STRING_DEGREE_CENTIGRADE)){
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(str);
            stringTempList.add(removetTempString);
        }
        if (str.contains(StringConstantsUtil.STRING_MINUTES)){
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(str);
            stringTimeList.add(removeTimeString);
        }

        iRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRokiDialog.isShow()){
                    iRokiDialog.dismiss();
                    short n = (short) 0;
                    steam226.setSteamCookMode(steam226.mode, Short.valueOf(stringTempList.get(stringTempList.size()-1)),
                            Short.valueOf(stringTimeList.get(stringTimeList.size()-1)), n, n, n, n, n, n, n, n, new VoidCallback() {
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


    }

    private void Pause_TempAndTimeSet() {

        if (iRokidialogAlarm != null && iRokidialogAlarm.isShow()) {
            iRokidialogAlarm.dismiss();
        }

        iRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
       if ("RS275".equals(steam226.getDt())){
           setParamChangeS275();
       }else{
           setParamChange();
       }

    }

    private void setParamChange(){
        switch (steam226.mode) {
            case SteamMode026.FISH://鱼
                initModeData(getList2(SteamMode026.FISH), 10,getList3(SteamMode026.FISH), 7);
                break;
            case SteamMode026.EGG://蛋
                initModeData(getList2(SteamMode026.EGG), 15,getList3(SteamMode026.EGG), 10);
                break;
            case SteamMode026.TENDON://蹄筋
                initModeData(getList2(SteamMode026.TENDON), 10,getList3(SteamMode026.TENDON), 40);
                break;
            case VERGETABLE://蔬菜
                initModeData(getList2(VERGETABLE), 15,getList3(VERGETABLE), 5);
                break;
            case SteamMode026.MEAT://五花肉
                initModeData(getList2(SteamMode026.MEAT), 10,getList3(SteamMode026.MEAT), 15);
                break;
            case SteamMode026.STEAMEDBREAD://馒头
                initModeData(getList2(SteamMode026.STEAMEDBREAD), 65,getList3(SteamMode026.STEAMEDBREAD), 20);
                break;
            case SteamMode026.RICE://米饭
                initModeData(getList2(SteamMode026.RICE), 10,getList3(SteamMode026.RICE), 25);
                break;
            case SteamMode026.STRONGSTEAM://强蒸
                initModeData(getList2(SteamMode026.STRONGSTEAM), 0,getList3(SteamMode026.STRONGSTEAM), 30);
                break;
            case SteamMode026.STERILIZATION://杀菌
                initModeData(getList2(SteamMode026.STERILIZATION), 0,getList3(SteamMode026.STERILIZATION), 25);

            default:
                break;
        }
    }

    private void setParamChangeS275(){
        switch (steam226.mode) {
            case SteamMode026.FISH://鱼
                initModeData(getList4(SteamMode026.FISH), 10,getList5(SteamMode026.FISH), 11);
                break;
            case SteamMode026.EGG://蛋
                initModeData(getList4(SteamMode026.EGG), 15,getList5(SteamMode026.EGG), 14);
                break;
            case VERGETABLE://蔬菜
                initModeData(getList4(VERGETABLE), 15,getList5(VERGETABLE), 9);
                break;
            case SteamMode026.STEAMEDBREAD://馒头
                initModeData(getList4(SteamMode026.STEAMEDBREAD), 65,getList5(SteamMode026.STEAMEDBREAD), 24);
                break;
            case SteamMode026.STRONGSTEAM://强蒸
                initModeData(getList4(SteamMode026.STRONGSTEAM), 0,getList5(SteamMode026.STRONGSTEAM), 9);
                break;
            case SteamMode026.UNFREESE://解冻
                initModeData(getList4(SteamMode026.UNFREESE), 0,getList5(SteamMode026.UNFREESE), 29);
                break;
            case SteamMode026.Clean://除垢
                initModeData(getList4(SteamMode026.Clean), 10,getList5(SteamMode026.Clean), 19);
                break;
            case SteamMode026.STERILIZATION://杀菌
                initModeData(getList4(SteamMode026.STERILIZATION), 0,getList5(SteamMode026.STERILIZATION), 29);
                break;
            default:
                break;
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
            steam226_working_img_circle.startAnimation(circleRotate);
        }
    }

    /**
     * 关闭动画和烟雾效果
     */
    private void stopAnimation() {
        if (circleRotate != null)
            circleRotate.cancel();
        circleRotate = null;
        steam226_working_img_circle.clearAnimation();
    }

    private void back() {
        if (from == 1) {
          //  UIService.getInstance().returnHome();
           /* Bundle bd = new Bundle();
            bd.putString(PageArgumentKey.Guid, steam226.getID());
            UIService.getInstance().postPage(PageKey.DeviceSteam226, bd);*/
            UIService.getInstance().popBack();
        } else {
            UIService.getInstance().popBack();
        }
    }

    /**
     * 报警
     **/
    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        alarmStatus(event.alarmId);
    }

    private boolean alarmStatus(short type) {
        boolean cause = false;
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
                ToastUtils.show("水箱缺水",Toast.LENGTH_SHORT);
                break;
            case AbsSteamoven.Event_Steam226_Alarm_heat:
                break;
            case AbsSteamoven.Event_Steam226_Alarm_sensor:
                break;
            case AbsSteamoven.Event_Steam226_Alarm_communication_fault:
                break;
            default:
                cause = true;
                break;
        }
        return cause;
    }

    public void closeAllDialog() {
        if (iRokidialogAlarm != null && iRokidialogAlarm.isShow())
            iRokidialogAlarm.dismiss();
    }
    boolean checkWaterOut_Dialog() {
        if (steam226.waterboxstate == 1) {
            if (dialogByType != null && dialogByType.isShow()) {
                dialogByType.dismiss();
            }
            return false;
        }else if (steam226.waterboxstate == 0 && signDialog == 1){
            dialogByType.setContentText(R.string.device_alarm_water_out);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
            dialogByType.show();
            signDialog = 1;
            return true;
        }
        return true;
    }

    private boolean checkConnection() {
        if (steam226.isConnected()) {
            return true;
        } else {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeAllDialog();
    }

}
