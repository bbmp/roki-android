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
import android.widget.RelativeLayout;
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
import com.robam.common.events.SteamWaterBoxEvent;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamMode;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Steam226PauseSettingDialog;
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

/**
 * Created by yinwei on 2017/9/5.
 */

public class AbsSteamWorkPage extends BasePage {
    @InjectView(R.id.title2288)//标题
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
            RelativeLayout steam226_working_ll_midcontent;
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
    @InjectView(R.id.steam228_working_img_light_circle)//灯圆圈
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
    @InjectView(R.id.steam228_opentank)
            ImageView steam228_opentank;
    short from;//来源 1 从主目录，0从菜谱控制
    String guid;
    AbsSteamoven steam226;
    LayoutInflater inflater;
    View contentView;

    Animation circleRotate;
    public IRokiDialog dialogByType;
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
        LogUtils.i("20170307","guid:"+guid+"from:"+from);
        steam226 = Plat.deviceService.lookupChild(guid);
        LogUtils.i("20170307","steam226:"+steam226);
        if (inflater == null) {
            inflater = LayoutInflater.from(cx);
        }
        this.inflater = inflater;
        contentView = inflater.inflate(R.layout.page_device_steam228_working, container, false);
        ButterKnife.inject(this, contentView);
        initView();
        dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        return contentView;
    }

    public void initView() {

    }

    //工作完成的时候的状态
    @Subscribe
    public void onEvent(SteamFinishEvent event) {

      //  if (event.alarmId != 0) return;
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

    @Subscribe
    public void onEvent(SteamWaterBoxEvent event){
        ToastUtils.show("水箱已弹出",Toast.LENGTH_SHORT);
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
            if (steam226PauseSettingDialog!=null&&steam226PauseSettingDialog.isShowing()){
                steam226PauseSettingDialog.dismiss();
            }
            if (iRokiDialog!=null&&iRokiDialog.isShow()){
                iRokiDialog.dismiss();
            }
            back();
        }
    }

    public boolean training_lock = false;



    Timer timer;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        workingReturn();
        return true;
    }

    @OnClick(R.id.steam226_return)
    public void OnClickReturn() {
        workingReturn();
    }


    //暂停时间调节
    @OnClick(R.id.steam226_working_ll_pause)
    public void onClickPauseSetting() {
        if (!checkConnection()) return;
        if (steam226.alarm!=0) return;
        if (checkWaterOut_Dialog()) return;
        if (steam226_working_img_pauseic1.getVisibility() == View.VISIBLE) {
            if (steam226.recipeId!=0){
                ToastUtils.show("菜谱模式不允许调整",Toast.LENGTH_SHORT);
                return;
            }
            Pause_TempAndTimeSet();
        }
    }

    //点击暂停
    @OnClick(R.id.steam226_working_fra_middle)
    public void OnClickCircle() {
        LogUtils.i("20180119","checkoutWaterOut:"+checkWaterOut_Dialog()+" alarmStatus(steam226.alarm):"+alarmStatus(steam226.alarm));
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (checkWaterOut_Dialog()) return;
        if (steam226.status == OvenStatus.Working || steam226.status == OvenStatus.PreHeat) {
            steam226.setSteamStatus(OvenStatus.Pause, null);
        } else if (steam226.status == OvenStatus.Pause) {
            steam226.setSteamStatus(OvenStatus.Working, null);
        }else if (steam226.status ==SteamStatus.Order){
            ToastUtils.show("不允许暂停",Toast.LENGTH_SHORT);
        }
    }

    @OnClick(R.id.steam226_working_fra_light)
    public void OnClickLight() {
        if (!checkConnection()) return;
        if (!alarmStatus(steam226.alarm)) return;
        if (checkWaterOut_Dialog()) return;
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

     public IRokiDialog closedialog = null;
    //结束工作
    @OnClick(R.id.steam226_working_ll_switch)
    public void onClickSwitch() {
        if (!checkConnection()) return;
        //if (checkWaterOut_Dialog()) return;
        closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (closedialog.isShow()){
                    closedialog.dismiss();
                    if (steam226.status==SteamStatus.AlarmStatus){
                        steam226.setSteamStatus(SteamStatus.Off, null);
                    }else{
                        steam226.setSteamStatus(SteamStatus.On, null);
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

    private void workingReturn(){
        if (from==1){
            UIService.getInstance().popBack();
        }else{
            UIService.getInstance().popBack().popBack();
        }
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
            steam226_working_img_circledown.setImageResource(R.mipmap.ic_oven228work_preheating);
            steam226_working_tv_circleabove.setVisibility(View.VISIBLE);
            steam226_working_tv_circleabove.setText("预热中");
            steam226_working_fra_middle.setClickable(false);
        }else if (steam226.status == SteamStatus.PreHeat) {
            steam226_working_img_circledown.setImageResource(R.mipmap.ic_oven228work_preheating);
            steam226_working_tv_circleabove.setVisibility(View.VISIBLE);
            steam226_working_tv_circleabove.setText("预热中");
        } else {
            if (steam226.mode==21||steam226.mode==20){
                steam226_working_img_pauseic1.setVisibility(View.INVISIBLE);
                steam226_working_img_pauseic2.setVisibility(View.INVISIBLE);
                steam228_opentank.setVisibility(View.INVISIBLE);
            }else{
                steam228_opentank.setVisibility(View.VISIBLE);
            }
            initModel();
        }
    }

    public void initModel(){

    }

    /**
     * 暂停模式
     */
    void setPauseMolde() {
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
        if (steam226.mode==19||steam226.mode==9||steam226.mode==14||steam226.mode==18||
                steam226.mode==21){
            steam226_working_img_pauseic1.setVisibility(View.INVISIBLE);
            steam226_working_img_pauseic2.setVisibility(View.INVISIBLE);
        }
        if (steam226.mode==21||steam226.mode==20){
            steam226_working_img_pauseic1.setVisibility(View.INVISIBLE);
            steam226_working_img_pauseic2.setVisibility(View.INVISIBLE);
            steam228_opentank.setVisibility(View.INVISIBLE);
        }else{
            steam228_opentank.setVisibility(View.VISIBLE);
        }
        if (steam226.status == SteamStatus.PreHeat) {
            steam226_working_img_circledown.setImageResource(R.mipmap.ic_oven026work_preheating);
            steam226_working_tv_circleabove.setVisibility(View.VISIBLE);
            steam226_working_tv_circleabove.setText("预热中");
        } else {
            initModel();
        }
    }

    /**
     * 预约模式
     */
    void setOrderMolde() {
        checkWaterOut_Dialog();
        light_change();
        if (steam226PauseSettingDialog != null && steam226PauseSettingDialog.isShowing()) {//暂停弹出框取消
            steam226PauseSettingDialog.dismiss();
            steam226PauseSettingDialog = null;
        }
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
        steam226_working_tv_circleabove.setText("预约开始时间");
        if (steam226.orderTime_min < 10){
            if (steam226.orderTime_hour<10){
                steam226_working_tv_circledown.setText("0"+steam226.orderTime_hour + ":0" + steam226.orderTime_min);
            }else{
                steam226_working_tv_circledown.setText(steam226.orderTime_hour + ":0" + steam226.orderTime_min);
            }
        }else{
            if (steam226.orderTime_hour<10){
                steam226_working_tv_circledown.setText("0"+steam226.orderTime_hour + ":" + steam226.orderTime_min);
            }else{
                steam226_working_tv_circledown.setText(steam226.orderTime_hour + ":" + steam226.orderTime_min);
            }
        }

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



    Steam226PauseSettingDialog steam226PauseSettingDialog;

    private void Pause_TempAndTimeSet() {
        if (iRokidialogAlarm != null && iRokidialogAlarm.isShow()) {
            iRokidialogAlarm.dismiss();
        }
        iRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        switch (steam226.mode) {
            case SteamMode.FRESHSTEAM://鲜嫩蒸
                initModeData(getList2(SteamMode.FRESHSTEAM), 15,getList3(SteamMode.FRESHSTEAM), 19);
                break;
            case SteamMode.NATRITIVE://营养蒸
                initModeData(getList2(SteamMode.NATRITIVE), 30,getList3(SteamMode.NATRITIVE), 19);
                break;
            case SteamMode.STRONG_STEAM://强力蒸
                initModeData(getList2(SteamMode.STRONG_STEAM), 0,getList3(SteamMode.STRONG_STEAM), 34);
                break;
            case SteamMode.FASTSTEAMSLOWSTEAM://快蒸慢炖
                initModeData(getList2(SteamMode.FASTSTEAMSLOWSTEAM), 0,getList3(SteamMode.FASTSTEAMSLOWSTEAM), 59);
                break;
            default:
                break;
        }
    }


    /*设置各种模式温度范围*/
    protected List<String> getList2(int index) {
        List<String> list = Lists.newArrayList();
        if (index == SteamMode.FRESHSTEAM ||index ==SteamMode.NATRITIVE ) {
            for (int i = 70; i <= 100; i++) {
                list.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        } else if (index == SteamMode.STRONG_STEAM) {
            list.add(105+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        } else if (index == SteamMode.FASTSTEAMSLOWSTEAM) {
            list.add(100+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        }
        return list;
    }

    /*设置各种模式时间范围*/
    protected List<String> getList3(int index) {
        List<String> list = Lists.newArrayList();
        if (index == SteamMode.FRESHSTEAM ||index ==SteamMode.NATRITIVE||index == SteamMode.STRONG_STEAM
                ) {
            for (int i = 1; i <= 90; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        }
        if (index == SteamMode.FASTSTEAMSLOWSTEAM){
            for (int i = 1; i <=180 ; i++) {
                list.add(i + StringConstantsUtil.STRING_MINUTES);
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

    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringTempList = new ArrayList<String>();
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
                    LogUtils.i("20180305","mode::"+steam226.mode+"temp::"+Short.valueOf(stringTempList.get(stringTempList.size()-1))+" time"+ Short.valueOf(stringTimeList.get(stringTimeList.size()-1)));
                    steam226.setSteamCookMode(steam226.mode, Short.valueOf(stringTempList.get(stringTempList.size()-1)),
                            Short.valueOf(stringTimeList.get(stringTimeList.size()-1)), n, n, n, n, n, n, n, n, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    LogUtils.i("20180305","success");
                                }
                                @Override
                                public void onFailure(Throwable t) {
                                }
                            });

                }
            }
        });


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


    public void back() {

    }


    public void Stean026Fault_Dialog(short type){

    }


    /**
     * 报警
     **/
    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        alarmStatus(event.alarmId);
    }

    public boolean alarmStatus(short type) {
        boolean cause = false;
        switch (type) {
            case AbsSteamoven.Event_Steam226_Alarm_lack_water:
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

    IRokiDialog iRokiDialog = null;
    IRokiDialog iRokidialogAlarm = null;
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
        }else if (steam226.waterboxstate == 0){
            dialogByType.setContentText(R.string.device_alarm_water_out);
            dialogByType.setToastShowTime(DialogUtil.LENGTH_SHORT);
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
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeAllDialog();
    }
}
