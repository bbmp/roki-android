package com.robam.roki.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.legent.Callback;
import com.legent.Callback2;
import com.legent.plat.Plat;
import com.legent.plat.events.UserLoginNewEvent;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.popoups.BasePickerPopupWindow;
import com.legent.ui.ext.popoups.BasePickerPopupWindow4;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.OvenUserAction;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.SteamUserAction;
import com.robam.common.pojos.UserAction;
import com.robam.common.pojos.WaterPurifierSetPeople;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;
import com.robam.roki.R;
import com.robam.roki.model.CrmArea;
import com.robam.roki.model.DeviceWorkMsg;
import com.robam.roki.model.NormalModeItemMsg;
import com.robam.roki.service.MobileStoveCookTaskService;
import com.robam.roki.ui.dialog.BlackPromptConfirmAndCancleDialog;
import com.robam.roki.ui.dialog.BlackPromptConfirmDialog;
import com.robam.roki.ui.dialog.BlackPromptDialog;
import com.robam.roki.ui.dialog.BlackPromptDialog2;
import com.robam.roki.ui.dialog.BlackPromptDialog526;
import com.robam.roki.ui.dialog.ChooseStoveByManualDialog;
import com.robam.roki.ui.dialog.ChooseStoveByWaitDialog;
import com.robam.roki.ui.dialog.CountDownDialog526;
import com.robam.roki.ui.dialog.DeviceSelectNewDialog;
import com.robam.roki.ui.dialog.DeviceSelectNewOrientailDialog;
import com.robam.roki.ui.dialog.DeviceSelectStoveDialog;
import com.robam.roki.ui.dialog.DeviceSelectStoveHeadDialog;
import com.robam.roki.ui.dialog.DialogWaterPurifierSmartTimeSet;
import com.robam.roki.ui.dialog.Fan8700KitchenCleaningDialog;
import com.robam.roki.ui.dialog.Fan8700RemindSoupNoticeDialog;
import com.robam.roki.ui.dialog.Fan8700RemindSoupSettingDialog;
import com.robam.roki.ui.dialog.Micro526PauseSettingDialog;
import com.robam.roki.ui.dialog.Microwave526CleanDialog;
import com.robam.roki.ui.dialog.Microwave526RecentlyUseDialog;
import com.robam.roki.ui.dialog.Microwave526TimeSettingDialog;
import com.robam.roki.ui.dialog.Microwave526WeightSettingDialog;
import com.robam.roki.ui.dialog.Microwave526WeightTimeSettingDialog1;
import com.robam.roki.ui.dialog.MicrowaveLinkArgumentSettingDialog;
import com.robam.roki.ui.dialog.MicrowaveSettingDialog;
import com.robam.roki.ui.dialog.MicrowaveTimeSettingDialog;
import com.robam.roki.ui.dialog.MicrowaveWeightSettingDialog;
import com.robam.roki.ui.dialog.MicrowaveWeightTimeSettingDialog;
import com.robam.roki.ui.dialog.NoStoveDialog;
import com.robam.roki.ui.dialog.Oven028RecentlyUseDialog;
import com.robam.roki.ui.dialog.RecipeSearchDialog;
import com.robam.roki.ui.dialog.SelectDialog;
import com.robam.roki.ui.dialog.Steam228RecentlyUseDialog;
import com.robam.roki.ui.dialog.StoveSelectAllOffTips;
import com.robam.roki.ui.dialog.StoveSelectTipsDialog;
import com.robam.roki.ui.dialog.WaterPurifierSetPeopleDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.view.OvenResetWheelView;
import com.robam.roki.ui.view.recipeclassify.RecipeFilterPopWindow;

import java.util.List;
import java.util.Map;

/**
 * Created by sylar on 15/6/11.
 */
public class Helper {

//    public static DisplayImageOptions DisplayImageOptions_UserFace = new DisplayImageOptions.Builder()
//            .showImageOnLoading(R.mipmap.ic_user_default_figure)
//            .showImageForEmptyUri(R.mipmap.ic_user_default_figure)
//            .showImageOnFail(R.mipmap.ic_user_default_figure).cacheInMemory(true)
//            .cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();

