package com.robam.roki.ui.page;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.legent.Callback2;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.microwave.LinkageStepInfo;
import com.robam.common.pojos.device.microwave.MicroWaveM509;
import com.robam.common.pojos.device.microwave.MicroWaveModel;
import com.robam.common.pojos.device.microwave.MicroWaveWheelMsg;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.Helper;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.BlackPromptDialog;
import com.robam.roki.utils.DialogUtil;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/13.
 */
public class DeviceMicrowaveLinkageSettingPage extends HeadPage {
    @InjectView(R.id.mic_linksetting_include1)
    View mic_linksetting_include1;//第一个选择项
    @InjectView(R.id.mic_linksetting_include2)
    View mic_linksetting_include2;//第二个选择项
    @InjectView(R.id.mic_linksetting_include3)
    View mic_linksetting_include3;//第三个选择项
    @InjectView(R.id.mic_linksetting_ll1)
    LinearLayout mic_linksetting_ll1;
    @InjectView(R.id.mic_linksetting_ll2)
    LinearLayout mic_linksetting_ll2;
    @InjectView(R.id.mic_linksetting_ll3)
    LinearLayout mic_linksetting_ll3;
    @InjectView(R.id.mic_linksetting_btn)
    Button mic_linksetting_btn;
    short step = 0;
    List<LinkageStepInfo> listData = Lists.newArrayList();
    MicroWaveM509 microWaveM509;

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        microWaveM509 = Plat.deviceService.lookupChild(guid);
        contentView = inflater.inflate(R.layout.page_device_microwave_linksetting,
                container, false);
        ButterKnife.inject(this, contentView);

