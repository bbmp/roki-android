package com.robam.roki.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.io.cloud.RetrofitCallback;
import com.legent.services.RestfulService;
import com.legent.utils.FileUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamCleanResetEvent;
import com.robam.common.events.SteamOvenStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.pojos.device.Sterilizer.AbsSterilizer;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.WaterPurifier.AbsWaterPurifier;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.integratedStove.AbsIntegratedStove;
import com.robam.common.pojos.device.integratedStove.SteamOvenFaultEnum;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.rika.AbsRika;
import com.robam.common.pojos.device.rika.RikaAlarmCodeBean;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.dictionary.StoveAlarm;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 14807 on 2018/7/27.
 */

public class AlarmDataUtils {

    public static String fileJson;
    static BroadcastReceiver broadcastReceiver;
    static Activity mActivity;
    static IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
    static IRokiDialog iRokiDialogAlarmType_02 = null;//二级级报警
    private static final int ONE_ALARM = 1;
    private static final int TWO_ALARM = 2;
    static int downloadCount = 0;//失败时下载次数

    public static void init(Activity activity) {
        LogUtils.i("20180726", " activity:" + activity);
        mActivity = activity;
        initAlarmData();
        initDialog();
    }

    public static void integratedAlarmStatus(AbsIntegratedStove  integratedStove , short alarm , short category) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarm);
        String dc = integratedStove.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject rjczobject = (JSONObject) object.get(dc);
            String deviceType = (String) rjczobject.get("deviceType");
            JSONObject dtObject = (JSONObject) rjczobject.get(integratedStove.getDt());
            JSONObject categoryObject ;
            if (category == 38){
                categoryObject = (JSONObject) dtObject.get("steamOven");
            }else {
                categoryObject = (JSONObject) dtObject.get("robic");
            }
            JSONObject o = (JSONObject)categoryObject.get(alarmCode);
            Integer alertLevel = (Integer) o.get("alertLevel");
            String alertName = (String) o.get("alertName");
            String alertDescr = (String) o.get("alertDescr");
            String alertCode = (String) o.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void rikaAlarmStatus(AbsRika rika, RikaAlarmCodeBean rikaAlarmCodeBean) {
        fileJson = PreferenceUtils.getString("alarm", null);
        try {
            if (IRokiFamily.RIKAX.equals(rika.getDp())) {
                short rikaAlarmCode = rikaAlarmCodeBean.getSterilAlarmCode();
                String alarmCode = String.valueOf(rikaAlarmCode);
                String dc = rika.getDc();
                String dt = rika.getDt();
                if (TextUtils.isEmpty(fileJson)) return;
                JSONObject object = new JSONObject(fileJson);
                JSONObject rikaDc = (JSONObject) object.get(dc);
                String deviceType = (String) rikaDc.get("deviceType");
                JSONObject rikaDt = (JSONObject) rikaDc.get(dt);
                JSONObject rikaSterilizer = (JSONObject) rikaDt.get("xiaodugui");
                JSONObject rikaSterilizerCode = (JSONObject) rikaSterilizer.get(alarmCode);
                Integer alertLevel = (Integer) rikaSterilizerCode.get("alertLevel");
                String alertName = (String) rikaSterilizerCode.get("alertName");
                String alertDescr = (String) rikaSterilizerCode.get("alertDescr");
                String alertCode = (String) rikaSterilizerCode.get("alertCode");
                if (ONE_ALARM == alertLevel) {
                    iRokiDialogAlarmType_01.setTitleText(deviceType);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                    iRokiDialogAlarmType_01.setContentText(alertDescr);
                    makePhoneCallListenr();
                } else if (TWO_ALARM == alertLevel) {
                    iRokiDialogAlarmType_02.setTitleText(deviceType);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                    iRokiDialogAlarmType_02.setContentText(alertDescr);
                    centerOneBtnListener();
                }
            } else if (IRokiFamily.RIKAZ.equals(rika.getDp())) {
                short rikaAlarmCode = rikaAlarmCodeBean.getSteamAlarmCode();
                String alarmCode = String.valueOf(rikaAlarmCode);
                String dc = rika.getDc();
                String dt = rika.getDt();
                if (TextUtils.isEmpty(fileJson)) return;
                JSONObject object = new JSONObject(fileJson);
                JSONObject rikaDc = (JSONObject) object.get(dc);
                String deviceType = (String) rikaDc.get("deviceType");
                JSONObject rikaDt = (JSONObject) rikaDc.get(dt);
                JSONObject rikaSteam = (JSONObject) rikaDt.get("zhengqilu");
                JSONObject rikaSteamCode = (JSONObject) rikaSteam.get(alarmCode);
                Integer alertLevel = (Integer) rikaSteamCode.get("alertLevel");
                String alertName = (String) rikaSteamCode.get("alertName");
                String alertDescr = (String) rikaSteamCode.get("alertDescr");
                String alertCode = (String) rikaSteamCode.get("alertCode");
                if (ONE_ALARM == alertLevel) {
                    iRokiDialogAlarmType_01.setTitleText(deviceType);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                    iRokiDialogAlarmType_01.setContentText(alertDescr);
                    makePhoneCallListenr();
                } else if (TWO_ALARM == alertLevel) {
                    iRokiDialogAlarmType_02.setTitleText(deviceType);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                    iRokiDialogAlarmType_02.setContentText(alertDescr);
                    centerOneBtnListener();
                }
            } else if (IRokiFamily.RIKAY.equals(rika.getDp())) {
                short rikaAlarmCode = rikaAlarmCodeBean.getSteamOvenAlarmCode();
                String alarmCode = String.valueOf(rikaAlarmCode);
                String dc = rika.getDc();
                String dt = rika.getDt();
                if (TextUtils.isEmpty(fileJson)) return;
                JSONObject object = new JSONObject(fileJson);
                JSONObject rikaDc = (JSONObject) object.get(dc);
                String deviceType = (String) rikaDc.get("deviceType");
                JSONObject rikaDt = (JSONObject) rikaDc.get(dt);
                JSONObject rikaSteamOven = (JSONObject) rikaDt.get("yitiji");
                JSONObject rikaSteamOvenCode = (JSONObject) rikaSteamOven.get(alarmCode);
                Integer alertLevel = (Integer) rikaSteamOvenCode.get("alertLevel");
                String alertName = (String) rikaSteamOvenCode.get("alertName");
                String alertDescr = (String) rikaSteamOvenCode.get("alertDescr");
                String alertCode = (String) rikaSteamOvenCode.get("alertCode");
                if (ONE_ALARM == alertLevel) {
                    iRokiDialogAlarmType_01.setTitleText(deviceType);
                    iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                    iRokiDialogAlarmType_01.setContentText(alertDescr);
                    makePhoneCallListenr();
                } else if (TWO_ALARM == alertLevel) {
                    iRokiDialogAlarmType_02.setTitleText(deviceType);
                    iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                    iRokiDialogAlarmType_02.setContentText(alertDescr);
                    centerOneBtnListener();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static void onMicroWaveAlarmEvent(AbsMicroWave microWave, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = microWave.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject microWaveDc = (JSONObject) object.get(dc);
            String deviceType = (String) microWaveDc.get("deviceType");
            JSONObject microWaveDcCode = (JSONObject) microWaveDc.get(alarmCode);
            Integer alertLevel = (Integer) microWaveDcCode.get("alertLevel");
            String alertName = (String) microWaveDcCode.get("alertName");
            String alertDescr = (String) microWaveDcCode.get("alertDescr");
            String alertCode = (String) microWaveDcCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //消毒柜
    public static void onSteriAlarmEvent(AbsSterilizer sterilizer, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = sterilizer.getDc();
        String dt = sterilizer.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject sterilizerDc = (JSONObject) object.get(dc);
            String deviceType = (String) sterilizerDc.get("deviceType");
            JSONObject sterilizerDt = (JSONObject) sterilizerDc.get(dt);
            JSONObject sterilizerCode = (JSONObject) sterilizerDt.get(alarmCode);
            Integer alertLevel = (Integer) sterilizerCode.get("alertLevel");
            String alertName = (String) sterilizerCode.get("alertName");
            String alertDescr = (String) sterilizerCode.get("alertDescr");
            String alertCode = (String) sterilizerCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void onEvent(SteamCleanResetEvent event) {
        ToastUtils.show("长时间使用，您需要除垢清洁蒸汽炉", Toast.LENGTH_SHORT);
    }

    //电烤箱
    public static void ovenAlarmStatus(short alarmId, AbsOven oven) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = oven.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject ovenDc = (JSONObject) object.get(dc);
            String deviceType = (String) ovenDc.get("deviceType");
            JSONObject ovenCode = (JSONObject) ovenDc.get(alarmCode);
            Integer alertLevel = (Integer) ovenCode.get("alertLevel");
            String alertName = (String) ovenCode.get("alertName");
            String alertDescr = (String) ovenCode.get("alertDescr");
            String alertCode = (String) ovenCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //灶具报警分类
    public static void onStoveAlarmEvent(Stove stove, StoveAlarm alarm) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarm.getID());
        String dc = stove.getDc();
        String dt = stove.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject stoveDc = (JSONObject) object.get(dc);
            String deviceType = (String) stoveDc.get("deviceType");
            JSONObject stoveDt = (JSONObject) stoveDc.get(dt);
            JSONObject stoveCode = (JSONObject) stoveDt.get(alarmCode);
            Integer alertLevel = (Integer) stoveCode.get("alertLevel");
            String alertName = (String) stoveCode.get("alertName");
            String alertDescr = (String) stoveCode.get("alertDescr");
            String alertCode = (String) stoveCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            } else {
                ToastUtils.show(alertDescr, Toast.LENGTH_SHORT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //灶具报警分类
    public static void onPotAlarmEvent(Pot pot, short alarm) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarm);
        String dc = pot.getDc();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject potDc = (JSONObject) object.get(dc);
            String deviceType = (String) potDc.get("deviceType");
            JSONObject potCode = (JSONObject) potDc.get(alarmCode);
            Integer alertLevel = (Integer) potCode.get("alertLevel");
            String alertName = (String) potCode.get("alertName");
            String alertDescr = (String) potCode.get("alertDescr");
            String alertCode = (String) potCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertName);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            } else {
                ToastUtils.show(alertDescr, Toast.LENGTH_SHORT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 蒸汽炉报警分类
     *
     * @param steam   设备
     * @param alarmId 报警编码
     * @return
     */
    public static void SteamAlarmStatus(AbsSteamoven steam, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = steam.getDc();
        String dt = steam.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject steamDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamDc.get("deviceType");
            JSONObject steamDt = (JSONObject) steamDc.get(dt);
            JSONObject steamCode = (JSONObject) steamDt.get(alarmCode);
            Integer alertLevel = (Integer) steamCode.get("alertLevel");
            String alertName = (String) steamCode.get("alertName");
            String alertDescr = (String) steamCode.get("alertDescr");
            String alertCode = (String) steamCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEvent(SteamOvenStatusChangedEvent event) {
        AbsSteamoven steam = event.pojo;
        if (steam.status == OvenStatus.Off) {
            if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
                iRokiDialogAlarmType_01.dismiss();
            }
            if (iRokiDialogAlarmType_02 != null && iRokiDialogAlarmType_02.isShow()) {
                iRokiDialogAlarmType_02.dismiss();
            }
        }
    }

    //一体机报警处理
    public static void steamOvenOneAlarmStatus(AbsSteameOvenOne steamOvenOne, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        LogUtils.i("202012071057", "fileJson::" + fileJson);
        String alarmCode = String.valueOf(alarmId);
        String dc = steamOvenOne.getDc();
        String dt = steamOvenOne.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);

            JSONObject steamOvenOneDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamOvenOneDc.get("deviceType");
            JSONObject steamOvenOneDt = (JSONObject) steamOvenOneDc.get(dt);
            JSONObject steamOvenOneCode = (JSONObject) steamOvenOneDt.get(alarmCode);
            Integer alertLevel = (Integer) steamOvenOneCode.get("alertLevel");
            String alertName = (String) steamOvenOneCode.get("alertName");
            String alertDescr = (String) steamOvenOneCode.get("alertDescr");
            String alertCode = (String) steamOvenOneCode.get("alertCode");
            LogUtils.i("202012071057", "alertLevel::" + alertLevel);
            LogUtils.i("20180831", "alertLevel:" + alertLevel);
            LogUtils.i("20180831", "alertDescr:" + alertDescr);
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            } else {
                ToastUtils.show(alertDescr, Toast.LENGTH_SHORT);
            }

        } catch (JSONException e) {
            LogUtils.i("20200414000", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void onWaterPurifiyAlarmEvent(AbsWaterPurifier purifier, short alarmId) {
        List<Short> list = new ArrayList<>();
        list.add(purifier.filter_state_pp);
        list.add(purifier.filter_state_cto);
        list.add(purifier.filter_state_ro1);
        list.add(purifier.filter_state_ro2);
        Short min = Collections.min(list);
        LogUtils.i("20181130", "min:" + min);
        if (alarmId == 255 && min > 10) {
            return;
        }
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = purifier.getDc();
        String dt = purifier.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject purifierDc = (JSONObject) object.get(dc);
            String deviceType = (String) purifierDc.get("deviceType");
            JSONObject purifierCode = (JSONObject) purifierDc.get(alarmCode);
            Integer alertLevel = (Integer) purifierCode.get("alertLevel");
            String alertName = (String) purifierCode.get("alertName");
            String alertDescr = (String) purifierCode.get("alertDescr");
            String alertCode = (String) purifierCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //电磁炉
    public static void cookerAlarm(AbsCooker absCooker, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = absCooker.getDc();
        String dt = absCooker.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject steamOvenOneDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamOvenOneDc.get("deviceType");
            JSONObject steamOvenOneDt = (JSONObject) steamOvenOneDc.get(dt);
            JSONObject steamOvenOneCode = (JSONObject) steamOvenOneDt.get(alarmCode);
            Integer alertLevel = (Integer) steamOvenOneCode.get("alertLevel");
            String alertName = (String) steamOvenOneCode.get("alertName");
            String alertDescr = (String) steamOvenOneCode.get("alertDescr");
            String alertCode = (String) steamOvenOneCode.get("alertCode");
            LogUtils.i("20180831", "alertLevel:" + alertLevel);
            LogUtils.i("20180831", "alertDescr:" + alertDescr);
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //洗碗机
    public static void dishWasherAlarm(AbsDishWasher washer, short alarmId) {
        fileJson = PreferenceUtils.getString("alarm", null);
        String alarmCode = String.valueOf(alarmId);
        String dc = washer.getDc();
        String dt = washer.getDt();
        if (TextUtils.isEmpty(fileJson)) return;
        try {
            JSONObject object = new JSONObject(fileJson);
            JSONObject steamOvenOneDc = (JSONObject) object.get(dc);
            String deviceType = (String) steamOvenOneDc.get("deviceType");
            JSONObject steamOvenOneDt = (JSONObject) steamOvenOneDc.get(dt);
            JSONObject steamOvenOneCode = (JSONObject) steamOvenOneDt.get(alarmCode);
            Integer alertLevel = (Integer) steamOvenOneCode.get("alertLevel");
            String alertDescr = (String) steamOvenOneCode.get("alertDescr");
            String alertCode = (String) steamOvenOneCode.get("alertCode");
            if (ONE_ALARM == alertLevel) {
                iRokiDialogAlarmType_01.setTitleText(deviceType);
                iRokiDialogAlarmType_01.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_01.setContentText(alertDescr);
                makePhoneCallListenr();
            } else if (TWO_ALARM == alertLevel) {
                iRokiDialogAlarmType_02.setTitleText(deviceType);
                iRokiDialogAlarmType_02.setTitleAralmCodeText(alertCode);
                iRokiDialogAlarmType_02.setContentText(alertDescr);
                centerOneBtnListener();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    //拨打电话
    public static void makePhoneCallListenr() {
        iRokiDialogAlarmType_01.setOkBtn(R.string.ok_sale_service, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(intent);
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn(R.string.can_good, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        iRokiDialogAlarmType_01.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_01.show();
        LogUtils.i("20170918", "show:" + iRokiDialogAlarmType_01.isShow());
    }

    /**
     * 缺水按钮设置
     */
    public static void centerOneBtnListener() {
        iRokiDialogAlarmType_02.setOkBtn(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_02.dismiss();
            }
        });
        iRokiDialogAlarmType_02.setCanceledOnTouchOutside(false);
        iRokiDialogAlarmType_02.show();
    }

    public static void onResume() {
        if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
            iRokiDialogAlarmType_01.dismiss();
        }
        if (iRokiDialogAlarmType_02 != null && iRokiDialogAlarmType_02.isShow()) {
            iRokiDialogAlarmType_02.dismiss();
        }
        iRokiDialogAlarmType_01 = null;
        iRokiDialogAlarmType_02 = null;
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(mActivity, DialogUtil.DIALOG_TYPE_01);
        iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(mActivity, DialogUtil.DIALOG_TYPE_02);

    }

    private static void initDialog() {

        if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
            iRokiDialogAlarmType_01.dismiss();
        }
        if (iRokiDialogAlarmType_02 != null && iRokiDialogAlarmType_02.isShow()) {
            iRokiDialogAlarmType_02.dismiss();
        }
        iRokiDialogAlarmType_01 = null;
        iRokiDialogAlarmType_02 = null;
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(mActivity, DialogUtil.DIALOG_TYPE_01);
        iRokiDialogAlarmType_02 = RokiDialogFactory.createDialogByType(mActivity, DialogUtil.DIALOG_TYPE_02);
    }

    private static void initAlarmData() {
        downloadJson();
    }

    private static void downloadJson() {

        final String url = PreferenceUtils.getString("downloadUrl", "");
        CloudHelper.getAllDeviceErrorInfo(Reponses.ErrorInfoResponse.class, new RetrofitCallback<Reponses.ErrorInfoResponse>() {
            @Override
            public void onSuccess(Reponses.ErrorInfoResponse errorInfoResponse) {
                String downloadUrl = errorInfoResponse.url;
                PreferenceUtils.setString("downloadUrl", downloadUrl);
                if (!TextUtils.isEmpty(downloadUrl) && !url.equals(downloadUrl)) {
                    RestfulService.getInstance().downFile(downloadUrl, "alarm.json", new Callback<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            LogUtils.i("20190614", " uri:" + uri);
                            if (uri != null) {
                                fileJson = AlarmDataUtils.getFileFromSD(uri.getPath());
                                PreferenceUtils.setString("alarm", fileJson);
                                FileUtils.deleteFile(uri.getPath());
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            LogUtils.i("20190614", " t:" + t.toString());
                        }
                    });
                }
            }

            @Override
            public void onFaild(String err) {
                downloadCount++;
                if (downloadCount <= 3) {
                    downloadJson();
                }
            }
        });

    }

    public static String getFileFromSD(String path) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static void closeReceiver() {
        iRokiDialogAlarmType_01 = null;
        iRokiDialogAlarmType_02 = null;
    }

    public static void closeDialog() {

        if (iRokiDialogAlarmType_01 != null && iRokiDialogAlarmType_01.isShow()) {
            iRokiDialogAlarmType_01.dismiss();
        }
        if (iRokiDialogAlarmType_02 != null && iRokiDialogAlarmType_02.isShow()) {
            iRokiDialogAlarmType_02.dismiss();
        }

    }
}
