package com.robam.roki.ui.page;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.views.CheckBoxView;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.Stove9B30C;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.RemoveManOrsymbolUtil;
import com.robam.roki.utils.StringConstantsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/5/29.
 */

public class StoveSettingPage extends BasePage implements CompoundButton.OnCheckedChangeListener{


    @InjectView(R.id.stove_return)
    ImageView stoveReturn;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.setting)
    TextView setting;
    @InjectView(R.id.stove_tip)
    TextView stoveTip;
    @InjectView(R.id.stove_check)
    CheckBoxView stoveCheck;
    @InjectView(R.id.toggle)
    LinearLayout toggle;
    @InjectView(R.id.line)
    View line;
    @InjectView(R.id.time1_set)
    TextView time1Set;
    @InjectView(R.id.set_btn1)
    TextView setBtn1;
    @InjectView(R.id.time2_set)
    TextView time2Set;
    @InjectView(R.id.set_btn2)
    TextView setBtn2;
    @InjectView(R.id.time3_set)
    TextView time3Set;
    @InjectView(R.id.set_btn3)
    TextView setBtn3;
    @InjectView(R.id.time4_set)
    TextView time4Set;
    @InjectView(R.id.set_btn4)
    TextView setBtn4;
    @InjectView(R.id.time5_set)
    TextView time5Set;
    @InjectView(R.id.set_btn5)
    TextView setBtn5;
    @InjectView(R.id.total_setting)
    LinearLayout totalSetting;
    private IRokiDialog mTimingDialog;

    private short timeForOne;
    private short timeForTwo;
    private short timeForThree;
    private short timeForFour;
    private short timeForFive;
    private short power;

    public static final short OFF = 0;
    public static final  short ON = 1;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    reSetTimeData((String) msg.obj,msg.arg1);
                    break;

                case 2:
                    updateUi();
                    break;

                case 3:
                    updateOffOrOn();
                    break;
            }
        }

    };

    private void updateOffOrOn(){
        if (power==0){
            stoveCheck.setChecked(false);
        }else{
            stoveCheck.setChecked(true);
        }
    }

    Stove stove;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        LogUtils.i("20180604","guid::"+guid);
        stove =Plat.deviceService.lookupChild(guid);
        View view = inflater.inflate(R.layout.stove_setting_page, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    private void initView() {
        getTimeForStove();
    }

    private void getTimeForStove(){
        LogUtils.i("20180604","stove::"+stove.getID());
        if (stove==null){
            return;
        }
        stove.setAutoPowerOffLook(new VoidCallback() {
            @Override
            public void onSuccess() {
                power = stove.power;
                timeForOne = stove.AutoPowerOffOne;
                timeForTwo = stove.AutoPowerOffTwo;
                timeForThree =stove.AutoPowerOffThree;
                timeForFour = stove.AutoPowerOffFour;
                timeForFive = stove.AutoPowerOffFive;
                //成功后在更新UI
                Message message = mHandler.obtainMessage();
                message.what = 2;
                mHandler.sendMessage(message);
                updateUi();

            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("下发失败",Toast.LENGTH_SHORT);
            }
        });
    }

    private void updateUi(){
        time1Set.setText(timeForFive+"");
        time2Set.setText(timeForFour+"");
        time3Set.setText(timeForThree+"");
        time4Set.setText(timeForTwo+"");
        time5Set.setText(timeForOne+"");
        stoveCheck.setChecked(power==1);
        if (power==1){
            totalSetting.setVisibility(View.VISIBLE);
        }else{
            totalSetting.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.stove_return, R.id.stove_check, R.id.set_btn1, R.id.set_btn2, R.id.set_btn3, R.id.set_btn4, R.id.set_btn5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.stove_return:
                UIService.getInstance().popBack();
                break;
            case R.id.stove_check:
                LogUtils.i("20180529","hhh::"+stoveCheck.isChecked());
                if (stoveCheck.isChecked()){
                    sendOffOrOnCommand(ON,(short) 0);
                }else{
                    sendOffOrOnCommand(OFF,(short) 0);
                }
                //
                break;
            case R.id.set_btn1:
                wheelViewMethod(5,19);
                break;
            case R.id.set_btn2:
                wheelViewMethod(4,59);
                break;
            case R.id.set_btn3:
                wheelViewMethod(3,89);
                break;
            case R.id.set_btn4:
                wheelViewMethod(2,119);
                break;
            case R.id.set_btn5:
                wheelViewMethod(1,179);
                break;
            default:
                break;
        }
    }

    private void sendOffOrOnCommand(final short power, short argument){
        stove.setStoveAutoPowerOff(power, argument, new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("指令下发成功",Toast.LENGTH_SHORT);
                if (power==0){
                    totalSetting.setVisibility(View.GONE);
                }else{
                    totalSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令下发失败",Toast.LENGTH_SHORT);
                Message msg = Message.obtain();
                msg.what = 3;
                mHandler.sendMessage(msg);

            }
        });
    }

    private void wheelViewMethod(final int num,int defaultIndex){

        if (mTimingDialog != null){
            mTimingDialog = null;
        }
        if (mTimingDialog == null){
            mTimingDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_03);
        }
        mTimingDialog.setWheelViewData(null, getListTime(), null, false, 0, defaultIndex, 0, null, new OnItemSelectedListenerCenter() {
            @Override
            public void onItemSelectedCenter(String contentCenter) {
                LogUtils.i("20180702","contentCenter:"+ contentCenter);
                Message message = mHandler.obtainMessage();
                message.what = 1;
                message.arg1 = num;
                message.obj = contentCenter;
                mHandler.sendMessage(message);
            }
        },null);
        mTimingDialog.show();
    }

    private void reSetTimeData(String data, final int num) {
        String removeString = RemoveManOrsymbolUtil.getRemoveString(data);
        final int i = Integer.parseInt(removeString);
       // final int seconds = 60 * i;
        mTimingDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimingDialog!=null&&mTimingDialog.isShow()){
                    mTimingDialog.dismiss();
                }
            }
        });

        mTimingDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimingDialog.dismiss();
                sendPowerOffTimeCommand((short) num,(short) i);
            }
        });
    }

    private void sendPowerOffTimeCommand(final short num,final short i){
        stove.setStoveAutoPowerOffTime(num, i, new VoidCallback() {
            @Override
            public void onSuccess() {
                setUpdateText(num,i);

                ToastUtils.show("指令设置成功", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show("指令设置失败", Toast.LENGTH_SHORT);
            }
        });
    }

    private void setUpdateText(int num,int i){
        switch (num){
            case 5:
                time1Set.setText(i+"");
                break;
            case 4:
                time2Set.setText(i+"");
                break;
            case 3:
                time3Set.setText(i+"");
                break;
            case 2:
                time4Set.setText(i+"");
                break;
            case 1:
                time5Set.setText(i+"");
                break;
            default:
                break;

        }
    }

    private List<String> getListTime(){
        List<String> listTime = new ArrayList<>();
        for (int i = 1 ; i <= 180; i++){
            listTime.add(i+ StringConstantsUtil.STRING_MINUTES);
        }
        return listTime;
    }

    @Override
    public void onCheckedChanged(CompoundButton chkBox, boolean isChecked) {
        LogUtils.i("20180529","chkBox:"+chkBox+" isChecked::"+isChecked);
    }
}
