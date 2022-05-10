package com.robam.roki.ui.page;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.popoups.PopoupHelper;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenAlarmEvent;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Oven.Oven039;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.common.pojos.device.Steamoven.SteamStatus;
import com.robam.common.ui.UiHelper;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.model.NormalModeItemMsg;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.OvenBrokenDialog;
import com.robam.roki.ui.view.HomeRecipeView32;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/21.
 */
public class DeviceOvenPage extends HeadPage {

    Oven039 oven;
    //
    @InjectView(R.id.chickenWingItem)//9个常用
            LinearLayout chickenWingItem;
    @InjectView(R.id.cakeItem)
    LinearLayout cakeItem;
    @InjectView(R.id.breadItem)
    LinearLayout breadItem;
    @InjectView(R.id.streakyPorkItem)
    LinearLayout streakyPorkItem;
    @InjectView(R.id.steakItem)
    LinearLayout steakItem;
    @InjectView(R.id.pisaItem)
    LinearLayout pisaItem;
    @InjectView(R.id.cookieItem)
    LinearLayout cookieItem;
    @InjectView(R.id.vegetableItem)
    LinearLayout vegetableItem;
    @InjectView(R.id.seafoodItem)
    LinearLayout seafoodItem;
    @InjectView(R.id.llProMode)
    LinearLayout llProMode;

    @InjectView(R.id.llOvenPage)
    LinearLayout llOvenPage;

    static final int PollStatus = 10;

    @InjectView(R.id.imgChickenWing)
    ImageView imgChickenWing;
    @InjectView(R.id.imgCake)
    ImageView imgCake;
    @InjectView(R.id.imgBread)
    ImageView imgBread;
    @InjectView(R.id.imgStreakPork)
    ImageView imgStreakPork;
    @InjectView(R.id.imgPisa)
    ImageView imgPisa;
    @InjectView(R.id.imgCookie)
    ImageView imgCookie;
    @InjectView(R.id.imgVegetable)
    ImageView imgVegetable;
    @InjectView(R.id.imgProMode)
    ImageView imgNormalMode;
    @InjectView(R.id.imgSteak)
    ImageView imgSteak;
    @InjectView(R.id.imgSeafood)
    ImageView imgSeafood;


    @InjectView(R.id.titleChickenWing)//9个常用
            TextView titleChickenWing;
    @InjectView(R.id.titleCake)//9个常用
            TextView titleCake;
    @InjectView(R.id.titleBread)//9个常用
            TextView titleBread;
    @InjectView(R.id.titleStreakPork)//9个常用
            TextView titleStreakPork;
    @InjectView(R.id.titleSteak)//9个常用
            TextView titleSteak;
    @InjectView(R.id.titlePisa)//9个常用
            TextView titlePisa;
    @InjectView(R.id.titleSeafood)//9个常用
            TextView titleSeafood;
    @InjectView(R.id.titleCookie)//9个常用
            TextView titleCookie;
    @InjectView(R.id.titleVegetable)//9个常用
            TextView titleVegetable;
    @InjectView(R.id.txtProMode)//9个常用
            TextView txtNormalMode;

    @InjectView(R.id.selfCleaningButton)
    RelativeLayout selfCleaningButton;
    @InjectView(R.id.txtSelfCleaning)
    TextView txtSelfCleaning;

    @InjectView(R.id.disconnectHintView)
    LinearLayout disconnectHintView;

