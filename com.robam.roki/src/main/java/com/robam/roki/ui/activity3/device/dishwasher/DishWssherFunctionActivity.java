package com.robam.roki.ui.activity3.device.dishwasher;

import static com.legent.ContextIniter.cx;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.gson.Gson;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.HideFunc;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.dishWasher.DishWasherStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.FanDeviceRemindSoup;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DeviceWorkActivity;
import com.robam.roki.ui.activity3.device.base.other.ActivityManager;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.activity3.device.base.other.SortUtils;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.base.table.DishModeEntity;
import com.robam.roki.ui.activity3.device.dishwasher.adapter.RvDishWssherFuntionAdapter;
import com.robam.roki.ui.activity3.device.dishwasher.bean.AllModeBean;
import com.robam.roki.ui.activity3.device.dishwasher.other.DishDialogHelper;
import com.robam.roki.ui.activity3.device.fan.FanLinkageActivity;
import com.robam.roki.ui.activity3.device.steamoven.SteamOvenModeActivity;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.litepal.LitePal;

import java.util.List;

import skin.support.content.res.SkinCompatResources;

/**
 * author : huxw
 * time   : 2022/06/07
 * desc   : 洗碗机功能界面
 */
public class DishWssherFunctionActivity<DishWssher extends AbsDishWasher> extends DeviceBaseActivity implements RvDishWssherFuntionAdapter.FuntionOnClickListener {
    private DishWssher dishWssher;
    private RecyclerView rvFunction;
    private RvDishWssherFuntionAdapter adapter;
    /**
     * 背景功能区
     */
    private BackgroundFunc backgroundFunc;
    /**
     * 漂洗剂
     */
    public final static String RINSE = "Rinse";
    /**
     * 水软盐
     */
    public final static String SALF = "Salt";


    @Subscribe
    public void onEvent(DisherWasherStatusChangeEvent event) {
        if (dishWssher == null || !Objects.equal(dishWssher.getID(), event.pojo.getID())) {
            return;
        }
        this.dishWssher = (DishWssher) event.pojo;

        setConnectedState(!dishWssher.isConnected());
        //设置提醒
        setNotifity();
        if (dishWssher.powerStatus > 1 && ActivityManager.getInstance().getTopActivity().equals(this)){
            //跳转到工作界面
            Intent intent = new Intent(this, DishWorkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
            startActivity(intent);
        }
    }

    /**
     * 上显现变更通知
     *
     * @param event
     */
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (dishWssher == null || !Objects.equal(dishWssher.getID(), event.device.getID()))
            return;

        setConnectedState(!event.isConnected);
        if (!event.isConnected) {
            toast(R.string.device_new_connected);

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_dish_function;
    }

    @Override
    protected void initView() {
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(8, getContext()));
        adapter = new RvDishWssherFuntionAdapter();
        rvFunction.setAdapter(adapter);
    }

    /**
     * 数据参数获取成功
     *
     * @param deviceResponse
     */
    @Override
    public void onLoadData(Reponses.DeviceResponse deviceResponse) {
        if (deviceResponse == null) {
            toast("设备参数为空");
            return;
        }
        if (mDevice instanceof AbsDishWasher) {
            dishWssher = (DishWssher) mDevice;
        }
        //设置标题栏名称
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        //背景图片
        String bgImgUrl = deviceResponse.viewBackgroundImg;
        //设置背景图片
        setScollBgImg(adapter, bgImgUrl);

        //主功能区数据
        MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
        List<DeviceConfigurationFunctions> mainFuncList = mainFunc.deviceConfigurationFunctions;
        //主功能排序
//        mainFuncList = SortUtils.funSort(mainFuncList, 0);

        //其他功能区
        OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;
        //背景功能区
        backgroundFunc = deviceResponse.modelMap.backgroundFunc;

//        //获取第一模块并排序
//        List<DeviceConfigurationFunctions> otherFun1 = SortUtils.funSort(deviceConfigurationFunctions, 1);
//        //获取第二模块并排序
        List<DeviceConfigurationFunctions> otherFun2 = SortUtils.funSort(deviceConfigurationFunctions, 2);
        adapter.addData(mainFuncList);
        adapter.addData(otherFun2);
        //添加点击事件
        adapter.addFuntionOnClickListener(this);
        HideFunc hideFunc = deviceResponse.modelMap.hideFunc;
        savaMode(hideFunc.deviceConfigurationFunctions);
        loadSuccess = true;
        //读取烟机设定状态
    }



    /**
     * 数据参数获取失败
     */
    @Override
    public void onFail() {
        toast("设备参数请求失败");
        finish();
    }

    private void setNotifity() {
        if ((dishWssher.LackRinseStatus == 1 && PreferenceUtils.getBool(RINSE, true))
                || (dishWssher.LackSaltStatus == 1 && PreferenceUtils.getBool(SALF, true))) {
            View rlMessage = findViewById(R.id.rl_message);
            rlMessage.setVisibility(View.VISIBLE);
            findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rlMessage.setVisibility(View.INVISIBLE);
                }
            });
            TextView tvMessage = (TextView) findViewById(R.id.tv_device_message);
            tvMessage.setVisibility(View.VISIBLE);
            String s = "";
            DeviceConfigurationFunctions deviceConfigurationFunctions;
            if (dishWssher.LackRinseStatus == 1) {
                s = "漂洗剂";
                deviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions.get(0);
            } else {
                s = "水软盐";
                deviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions.get(1);
            }
            SpannableString demoSpannable = new SpannableString(s + "不足，请及时添加，点此查看教程");

            demoSpannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
