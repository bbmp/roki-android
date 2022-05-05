package com.robam.roki.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;

import com.google.common.eventbus.Subscribe;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.FanOilCupCleanEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.services.NotifyService;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.dialog.DeviceNoticDialog;
import com.robam.roki.ui.dialog.FanPlateRemoveDialog;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.utils.DialogUtil;

/**
 * Created by sylar on 15/6/21.
 */
public class MobNotifyService extends NotifyService {
    static MobNotifyService instance = new MobNotifyService();
    public static final String BROADCAST_ACTION = "com.robam.roki.sendclenlockcommand";

    synchronized public static MobNotifyService getInstance() {
        return instance;
    }

    private MobNotifyService() {

    }

    @Override
    protected void onFanNeedClean(AbsFan fan) {
        Activity atv = UIService.getInstance().getTop().getActivity();
        if (atv == null) {
            ToastUtils.showShort("油烟机需要清洗了！");
            return;
        }

//        DeviceNoticDialog.show(atv, fan, DeviceNoticDialog.Notic_Type_5_CleanNotic);
    }

    //油烟机清洗锁定 flag:1开启油烟清理，0结束油烟清理
    @Override
    protected void onFanCleanLockEvent(AbsFan fan, short flag) {
        Activity atv = UIService.getInstance().getTop().getActivity();
        if (atv == null) {
            ToastUtils.showShort("油烟机清洗锁定！");
            return;
        }
        sendCleanLockCommand(flag, atv);
//        DeviceNoticDialog.show(atv, fan, DeviceNoticDialog.Notic_Type_15_FanCleanLock);

    }

    private Dialog fanPlateRemoveDg;

    //挡风板移除
    @Override
    protected void onFanPlateRemoveEvent(AbsFan fan, short flag) {
        Activity atv = UIService.getInstance().getTop().getActivity();
        if (flag == 1) {
            if (atv == null) {
                ToastUtils.showShort("油烟机清洗锁定！");
                return;
            }
//            fanPlateRemoveDg = new FanPlateRemoveDialog(atv, R.style.Dialog);
//            fanPlateRemoveDg.show();
//
//            DeviceNoticDialog.show(atv, fan, DeviceNoticDialog.Notic_Type_16_FanPlateRemove);
        } else if (flag == 0) {
//            if (fanPlateRemoveDg != null && fanPlateRemoveDg.isShowing())
//                fanPlateRemoveDg.dismiss();
        }
//        sendCleanLockCommand(receiverType_FanPlateRemove,flag, atv);
    }

    //type:广播事件类型（0油烟机清洗锁定、1油烟机清洗解锁）
    private void sendCleanLockCommand(short flag, Activity atv) {
        Intent intent = new Intent();
        intent.setAction(BROADCAST_ACTION);
        intent.putExtra("flag", flag);
        atv.sendBroadcast(intent);
    }
}