    public static RequestOptions DisplayImageOptions_UserFace = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.ic_user_default_figure)
            .error(R.mipmap.ic_user_default_figure)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .transform(new CircleCrop());

    public static void login(final Activity atv, String account, String pwdMd5) {
        Plat.accountService.login(account, pwdMd5, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                Helper.onLoginCompleted(atv, user);
            }
            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    public static void login(String account, String pwdMd5) {
        Plat.accountService.login(account, pwdMd5, new Callback<User>() {

            @Override
            public void onSuccess(User user) {
                Helper.onLoginCompleted(user);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    public static SelectDialog newSelectDialog(Context cx, List<IDevice> list, final Callback<IDevice> callback){
        final SelectDialog dialog = SelectDialog.show(cx,list);
        dialog.setPickListener(new SelectDialog.PickListener() {

            @Override
            public void onConfirm(IDevice dev) {
                if (callback!=null){
                    callback.onSuccess(dev);
                }
            }
        });
        return dialog;
    }

    public static void onLoginCompleted(Activity atv, final User user) {

        ToastUtils.showShort("登录成功");
        EventUtils.postEvent(new UserLoginNewEvent());

        if (atv instanceof MainActivity) {
            UIService.getInstance().popBack();
        } else {
            MainActivity.start(atv);
        }

    }

    public static void onLoginCompleted(final User user) {

        UIService.getInstance().returnHome();
    }




//    public static void startCook(Context cx, Recipe recipe) {
//        if (MobileStoveCookTaskService.getInstance().isRunning()) {
//            ToastUtils.showShort("正在烧菜中,不可同时烧菜!");
//            return;
//        }
//
//        AbsFan fan = Utils.getDefaultFan();
//        Stove stove = Utils.getDefaultStove();
//        if (stove == null || !stove.isConnected()) {
//            if (fan == null || !fan.isConnected()) {
//                Bundle bd = new Bundle();
//                bd.putLong(PageArgumentKey.BookId, recipe.id);
//                UIService.getInstance().postPage(PageKey.CookWithoutDevice, bd);
//
//            } else {
//                NoStoveDialog.show(cx,recipe);//未检测到灶具
//            }
//        } else {
//                if ("RRQZ".equals(recipe.getJs_cookSteps().get(0).getDc())){
//                boolean isOffLeft = stove.leftHead != null && stove.leftHead.status == StoveStatus.Off;
//                boolean isOffRight = stove.rightHead != null && stove.rightHead.status == StoveStatus.Off;
//
//                if (isOffLeft != isOffRight) {
//                    //有一个是开机状态
//                    startCook(!isOffLeft ? stove.leftHead : stove.rightHead, recipe);
//                } else {
//                    //左右灶状态一样
//                    if (isOffLeft) {
//                        //都是关机状态
//                        //等待开火，请在灶具上开启
//                        ChooseStoveByWaitDialog.show(cx, recipe);
//                    } else {
//                        //都是开机状态
//                        boolean isStandByLeft = stove.leftHead != null && stove.leftHead.status == StoveStatus.StandyBy;
//                        boolean isStandByRight = stove.rightHead != null && stove.rightHead.status == StoveStatus.StandyBy;
//                        if (isStandByLeft != isStandByRight) {
//                            //其中有一个是待机状态
//                            startCook(isStandByLeft ? stove.leftHead : stove.rightHead, recipe);
//                        } else {
//                            //都是待机或都是工作中时，需要选择
//                            ChooseStoveByManualDialog.show(cx, recipe);
//                        }
//                    }
//                }
//            }else{
//                    //MobileStoveCookTaskService.getInstance().start(null, recipe);
//                }
//        }
//    }

    public static void startCook(Stove.StoveHead head, Recipe recipe) {
        //ToastUtils.show("11",Toast.LENGTH_SHORT);
      //  MobileStoveCookTaskService.getInstance().start(head, recipe);
    }


    public static PopupWindow newOvenResetTwoSettingPicker(Context cx, final Callback2<NormalModeItemMsg> callback, NormalModeItemMsg msg) {
        final OvenResetWheelView view = new OvenResetWheelView(cx, msg.getType());
        BasePickerPopupWindow4.PickListener listener = new BasePickerPopupWindow4.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm() {
                if (callback != null) {
                    callback.onCompleted(view.getSelected());
                }
            }
        };
        BasePickerPopupWindow4 pop = new BasePickerPopupWindow4(cx, view);
        pop.setPickListener(listener);
        return pop;
    }


    /**
     * 联动料理选择框
     *
     * @param cx
     * @param callback
     * @param i
     * @return
     */
    public static MicrowaveLinkArgumentSettingDialog newMicrowaveArgumentSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short i) {
        MicrowaveLinkArgumentSettingDialog dialog = MicrowaveLinkArgumentSettingDialog.show(cx, i);
        dialog.setPickListener(new MicrowaveLinkArgumentSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short model, short fire, short time) {
                Log.i("mic", "onConfirm");
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setModel(model);
                msg.setFire(fire);
                msg.setTime(time);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     *设置家庭人数
     * @param cx
     * @param callback
     * @param guid
     * @return
     */
    public static WaterPurifierSetPeopleDialog newWaterPurifierSetPeopleDialog(Context cx, String guid,final Callback2<WaterPurifierSetPeople> callback){
        WaterPurifierSetPeopleDialog dialog= WaterPurifierSetPeopleDialog.show(cx,guid);
        dialog.setPickListener(new WaterPurifierSetPeopleDialog.PickListener() {

            @Override
            public void onConfirm(Boolean flag,String memberCount) {
                WaterPurifierSetPeople msg=new WaterPurifierSetPeople();
                msg.setFlag(flag);
                msg.setMemberCount(memberCount);
                if (callback!=null){
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    //净水器智能设定
    public static DialogWaterPurifierSmartTimeSet newDialogWaterPurifierSmartTimeSet(Context cx,final Callback2<Object> callback){
        DialogWaterPurifierSmartTimeSet dialog= (DialogWaterPurifierSmartTimeSet) DialogWaterPurifierSmartTimeSet.show(cx);
        dialog.setPickListener(new DialogWaterPurifierSmartTimeSet.PickListener(){

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short value) {
                if (callback!=null){
                    LogUtils.i("20170524","value:"+value);
                    callback.onCompleted(value);
                }
            }


        });

        return dialog;
    }

    /**
     * 重量选择框
     *
     * @param cx
     * @param callback
     * @param model
     * @return
     */
    public static MicrowaveWeightSettingDialog newMicrowaveWeightSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short model) {
        final MicrowaveWeightSettingDialog dialog = MicrowaveWeightSettingDialog.show(cx, model);
        dialog.setPickListener(new MicrowaveWeightSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short weight) {
                Log.i("mic", "onConfirm");
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setWeight(weight);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     * 最近使用对话框
     * @param cx
     * @param callback2
     * @param
     * @return
     */
    public static Microwave526RecentlyUseDialog newMicrowave526RecentlyUseDialog(Context cx, final Callback2<UserAction> callback2,AbsMicroWave microWave){
        final Microwave526RecentlyUseDialog dialog = Microwave526RecentlyUseDialog.show(cx,microWave);
        dialog.setPickListener(new Microwave526RecentlyUseDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(UserAction userAction) {
                if (callback2!=null){
                    callback2.onCompleted(userAction);
                }
            }
        });
        return dialog;
    }

    public static Oven028RecentlyUseDialog newOven028RecentlyUseDialog(Context cx, final Callback2<OvenUserAction> callback2, AbsOven oven){
        final Oven028RecentlyUseDialog dialog = Oven028RecentlyUseDialog.show(cx,oven);
        dialog.setPickListener(new Oven028RecentlyUseDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(OvenUserAction ovenuserAction) {
                if (callback2!=null){
                    callback2.onCompleted(ovenuserAction);
                }
            }
        });
        return dialog;
    }

    public static Steam228RecentlyUseDialog newSteam228RecentlyUseDialog(Context cx, final Callback2<SteamUserAction> callback2, AbsSteamoven steam){
        final Steam228RecentlyUseDialog dialog = Steam228RecentlyUseDialog.show(cx,steam);
        dialog.setPickListener(new Steam228RecentlyUseDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(SteamUserAction steamuserAction) {
                if (callback2!=null){
                    callback2.onCompleted(steamuserAction);
                }
            }

        });
        return dialog;
    }

    public static Micro526PauseSettingDialog newMicro526PauseSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short model){
        final Micro526PauseSettingDialog dialog= Micro526PauseSettingDialog.show(cx,model);
        dialog.setPickListener(new Micro526PauseSettingDialog.PickListener(){

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short min) {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setTime(min);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }


        });
        return dialog;
    }


    /**
     *
     * @param cx
     * @param callback
     * @param model
     * @return
     */

    public static Microwave526WeightSettingDialog newMicrowave526WeightSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short model,AbsMicroWave microWave) {
        final Microwave526WeightSettingDialog dialog = Microwave526WeightSettingDialog.show(cx, model,microWave);
        dialog.setPickListener(new Microwave526WeightSettingDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(short weight) {
                Log.i("mic", "onConfirm");
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setWeight(weight);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     * 火力时间选择框
     *
     * @param cx
     * @param callback
     * @param model
     * @return
     */
    public static MicrowaveWeightTimeSettingDialog newMicrowaveWeightTimeSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short model,AbsMicroWave microWave) {
        final MicrowaveWeightTimeSettingDialog dialog = MicrowaveWeightTimeSettingDialog.show(cx, model,microWave);
        dialog.setPickListener(new MicrowaveWeightTimeSettingDialog.PickListener() {
            @Override
            public void onCancel() {
            }
            @Override
            public void onConfirm(short model, short fire, short min, short sec) {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setFire(fire);
                short sum=(short) (min*60+sec);
                msg.setTime(sum);
                msg.setModel(model);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     *
     * @param cx
     * @param callback
     * @return
     */
    public static Microwave526CleanDialog newMicrowave526CleanDialog(Context cx, final Callback2<Object> callback,AbsMicroWave microWave){
         final Microwave526CleanDialog dialog = Microwave526CleanDialog.show(cx,microWave);
        dialog.setPickListener(new Microwave526CleanDialog.PickListener(){

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short weight) {
                LogUtils.i("gg","weight:"+weight);
                if (callback!=null){
                    callback.onCompleted(weight);
                }
            }
        });
        return dialog;
    }


    public static Microwave526WeightTimeSettingDialog1 newMicrowave526WeightTimeSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short model,AbsMicroWave microWave) {
        final Microwave526WeightTimeSettingDialog1 dialog = Microwave526WeightTimeSettingDialog1.show(cx, model,microWave);
        dialog.setPickListener(new Microwave526WeightTimeSettingDialog1.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(short model, short fire, short min,short sec) {
                LogUtils.i("20170504","min:"+min+" "+" sec:"+sec);
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setFire(fire);
                short sum=(short) (min*60+sec);
                msg.setTime(sum);
                msg.setModel(model);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     *
     * @param cx
     * @param callback
     * @param model
     * @return
     */

    public static MicrowaveSettingDialog newMicrowaveSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, short model,AbsMicroWave microWave) {
        final MicrowaveSettingDialog dialog = MicrowaveSettingDialog.show(cx, model,microWave);
        dialog.setPickListener(new MicrowaveSettingDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(short model, short fire, short min,short sec) {
                LogUtils.i("20170504","min:"+min+" "+" sec:"+sec);
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setFire(fire);
                short sum=(short) (min*60+sec);
                msg.setTime(sum);
                msg.setModel(model);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }


    /**
     * 门控提示框
     *
     * @param cx
     * @param callback2
     * @param res
     * @return
     */
    public static BlackPromptDialog newBlackPromptDialog(Context cx, final Callback2<?> callback2, int res) {
        BlackPromptDialog dlg = BlackPromptDialog.show(cx, res);
        dlg.setPickListener(new BlackPromptDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(int what, Object o) {

            }
        });
        return dlg;
    }

    /**
     * 526门控对话框
     * @param cx
     * @param state
     * @param
     * @return
     */
    public static BlackPromptDialog526 new526BlackPromptDialog(Context cx, final Callback<?> callback, short state) {
        BlackPromptDialog526 dlg =BlackPromptDialog526.show(cx, state);
        dlg.setPickListener(new BlackPromptDialog526.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(int what, Object m) {

            }
        });

        return dlg;
    }

    public static BlackPromptDialog2 newBlackPromptDialog2(Context cx, View view) {
        return newBlackPromptDialog2(cx, view, 0);
    }

    public static BlackPromptDialog2 newBlackPromptDialog2(Context cx, View view, Integer style) {
        BlackPromptDialog2 dlg = null;
        if (style == 0)
            dlg = BlackPromptDialog2.show(cx, view);
        else
            dlg = BlackPromptDialog2.show(cx, view, style);
        return dlg;
    }

    /**
     * 倒计时
     * @param cx
     * @param callback2
     * @return
     */
    public static CountDownDialog526 newCountDownDialog526(Context cx, final Callback2<Object> callback2){
        CountDownDialog526 dlg=CountDownDialog526.show(cx,4);
        dlg.setPickListener(new  CountDownDialog526.PickListener(){

            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(String finish) {
                if (callback2!=null){
                    callback2.onCompleted(finish);
                }

            }
        });
        return dlg;
    }
    /**
     * 确认框
     *
     * @param cx
     * @param callback2
     * @param res
     * @return
     */
    public static BlackPromptConfirmDialog newBlackPromptConfirmDialog(Context cx, final Callback2<Object> callback2, int res) {
        BlackPromptConfirmDialog dlg = BlackPromptConfirmDialog.show(cx, res);
        dlg.setPickListener(new BlackPromptConfirmDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(String confirm) {
                callback2.onCompleted(confirm);
            }

        });
        return dlg;
    }

    /**
     * 确认取消框
     *
     * @param cx
     * @param callback2
     * @param res
     * @return
     */
    public static BlackPromptConfirmAndCancleDialog newBlackPromptConfirmAndCancleDialog(Context cx, final BlackPromptConfirmAndCancleDialog.PickListener callback2, int res) {
        BlackPromptConfirmAndCancleDialog dlg = BlackPromptConfirmAndCancleDialog.show(cx, callback2, res);
        return dlg;
    }

    /**
     * 微博时间选择框
     *
     * @return
     */
    public static MicrowaveTimeSettingDialog newMicrowaveTimeSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback) {
        final MicrowaveTimeSettingDialog dialog = MicrowaveTimeSettingDialog.show(cx);
        dialog.setPickListener(new MicrowaveTimeSettingDialog.PickListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onConfirm(short time) {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setTime(time);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    public static Microwave526TimeSettingDialog newMicrowave526TimeSettingDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback, AbsMicroWave microWave) {
        final Microwave526TimeSettingDialog dialog = Microwave526TimeSettingDialog.show(cx,microWave);
        dialog.setPickListener(new Microwave526TimeSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short time) {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setTime(time);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     * 烟机8700时间选择框
     *
     * @param cx
     * @param callback
     * @return
     */
    public static Fan8700RemindSoupSettingDialog newFan8700RemindSoupSettingDialog(Context cx, String tag,
                                                                                   final Callback2<MicroWaveWheelMsg> callback , int resId, String title) {
        final Fan8700RemindSoupSettingDialog dialog = Fan8700RemindSoupSettingDialog.show(cx, tag,resId, title);
        dialog.setPickListener(new Fan8700RemindSoupSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short hour, short min) {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setFire(hour);
                msg.setTime(min);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     * 烟机8700煲汤提醒选择框
     */
    public static Fan8700RemindSoupNoticeDialog newFan8700RemindSoupNoticeDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback
            , String title1, String title2, String btnTitle) {
        final Fan8700RemindSoupNoticeDialog dialog = Fan8700RemindSoupNoticeDialog.show(cx, title1, title2, btnTitle);
        dialog.setPickListener(new Fan8700RemindSoupNoticeDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm() {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    /**
     *
     */
    public static DeviceSelectNewDialog newDeviceSelectNewDialog(Context cx, List<IDevice> iDevices, final Callback<IDevice> callback){
        final  DeviceSelectNewDialog dialog = DeviceSelectNewDialog.show(cx,iDevices);
        dialog.setPickDeviceSelectLister(new DeviceSelectNewDialog.PickDeviceSelectLister(){
            @Override
            public void onConfirm(IDevice dev) {
                if (callback!=null){
                    callback.onSuccess(dev);
                }
            }
        });
        return dialog;
    }


    public static DeviceSelectNewOrientailDialog newDeviceSelectNewOrientailDialog(Context cx, List<IDevice> iDevices, final Callback<IDevice> callback){
        final DeviceSelectNewOrientailDialog dialog = DeviceSelectNewOrientailDialog.show(cx,iDevices);
        dialog.setPickNewDeviceSelectLister(new DeviceSelectNewOrientailDialog.PickNewDeviceSelectLister(){
            @Override
            public void onConfirm(IDevice dev) {
                if (callback!=null){
                    callback.onSuccess(dev);
                }
            }
        });
        return dialog;
    }

    public static DeviceSelectStoveHeadDialog newDeviceSelectStoveHeadDialog(Context cx, final Callback<Integer> callback){
        final DeviceSelectStoveHeadDialog dialog = DeviceSelectStoveHeadDialog.show(cx);
        dialog.setPickNewDeviceSelectLister(new DeviceSelectStoveHeadDialog.PickNewDeviceSelectLister() {

            @Override
            public void onConfirm(int stoveHead) {
                if (callback!=null){
                    callback.onSuccess(stoveHead);
                }
            }
        });
        return dialog;
    }

    public static DeviceSelectStoveDialog newDeviceSelectStoveDialog(Context cx, List<Stove> stoveList, final Callback<IDevice> callback){
        final DeviceSelectStoveDialog dialog = DeviceSelectStoveDialog.show(cx,stoveList);
        dialog.setPickDeviceSelectLister(new DeviceSelectStoveDialog.PickDeviceSelectLister() {


            @Override
            public void onConfirm(IDevice dev) {
                if (callback!=null){
                    callback.onSuccess(dev);
                }
            }
        });
        return dialog;
    }

    public static StoveSelectAllOffTips newStoveSelectAllOffTips(Context cx, Stove stove, final Callback<Integer> callback){
        final StoveSelectAllOffTips dialog = StoveSelectAllOffTips.show(cx,stove);
        dialog.setAllOffTipsDialogLister(new StoveSelectAllOffTips.AllOffTipsDialogLister() {
            @Override
            public void onConfirm(Integer stoveHeadId) {
                if (callback!=null){
                    callback.onSuccess(stoveHeadId);
                }
            }
        });
        return dialog;
    }

    public static StoveSelectTipsDialog newStoveSelectTipsDialog(Context cx, Stove stove, Integer integer, final Callback<Integer> callback){
        final StoveSelectTipsDialog dialog = StoveSelectTipsDialog.show(cx,stove,integer);
        dialog.setStoveSelectTipsDialogLister(new StoveSelectTipsDialog.StoveSelectTipsDialogLister() {
            @Override
            public void onConfirm(Integer flag) {
                if (callback!=null){
                    callback.onSuccess(flag);
                }
            }

        });
        return dialog;
    }


    /**
     * 烟机8700厨房净化
     *
     * @param cx
     * @param callback
     * @return
     */
    public static Fan8700KitchenCleaningDialog newFan8700KitchenCleaningDialog(Context cx, final Callback2<MicroWaveWheelMsg> callback) {
        final Fan8700KitchenCleaningDialog dialog = Fan8700KitchenCleaningDialog.show(cx);
        dialog.setPickListener(new Fan8700KitchenCleaningDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(short min) {
                MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
                msg.setTime(min);
                if (callback != null) {
                    callback.onCompleted(msg);
                }
            }
        });
        return dialog;
    }

    public static RecipeSearchDialog newRecipeSearchDialog(Context cx, RecipeSearchDialog.OnSearchCallback callback){
        RecipeSearchDialog searchDialog = RecipeSearchDialog.show(cx,callback);
        return searchDialog;
    }

    //菜谱筛选弹窗
    public static PopupWindow newRecipeFilterPopWindow(Context cx, Map sourceData, Map electricKitchenData, Map flavorData, final Callback2<DeviceWorkMsg> callback) {
        RecipeFilterPopWindow pop = new RecipeFilterPopWindow(cx, sourceData, electricKitchenData, flavorData);
        return pop;
    }


}