        initView();
        return contentView;
    }


    private void initView() {
        mic_linksetting_include1.setAlpha(1.0f);
        initItemStatus(mic_linksetting_include1, (short) 1);
        TextView textNum = mic_linksetting_ll2.findViewById(R.id.mic_linksetting_num);
        textNum.setText("二");
        textNum = mic_linksetting_ll2.findViewById(R.id.mic_linksetting_txt);
        textNum.setText("2");
        textNum = mic_linksetting_ll3.findViewById(R.id.mic_linksetting_num);
        textNum.setText("三");
        textNum = mic_linksetting_ll3.findViewById(R.id.mic_linksetting_txt);
        textNum.setText("3");
    }

    @OnClick(R.id.mic_linksetting_ll1)//点击第一个选项 弹出框 并回调处理
    public void onClickInclude1() {
        Log.i("mic", "onClickInclude1");
        MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
        Helper.newMicrowaveArgumentSettingDialog(cx, new Callback2<MicroWaveWheelMsg>() {
            @Override
            public void onCompleted(final MicroWaveWheelMsg message) {//回调处理
                MicroWaveWheelMsg message2 = new MicroWaveWheelMsg(message);
                ChangeData(changeFire(message), (short) 1);
                BackToWrite(mic_linksetting_ll1, message2);
            }
        }, (short) 1);
    }

    @OnClick(R.id.mic_linksetting_ll2)//点击第2个选项 弹出框 并回调处理
    public void onClickInclude2() {
        if (step == 0) return;
        Log.i("mic", "onClickInclude1");
        MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
        Helper.newMicrowaveArgumentSettingDialog(cx, new Callback2<MicroWaveWheelMsg>() {
            @Override
            public void onCompleted(final MicroWaveWheelMsg message) {//回调处理
                MicroWaveWheelMsg message2 = new MicroWaveWheelMsg(message);
                ChangeData(changeFire(message), (short) 2);
                BackToWrite(mic_linksetting_ll2, message2);
            }
        }, (short) 2);
    }

    @OnClick(R.id.mic_linksetting_ll3)//点击第3个选项 弹出框 并回调处理
    public void onClickInclude3() {
        if (step == 0 || step == 1) return;
        Log.i("mic", "onClickInclude1");
        MicroWaveWheelMsg msg = new MicroWaveWheelMsg();
        Helper.newMicrowaveArgumentSettingDialog(cx, new Callback2<MicroWaveWheelMsg>() {
            @Override
            public void onCompleted(final MicroWaveWheelMsg message) {//回调处理
                MicroWaveWheelMsg message2 = new MicroWaveWheelMsg(message);
                ChangeData(changeFire(message), (short) 3);
                BackToWrite(mic_linksetting_ll3, message2);
            }
        }, (short) 3);
    }

    /**
     * 根据选择的模式 火力 时间 来设置新的选项框
     *
     * @param message
     */
    private void ChangeData(MicroWaveWheelMsg message, short index) {
        switch (index) {
            case 1:
                if (listData.size() >= 1) {
                    listData.get(0).setLink_model(message.getModel());
                    listData.get(0).setLink_fire(message.getFire());
                    listData.get(0).setLink_time(message.getTime());
                } else {
                    LinkageStepInfo info1 = new LinkageStepInfo();
                    info1.setLink_step(index);
                    info1.setLink_model(message.getModel());
                    info1.setLink_fire(message.getFire());
                    info1.setLink_time(message.getTime());
                    listData.add(info1);
                }
                break;
            case 2:
                if (listData.size() >= 2) {
                    listData.get(1).setLink_model(message.getModel());
                    listData.get(1).setLink_fire(message.getFire());
                    listData.get(1).setLink_time(message.getTime());
                } else {
                    LinkageStepInfo info2 = new LinkageStepInfo();
                    info2.setLink_step(index);
                    info2.setLink_model(message.getModel());
                    info2.setLink_fire(message.getFire());
                    info2.setLink_time(message.getTime());
                    listData.add(info2);
                }
                break;
            case 3:
                if (listData.size() >= 3) {
                    listData.get(2).setLink_model(message.getModel());
                    listData.get(2).setLink_fire(message.getFire());
                    listData.get(2).setLink_time(message.getTime());
                } else {
                    LinkageStepInfo info3 = new LinkageStepInfo();
                    info3.setLink_step(index);
                    info3.setLink_model(message.getModel());
                    info3.setLink_fire(message.getFire());
                    info3.setLink_time(message.getTime());
                    listData.add(info3);
                }
                break;
        }
    }

    private void BackToWrite(ViewGroup viewGroup, MicroWaveWheelMsg message) {
        Log.i("mic", message.toString());
        viewGroup.removeAllViews();//删除原有的View
        View view = LayoutInflater.from(cx).inflate(R.layout.page_device_microwave_linksetting_hasset, null);
        int img_res = 0;
        String text = "";
        if (message.getModel() == MicroWaveModel.Barbecue) {//烧烤图片
            img_res = R.mipmap.ic_microwave_barbecue;
            text = "烧烤";
        } else if (message.getModel() == MicroWaveModel.MicroWave) {//微波图片
            img_res = R.mipmap.ic_microwave_microwave;
            text = "微波";
        } else if (message.getModel() == MicroWaveModel.ComibineHeating) {//组合加热图片
            img_res = R.mipmap.ic_microwave_combineheating;
            text = "组合加热";
        }
        ((ImageView) view.findViewById(R.id.mic_linksetting_set_model_img)).setImageResource(img_res);
        ((TextView) view.findViewById(R.id.mic_linksetting_set_model)).setText(text);
        ((TextView) view.findViewById(R.id.mic_linksetting_set_fire)).setText(message.getFire() + "");
        short min = (short) (message.getTime() / 60);//获取分钟
        short sec = (short) (message.getTime() % 60);//获取秒
        //分秒显示控制，并赋值
        view.findViewById(R.id.mic_linksetting_set_time_min_unity).setVisibility(min > 0 ? View.VISIBLE : View.GONE);
        ((TextView) view.findViewById(R.id.mic_linksetting_set_time_min)).setText(min + "");
        view.findViewById(R.id.mic_linksetting_set_time_min).setVisibility(min > 0 ? View.VISIBLE : View.GONE);
        view.findViewById(R.id.mic_linksetting_set_time_sec_unity).setVisibility(sec > 0 ? View.VISIBLE : View.GONE);
        ((TextView) view.findViewById(R.id.mic_linksetting_set_time_sec)).setText(sec + "");
        view.findViewById(R.id.mic_linksetting_set_time_sec).setVisibility(sec > 0 ? View.VISIBLE : View.GONE);
        TextView tv = view.findViewById(R.id.mic_linksetting_set_txt);
        switch (viewGroup.getId()){
            case R.id.mic_linksetting_ll1:
                tv.setText("1");break;
            case R.id.mic_linksetting_ll2:
                tv.setText("2");break;
            case R.id.mic_linksetting_ll3:
                tv.setText("3");break;
        }
        viewGroup.addView(view);
        step++;
        if (step == 1) {
            mic_linksetting_btn.setVisibility(View.VISIBLE);
            initItemStatus(mic_linksetting_include2, (short) 2);
        } else if (step == 2)
            initItemStatus(mic_linksetting_include3, (short) 3);
    }

    private void initItemStatus(View view, short index) {
        view.setAlpha(1.0f);
        TextView textView = view.findViewById(R.id.mic_linksetting_txt);
        textView.setBackgroundResource(R.mipmap.ic_device_microwave_linksetting_triangle_yellow);
        textView.setText(String.valueOf(index));//设置左上角图标 黄和数字为1
        //初始化第一个选择项
        ((ImageView) view.findViewById(R.id.mic_linksetting_add)).setImageResource(R.mipmap.img_device_add);
        ((ImageView) view.findViewById(R.id.mic_linksetting_fire)).setImageResource(R.mipmap.ic_device_microwave_linksetting_fire_yellow);
        ((TextView) view.findViewById(R.id.mic_linksetting_text1)).setTextColor(r.getColor(R.color.c02));
        ((TextView) view.findViewById(R.id.mic_linksetting_text2)).setTextColor(r.getColor(R.color.c02));
        TextView textNum = view.findViewById(R.id.mic_linksetting_num);
        textNum.setTextColor(r.getColor(R.color.c02));
        String num = "一";
        switch (index) {
            case 1:
                num = "一";
                break;
            case 2:
                num = "二";
                break;
            case 3:
                num = "三";
                break;
            default:
                break;
        }
        textNum.setText(num);
    }

    /**
     * 改变烧烤和组合火力值
     */
    private MicroWaveWheelMsg changeFire(MicroWaveWheelMsg microWaveWheelMsg) {
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
        return microWaveWheelMsg;
    }

    @OnClick(R.id.mic_linksetting_btn)
    public void onClickBtn() {
        if(!isConnectedOrDoorOpen())return;
        microWaveM509.setMicroWaveLinkdCook(listData, new VoidCallback() {
            @Override
            public void onSuccess() {
                UIService.getInstance().popBack();
                Bundle bundle = new Bundle();
                bundle.putString(PageArgumentKey.Guid, microWaveM509.getID());
                UIService.getInstance().postPage(PageKey.DeviceMicrowaveLinkageWorking, bundle);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    /**
     * 判断是否联网和门开
     */
    private boolean isConnectedOrDoorOpen() {
        IRokiDialog gitingDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        if (!microWaveM509.isConnected()) {
            gitingDialog.setContentText(R.string.device_connected);
            gitingDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            gitingDialog.show();
            return false;
        }
        if(microWaveM509.doorState==1){
            gitingDialog.setContentText(R.string.device_alarm_gating_content);
            gitingDialog.setToastShowTime(DialogUtil.LENGTH_CENTER);
            gitingDialog.show();
            return false;
        }
        return true;
    }
}
