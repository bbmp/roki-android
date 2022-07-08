package com.robam.roki.ui.activity3.device.fan;

import static com.legent.ContextIniter.cx;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.base.BaseDialog;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.SmartParams;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.fan.FanStatus;
import com.robam.common.util.AlarmManagerUtil;
import com.robam.roki.R;
import com.robam.roki.broadcast.Fan8700alarmReceiver;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.FanDeviceRemindSoup;
import com.robam.roki.model.bean.FanKitchenCleanParams;
import com.robam.roki.model.bean.FanMainParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceBaseActivity;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.DeviceWorkActivity;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.base.other.FuncCodeKey;
import com.robam.roki.ui.activity3.device.base.other.SortUtils;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.dishwasher.DishMoreActivity;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuntionAdapter;
import com.robam.roki.ui.activity3.device.fan.adapter.RvPickerAdapter;
import com.robam.roki.ui.mdialog.PickerLayoutManager;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author : huxw
 * time   : 2022/06/07
 * desc   : 烟机功能界面
 */
public final class FanFunctionActivity<Fan extends AbsFan> extends DeviceBaseActivity implements RvFuntionAdapter.FuntionOnClickListener {
    private Fan fan;
    private RecyclerView rvFunction;
    private RvFuntionAdapter adapter;


