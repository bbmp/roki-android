package com.robam.common.pojos.device.Oven;

import android.util.Log;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.io.RCMsgCallbackWithVoid;
import com.legent.plat.io.device.msg.Msg;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.utils.LogUtils;
import com.robam.common.io.device.MsgKeys;
import com.robam.common.io.device.MsgParams;
import com.robam.common.pojos.OvenUserAction;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.robam.common.io.device.MsgParams.UserId;

/**
 * Created by yinwei on 2017/8/29.
 */

public class Oven028 extends AbsOven {

    public Oven028(DeviceInfo devInfo) {
        super(devInfo);
    }

    long userid= Plat.accountService.getCurrentUserId();

    @Override
    public void setOvenRunMode(final short mode, final short setTempTime, final short setTempUp, short preflag, short recipeId, short recipeStep, short ArgumentNumber, final short SetTempDown,
                               short orderTime_min, short orderTime_hour, VoidCallback callback) {
        try {
            LogUtils.i("20180319","userid::"+userid);
            Msg msg = newReqMsg(MsgKeys.SetOven_RunMode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(UserId, getSrcUser());
            msg.putOpt(MsgParams.OvenMode, mode);
            msg.putOpt(MsgParams.OvenTemp, setTempUp);
            msg.putOpt(MsgParams.OvenTime, setTempTime);
            msg.putOpt(MsgParams.OvenPreFlag, preflag);
            msg.putOpt(MsgParams.OvenRecipeId, recipeId);
            msg.putOpt(MsgParams.OvenRecipeStep, recipeStep);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                if (SetTempDown != 0) {
                    msg.putOpt(MsgParams.SetTempDownKey, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownLength, (short) 1);
                    msg.putOpt(MsgParams.SetTempDownValue, SetTempDown);

                }
                if (orderTime_min != 255 && orderTime_hour != 255) {
                    msg.putOpt(MsgParams.OrderTime_key, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_length, (short) 2);
                    msg.putOpt(MsgParams.OrderTime_value_min, orderTime_min);
                    msg.putOpt(MsgParams.OrderTime_value_hour, orderTime_hour);
                }
            }
            LogUtils.i("oven_st_", "key:" + MsgKeys.SetOven_RunMode_Req + " TerminalType:" + terminalType + " UserId:" + getSrcUser() + " mode:" + mode
                    + " setTempUp:" + setTempUp + " setTime" + setTime + " preflag" + preflag + " recipeId:" + recipeId + " recipeStep:" + recipeStep
                    + " ArgumentNumber:" + ArgumentNumber + " SetTempDown:" + SetTempDown + " orderTime_min:" + orderTime_min + " orderTime_hour:" + orderTime_hour);

            if (mode == 9){
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        try{
                            String strTime = getCurrentTime();
                            OvenUserAction user = new OvenUserAction(userid, "EXP", strTime, (short)9, (short) 0, setTempUp,
                                    SetTempDown , setTempTime);
                            LogUtils.i("20180319","id:"+user.getID() + " Name:"+user.getName());
                            user.save2db();
                        }catch (Exception e){
                            Log.e("absOven","error:"+e.getMessage());
                        }
                        setTempDownValue = SetTempDown;
                        setTemp = setTempUp;
                        setTime = setTempTime;
                        //  LogUtils.i("20170824","SetTempDown:"+SetTempDown+"setTempUp:"+setTempUp+"setTime:"+setTime);
                        onStatusChanged();
                    }
                });
            }else {
                sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                    protected void afterSuccess(Msg resMsg) {
                        try{
                            String strTime = getCurrentTime();
                            OvenUserAction user = new OvenUserAction(userid, "专业模式", strTime,mode, setTempUp,(short) 0,
                                    (short)0 , setTempTime);
                            user.save2db();
                            LogUtils.i("20170824","usersave::");
                        }catch (Exception e){
                            LogUtils.i("20180319","e:;"+e.getMessage());
                        }
                        setTemp = setTempUp;
                        setTime = setTempTime;
                        onStatusChanged();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setOvenAutoRunMode(final short autoMode, final short setTimeTemp, short ArgumentNumber,
                                   short min, short hour, VoidCallback callback) {
        try {
            Msg msg = newReqMsg(MsgKeys.Set_Oven_Auto_Mode_Req);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());

            msg.putOpt(MsgParams.ovenAutoMode, autoMode);
            msg.putOpt(MsgParams.OvenSetTime, setTimeTemp);
            msg.putOpt(MsgParams.ArgumentNumber, ArgumentNumber);
            if (ArgumentNumber > 0) {
                msg.putOpt(MsgParams.OrderTime_key, 1);
                msg.putOpt(MsgParams.OrderTime_length, 2);
                msg.putOpt(MsgParams.OrderTime_value_min, min);
                msg.putOpt(MsgParams.OrderTime_value_hour, hour);
            }
            LogUtils.i("20161021", " autoModel:" + autoMode + " setTime:" + setTimeTemp + " ArgumentNumber:" + ArgumentNumber
                    + " min:" + min + " hour:" + hour);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    String strTime = getCurrentTime();
                    OvenUserAction user = new OvenUserAction(userid, "自动模式", strTime,autoMode,(short) 0,(short) 0,
                            (short)0 , setTimeTemp);
                    user.save2db();
                    setTime = setTimeTemp;
                    onStatusChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 设置多段
     */
    public void setOvenMoreMode(short prflag,short recipeId,short recipeStep,short argument,
                                short stage,short step1_mode,short step1_temp,short step1_time,
                                short step2_mode, short step2_temp,short step2_time,
                                final VoidCallback callback){
        try{
            Msg msg = newReqMsg(MsgKeys.Set_Oven_More_Cook);
            msg.putOpt(MsgParams.TerminalType, terminalType);
            msg.putOpt(MsgParams.UserId, getSrcUser());
            msg.putOpt(MsgParams.PreFlag,prflag);
            msg.putOpt(MsgParams.OvenRecipeId,recipeId);
            msg.putOpt(MsgParams.ArgumentNumber,argument);

            msg.putOpt(MsgParams.OvenStagekey,(short)1);
            msg.putOpt(MsgParams.OvenStageLength,(short)1);
            msg.putOpt(MsgParams.OvenStageValue,stage);

            msg.putOpt(MsgParams.OvenStep1Modekey,(short)2);
            msg.putOpt(MsgParams.OvenStep1ModeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep1ModeValue,step1_mode);

            msg.putOpt(MsgParams.OvenStep1SetTempkey ,(short)3);
            msg.putOpt(MsgParams.OvenStep1SetTempLength,(short)1);
            msg.putOpt(MsgParams.OvenStep1SetTempValue,step1_temp);

            msg.putOpt(MsgParams.OvenStep1SetTimekey ,(short)4);
            msg.putOpt(MsgParams.OvenStep1SetTimeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep1SetTimeValue,step1_time);

            msg.putOpt(MsgParams.OvenStep2Modekey,(short)5);
            msg.putOpt(MsgParams.OvenStep2ModeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep2ModeValue,step2_mode);

            msg.putOpt(MsgParams.OvenStep2SetTempkey,(short)6);
            msg.putOpt(MsgParams.OvenStep2SetTempLength,(short)1);
            msg.putOpt(MsgParams.OvenStep2SetTempValue,step2_temp);

            msg.putOpt(MsgParams.OvenStep2SetTimekey,(short)7);
            msg.putOpt(MsgParams.OvenStep2SetTimeLength,(short)1);
            msg.putOpt(MsgParams.OvenStep2SetTimeValue,step2_time);
            sendMsg(msg, new RCMsgCallbackWithVoid(callback) {
                protected void afterSuccess(Msg resMsg) {
                    LogUtils.i("20171030","resMsg:"+resMsg.toString());
                }
            });
        }catch (Exception e){

        }
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日,HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
