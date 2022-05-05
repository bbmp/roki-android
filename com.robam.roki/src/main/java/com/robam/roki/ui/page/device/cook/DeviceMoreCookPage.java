package com.robam.roki.ui.page.device.cook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.cook.AbsCooker;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.AbsSettingDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.TestDatas;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/13.
 * 智能灶 更多页面
 */

public class DeviceMoreCookPage extends BasePage {


    public String mGuid;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.iv1)
    ImageView iv1;
    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.item1)
    FrameLayout item1;
    @InjectView(R.id.iv2)
    ImageView iv2;
    @InjectView(R.id.tv2)
    TextView tv2;
    @InjectView(R.id.item2)
    FrameLayout item2;
    @InjectView(R.id.iv3)
    ImageView iv3;
    @InjectView(R.id.tv3)
    TextView tv3;
    @InjectView(R.id.item3)
    FrameLayout item3;
    @InjectView(R.id.iv4)
    ImageView iv4;
    @InjectView(R.id.tv4)
    TextView tv4;
    @InjectView(R.id.t1)
    TextView t1;
    @InjectView(R.id.item4)
    FrameLayout item4;
    @InjectView(R.id.iv5)
    ImageView iv5;
    @InjectView(R.id.tv5)
    TextView tv5;
    @InjectView(R.id.t2)
    TextView t2;
    @InjectView(R.id.item5)
    FrameLayout item5;
    @InjectView(R.id.iv6)
    ImageView iv6;
    @InjectView(R.id.tv6)
    TextView tv6;
    @InjectView(R.id.item6)
    FrameLayout item6;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    t2.setText(voiceCon + "");
                    break;
                case 1:
                    if (voiceMode == 0) {
                        t1.setText("男声");
                    } else {
                        t1.setText("女声");
                    }
                    t2.setText(voiceCon + "");
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        absCooker = Plat.deviceService.lookupChild(mGuid);
        LogUtils.i("20180601", "mguid:33:" + mGuid);
        View view = inflater.inflate(R.layout.page_more_cooker, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (absCooker==null) {
            return;
        }
    }

    private void initView() {
        absCooker.setCookerInfoLook(new VoidCallback() {
            @Override
            public void onSuccess() {
                voiceMode = absCooker.voice;
                voiceCon = absCooker.voiceCon;
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("获取数据失败", Toast.LENGTH_SHORT);
            }
        });
    }

    AbsSettingDialog absSettingDialog;
    AbsCooker absCooker;

    public void callAfterSale() {
        final IRokiDialog dialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        dialog.setTitleText(R.string.after_sale_phone);
        dialog.show();
        dialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Uri uri = Uri.parse(String.format("tel:%s", cx.getString(R.string.after_sale_phone)));
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(it);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @OnClick({R.id.iv_back, R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5,
            R.id.item6, R.id.item7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
            case R.id.item1:
                UIService.getInstance().postPage(PageKey.Chat);
                break;
            case R.id.item2:
                callAfterSale();
                break;
            case R.id.item3:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.ScanCode, bd);
                break;
            //语音设置
            case R.id.item4:
                if (!absCooker.isConnected()) {
                    ToastUtils.show("设备已离线", Toast.LENGTH_SHORT);
                    return;
                }
                List<String> voiceDate = TestDatas.createVoiceDatas();
                setDialogVoice(voiceDate, 1, null);
                break;
            //音量设置
            case R.id.item5:
                if (!absCooker.isConnected()) {
                    ToastUtils.show("设备已离线", Toast.LENGTH_SHORT);
                    return;
                }


                List<Integer> voiceControl = TestDatas.createVoiceContrl();
                setDialogVoiceControl(voiceControl, 9, "档");
                break;
            case R.id.item6:
                Bundle bd1 = new Bundle();
                bd1.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.DeviceInformation, bd1);
                break;
            //检查固件升级
            case R.id.item7:
                if (absCooker!=null) {
                    ToolUtils.logEvent(absCooker.getDt(),"固件升级成功","roki_设备");
                }
                Bundle bd2 = new Bundle();
                bd2.putString(PageArgumentKey.Guid, mGuid);
                UIService.getInstance().postPage(PageKey.CookerUpdateDevice, bd2);
                break;
            default:
                break;
        }
    }

    //音量设置
    private void setDialogVoiceControl(final List<Integer> listTempture, int num, String str) {
        List<String> listButton = TestDatas.createButtonText(str, "取消", "确定", null);
        absSettingDialog = new AbsSettingDialog<Integer>(cx, listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                LogUtils.i("20180613", "index::" + (int) index);
                sendVoiceContrlCom((int) index);
            }
        });
    }

    short voiceMode;
    short voiceCon;
    int vLevel;

    private void sendVoiceContrlCom(int voiceLevel) {
        vLevel = voiceLevel;
        absCooker.setSetCookerInfo(voiceMode, (short) voiceLevel, new VoidCallback() {
            @Override
            public void onSuccess() {


                voiceCon = (short) vLevel;
                if (absCooker!=null) {
                    ToolUtils.logEvent(absCooker.getDt(), "设置音量:" + voiceCon, "roki_设备");
                }

                ToastUtils.show("设置成功", Toast.LENGTH_SHORT);
                Message msg = Message.obtain();
                msg.what = 0;
                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
    String sex;
    private void setDialogVoice(List<String> listTempture, int num, String str) {
        List<String> listButton = TestDatas.createButtonText(str, "取消", "确定", null);
        absSettingDialog = new AbsSettingDialog<String>(cx, listTempture, listButton, num);
        absSettingDialog.show(absSettingDialog);
        absSettingDialog.setListener(new AbsSettingDialog.PickListener() {
            @Override
            public void onCancel() {

            }

            @Override
            public void onConfirm(Object index) {
                LogUtils.i("20180613", "index::" + index);

                switch ((String) index) {
                    //男
                    case "男声":
                        sex="男";
                        break;
                    //女
                    case "女声":
                        sex="女";
                        break;
                }
                if (absCooker!=null) {
                    ToolUtils.logEvent(absCooker.getDt(), "语音设置:"+sex, "roki_设备");
                }
                sendVoice((String) index);
            }
        });
    }

    short vMode;

    private void sendVoice(String voice) {
        if ("男声".equals(voice)) {
            vMode = 0;
        } else {
            vMode = 1;
        }
        absCooker.setSetCookerInfo(vMode, voiceCon, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("发送成功", Toast.LENGTH_SHORT);
                voiceMode = vMode;
                if (vMode == 0) {
                    t1.setText("男声");
                } else {
                    t1.setText("女声");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