    @InjectView(R.id.relSwitch)
    RelativeLayout relSwitch;
    @InjectView(R.id.imgSwitchLine)
    ImageView imgSwitchLine;
    @InjectView(R.id.txtSwitch)
    TextView txtSwitch;
    @InjectView(R.id.imgSwitch)
    ImageView imgSwitch;
    @InjectView(R.id.txtRecipe)
    TextView txtRecipe;
    Resources resources;
    short status = 1;
    static boolean isConnect = true;
    IRokiDialog rokiDialog = null;
    List<String> stringTempList = Lists.newArrayList();
    List<String> stringTimeList = Lists.newArrayList();
    private final int TEMP = 0;//温度
    private final int TIME = 1;//时间
    private IRokiDialog mDeviceSwitchDialog = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PollStatus:
                    if (oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause) {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, oven.getID());
                        UIService.getInstance().postPage(PageKey.DeviceOvenWorking039, bundle);
                    }
                    break;
                case TEMP:
                    setDeviceTempAndTime((String) msg.obj);
                    break;
                case TIME:
                    setDeviceTempAndTime((String) msg.obj);
                default:
                    break;
            }
        }
    };
    private NormalModeItemMsg msg;


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven_normal, container, false);
        resources = getResources();
        ButterKnife.inject(this, contentView);
        initView();
        setPageTitle("");
        return contentView;
    }

    // -----------------------------------0----- 开关按钮 ------------------------------------------
    @OnClick(R.id.relSwitch)
    public void onClickSwitch() {
        short status = (oven.status == OvenStatus.Off || oven.status == OvenStatus.Wait) ? OvenStatus.On : OvenStatus.Off;
        setStatus(status);
    }

    private void setStatus(short status) {
        if (!checkConnection()) {
            return;
        }

        oven.setOvenStatus(status, new VoidCallback() {
            @Override
            public void onSuccess() {
                setSwitch(oven.status);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }

    void initView() {
        mDeviceSwitchDialog = RokiDialogFactory.createDialogByType(cx,DialogUtil.DIALOG_TYPE_09);
        disconnectHintView.setVisibility(View.INVISIBLE);
        onRefresh();
    }

    private void onRefresh() {
        if (Plat.DEBUG)
            Log.i("onRefresh", "是否连接" + oven.isConnected() + ",是否oven关闭" +
                    oven.status);
        disconnectHintView.setVisibility(oven != null && !oven.isConnected()
                ? View.VISIBLE
                : View.INVISIBLE);

        if (oven == null)
            return;

        boolean isOn = oven.isConnected() && oven.status != OvenStatus.Off;
        setSwitch(oven.status);
    }

    @OnClick(R.id.txtRecipe)
    public void onClickRecipe() {
        //ToastUtils.show(R.string.please_look_forward_opening, Toast.LENGTH_SHORT);
        getActivity().onBackPressed();
        HomeRecipeView32.recipeCategoryClick(DeviceType.RDKX);

    }

    private void setSwitch(int status) {

        boolean isOn = false;
        isOn = !(status == SteamStatus.Wait || status == SteamStatus.Off || !isConnect);
        imgChickenWing.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_chicken_wing_working : R.mipmap.ic_device_oven_chicken_wing_unworking));
        imgCake.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_cake_working : R.mipmap.ic_device_oven_cake_unworking));
        imgBread.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_bread_working : R.mipmap.ic_device_oven_bread_unworking));
        imgStreakPork.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_streaky_pork_working : R.mipmap.ic_device_oven_streaky_pork_unworking));
        imgSteak.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_steak_working : R.mipmap.ic_device_oven_steak_unworking));
        imgSeafood.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_seafood_working : R.mipmap.ic_device_oven_seafood_unworking));
        imgCookie.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_cookie_working : R.mipmap.ic_device_oven_cookie_unworking));
        imgVegetable.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_vegetable_working : R.mipmap.ic_device_oven_vegetable_unworking));
        imgNormalMode.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_normal_working : R.mipmap.ic_device_oven_normal_unworking));
        imgPisa.setImageDrawable(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_pisa_working : R.mipmap.ic_device_oven_pisa_unworking));

        titleChickenWing.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleCake.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleBread.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleStreakPork.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleSteak.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titlePisa.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleSeafood.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleCookie.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        titleVegetable.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        txtNormalMode.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));
        txtSelfCleaning.setBackground(resources.getDrawable(isOn ? R.mipmap.ic_device_oven_white_circle : R.mipmap.ic_device_oven_gray_circle));
        txtSelfCleaning.setTextColor(resources.getColor(isOn ? R.color.c14 : R.color.Gray_57));

        if (status == SteamStatus.Wait || !isConnect) {
            imgSwitch.setImageResource(R.mipmap.ic_device_switch_normal);
            txtSwitch.setTextColor(resources.getColor(R.color.c19));
            txtSwitch.setText(R.string.device_close);
            imgSwitchLine.setImageResource(R.mipmap.img_steamoven_leanline_gray);
        } else if (status == SteamStatus.Off) {
            imgSwitch.setImageResource(R.mipmap.img_steamoven_switch_open);
            txtSwitch.setTextColor(resources.getColor(R.color.c14));
            txtSwitch.setText(R.string.device_close);
            imgSwitchLine.setImageResource(R.mipmap.img_steamoven_leanline_white);
        } else if (status == SteamStatus.On) {
            imgSwitch.setImageResource(R.mipmap.img_switch_yellow);
            txtSwitch.setTextColor(resources.getColor(R.color.home_bg));
            txtSwitch.setText(R.string.device_open);
            imgSwitchLine.setImageResource(R.mipmap.img_steamoven_leanline_yellow);
        }
    }



    //------------------------------------------- 专业模式按钮 --------------------------------------
    @OnClick(R.id.llProMode)
    public void onClickContext() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            if (UiHelper.checkAuthWithDialog(getContext(), PageKey.UserLogin)) {
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, oven.getID());
                UIService.getInstance().postPage(PageKey.DeviceOvenProfessionalSetting039, bundle);
            }
        }
    }


    @OnClick({R.id.chickenWingItem})
    public void onClickChicken() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {

            modeSelection(StringConstantsUtil.STRING_CHICKENWING);
        }
    }

    @OnClick({R.id.cakeItem})
    public void onClickCake() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_CAKE);
        }
    }

    @OnClick({R.id.breadItem})
    public void onClickBread() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_BREAD);
        }
    }

    @OnClick({R.id.streakyPorkItem})
    public void onClickStreakPork() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_STREAKY);
        }
    }

    @OnClick({R.id.steakItem})
    public void onClickSteak() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_STEAK);
        }
    }

    @OnClick({R.id.pisaItem})
    public void onClickPisa() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_PIZZA);
        }
    }

    @OnClick({R.id.seafoodItem})
    public void onClickSeaFood() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_SEAFOOT);
        }
    }

    @OnClick({R.id.cookieItem})
    public void onClickCookie() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_COOKIES);
        }
    }

    @OnClick({R.id.vegetableItem})
    public void onClickVegetable() {
        if (!oven.isConnected())
            return;
        if (oven.status == OvenStatus.Off) {
            openDeviceDialog();
        } else if (oven.status != OvenStatus.Off) {
            modeSelection(StringConstantsUtil.STRING_VEGETABLES);
        }


    }

    //提示打开设备
    private void openDeviceDialog() {
        mDeviceSwitchDialog.setContentText(R.string.open_device);
        mDeviceSwitchDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
        mDeviceSwitchDialog.show();
    }


    private void modeSelection(final String type) {
        msg = new NormalModeItemMsg();
        msg.setType(type);
        int rearIndex = typeSelectInitIndex(type);
        if(rokiDialog != null){
            rokiDialog = null;
        }
        if (null == rokiDialog){
            rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        }
        rokiDialog.setWheelViewData(getListRecipeType(type), null,getListTime(type), false, 0, 0, rearIndex, new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                Message message = handler.obtainMessage();
                message.what = TEMP;
                message.obj = contentFront;
                handler.sendMessage(message);
            }
        }, null, new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                Message message = handler.obtainMessage();
                message.what = TIME;
                message.obj = contentRear;
                handler.sendMessage(message);
            }
        });
        rokiDialog.show();
    }

    private int typeSelectInitIndex(String type) {
        int rearIndex = 0;
        switch (type){
            case StringConstantsUtil.STRING_CHICKENWING:
                rearIndex = 2;
                break;
            case StringConstantsUtil.STRING_CAKE:
                rearIndex = 2;
                break;
            case StringConstantsUtil.STRING_BREAD:
                rearIndex = 3;
                break;
            case StringConstantsUtil.STRING_STREAKY:
                rearIndex = 0;
                break;
            case StringConstantsUtil.STRING_STEAK:
                rearIndex =2;
                break;
            case StringConstantsUtil.STRING_PIZZA:
                rearIndex =4;
                break;
            case StringConstantsUtil.STRING_SEAFOOT:
                rearIndex =3;
                break;
            case StringConstantsUtil.STRING_COOKIES:
                rearIndex =4;
                break;
            case StringConstantsUtil.STRING_VEGETABLES:
                rearIndex =0;
                break;
        }

        return rearIndex;
    }

    //设置时间和温度
    private void setDeviceTempAndTime(String data) {
        if (data.contains(StringConstantsUtil.STRING_DEGREE_CENTIGRADE)){
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringTempList.add(removetTempString);
        }
        if (data.contains(StringConstantsUtil.STRING_MINUTES)){
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringTimeList.add(removeTimeString);
        }
        rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.dismiss();
                msg.setTime(String.valueOf(stringTimeList.get(stringTimeList.size()-1)));
                msg.setTemperature(String.valueOf(stringTempList.get(stringTempList.size()-1)));
                oven.setOvenAirBarbecue(Short.valueOf(stringTimeList.get(stringTimeList.size()-1)),
                        Short.valueOf(stringTempList.get(stringTempList.size()-1)), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                Bundle bundle1 = new Bundle();
                                bundle1.putString(PageArgumentKey.Guid, oven.getID());
                                bundle1.putSerializable("msg",msg);
                                UIService.getInstance().postPage(PageKey.DeviceOvenWorking039, bundle1);
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.showThrowable(t);
                            }
                        });
            }
        });

    }

    protected List<String> getListRecipeType(String s) {
        List<String> list = Lists.newArrayList();
        if (s.equals(cx.getString(R.string.device_oven_model_jichi))) {
            list.add(180 + StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        } else if (s.equals(cx.getString(R.string.device_oven_model_dangao))) {
            list.add(160+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        } else if (s.equals(cx.getString(R.string.device_oven_model_mianbao)))
            list.add(165+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        else if (s.equals(cx.getString(R.string.device_oven_model_wuhuarou)))
            list.add(215+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        else if (s.equals(cx.getString(R.string.device_oven_model_niupai)))
            list.add(180+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        else if (s.equals(cx.getString(R.string.device_oven_model_pisa)))
            list.add(200+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        else if (s.equals(cx.getString(R.string.device_steam_model_shucai)))
            list.add(200+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        else if (s.equals(cx.getString(R.string.device_oven_model_haixian)))
            list.add(200+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        else if (s.equals(cx.getString(R.string.device_oven_model_binggan)))
            list.add(170+ StringConstantsUtil.STRING_DEGREE_CENTIGRADE);
        return list;
    }

    protected List<String> getListTime(String type) {
        List<String> list1 = Lists.newArrayList();
        if (type.equals(cx.getString(R.string.device_oven_model_jichi))) {
            for (int i = 14; i <= 23; i++) {
                list1.add(i + StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_dangao))) {
            for (int i = 23; i <= 28; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_mianbao))) {
            for (int i = 15; i <= 22; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_wuhuarou))) {
            for (int i = 45; i <= 50; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_niupai))) {
            for (int i = 13; i <= 20; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_pisa))) {
            for (int i = 16; i <= 25; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_haixian))) {
            for (int i = 20; i <= 25; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_oven_model_binggan))) {
            for (int i = 12; i <= 20; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        } else if (type.equals(cx.getString(R.string.device_steam_model_shucai))) {
            for (int i = 15; i <= 30; i++) {
                list1.add(i+ StringConstantsUtil.STRING_MINUTES);
            }
        }
        return list1;
    }

    private boolean checkConnection() {
        if (!oven.isConnected()) {
            ToastUtils.showShort(R.string.oven_invalid_error);
            return false;
        } else {
            return true;
        }
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (oven == null || !Objects.equal(oven.getID(), event.device.getID()))
            return;
        disconnectHintView.setVisibility(event.isConnected ? View.INVISIBLE : View.VISIBLE);
        isConnect = event.isConnected;
        setSwitch(oven.status);
    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (Plat.DEBUG)
            Log.i("DeviceOvenPage->onEvent", "true");
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        onRefresh();
        handler.sendEmptyMessage(PollStatus);
    }

}