    /**
     * 设备状态变更
     *
     * @param event
     */
    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        LogUtils.i("20200611", "FanStatusChangedEvent:::" + event.pojo.toString());
        if (fan == null || !Objects.equal(fan.getID(), event.pojo.getID())) {
            return;
        }
        //数据未加载完毕
        if (!loadSuccess) {
            return;
        }
        fan = (Fan) event.pojo;
        setConnectedState(!fan.isConnected());
        //更新主功能区数据
        adapter.fanNotify(fan);
    }

    /**
     * 上显现变更通知
     *
     * @param event
     */
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (fan == null || !Objects.equal(fan.getID(), event.device.getID()))
            return;
        //设置设备状态
        setConnectedState(event.isConnected);
        if (!event.isConnected) {
            toast(R.string.device_new_connected);

        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_fan_function;
    }

    @Override
    protected void initView() {
        rvFunction = findViewById(R.id.rv_function);
        rvFunction.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //设置功能区块间距
        rvFunction.addItemDecoration(new VerticalItemDecoration(18, getContext()));
        adapter = new RvFuntionAdapter();
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
        if (mDevice instanceof AbsFan) {
            fan = (Fan) mDevice;
        }
        //设置标题栏名称
        setTitle(mDevice.getName() == null || mDevice.getName().equals(mDevice.getCategoryName()) ? mDevice.getDispalyType() : mDevice.getName());

        //背景图片
        String bgImgUrl = deviceResponse.viewBackgroundImg;
        //设置背景图片
//        setDeviceBgImg(bgImgUrl);
        setScollBgImg(adapter , bgImgUrl);
        //主功能区数据
        MainFunc mainFunc = deviceResponse.modelMap.mainFunc;
        List<DeviceConfigurationFunctions> mainFuncList = mainFunc.deviceConfigurationFunctions;
        //主功能排序
        mainFuncList = SortUtils.funSort(mainFuncList, 0);

        //其他功能区
        OtherFunc otherFunc = deviceResponse.modelMap.otherFunc;
        List<DeviceConfigurationFunctions> deviceConfigurationFunctions = otherFunc.deviceConfigurationFunctions;

        //获取第一模块并排序
        List<DeviceConfigurationFunctions> otherFun1 = SortUtils.funSort(deviceConfigurationFunctions, 1);
        //获取第二模块并排序
        List<DeviceConfigurationFunctions> otherFun2 = SortUtils.funSort(deviceConfigurationFunctions, 2);
        adapter.addData(mainFuncList);
        adapter.addData(otherFun1);
        adapter.addData(otherFun2);
        //添加点击事件
        adapter.addFuntionOnClickListener(this);
        loadSuccess = true;
        //读取烟机设定状态
        redSmartConfig();
    }


    /**
     * 读取烟机设定状态
     */
    private void redSmartConfig() {
        fan.getSmartConfig(new Callback<SmartParams>() {
            @Override
            public void onSuccess(SmartParams smartParams) {
                //更新主功能区数据
                adapter.fanNotify(fan);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }
    /**
     * 数据参数获取失败
     */
    @Override
    public void onFail() {
        toast("设备参数请求失败");
        finish();
    }

    /**
     * 点击事件
     *
     * @param func
     */
    @Override
    public void onClick(DeviceConfigurationFunctions func) {
//        toast(func.functionName);
        if (! fan.isConnected()){
            toast("设备不在线");
            return;
        }

        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, fan.getGuid().getGuid());
        bd.putSerializable(DeviceBaseFuntionActivity.FUNCTION, func);
        Intent intent = new Intent();
        String functionCode = func.functionCode;

        switch (functionCode) {
            case FuncCodeKey.FRY:
            case FuncCodeKey.DECOCT:
            case FuncCodeKey.STEW:
                //设置烟机档位
                setLevel(func);

                break;
            case FuncCodeKey.KITCHEN:
                //通风换气
                kitchenCleanupDialog(func);
                break;
            case FuncCodeKey.TIMEREMINDING:
                //计时提醒
                timeReminding(func);
                break;
            case FuncCodeKey.SMOKELINKAGE:
                //联动功能
                intent.setClass(this, FanLinkageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                //烟灶联动
                break;
            case FuncCodeKey.OILNETDET:
                //油网检测
                oilNetDet();
                break;
            case FuncCodeKey.HOLIDAY:
                //假日模式
                intent.setClass(this, FanHolidayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            case FuncCodeKey.SETLINKAGETIME:
                //设置联动时间
                setLinkageTime();
                break;
            case FuncCodeKey.AUTOPOWER:
                //变速巡航
                intent.setClass(this, FanConSuctionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bd);
                startActivity(intent);
                break;
            default:
                toast("暂不支持此功能");
                break;

        }
    }

    /**
     * 计时提醒
     */
    private void timeReminding(DeviceConfigurationFunctions func) {
        String functionParams = func.functionParams;
        try {
            FanDeviceRemindSoup fanDeviceRemindSoup = JsonUtils.json2Pojo(functionParams, FanDeviceRemindSoup.class);
            if (fanDeviceRemindSoup == null) return;
            List<Integer> hour = fanDeviceRemindSoup.getParam().getHour().getValue();
            List<String> hours = generateModelWheelData(hour.get(hour.size() -1) ,hour.get(0));
             List<Integer> minute = fanDeviceRemindSoup.getParam().getMinute().getValue();
             List<String> minus = generateModelWheelData(minute.get(1) ,minute.get(0));
             //获取默认参数
            int defHour = fanDeviceRemindSoup.getParam().getHourDefault().getValue();
            int defMinu = fanDeviceRemindSoup.getParam().getMinuteDefault().getValue();


            new DialogUtils(this).hourMinuSelect(hours, minus,defHour ,defMinu ,  new DialogUtils.OnSelectListener() {
                 @Override
                 public void onSelectTime(int data) {
                    toast(data+"");
                    PreferenceUtils.setLong("timeReminding" , System.currentTimeMillis() + (long) data * 60 * 1000);
                    startBroadcastReceiver(System.currentTimeMillis() + (long) data * 60 * 1000);
                    adapter.notifyDataSetChanged();
                 }
             });
        } catch (Exception e) {
            toast("平台配置参数错误");
            e.printStackTrace();
        }
    }

    /**
     * 开启定时广播
     * @param time
     */
    private void startBroadcastReceiver(long time) {
        AlarmManager alarmManager = AlarmManagerUtil.getInstance(cx);
        Intent intent = new Intent(cx, Fan8700alarmReceiver.class);
        intent.setAction("Fan8700alarmReceiver");
        intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(cx, 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
    /**
     * 获取配置时间范围
     * @param max
     * @param start
     * @return
     */
    private List<String> generateModelWheelData(int max, int start) {
        List<String> list = Lists.newArrayList();
        for (int i = start; i <= max; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 关机
     */
    @Override
    protected void onSwitch() {
        super.onSwitch();
        if (fan.level != AbsFan.PowerLevel_0 || fan.status == FanStatus.On) {
            final IRokiDialog closeDialog = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_10);
            closeDialog.setTitleText(R.string.device_off);
            closeDialog.setContentText(R.string.device_off_desc);
            closeDialog.show();
            closeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeDialog.dismiss();
                    fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
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
        } else if (FanStatus.CleanLock == fan.status) {
            toast(R.string.device_lock_text);
        } else {
            toast(R.string.device_close_text);
        }
    }

    /**
     * 更多
     */
    @Override
    protected void toMore() {
        super.toMore();
        Intent intent = new Intent(this , FanMoreActivity.class);
        intent.putExtra(DeviceBaseFuntionActivity.BUNDLE, bundle);
        startActivity(intent);
    }
    /**
     * 设置烟灶档位
     */
    private void setLevel(DeviceConfigurationFunctions func
    ) {
        try {
            FanMainParams fanMainParams = JsonUtils.json2Pojo(func.functionParams, FanMainParams.class);
            String param = fanMainParams.getParam().getPower().getValue();
            short level = Short.parseShort(param);
            if (level == fan.level) {
                //一样就设置成关闭
                level = AbsFan.PowerLevel_0;
            }
            //设置档位
            fan.setFanLevel(level, new VoidCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20200610", " onFailure:" + t);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通风换气
     */
    private void kitchenCleanupDialog(DeviceConfigurationFunctions func) {

        List<DeviceConfigurationFunctions> subList = func.subView.subViewModelMap.
                subViewModelMapSubView.deviceConfigurationFunctions;
        if (subList == null || subList.size() == 0) {
          return;
        }
        if (fan.ventilationRemainingTime == 0 ) {
            new DialogUtils(this).funcSelectDialog(subList, new DialogUtils.OnSelectListener() {
                @Override
                public void onSelectFunc(DeviceConfigurationFunctions func) {
                    kitchen(func);
                }
            });
        } else {
            final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_10);
            dialogByType.setTitleText(R.string.close_work);
            dialogByType.setContentText(this.getString(R.string.device_close_work));
            dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogByType.dismiss();
                    fan.setFanStatus(AbsFan.PowerLevel_0, new VoidCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onFailure(Throwable t) {
                        }
                    });
                }
            });
            dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogByType.dismiss();
                }
            });
            dialogByType.show();
        }
    }

    /**
     * 通风换气指令发送
     * @param func
     */
    private void kitchen(DeviceConfigurationFunctions func){
        if (func != null){
            try {
                FanKitchenCleanParams kitchenCleanParams = JsonUtils.json2Pojo(func.functionParams, FanKitchenCleanParams.class);
                short time = (short) kitchenCleanParams.getParam().getMinute().getValue();
                short power = (short) kitchenCleanParams.getParam().getPower().getValue();
                //开启厨房净化
                fan.setFanTimeWork(power, time, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        toast("设置成功");
                    }
                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 油网检测
     */
    private void oilNetDet() {
        new DialogUtils(this).checkUpOilNetDialog(fan.clean , new DialogUtils.OnSelectListener() {
            @Override
            public void onReset() {
                fan.restFanCleanTime(new VoidCallback() {
                    @Override
                    public void onSuccess() {

                        toast(R.string.fan_oil_detection_clean_reset_succeed);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
    }

    /**
     * 延时关机时间设置
     */
    private void setLinkageTime() {
        ArrayList<Integer> times = new ArrayList<>();
        times.add(1);
        times.add(2);
        times.add(3);
        times.add(4);
        times.add(5);
        //设置index
        int index = fan.smartParams.ShutdownDelay - times.get(0);
        new DialogUtils(this).minSelectDialog(times,index , new DialogUtils.OnSelectListener() {
            @Override
            public void onSelectTime(int data) {
                fan.setDelayedShut(data, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        fan.smartParams.ShutdownDelay = (short)data ;
                        adapter.fanNotify(fan);
                        toast("设置成功");

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });

    }
}