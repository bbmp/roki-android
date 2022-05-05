package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.OvenStatusChangedEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Oven.AbsOven;
import com.robam.common.pojos.device.Oven.OvenStatus;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.Oven026OrderTimeDialog;
import com.robam.roki.ui.view.DeviceNumWheel;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.OvenOrderTimeDataUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by linxiaobin on 2015/12/22.
 */
public class DeviceOven026ExpSettingPage extends HeadPage {
    AbsOven oven;
    public View view;
    @InjectView(R.id.wv1)
    DeviceNumWheel wv1;
    @InjectView(R.id.wv2)
    DeviceNumWheel wv2;
    @InjectView(R.id.wv3)
    DeviceNumWheel wv3;

    @InjectView(R.id.btnorder)
    TextView btnorder;
    @InjectView(R.id.btnConfirm)
    TextView btnConfirm;
    @InjectView(R.id.btn028)
    TextView btn028;

    private final int HOUR_SELE = 0;//小时
    private final int MIN_SELE = 1;//分钟
    List<String> stringHourList = new ArrayList<>();
    List<String> stringMinList = new ArrayList<>();
    private IRokiDialog rokiOrderTimeDialog = null;

    Handler mHandel = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HOUR_SELE:
                    setDeviceOrderTime((String) msg.obj);
                    break;
                case MIN_SELE:
                    setDeviceOrderTime((String) msg.obj);
                    break;
            }
        }
    };


    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        oven = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_oven026_exp_setting,
                container, false);
        ButterKnife.inject(this, contentView);
        initView();
        initData();
        return contentView;
    }

    private void initView(){
        if ("RR026".equals(oven.getDt())||"RR016".equals(oven.getDt())||IRokiFamily.HK906.equals(oven.getDt())){
            btnorder.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
            btn028.setVisibility(View.GONE);
        }else{
            btnorder.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            btn028.setVisibility(View.VISIBLE);
        }
    }

    //==============设置选择对话框数据=============================
    private void initData() {
        ArrayList<Integer> list = Lists.newArrayList();
        if ("RR026".equals(oven.getDt())||"RR016".equals(oven.getDt())||"HK906".equals(oven.getDt())){
            for (int i = 50; i <= 250; i++) {
                list.add(i);
            }
            wv1.setData(list);
            wv1.setDefault(110);
        }else{
            for (int i = 100; i <= 250; i++) {
                list.add(i);
            }
            wv1.setData(list);
            wv1.setDefault(60);
        }
        ArrayList<Integer> list2 = Lists.newArrayList();
        if ("RR026".equals(oven.getDt())||"RR016".equals(oven.getDt())||"HK906".equals(oven.getDt())){
            for (int i = 130; i <= 190; i++) {
                list2.add(i);
            }
            wv2.setData(list2);
            wv2.setDefault(0);
        }else{
            for (int i = 130; i <= 190; i++) {
                list2.add(i);
            }
            wv2.setData(list2);
            wv2.setDefault(0);
        }

        wv1.setOnSelectListener(new DeviceNumWheel.OnSelectListener() {
            @Override
            public void endSelect(int index, Object item) {
                List<?> list = getDownTempRange(item);
                wv2.setData(list);
                wv2.setDefault(0);
            }

            @Override
            public void selecting(int index, Object item) {

            }
        });
        ArrayList<Integer> list1 = Lists.newArrayList();
        if ("RR026".equals(oven.getDt())||"RR016".equals(oven.getDt())||"HK906".equals(oven.getDt())){
            for (int i = 5; i <= 90; i++) {
                list1.add(i);
            }
            wv3.setData(list1);
            wv3.setDefault(15);
        }else if ("RR075".equals(oven.getDt())){
            for (int i = 1; i <= 90; i++) {
                list1.add(i);
            }
            wv3.setData(list1);
            wv3.setDefault(19);
        } else{
            for (int i = 1; i <= 120; i++) {
                list1.add(i);
            }
            wv3.setData(list1);
            wv3.setDefault(19);
        }
        wv1.startx_offset = 130;
        wv2.startx_offset = 130;
        wv3.startx_offset = 130;
        wv1.setUnit("");
        wv2.setUnit("");
        wv3.setUnit("");
    }

    //根据选中的上管温度设置下管温度范围
    protected List<?> getDownTempRange(Object item) {
        if (item == null) {
            return null;
        }
        List<Integer> list = Lists.newArrayList();
        Integer wv1SelectValue = (Integer) item;
        if ("RR026".equals(oven.getDt())||"RR016".equals(oven.getDt())||"HK906".equals(oven.getDt())){
            if (wv1SelectValue <= 80) {
                for (int i = 50; i <= wv1SelectValue+30; i++) {
                    list.add(i);
                }
                return list;
            }
            if (wv1SelectValue > 80 && wv1SelectValue <= 220) {

                for (int i = wv1SelectValue - 30; i <= wv1SelectValue + 30; i++) {
                    list.add(i);
                }
                return list;
            }

            if (wv1SelectValue >= 220) {
                for (int i = wv1SelectValue - 30; i <= 250; i++) {
                    list.add(i);
                }
                return list;
            }
        }else{
            if (wv1SelectValue <= 130) {
                for (int i = 100; i <= wv1SelectValue+30; i++) {
                    list.add(i);
                }
                return list;
            }
            if (wv1SelectValue > 130 && wv1SelectValue <= 220) {

                for (int i = wv1SelectValue - 30; i <= wv1SelectValue + 30; i++) {
                    list.add(i);
                }
                return list;
            }

            if (wv1SelectValue >= 220) {
                for (int i = wv1SelectValue - 30; i <= 250; i++) {
                    list.add(i);
                }
                return list;
            }
        }

        return list;
    }


    //==============点击事件=============================
    //开始烹饪
    @OnClick(R.id.btnConfirm)
    public void onClickConfirm() {
        if (!oven.isConnected()) {
            ToastUtils.show(R.string.device_connected, Toast.LENGTH_SHORT);
            return;
        }
        if (isInAlarmStatus()) {
            ToastUtils.show(getString(R.string.mac_error),Toast.LENGTH_SHORT);
            return;
        }
        oven.setOvenRunMode((short) 9, Short.valueOf(wv3.getSelectedText()), Short.valueOf(wv1.getSelectedText()),
                (short) 0, (short) 0, (short) 0, (short) 1, Short.valueOf(wv2.getSelectedText()), (short) 255, (short) 255, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, oven.getID());
                        bundle.putShort("from", (short) 0);
                        UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }
    //开始烹饪
    @OnClick(R.id.btn028)
    public void onClickbtn028() {
        if (!oven.isConnected()) {
            ToastUtils.show("网络通讯异常", Toast.LENGTH_SHORT);
            return;
        }
        if (isInAlarmStatus()) {
            ToastUtils.show(getString(R.string.mac_error),Toast.LENGTH_SHORT);
            return;
        }
       /* LogUtils.i("20171114","wv3:"+wv3.getSelectedText()+"wv1:"+wv1.getSelectedText()
        +"wv2:"+wv2.getSelectedText());*/
        oven.setOvenRunMode((short) 9, Short.valueOf(wv3.getSelectedText()), Short.valueOf(wv1.getSelectedText()),
                (short) 0, (short) 0, (short) 0, (short) 1, Short.valueOf(wv2.getSelectedText()), (short) 255, (short) 255, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        UIService.getInstance().popBack();
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, oven.getID());
                        bundle.putShort("from", (short) 0);
                        if (IRokiFamily.RR075.equals(oven.getDt())){
                            UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
                        }else{
                            UIService.getInstance().postPage(PageKey.DeviceOven028Working, bundle);
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
    }

    private IRokiDialog rokiDialog=null;
    private boolean isInAlarmStatus() {
       /* if (ovenWarningDialog != null && ovenWarningDialog.isShowing()) {
            ovenWarningDialog.dismiss();
            ovenWarningDialog = null;
        }*/
     /*   if (rokiDialog != null && rokiDialog.isShow()){
            rokiDialog.dismiss();
            rokiDialog=null;
        }*/
        /* rokiDialog= RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_01);
        rokiDialog.setTitleText("电烤箱");

            switch (oven.alarm) {
                case AbsOven.Event_Oven_Heat_Fault:
                  //  showDialog("错误：加热故障", oven.alarm);
                    rokiDialog.setTitleAralmCodeText("E3");
                    rokiDialog.setContentText("加热故障，请关闭电烤箱并申请售后服务。");
                    Dialogshow(rokiDialog);
                    break;
                case AbsOven.Event_Oven_Alarm_Senor_Fault:
                   // showDialog("错误：传感器故障", oven.alarm);
                    rokiDialog.setTitleAralmCodeText("E5");
                    rokiDialog.setContentText("传感器故障，请关闭电烤箱并申请售后服务。");
                    Dialogshow(rokiDialog);
                    break;
                case AbsOven.Event_Oven_Communication_Fault:
                   // showDialog("错误：通信故障", oven.alarm);
                    rokiDialog.setTitleAralmCodeText("E6");
                    rokiDialog.setContentText("通讯故障，请关闭电烤箱并申请售后服务。");
                    Dialogshow(rokiDialog);
                    break;
                default:
                    break;
            }*/
        return oven.status == OvenStatus.AlarmStatus;
    }

   /* private void Dialogshow(final IRokiDialog rokiDialog){
        rokiDialog.setOkBtn("拨打电话", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(String.format("tel:%s","95105855"));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                cx.startActivity(it);
            }
        });
        rokiDialog.setCancelBtn("好", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rokiDialog.isShow()){
                    rokiDialog.dismiss();
                }
            }
        });
        if (rokiDialog!=null){
            rokiDialog.show();
        }
    }*/

    Oven026OrderTimeDialog ordertime_dialog;
    //预约点击事件
    @OnClick(R.id.btnorder)
    public void OnOrderClick() {

        rokiOrderTimeDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_04);
        rokiOrderTimeDialog.setWheelViewData(OvenOrderTimeDataUtil.getListOrderTimeHourData(), null,
                OvenOrderTimeDataUtil.getListOrderTimeMinData(), false, 12, 0, 30, new OnItemSelectedListenerFrone() {
                    @Override
                    public void onItemSelectedFront(String contentFront) {
                        Message message = mHandel.obtainMessage();
                        message.obj = contentFront;
                        message.what = HOUR_SELE;
                        mHandel.sendMessage(message);
                    }
                }, null, new OnItemSelectedListenerRear() {
                    @Override
                    public void onItemSelectedRear(String contentRear) {
                        Message message = mHandel.obtainMessage();
                        message.obj = contentRear;
                        message.what = MIN_SELE;
                        mHandel.sendMessage(message);
                    }
                });
        rokiOrderTimeDialog.show();

    }


    //设置预约时间
    private void setDeviceOrderTime(String data) {
        if (data.contains(StringConstantsUtil.STR_HOUR)){
            String removetTempString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringHourList.add(removetTempString);
        }
        if (data.contains(StringConstantsUtil.STRING_MINUTES)){
            String removeTimeString = RemoveManOrsymbolUtil.getRemoveString(data);
            stringMinList.add(removeTimeString);
        }

        rokiOrderTimeDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiOrderTimeDialog.dismiss();

                oven.setOvenRunMode((short) 9, Short.valueOf(wv3.getSelectedText()), Short.valueOf(wv1.getSelectedText()), (short) 1, (short) 0, (short) 0, (short) 2,
                        Short.valueOf(wv2.getSelectedText()), Short.parseShort(String.valueOf(stringMinList.get(stringMinList.size()-1))),
                        Short.parseShort(String.valueOf(stringHourList.get(stringHourList.size()-1))), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                if (ordertime_dialog != null && ordertime_dialog.isShowing()) {
                                    ordertime_dialog.dismiss();
                                    ordertime_dialog = null;
                                }
                                UIService.getInstance().popBack();
                                Bundle bundle = new Bundle();
                                bundle.putString(PageArgumentKey.Guid, oven.getID());
                                bundle.putShort("from", (short) 0);
                                UIService.getInstance().postPage(PageKey.DeviceOven026Working, bundle);
                            }
                            @Override
                            public void onFailure(Throwable t) {
                            }
                        });

            }
        });


    }

    @Subscribe
    public void onEvent(OvenStatusChangedEvent event) {
        if (!UIService.getInstance().getTop().getCurrentPageKey().equals(PageKey.DeviceOven026ExpSetting)) {
            return;
        }
        if (oven == null || !Objects.equal(oven.getID(), event.pojo.getID()))
            return;
        if (oven.status == OvenStatus.Wait || oven.status == OvenStatus.Off || oven.status == OvenStatus.Working || oven.status == OvenStatus.Pause
                || oven.status == OvenStatus.Order || oven.status == OvenStatus.PreHeat) {
            if (ordertime_dialog != null && ordertime_dialog.isShowing())
                ordertime_dialog.dismiss();
            ordertime_dialog = null;
            UIService.getInstance().popBack();
        }
    }


}
