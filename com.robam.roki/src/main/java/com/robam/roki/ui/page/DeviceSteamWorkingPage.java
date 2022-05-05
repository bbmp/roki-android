package com.robam.roki.ui.page;

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
import android.widget.PopupWindow;
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
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamAlarmEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.Steamoven.Steam209;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.DeviceWorkMsg;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.dialog.SteamOvenSensorBrokeDialog;
import com.robam.roki.ui.dialog.SteamOvenWarningDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rosicky on 15/12/28.
 */
public class DeviceSteamWorkingPage extends BasePage {

    static final int Start = 4;
    static final int Pause = 0;
    static final int Working = 1;
    static final int Done = 2;
    static final int CountDown = 3;
    static final int ReturnHome = 6;
    static final int PollStatusChanged = 7;
    static final int RefreshTime = 8;
    static final int adjustTime = 9;
    static final int adjustTemp = 10;

    Steam209 steam;
    @InjectView(R.id.relCurTem)
    RelativeLayout relCurTem;//实时温度 RelativeLayout
    @InjectView(R.id.relCurTime)
    RelativeLayout relCurTime;//实时时间 RelativeLayout
    @InjectView(R.id.txtTemSet)
    TextView txtTemSet;//预定温度
    @InjectView(R.id.txtTimeSet)
    TextView txtTimeSet;//预定时间

    private short mode;
    private short preTime;
    private short preTemp;
    private String type = "";
    private int lastTime;
    private boolean canCountDown = false;
    private boolean fromSetting = false;
    private boolean isFirst = false;

    private SteamOvenWarningDialog dlg = null;

    private Animation circleRotate = null;

    View contentView;
    @InjectView(R.id.txtCurrentTem)
    TextView txtCurrentTem;//实时温度文字
    @InjectView(R.id.txtCurrentTime)
    TextView txtCurrentTime;//实时时间文字
    @InjectView(R.id.imgCurVolumn)
    ImageView imgCurVolumn;//水量图标
    @InjectView(R.id.imgSpinCircle)
    ImageView imgSpinCircle;//中间转动圆圈
    @InjectView(R.id.imgCurTem)
    ImageView imgCurTem;//温度上下按钮
    @InjectView(R.id.imgCurTime)
    ImageView imgCurTime;//时间上下按钮
    @InjectView(R.id.imgContent)
    ImageView imgContent;//中间蒸箱图标
    @InjectView(R.id.txtWarning)
    TextView txtWarning;//开门警告
    @InjectView(R.id.imgPause)
    ImageView imgPause;//暂停
    @InjectView(R.id.imgDone)
    ImageView imgDone;//完成图标
    @InjectView(R.id.workType1)
    TextView workType1;//蒸
    @InjectView(R.id.workType2)
    TextView workType2;//“自洁” “杀毒” “已完成”
    @InjectView(R.id.imgReturn)
    ImageView imgReturn;//返回图标
    @InjectView(R.id.txtRecipe)
    TextView imgRecipe;//菜谱

