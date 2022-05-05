package com.robam.roki.ui.page.device.gassensor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringConstantsUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Dell on 2018/6/1.
 */

public class GasAlarmDealPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.txt_tel)
    ImageView txtTel;
    @InjectView(R.id.title)
    RelativeLayout title;
    IRokiDialog iRokiDialogAlarmType_01 = null;//一级报警
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gas_alarm_deal, container, false);
        ButterKnife.inject(this, view);
        iRokiDialogAlarmType_01 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_10);
        return view;
    }

    @OnClick(R.id.iv_back)
    public void onClickBack(){
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.txt_tel)
    public void OnClickTel(){
       // ToastUtils.show("拨打售后", Toast.LENGTH_SHORT);
        AlarmDialog();
    }

    private void AlarmDialog(){
        iRokiDialogAlarmType_01.setTitleText("一键售后");
        iRokiDialogAlarmType_01.setContentText("拨打95105855");
        iRokiDialogAlarmType_01.setOkBtn("直接拨打", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRokiDialogAlarmType_01.dismiss();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + StringConstantsUtil.STRING_SERVICE_PHONE));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        iRokiDialogAlarmType_01.setCancelBtn("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iRokiDialogAlarmType_01!=null&&iRokiDialogAlarmType_01.isShow()){
                    iRokiDialogAlarmType_01.dismiss();
                }
            }
        });
        iRokiDialogAlarmType_01.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
