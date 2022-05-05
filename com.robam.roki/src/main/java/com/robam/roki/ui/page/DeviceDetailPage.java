package com.robam.roki.ui.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.events.DeleteChildDeviceEvent;
import com.legent.plat.pojos.User;
import com.legent.plat.pojos.device.AbsDevice;
import com.legent.plat.pojos.device.AbsDeviceHub;
import com.legent.plat.pojos.device.DeviceGuid;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.NestedListView;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.DeviceDeleteEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.gassensor.GasSensor;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 * 厨电详情
 */
public class DeviceDetailPage extends MyBasePage<MainActivity> {

    @InjectView(R.id.deviceListView)
    NestedListView deviceListView;
    @InjectView(R.id.userListView)
    NestedListView userListView;

    DeviceAdapter deviceAdapter;
    UserAdapter userAdapter;
    AbsDeviceHub deviceHub;
    @InjectView(R.id.txtDevice)
    TextView mTxtDevice;
    @InjectView(R.id.tv_delete)
    TextView mTvDelete;
    @InjectView(R.id.img_back)
    ImageView mImgBack;
    private boolean ifDelete = true;//从fan8700烟机进入 删除文字 禁用
    AbsDevice absDevice;
    String guid;

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        guid = getArguments().getString(PageArgumentKey.Guid);
//        ifDelete = getArguments().getBoolean(PageArgumentKey.IfDeleteInDeviceDetail, true);
//        if (guid != null && guid.contains(IRokiFamily.R0003)) {
//            absDevice = Plat.deviceService.lookupChild(guid);
//        } else {
//            deviceHub = Plat.deviceService.lookupChild(guid);
//        }
//
//        View view = inflater.inflate(R.layout.page_device_detail, container, false);
//        ButterKnife.inject(this, view);
//
//        regsitRightView();
//        deviceAdapter = new DeviceAdapter();
//        userAdapter = new UserAdapter();
//
//        deviceListView.setAdapter(deviceAdapter);
//        userListView.setAdapter(userAdapter);
//        initData();
//        return view;
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_device_detail;
    }

    @Override
    protected void initView() {
        guid = getArguments().getString(PageArgumentKey.Guid);
        ifDelete = getArguments().getBoolean(PageArgumentKey.IfDeleteInDeviceDetail, true);
        if (guid != null && guid.contains(IRokiFamily.R0003)) {
            absDevice = Plat.deviceService.lookupChild(guid);
        } else {
            deviceHub = Plat.deviceService.lookupChild(guid);
        }

        regsitRightView();
        deviceAdapter = new DeviceAdapter();
        userAdapter = new UserAdapter();

        deviceListView.setAdapter(deviceAdapter);
        userListView.setAdapter(userAdapter);
//        initData();
    }


    @Override
    protected void initData() {

        List<IDevice> devices = Lists.newArrayList();
        if (IRokiFamily.R0003.equals(guid.substring(0, 5))) {
            devices.add(absDevice);
            id = absDevice.getID();
            falg = true;
        } else {
            devices.add(deviceHub);
            Stove stove = deviceHub.getChild();
            List<GasSensor> gasSensor = deviceHub.getChildGasSensor();
            Pot pot = deviceHub.getChildPot();
            LogUtils.i("20180606", "gas::" + deviceHub.getChildGasSensor());
            //getChildByDeviceType(IRokiFamily.R9W70);
            if (stove != null) {
                devices.add(stove);
            }

            if (pot != null) {
                devices.add(pot);
            }

            if (gasSensor != null && gasSensor.size() > 0) {
                for (GasSensor gas : gasSensor) {
                    devices.add(gas);
                }
                falg = false;
            }

            id = deviceHub.getID();
        }
        LogUtils.i("20180817", "devices::" + devices.toString());
        deviceAdapter.loadData(devices);
        long ownerId = Plat.accountService.getCurrentUserId();
        Plat.deviceService.getDeviceUsers(ownerId, id, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                userAdapter.loadData(users);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.showThrowable(t);
            }
        });
    }
    @Subscribe
    public void onEvent(DeleteChildDeviceEvent event) {
        boolean isSuccue = event.isSuccue;
        if (isSuccue) {
           /* List<IDevice> list = Plat.deviceService.queryAll();
            deviceAdapter.loadData(list);*/
            ToastUtils.showShort(R.string.delete_device_success);
        } else {

            ToastUtils.show(R.string.delete_fali, Toast.LENGTH_LONG);
        }
//        UIService.getInstance().popBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    String id;
    boolean falg = false;



    void onUnbind() {

        final IRokiDialog dialogByType = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        dialogByType.setTitleText(R.string.title_dele_device);
        dialogByType.show();
        dialogByType.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogByType.isShow()) {
                    dialogByType.dismiss();
                }

                ProgressDialogHelper.setRunning(cx, true);
                LogUtils.i("20190605", "id:" + id);
                Plat.deviceService.deleteWithUnbind(id, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ProgressDialogHelper.setRunning(cx, false);
                        UIService.getInstance().popBack();
                        EventUtils.postEvent(new DeviceDeleteEvent(""));
                        ToastUtils.showShort(R.string.delete_device_success);
                        LogUtils.i("20190605", "onSuccess成功");
                        if (!ifDelete) {
                            UIService.getInstance().returnHome();
                        }
                        sendUIRefreshCommand();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ProgressDialogHelper.setRunning(cx, false);
                        ToastUtils.showThrowable(t);
                        LogUtils.i("20190605", "onFailure成功");
                    }
                });


            }
        });

        dialogByType.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogByType.isShow()) {
                    dialogByType.dismiss();
                }
            }
        });

    }

    //发送界面刷新广播
    private void sendUIRefreshCommand() {
        Activity atv = UIService.getInstance().getTop().getActivity();
        Intent intent = new Intent();
        intent.setAction("com.robam.roki.senduirefreshcommand");
        atv.sendBroadcast(intent);
    }

    void regsitRightView() {
        if (!ifDelete) {
            return;
        }
        TextView txtView = TitleBar.newTitleTextView(cx, cx.getString(R.string.my_device_delete),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onUnbind();
                    }
                });

        txtView.setTextColor(getResources().getColor(R.color.c11));
    }

    @OnClick(R.id.img_back)
    public void onMImgBackClicked() {
        UIService.getInstance().popBack();
    }

    @OnClick(R.id.tv_delete)
    public void onMTvDeleteClicked() {
        if (absDevice!=null) {
            ToolUtils.logEvent("设备管理","删除:"+absDevice.getDt(),"roki_个人");
        }

        onUnbind();
    }


    class DeviceAdapter extends ExtBaseAdapter<IDevice> {

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            final ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(cx).inflate(R.layout.view_device_detail, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            final IDevice dev = list.get(position);
            vh.showData(dev);
            vh.mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isDeleteToast(dev, list, position);
                }
            });
            return convertView;
        }


        class ViewHolder {
            @InjectView(R.id.imgDevice)
            ImageView imgDevice;
            @InjectView(R.id.txtDevice)
            TextView txtDevice;
            @InjectView(R.id.txtDeviceType)
            TextView txtDeviceType;
            @InjectView(R.id.txtBid)
            TextView txtBid;
            @InjectView(R.id.txtOtaVer)
            TextView txtOtaVer;
            @InjectView(R.id.tv_delete)
            Button mTvDelete;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            void showData(IDevice device) {
                DeviceGuid dg = device.getGuid();
                DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(dg.getGuid());

                String dispalyType = device.getDispalyType();
                txtDeviceType.setText(dispalyType);
                txtDevice.setText(device.getCategoryName() + cx.getString(R.string.detail));
                txtBid.setText(device.getBid());
                txtOtaVer.setText(String.valueOf(device.getVersion()));

                if (Utils.isFan(device.getID())) {
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_detail_fan);
                } else if (Utils.isStove(device.getID())) {
                    mTvDelete.setVisibility(View.VISIBLE);
                    imgDevice.setImageResource(R.mipmap.ic_device_detail_stove);
                } else if (Utils.isPot(device.getID())) {
                    mTvDelete.setVisibility(View.VISIBLE);
                    imgDevice.setImageResource(R.mipmap.ic_device_detail_pot);

                } else if (Utils.isGasSensor(device.getID())) {
                    if (falg) {
                        mTvDelete.setVisibility(View.INVISIBLE);
                    } else {
                        mTvDelete.setVisibility(View.VISIBLE);
                    }

                    imgDevice.setImageResource(R.mipmap.ic_device_gas);
                }

                if (Utils.isSteam(device.getID())) {//蒸箱
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_zhengxiang);
                } else if (Utils.isMicroWave(device.getID())) {//微波炉
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_weibolu);
                } else if (Utils.isOven(device.getID())) {//烤箱
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_kaoxiang);
                } else if (Utils.isWaterPurifier(device.getID())) {//净水器
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_jingshuiji);
                } else if (Utils.isSterilizer(device.getID())) {//消毒柜
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_xiaodugui);
                } else if (Utils.isSteamOvenMsg(device.getID())) {//一体机
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_yitiji);
                } else if (Utils.isRikaMsg(device.getID())) {//RIKA
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_detail_rika);
                }else if (Utils.isDishWasher(device.getID())){//洗碗机
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_detail_washer);

                }else if (Utils.isHidKitMsg(device.getID())){
                    mTvDelete.setVisibility(View.GONE);
                    imgDevice.setImageResource(R.mipmap.ic_device_voice);
                }

            }
        }
    }

    private void isDeleteToast(final IDevice device, final List list, final int position) {
        final IRokiDialog deleteDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        deleteDialog.setTitleText(R.string.is_delete_content);
        deleteDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
                if (Utils.isStove(device.getID())) {
                    Stove stove = (Stove) device;
                    AbsFan parent = (AbsFan) stove.getParent();
                    parent.delPotDevice(stove.getGuid().getGuid());
                    list.remove(position);
                    deviceAdapter.notifyDataSetChanged();
                } else if (Utils.isGasSensor(device.getID())) {
                    GasSensor gasSensor = (GasSensor) device;
                    AbsFan parent = (AbsFan) gasSensor.getParent();
                    parent.delPotDevice(gasSensor.getGuid().getGuid());
                    list.remove(position);
                    deviceAdapter.notifyDataSetChanged();
                } else if (Utils.isPot(device.getID())) {
                    if (list.size() > position) {
                        Pot pot = (Pot) device;
                        AbsFan parent = (AbsFan) pot.getParent();
                        parent.delPotDevice(pot.getGuid().getGuid());
                        list.remove(position);
                        deviceAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        deleteDialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show();
    }

    class UserAdapter extends ExtBaseAdapter<User> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {
                convertView = LayoutInflater.from(cx).inflate(R.layout.view_device_user_item, parent, false);
                vh = new ViewHolder(convertView);
                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            User user = list.get(position);
            vh.showData(user);
            return convertView;
        }


        class ViewHolder {
            @InjectView(R.id.txtUserName)
            TextView txtUserName;
            @InjectView(R.id.txtDesc)
            TextView txtDesc;

            ViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

            void showData(User user) {
                txtUserName.setText(user.name);
                txtDesc.setText(user.phone);
            }
        }
    }

}