    boolean doneWork = false;
    int preStatus;
    SteamOvenSensorBrokeDialog tempDlg;
    boolean hasTempDlgShown=false;
    PopupWindow pop;
    boolean timeLock=false;
    private IRokiDialog iRokiDialog = null;
    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringTempList = new ArrayList<String>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Start:  // 从设置页进入，设置专业模式，成功则开始转圈，不倒计时
                    steam.setSteamWorkMode(mode, preTemp, preTime, (short) 0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            if (Plat.DEBUG)
                                Log.i("steam.setSteamWorkMode:",steam.status+"");
                            relCurTem.setClickable(false);
                            relCurTime.setClickable(false);
                            if (circleRotate == null) {
                                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_steamoven_circle_rotate);
                                LinearInterpolator lin = new LinearInterpolator();
                                circleRotate.setInterpolator(lin);
                            }
                            imgSpinCircle.startAnimation(circleRotate);
                            if(mode!=0)
                                imgContent.setClickable(true);
                            else
                                imgContent.setClickable(false);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);
                        }
                    });
                    break;
                case Pause:
                    steam.setSteamStatus(SteamStatus.Pause, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            imgCurTem.setVisibility(View.VISIBLE);
                            imgCurTime.setVisibility(View.VISIBLE);
                            txtWarning.setVisibility(View.GONE);
                            imgPause.setVisibility(View.VISIBLE);
                            imgPause.bringToFront();
                            relCurTem.setClickable(true);
                            relCurTime.setClickable(true);
                            imgSpinCircle.clearAnimation();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);
                        }
                    });
                    break;
                case Working:

                    steam.setSteamStatus(SteamStatus.Working, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                            imgCurTem.setVisibility(View.GONE);
                            imgCurTime.setVisibility(View.GONE);
                            txtWarning.setVisibility(View.VISIBLE);
                            imgPause.setVisibility(View.GONE);
                            relCurTem.setClickable(false);
                            relCurTime.setClickable(false);
                            if (circleRotate == null) {
                                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_steamoven_circle_rotate);
                                LinearInterpolator lin = new LinearInterpolator();
                                circleRotate.setInterpolator(lin);
                            }
                            imgSpinCircle.startAnimation(circleRotate);

                            handler.sendEmptyMessage(CountDown); // 设置运行状态成功后开始倒计时
                            canCountDown = true;
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                    break;
                case Done:
                    imgDone.setVisibility(View.VISIBLE);
                    txtWarning.setVisibility(View.GONE);
                    imgContent.setVisibility(View.GONE);
                    imgSpinCircle.clearAnimation();
                    imgSpinCircle.setVisibility(View.GONE);
                    workType1.setVisibility(View.GONE);
                    workType2.setText(cx.getString(R.string.device_yi_finish));
                    workType2.setVisibility(View.VISIBLE);
                    // 10min后自动关机
                    handler.sendEmptyMessageDelayed(ReturnHome, 3 * 1000);
                    txtCurrentTime.setText("0");timeLock=true;
                    break;
                case CountDown:
                    break;
                case adjustTime:
                    setDeviceRunData((String) msg.obj);
                    break;
                case adjustTemp:
                    setDeviceRunData((String) msg.obj);
                    break;
                case ReturnHome:
                    imgSpinCircle.clearAnimation();
                    UIService.getInstance().returnHome();
                    break;
                case PollStatusChanged:
                    try {
                        if (steam.mode == 0&&steam.alarm==0&&steam.status!=SteamStatus.Wait) {
                            imgContent.setClickable(false);
                            workType1.setVisibility(View.GONE);
                            workType2.setVisibility(View.VISIBLE);
                        } else if (steam.status != SteamStatus.On&&steam.status!=SteamStatus.Wait) {
                            imgContent.setClickable(true);
                            setImgContentBack();
                            workType1.setVisibility(View.VISIBLE);
                            workType2.setVisibility(View.GONE);
                        }
                        int min = steam.time / 60;
                        int sec = steam.time % 60;
                        if (Plat.DEBUG)
                            Log.i("test00", "steam.status:" + steam.status + " steam.tmpRel:" + steam.temp + " steam.timeRel:" + steam.time + " min:" + min + " sec:" + sec
                                +" steam.alarm"+steam.alarm);
                        if (Plat.DEBUG)
                            Log.i("test01", "实时:" + steam.temp + "设置:" + steam.tempSet);
                        if (Plat.DEBUG)
                            Log.i("test00", "steam.mode" + steam.mode);
                        if (sec > 0) {
                            min++;
                        }

                        if (steam.status != SteamStatus.On) {
                            txtTemSet.setText(String.valueOf(steam.tempSet));
                            txtTimeSet.setText(String.valueOf(steam.timeSet));
                            if (steam.status != SteamStatus.Pause) {
                                if(!timeLock)
                                    txtCurrentTime.setText(String.valueOf(min));
                                txtCurrentTem.setText(String.valueOf(steam.temp));
                            }
                        }
                        if (min == 0 && sec == 0&&steam.status!=0&&steam.oldstatus==SteamStatus.Working&&steam.status==SteamStatus.On) {
                            doneWork = true;
                            handler.sendEmptyMessage(Done);
                            break;
                        }
                        if (steam.status == SteamStatus.Pause) {
                            if(steam.mode!=0){
                                relCurTem.setClickable(true);
                                relCurTime.setClickable(true);
                                imgCurTem.setVisibility(View.VISIBLE);
                                imgCurTime.setVisibility(View.VISIBLE);
                            }else{
                                relCurTem.setClickable(false);
                                relCurTime.setClickable(false);
                                imgCurTem.setVisibility(View.GONE);
                                imgCurTime.setVisibility(View.GONE);
                            }
                            txtWarning.setVisibility(View.GONE);
                            imgPause.setVisibility(View.VISIBLE);
                            imgSpinCircle.clearAnimation();
                            circleRotate = null;
                            imgPause.bringToFront();
                            setImgContentBack();
                        } else if (steam.status == SteamStatus.Working) {
                            /*if (dlg != null&&dlg.isShowing()&&steam.alarm==0) {
                                dlg.dismiss();
                                dlg = null;
                            }*/
                            /*if (tempDlg != null&&tempDlg.isShowing()&&steam.alarm==0) {
                                tempDlg.dismiss();
                                tempDlg = null;
                            }*/
                            imgCurVolumn.setImageResource(R.mipmap.img_steam_oven_volumn_has);
                            imgCurTem.setVisibility(View.GONE);
                            imgCurTime.setVisibility(View.GONE);
                            txtWarning.setVisibility(View.VISIBLE);
                            imgPause.setVisibility(View.GONE);
                            relCurTem.setClickable(false);
                            relCurTime.setClickable(false);
                            if (circleRotate == null) {
                                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_steamoven_circle_rotate);
                                LinearInterpolator lin = new LinearInterpolator();
                                circleRotate.setInterpolator(lin);
                                imgSpinCircle.startAnimation(circleRotate);
                            }
                            if (steam.time < lastTime) {
                                if (!canCountDown) {
                                    handler.sendEmptyMessage(CountDown);
                                    canCountDown = true;
                                }
                            } else {
                                lastTime = steam.time;
                                canCountDown = false;
                            }
                        } else if (steam.status == SteamStatus.Off) {
                            UIService.getInstance().returnHome();
                        } else if (steam.status == SteamStatus.Wait) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case RefreshTime:
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        DeviceWorkMsg msg = bd == null ? null : (DeviceWorkMsg) bd.getSerializable("msg");
        if (msg != null) {
            if (Plat.DEBUG)
                Log.i("DeviceSteamWorkingPage", "preTime:" + preTime + " preTemp:" + preTemp + " mode:" + mode + " type:" + type);
            preTime = Short.valueOf(msg.getTime());
            preTemp = Short.valueOf(msg.getTemperature());
            mode = msg.getMode();
            type = msg.getType();
        }
        steam = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_steamoven_work,
                container, false);
        ButterKnife.inject(this, contentView);

        // ------- 检测到蒸汽炉处于暂停或工作状态（从首页进入）
        if (steam.status == SteamStatus.Pause || steam.status == SteamStatus.Working) {
            initView();
        }
        // ------- 检测到蒸汽炉处于开机但没工作状态（从设置页进入）
        if (steam.status == SteamStatus.On) {
            restoreView();
        }
        checkDoorState();
        return contentView;
    }

    private void initView() {
        int min = steam.time / 60;
        int sec = steam.time % 60;
        if (sec > 0) {
            min++;
        }
        imgContent.setClickable(false);
        //txtCurrentTime.setText(String.valueOf(min));
        if (steam.status == SteamStatus.Pause) {
            imgCurTem.setVisibility(View.VISIBLE);
            imgCurTime.setVisibility(View.VISIBLE);
            txtWarning.setVisibility(View.GONE);
            imgPause.setVisibility(View.VISIBLE);
            relCurTem.setClickable(true);
            relCurTime.setClickable(true);
            imgSpinCircle.clearAnimation();
            imgPause.bringToFront();
        } else {
            imgCurTem.setVisibility(View.GONE);
            imgCurTime.setVisibility(View.GONE);
            txtWarning.setVisibility(View.VISIBLE);
            imgPause.setVisibility(View.GONE);
            relCurTem.setClickable(false);
            relCurTime.setClickable(false);
            if (circleRotate == null) {
                circleRotate = AnimationUtils.loadAnimation(getContext(), R.anim.device_steamoven_circle_rotate);
                LinearInterpolator lin = new LinearInterpolator();
                circleRotate.setInterpolator(lin);
            }
            imgSpinCircle.startAnimation(circleRotate);
        }
        setImgContentBack();
    }

    private void setImgContentBack() {
        imgContent.setImageResource(getRes(steam.mode));
        switch (steam.mode) {
            case 2:
                workType1.setText(StringConstantsUtil.STRING_MEAT);
                break;
            case 7:
                workType1.setText(StringConstantsUtil.STRING_VEGETABLES);
                break;
            case 4:
                workType1.setText(StringConstantsUtil.STRING_WATER_STEAMED_EGG);
                break;
            case 3:
                workType1.setText(StringConstantsUtil.STRING_SEAFOOT);
                break;
            case 5:
                workType1.setText(StringConstantsUtil.STRING_PASTRY);
                break;
            case 8:
                workType1.setText(StringConstantsUtil.STRING_NOODLES);
                break;
            case 6:
                workType1.setText(StringConstantsUtil.STRING_TENDON);
                break;
            case 9:
                workType1.setText(StringConstantsUtil.STRING_THOW);
                break;
            case 0:
                workType1.setVisibility(View.GONE);
                workType2.setVisibility(View.VISIBLE);
                workType2.setText(StringConstantsUtil.STRING_CUSTOM);
                break;
            default:
                workType1.setText("");
                if (Plat.DEBUG)
                    Log.i("orderyiguodetail", "switchmissing");
                break;
        }
    }

    private void restoreView() {
        txtTemSet.setText(preTemp + "");
        txtTimeSet.setText(preTime + "");
        isFirst = true;
        if (type.equals(StringConstantsUtil.STRING_SELF_CLEANING) ||
                type.equals(StringConstantsUtil.STRING_STERILIZATION)) {
            workType1.setVisibility(View.GONE);
            workType2.setVisibility(View.VISIBLE);
            workType2.setText(StringConstantsUtil.STRING_CUSTOM);
            imgContent.setImageResource(R.mipmap.img_steamoven_clean);
            imgContent.setClickable(false);
            imgSpinCircle.setClickable(false);
        } else {
            fromSetting = true;
            workType1.setText(type);
            imgContent.setClickable(true);
            imgContent.setImageResource(getRes(mode));
        }
        handler.sendEmptyMessageDelayed(Start, 3000);
    }

    int getRes(short mode) {
        int res = -1;
        switch (mode) {
            case 2:
                res = R.mipmap.img_steam_content_meat;
                break;
            case 3:
                res = R.mipmap.img_steam_content_seafood;
                break;
            case 4:
                res = R.mipmap.img_steam_content_egg;
                break;
            case 5:
                res = R.mipmap.img_steam_content_cake;
                break;
            case 6:
                res = R.mipmap.img_steam_content_tijin;
                break;
            case 7:
                res = R.mipmap.img_steam_content_vege;
                break;
            case 8:
                res = R.mipmap.img_steam_content_noddle;
                break;
            case 9:
                res = R.mipmap.img_steam_content_unfreeze;
                break;
            case 0:
                res = R.mipmap.img_steamoven_clean;
            default:
                break;
        }
        return res;
    }

    private void checkDoorState() {
        if (steam.doorState == Steam209.Steam_Door_Open) {
            gatingShowDialog(Steam209.Steam_Door_Open);
        }else{
            readyToWork();
        }
    }
    //倒计时3 2  1
    private void readyToWork() {
        lastTime = 0;
        if (steam.status == SteamStatus.On) {
            IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_07);
            dialogByType.show();
        }
    }

    //门控提示
    private void gatingShowDialog(short type) {
        switch (type) {
            case Steam209.Steam_Door_Open:
                IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
                dialogByType.setContentText(R.string.device_alarm_gating_content);
                dialogByType.setToastShowTime(DialogUtil.LENGTH_CENTER);
                dialogByType.show();
                break;
        }
    }

    /**
     * ========================================================
     * onClick代码段，响应各种点击事件
     */
    @OnClick({R.id.relCurTem, R.id.relCurTime})
    public void OnClickReset() {
        if(steam.alarm!=0){
            alarmEventDispatch(steam.alarm);return;
        }
//        DeviceWorkMsg msg = new DeviceWorkMsg();
        String s = StringConstantsUtil.STRING_MEAT;
        short type = steam.mode;
        if (type == 2) {
            s = StringConstantsUtil.STRING_MEAT;
        } else if (type == 3) {
            s = StringConstantsUtil.STRING_SEAFOOT;
        } else if (type == 4) {
            s = StringConstantsUtil.STRING_WATER_STEAMED_EGG;
        } else if (type == 5) {
            s = StringConstantsUtil.STRING_PASTRY;
        } else if (type == 6) {
            s = StringConstantsUtil.STRING_TENDON;
        } else if (type == 7) {
            s = StringConstantsUtil.STRING_VEGETABLES;
        } else if (type == 8) {
            s = StringConstantsUtil.STRING_NOODLES;
        } else if (type == 9) {
            s = StringConstantsUtil.STRING_THOW;
        }
//        msg.setType(s);
        Pause_TempAndTimeSet(s);

    }
    //暂停时设置时间温度
    private void Pause_TempAndTimeSet(String type) {
        iRokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);

        if (type.equals(StringConstantsUtil.STRING_VEGETABLES)){
            initModeData(getListTemp(type),3,getListTime(type),35);
        }else if (type.equals(StringConstantsUtil.STRING_WATER_STEAMED_EGG)){
            initModeData(getListTemp(type),10,getListTime(type),20);
        }else if (type.equals(StringConstantsUtil.STRING_MEAT)){
            initModeData(getListTemp(type),15,getListTime(type),40);
        }else if (type.equals(StringConstantsUtil.STRING_SEAFOOT)){
            initModeData(getListTemp(type),10,getListTime(type),25);
        }else if (type.equals(StringConstantsUtil.STRING_PASTRY)){
            initModeData(getListTemp(type),10,getListTime(type),25);
        }else if (type.equals(StringConstantsUtil.STRING_NOODLES)){
            initModeData(getListTemp(type),5,getListTime(type),25);
        }else if (type.equals(StringConstantsUtil.STRING_TENDON)){
            initModeData(getListTemp(type),10,getListTime(type),55);
        }else if (type.equals(StringConstantsUtil.STRING_THOW)){
            initModeData(getListTemp(type),0,getListTime(type),35);
        }
    }

    public void initModeData(List<String> listTemp, int indexTemp, List<String> listTime, int indexTime) {
        if (listTemp == null || listTime == null) return;
        iRokiDialog.setWheelViewData(listTemp, null, listTime, false, indexTemp, 0, indexTime, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                Message msg = handler.obtainMessage();
                msg.obj = contentFront;
                msg.what = 10;
                handler.sendMessage(msg);
            }
        }, null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message msg = handler.obtainMessage();
                msg.obj = contentRear;
                msg.what = 9;
                handler.sendMessage(msg);
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
        iRokiDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
        iRokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRokiDialog.isShow()){
                    iRokiDialog.dismiss();

                    if (stringTempList == null || stringTempList.size() == 0 || stringTimeList == null || stringTimeList.size() == 0) return;
                    steam.setSteamWorkTemp(Short.valueOf(stringTempList.get(stringTempList.size()-1)), new VoidCallback() {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);}
                    });
                    steam.setSteamWorkTime(Short.valueOf(stringTimeList.get(stringTimeList.size()-1)), new VoidCallback() {
                        @Override
                        public void onSuccess() {}
                        @Override
                        public void onFailure(Throwable t) {
                            ToastUtils.showThrowable(t);
                        }
                    });

                }
            }
        });



    }

    //根据类型获取温度
    protected List<String> getListTemp(String type){
        List<String> listTemp = Lists.newArrayList();
        if (type.equals(StringConstantsUtil.STRING_VEGETABLES)){
            for (int i = 95; i <= 100; i++) {
                listTemp.add(i+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (type.equals(StringConstantsUtil.STRING_WATER_STEAMED_EGG) || type.equals(StringConstantsUtil.STRING_NOODLES)){
            for (int i = 85; i <= 95; i++) {
                listTemp.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (type.equals(StringConstantsUtil.STRING_MEAT)){
            for (int i = 85; i <= 100; i++) {
                listTemp.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (type.equals(StringConstantsUtil.STRING_SEAFOOT)){
            for (int i = 75; i <= 95; i++) {
                listTemp.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (type.equals(StringConstantsUtil.STRING_PASTRY)){
            for (int i = 85; i <= 95; i++) {
                listTemp.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (type.equals(StringConstantsUtil.STRING_TENDON)){
            for (int i = 90; i <= 100; i++) {
                listTemp.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }else if (type.equals(StringConstantsUtil.STRING_THOW)){
            for (int i = 55; i <= 65; i++) {
                listTemp.add(i + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
            }
        }

        return listTemp;
    }
    //根据类型获取时间
    protected List<String> getListTime(String type){
        List<String> listTime = Lists.newArrayList();
        if (type.equals(StringConstantsUtil.STRING_VEGETABLES) || type.equals(StringConstantsUtil.STRING_WATER_STEAMED_EGG)
                || type.equals(StringConstantsUtil.STRING_SEAFOOT) || type.equals(StringConstantsUtil.STRING_PASTRY)
                || type.equals(StringConstantsUtil.STRING_NOODLES)){
            for (int i = 5; i <= 45; i++) {
                listTime.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        }else if(type.equals(StringConstantsUtil.STRING_MEAT) || type.equals(StringConstantsUtil.STRING_TENDON) ||
                type.equals(StringConstantsUtil.STRING_THOW)){
            for (int i = 5; i <= 60; i++) {
                listTime.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        }

        return listTime;
    }

    @OnClick(R.id.imgContent)
    public void onClickContent() {

        if (steam.status == SteamStatus.Working) {
            handler.sendEmptyMessage(Pause);
        } else if (steam.status == SteamStatus.Pause) {
            handler.sendEmptyMessage(Working);
        } else if (steam.status == SteamStatus.AlarmStatus) {

        }
    }

    @OnClick(R.id.imgSwitch1)
    public void onClickSwitch() {
        if(steam.alarm!=0){
            alarmEventDispatch(steam.alarm);return;
        }

        final IRokiDialog closedialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closedialog.dismiss();
                steam.setSteamStatus(SteamStatus.Off, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        if (fromSetting) {
                            UIService.getInstance().returnHome();
                        } else {
                            UIService.getInstance().returnHome();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
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

    @OnClick(R.id.imgReturn)
    public void onClickReturn() {
        UIService.getInstance().returnHome();
    }

    @OnClick(R.id.txtRecipe)
    public void onClickRecipe() {
        //HomeRecipeView.recipeCategoryClick(DeviceType.RZQL);
        ToastUtils.show(R.string.please_look_forward_opening, Toast.LENGTH_SHORT);
    }

    private void setImgContentBackGround(int mode) {

    }

    /**
     * ==========================================================================
     * onEvent 事件代码段
     *
     */

    /**
     * 报警事件
     *
     * @param event
     */
    @Subscribe
    public void onEvent(SteamAlarmEvent event) {
        Log.e("event.alarm", String.valueOf(event.alarmId));
        Log.e("steam.alarm", String.valueOf(steam.alarm));
        alarmEventDispatch(event.alarmId);
        }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        if (Plat.DEBUG)
            Log.i("SteamOvenStatustWorking","status:"+steam.status+" alarm:"+steam.alarm);
        if (steam == null || !Objects.equal(steam.getID(), event.pojo.getID())) {
            return;
        }
        if (doneWork) {
            return;
        }
        imgCurVolumn.setImageResource(R.mipmap.img_steam_oven_volumn_has);
        handler.sendEmptyMessage(PollStatusChanged);
    }
    private void alarmEventDispatch(int status){
        if (Plat.DEBUG)
            Log.i("steam0","status:"+steam.status+" alarm:"+steam.alarm);
        switch (status){
            case 3://门控开关故障
                gatingShowDialog(Steam209.Steam_Door_Open);
                break;
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (steam == null || !Objects.equal(steam.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            UIService.getInstance().popBack();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                UIService.getInstance().returnHome();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(pop!=null){
            pop.dismiss();
        }
        ButterKnife.reset(this);
    }
}
