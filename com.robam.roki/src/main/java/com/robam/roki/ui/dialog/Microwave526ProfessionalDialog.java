package com.robam.roki.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.dialogs.AbsDialog;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.microwave.AbsMicroWave;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveStatus;
import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/1.
 */

public class Microwave526ProfessionalDialog extends AbsDialog {
    @InjectView(R.id.mic_dialog_pro_LinkageRestaurant)
    LinearLayout linkageRestaurant;//去味
    Context cx;
    public static Microwave526ProfessionalDialog dlg;
    String guid;
    AbsMicroWave microWave;
    private IRokiDialog rokiGatingDialog = null;
    private IRokiDialog rokiDialog = null;
    List<String> stringTimeList = new ArrayList<String>();
    List<String> stringFireList = new ArrayList<String>();
    private final int MAJOR_ONE = 0;//专业模式1
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MAJOR_ONE:
                    setDeviceMajorOneData((String) msg.obj, msg.arg1);
                    break;
            }
        }
    };

    private void setDeviceMajorOneData(String data, final int model) {
        final String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        rokiDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.dismiss();
                if (!isConnectedOrDoorOpen()) return;
                microWave.setMicroWaveKindsAndHeatCold((short) model, Short.parseShort(removeString), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, microWave.getID());
                        bundle.putString(PageArgumentKey.selectflag, "true");
                        bundle.putString(PageArgumentKey.MicroWavePageArg, "0");
                        UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bundle);
                        if (dlg != null && dlg.isShowing()) {
                            dlg.dismiss();
                            dlg = null;
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
    }

    public Microwave526ProfessionalDialog(Context context, String guid) {
        super(context, R.style.Dialog_Microwave_professtion_bottom);
        this.cx = context;
        this.guid = guid;
        microWave = Plat.deviceService.lookupChild(guid);
        rokiGatingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
    }

    public static Dialog dialog1;
    public static Dialog dialog2;
    public static Dialog dialog3;

    @Override
    protected int getViewResId() {
        return R.layout.dialog_microwave526_professional;
    }

    @Override
    protected void initView(View view) {
        ButterKnife.inject(this, view);
    }

    @OnClick(R.id.mic_profession_heatingagain)//再加热
    public void onHeatingAgain() {
        if (microWave.doorState == 1) {
            gatingShow();
            return;
        }
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
        ModeSelection(MicroWaveModel.HeatingAgain);
    }

    @OnClick(R.id.mic_dialog_pro_LinkageRestaurant)//去味
    public void onClickLinkageRestaurant() {
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
        dialog1 = Helper.newMicrowave526CleanDialog(cx, new Callback2<Object>() {

            @Override
            public void onCompleted(Object fire) {
                LogUtils.i("20170528", "fire");
                microWave.setMicroWaveCleanAir(MicroWaveModel.MicroWave, (short) 180, (short) 3, 0, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i("20170528", "ddd");
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, microWave.getID());
                        bundle.putString(PageArgumentKey.selectflag, "true");
                        bundle.putString(PageArgumentKey.MicroWavePageArg, "1");
                        UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bundle);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }, microWave);
    }


    @OnClick(R.id.mic_dialog_pro_LinkageUnfreeze)//解冻
    public void onClickUnfreeze() {
        if (microWave.doorState == 1) {
            gatingShow();
            return;
        }
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
        ModeSelection(MicroWaveModel.Unfreeze);
    }

    @OnClick(R.id.mic_dialog_pro_Linkagebarbecue)//烧烤
    public void onClickBarbecue() {
        if (microWave.doorState == 1) {
            gatingShow();
            return;
        }
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
        ProModeSelection(MicroWaveModel.Barbecue);
    }

    @OnClick(R.id.mic_dialog_pro_LinkageMicrowave)//微波
    public void onClickMicrowave() {
        if (microWave.doorState == 1) {
            gatingShow();
            return;
        }
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
        ProModeSelection(MicroWaveModel.MicroWave);
    }

    @OnClick(R.id.mic_dialog_pro_CombineHeating)//组合加热
    public void onClickCombineHeating() {
        if (microWave.doorState == 1) {
            gatingShow();
            return;
        }
        if (dlg != null && dlg.isShowing()) {
            dlg.dismiss();
            dlg = null;
        }
        ProModeSelection(MicroWaveModel.ComibineHeating);
    }

    //门控提示
    private void gatingShow() {
        rokiGatingDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
        rokiGatingDialog.setContentText(R.string.device_alarm_gating_content);
        rokiGatingDialog.show();
    }

    /**
     * 专业模式选择器
     *
     * @param-专业模式编码code
     */
    private void ProModeSelection(final short model) {
        dialog2 = Helper.newMicrowave526WeightTimeSettingDialog(cx, new Callback2<MicroWaveWheelMsg>() {
            @Override
            public void onCompleted(MicroWaveWheelMsg microWaveWheelMsg) {

                if (dlg != null && dlg.isShowing()) {
                    dlg.dismiss();
                }
                if (microWaveWheelMsg.getModel() == MicroWaveModel.Barbecue) {
                    switch (microWaveWheelMsg.getFire()) {
                        case 6:
                            microWaveWheelMsg.setFire((short) 7);
                            break;
                        case 4:
                            microWaveWheelMsg.setFire((short) 8);
                            break;
                        case 2:
                            microWaveWheelMsg.setFire((short) 9);
                            break;
                        default:
                            break;
                    }
                } else if (microWaveWheelMsg.getModel() == MicroWaveModel.ComibineHeating) {
                    switch (microWaveWheelMsg.getFire()) {
                        case 6:
                            microWaveWheelMsg.setFire((short) 10);
                            break;
                        case 4:
                            microWaveWheelMsg.setFire((short) 11);
                            break;
                        case 2:
                            microWaveWheelMsg.setFire((short) 12);
                            break;
                        default:
                            break;
                    }
                }
                if (!isConnectedOrDoorOpen()) return;
                microWave.setMicroWaveProModeHeat(model, microWaveWheelMsg.getTime(), microWaveWheelMsg.getFire(), new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        Bundle bundle = new Bundle();
                        bundle.putString(PageArgumentKey.Guid, microWave.getID());
                        bundle.putString(PageArgumentKey.MicroWavePageArg, "1");
                        bundle.putString(PageArgumentKey.selectflag, "true");
                        UIService.getInstance().postPage(PageKey.DeviceMicrowave526NormalWorking, bundle);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        }, model, microWave);
    }

    /**
     * 非专业模式 model
     *
     * @param model 非专业模式编码code
     */
    private void ModeSelection(final short model) {
        if (null == rokiDialog) {
            rokiDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_03);
        }
        rokiDialog.setWheelViewData(null, getModeListData(model), null, false, 0, 0, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                Message message = mHandler.obtainMessage();
                message.what = MAJOR_ONE;
                message.obj = contentCenter;
                message.arg1 = model;
                mHandler.sendMessage(message);
            }
        }, null);
        rokiDialog.show();
    }

    //专业模式中的子模式
    private List<String> getModeListData(short mode) {
        List<String> list = Lists.newArrayList();
        list.clear();
        switch (mode) {
            case MicroWaveModel.Unfreeze://解冻
                for (int i = 100; i <= 3000; i = i + 100) {
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
            case MicroWaveModel.HeatingAgain://再加热
                for (int i = 200; i <= 600; i = i + 100) {
                    list.add(i + StringConstantsUtil.STRING_G);
                }
                break;
        }
        return list;
    }

    static public Microwave526ProfessionalDialog show(Context cx, String guid) {
        dlg = new Microwave526ProfessionalDialog(cx, guid);
        Window win = dlg.getWindow();
        win.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        dlg.show();
        return dlg;
    }

    /**
     * 判断是否联网和门开
     */
    private boolean isConnectedOrDoorOpen() {
        if (!microWave.isConnected()) {
            ToastUtils.show("当前设备已离线", Toast.LENGTH_SHORT);
            return false;
        }

        if (microWave.state == MicroWaveStatus.Run) {
            if (dialog1 != null && dialog1.isShowing()) {
                dialog1.dismiss();
            }
            if (dialog2 != null && dialog2.isShowing()) {
                dialog2.dismiss();
            }
            if (dialog3 != null && dialog3.isShowing()) {
                dialog3.dismiss();
            }
            return false;
        }
        if (microWave.doorState == 1) {
            gatingShow();
            return false;
        }

        return true;
    }
}
