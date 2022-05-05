package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Oven026OrderTimeDialog;
import com.robam.roki.ui.dialog.OvenBroken026Dialog;
import com.robam.roki.ui.view.DeviceOven028AssistWheel;
import com.robam.roki.ui.view.DeviceOvenTemWheel;
import com.robam.roki.ui.view.DeviceOvenTimeWheel;
import com.robam.roki.utils.DialogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by yinwei on 2017/8/18.
 */

public class DeviceOven028AssistPage  extends HeadPage {
    AbsOven oven;

    public View view;

    private short time;
    private short temp;
    private short modeKind;
    private Map<String, Short> modeKingMap = new HashMap<String, Short>();

    @InjectView(R.id.wheelView)
    LinearLayout wheelView;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
    @InjectView(R.id.wv1)
    DeviceOven028AssistWheel modeWheel;
    @InjectView(R.id.wv2)
    DeviceOvenTemWheel temWheel;
    @InjectView(R.id.wv3)
    DeviceOvenTimeWheel timeWheel;
    @InjectView(R.id.txt1)
    TextView txtMode;
    @InjectView(R.id.txt2)
    TextView txtContext;

    IRokiDialog rokiDialog=null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x03) {
                txtMode.setText(modeWheel.getSelectedText());
                if ("解冻".equals(modeWheel.getSelectedText()))
                    txtContext.setText("尤其适合禽类和肉类的解冻。");
                else if ("发酵".equals(modeWheel.getSelectedText()))
                    txtContext.setText("适用于面团的发酵。");
                else if ("杀菌".equals(modeWheel.getSelectedText()))
                    txtContext.setText("针对耐高温器皿进行高温杀菌。");

            }
        }
    };


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven028_assist, container, false);
        ButterKnife.inject(this, contentView);
        initData();
        modeWheel.setOnSelectListener(modeWheelLitener);
        modeWheel.setDefault(0);
        txtMode.setText(modeWheel.getSelectedText());
//        Log.e("mode", mode);

//        handler.sendEmptyMessage(1);
        rokiDialog= RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_02);
        return contentView;
    }

    private void initData() {
        modeKingMap.put("解冻", (short) 12);
        modeKingMap.put("发酵", (short) 13);
        modeKingMap.put("杀菌", (short) 14);
    }

   DeviceOven028AssistWheel.OnSelectListener modeWheelLitener = new DeviceOven028AssistWheel.OnSelectListener() {
        @Override
        public void endSelect(int index, Object item) {
            Log.i("index+Item:endSelect", index + "+" + item);
            List<?> list1 = getList2(item);
            List<?> list2 = getList3(item);
            temWheel.setData(list1);
            timeWheel.setData(list2);
            int def1 = 0, def2 = 0;
            if (index == 0) {
                def1 = 20;
                def2 = 19;
            } else if (index == 1) {
                def1 = 3;
                def2 = 49;
            } else if (index == 2) {
                def1 = 0;
                def2 = 0;
            }
            handler.sendEmptyMessage(0x03);
            temWheel.setDefault(def1);
            timeWheel.setDefault(def2);
        }

        @Override
        public void selecting(int index, Object item) {
        }
    };

    /*设置各种模式温度范围*/
    protected List<?> getList2(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;

        if ("解冻".equals(s)) {
            for (int i = 40; i <= 80; i++) {
                list.add(i);
            }
        } else if ("发酵".equals(s)) {
            for (int i = 35; i <= 60; i++) {
                list.add(i);
            }
        } else if ("杀菌".equals(s)) {
            list.add(120);
        }
        return list;
    }


    /*设置各种模式时间*/
    protected List<?> getList3(Object item) {
        List<Integer> list = Lists.newArrayList();
        String s = (String) item;
        if ("解冻".equals(s) || "发酵".equals(s) ) {
            for (int i = 1; i <= 120; i++) {
                list.add(i);
            }
        }else if("杀菌".equals(s)){
            list.add(20);
        }
        return list;
    }

    short model = 0;

    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!oven.isConnected()) {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return;
        }
        if (ovenWarningDialog != null && ovenWarningDialog.isShowing()) {
            ovenWarningDialog.dismiss();
            ovenWarningDialog = null;
        }
        alarmModel();
        compareMode();
        time = Short.valueOf(timeWheel.getSelectedText());
        temp = Short.valueOf(temWheel.getSelectedText());
        if (model==13){
            rokiDialog.setTitleText("发酵提示");
            rokiDialog.setContentText("盛面团的容器盖上保鲜膜放入烤盘，盘底放200ml清水或热水。");
            rokiDialog.setOkBtn("已放入烤盘", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMsg();
                    rokiDialog.dismiss();
                }
            });
            rokiDialog.show();
        }else if(model==14){
            rokiDialog.setTitleText("杀菌提示");
            rokiDialog.setContentText("请勿放入非耐高温器皿，比如塑料、橡胶等。");
            rokiDialog.setOkBtn("知道了", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendMsg();
                    rokiDialog.dismiss();
                }
            });
            rokiDialog.show();
        }else{
            sendMsg();
        }
    }

    private void sendMsg(){
        oven.setOvenRunMode(model, time, temp, (short) 0, (short) 0, (short) 0, (short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, oven.getID());
                bundle.putShort("from", (short) 0);
                UIService.getInstance().postPage(PageKey.DeviceOven028Working, bundle);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void compareMode(){
        if ("解冻".equals(modeWheel.getSelectedText())) {
            model = 5;
        } else if ("发酵".equals(modeWheel.getSelectedText())) {
            model = 13;
        } else if ("杀菌".equals(modeWheel.getSelectedText())) {
            model = 14;
        }
    }

    private void alarmModel(){
        if (oven.status == OvenStatus.AlarmStatus) {
            switch (oven.alarm) {
                case AbsOven.Event_Oven_Heat_Fault:
                    //   showDialog("错误：加热故障", oven.alarm);
                    break;
                case AbsOven.Event_Oven_Alarm_Senor_Fault:
                    //   showDialog("错误：传感器故障", oven.alarm);
                    break;
                case AbsOven.Event_Oven_Communication_Fault:
                    //    showDialog("错误：通信故障", oven.alarm);
                    break;
                default:
                    break;
            }
            return;
        }
    }


    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        LogUtils.i("20171229","ev:::::"+event.pojo.getDt());
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceOven028Assist)) {
            return;
        }
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        if (oven.status == OvenStatus.Wait || oven.status == OvenStatus.Off || oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause
                || oven.status == OvenStatus.Order || oven.status == OvenStatus.PreHeat) {
            if (rokiDialog!=null && rokiDialog.isShow()){
                rokiDialog.dismiss();
            }
            UIService.getInstance().popBack();
        }
    }

    private OvenBroken026Dialog ovenWarningDialog = null;//报警


}