//                DishWssherFunctionActivity.this.onClick(deviceConfigurationFunctions);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(Color.parseColor("#61ACFF"));
                    ds.setUnderlineText(true);
                }

            }, 12, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 下划线
            UnderlineSpan underlineSpan = new UnderlineSpan();
            demoSpannable.setSpan(underlineSpan, 12, 18, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            // TextView设置内容
            tvMessage.setText(demoSpannable);
            tvMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DishWssherFunctionActivity.this.onClick(deviceConfigurationFunctions);
                }
            });
        }else {
            View rlMessage = findViewById(R.id.rl_message);
            rlMessage.setVisibility(View.GONE);
        }
    }

    /**
     * 点击事件
     *
     * @param func
     */
    @Override
    public void onClick(DeviceConfigurationFunctions func) {
//        toast(func.functionName);
        if (! dishWssher.isConnected()){
            toast("设备不在线");
            return;
        }

        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, dishWssher.getGuid().getGuid());
        bd.putSerializable(DeviceBaseFuntionActivity.FUNCTION, func);
        Intent intent = new Intent();
        String functionCode = func.functionCode;
        switch (functionCode) {
            case FuncCodeKey.AUTOVENTILATION:
                new DishDialogHelper(this).autoVen(new DialogUtils.OnSelectListener() {
                    @Override
                    public void onReset() {
                        //通风换气

                        if (dishWssher.powerStatus == 0) {
                            //关机状态需要先开机
                            dishWssher.setDishWasherStatusControl((short) 1, new VoidCallback() {
                                @Override
                                public void onSuccess() {
                                    startWork(9 , 0 , 1 , 0 , 0  , 0 , 0);
                                }

                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });
                        } else {
                            startWork(9 , 0 , 0 , 0 , 0  , 0 , 0);
                        }

                    }
                });
                break;
            case FuncCodeKey.FUNC_ZYMS:
                intent.setClass(this, DishModeSelectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            case FuncCodeKey.FUNC_JSTJ:
                intent.setClass(this, DishWaterCensusActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            case "func_rinse_course":
            case "func_salf_course":
                intent.setClass(this, DishCourseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            default:
                new DishDialogHelper(this).funcSelectDialog(func, new DialogUtils.OnSelectListener() {
                    @Override
                    public void onSelectFunc(int workMode, int downWsshSw, int autoVentiSw, int drySw) {

                    }
                });
                break;
        }
    }

    /**
     * 关机
     */
    @Override
    protected void onSwitch() {
        super.onSwitch();
        if (dishWssher.powerStatus == DishWasherStatus.off) {
            ToastUtils.show(getString(R.string.oven_common_off), Toast.LENGTH_SHORT);
        } else {
            if (dishWssher.powerStatus == DishWasherStatus.working || dishWssher.powerStatus == DishWasherStatus.pause) {
                final IRokiDialog closeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
                closeDialog.setTitleText(R.string.device_off);
                closeDialog.setContentText(R.string.device_off_desc);
                closeDialog.show();
                closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeDialog.dismiss();
                        off();

                    }
                });
                closeDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (closeDialog.isShow()) {
                            closeDialog.dismiss();
                        }
                    }
                });
            } else {
                off();
            }

        }
    }

    /**
     * 关机
     */
    private void off() {
        dishWssher.setDishWasherStatusControl(DishWasherStatus.off, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast(R.string.oven_common_off);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 更多页面
     */
    @Override
    protected void toMore() {
        super.toMore();
        Intent intent = new Intent(this, DishMoreActivity.class);
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
        startActivity(intent);
    }

    /**
     *
     * @param code 模式code
     * @param downWsshSw 下层洗开关
     * @param autoVentiSw 自动换气开关
     * @param drySw 加强干燥开关
     * @param orderTime 预约时间 分钟
     * @param auxFunc
     * @param orderTime
     */
    public  void startWork(int code , int downWsshSw ,int autoVentiSw  , int drySw , int orderSw , int orderTime ,  int auxFunc){


        dishWssher.setDishWasherWorkMode(code, downWsshSw, autoVentiSw, drySw, orderSw, orderTime, new VoidCallback() {
            @Override
            public void onSuccess() {
                toast("设置成功");

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    private void savaMode( List<DeviceConfigurationFunctions> funcs) {
        boolean exist = LitePal.isExist(DishModeEntity.class);
        if (exist){
            DishModeEntity bean = LitePal.where("dt = ? ", mDevice.getDt()).findFirst(DishModeEntity.class);
            if (bean != null){
                loadSuccess = true ;
                return;
            }
        }

        if (funcs == null || funcs.size() < 2) {
            loadSuccess = true ;
            return;
        }
        AllModeBean allModeBean = new Gson().fromJson(funcs.get(1).functionParams, AllModeBean.class);
        LitePal.saveAll(allModeBean.modes);
        loadSuccess = true ;

    }
}