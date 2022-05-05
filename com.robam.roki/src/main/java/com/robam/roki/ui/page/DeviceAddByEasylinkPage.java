package com.robam.roki.ui.page;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.PrefsKey;
import com.legent.plat.io.device.IDeviceFinder;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.legent.utils.api.WifiUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.roki.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

@SuppressLint("InflateParams")
public class DeviceAddByEasylinkPage extends HeadPage {
    @InjectView(R.id.txtWifi)
    TextView txtWifi;
    @InjectView(R.id.edtPwd)
    EditText edtPwd;
    @InjectView(R.id.txtConnect)
    TextView txtConnect;

    @Override
    public View onCreateContentView(LayoutInflater inflater,
                                    ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_device_add_by_easylink,
                container, false);

        ButterKnife.inject(this, view);

        String ssid = WifiUtils.getSSIDByNetworkId(Plat.app);
        txtWifi.setText(ssid);
        if (ssid != null) {
            String pwd = PreferenceUtils.getString(ssid, null);
            edtPwd.setText(pwd);
            if (Strings.isNullOrEmpty(pwd)) {
                edtPwd.requestFocus();
            }
        }

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnCheckedChanged(R.id.chkShowPwd)
    public void onCheckedChanged(boolean checked) {
        if (!checked)
            edtPwd.setInputType(InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else
            edtPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    @OnClick(R.id.txtConnect)
    public void onClickConnect() {
        try {
            easylink();
        } catch (Exception ex) {
            ToastUtils.showShort(ex.getMessage());
        }
    }


    private void easylink() {

        final String ssid = txtWifi.getText().toString();
        final String pwd = edtPwd.getText().toString();

        Preconditions.checkState(!Strings.isNullOrEmpty(ssid),
                getString(R.string.roki_error_no_wifi));
        Preconditions.checkState(!Strings.isNullOrEmpty(pwd),
                getString(R.string.roki_wifi_pwd_hint));

        PreferenceUtils.setString(PrefsKey.Ssid, ssid);
        PreferenceUtils.setString(ssid, pwd);

       IDeviceFinder finder =  Plat.dcMqtt.getDeviceFinder();

        ProgressDialogHelper.setRunning(cx, true);
       /* ToastUtils.show("ssid:"+ssid, Toast.LENGTH_SHORT);
        ToastUtils.show("pwd:"+pwd, Toast.LENGTH_SHORT);
        Log.i("checkConnect","ssid:"+ssid+" pwd:"+pwd);*/
        finder.start(ssid, pwd, 1000 * 40, new Callback<DeviceInfo>() {

            @Override
            public void onSuccess(DeviceInfo result) {
                Log.i("20170323","ssid:"+ssid+"pwd:"+pwd);
                Log.i("20170323","result:"+result);
                ProgressDialogHelper.setRunning(cx, false);
                PreferenceUtils.setString(ssid, pwd);

                addKettle(result);
            }

            @Override
            public void onFailure(Throwable t) {
             //   Log.i("20170323","再试一次"+t.getMessage());
                ProgressDialogHelper.setRunning(cx, false);

                txtConnect.setText("再试一次");
                ToastUtils.showThrowable(t);
            }
        });
    }


    void addKettle(final DeviceInfo devInfo) {
        ToastUtils.show("devInfo:"+devInfo,Toast.LENGTH_SHORT);
        devInfo.ownerId = Plat.accountService.getCurrentUserId();
        if (Strings.isNullOrEmpty(devInfo.name)) {
            DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(
                    devInfo.guid);
            devInfo.name = dt.getName();
        }

        Plat.deviceService.addWithBind(devInfo.guid, devInfo.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort("添加完成");
                        LogUtils.i("20170323","devInfo"+devInfo);
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(devInfo));
                        UIService.getInstance().returnHome();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });

    }

}
