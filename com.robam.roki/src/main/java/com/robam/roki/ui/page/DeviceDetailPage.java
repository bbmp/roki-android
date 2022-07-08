package com.robam.roki.ui.page;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
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
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.ui.ext.views.TitleBar;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.DeviceDeleteEvent;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.Stove.Stove;
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
    RecyclerView deviceListView;
    @InjectView(R.id.userListView)
    RecyclerView userListView;

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
        deviceListView.setLayoutManager(new LinearLayoutManager(getContext()));
        userListView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        deviceAdapter.setList(devices);
        deviceAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter baseQuickAdapter, @NonNull View view, int i) {
                switch (view.getId()) {
                    case R.id.tv_delete:
                        isDeleteToast(i);
                        break;
                }
            }
        });
        long ownerId = Plat.accountService.getCurrentUserId();
        Plat.deviceService.getDeviceUsers(ownerId, id, new Callback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                userAdapter.setList(users);
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

    void showData(BaseViewHolder baseViewHolder, IDevice device) {
        DeviceGuid dg = device.getGuid();
        DeviceType dt = DeviceTypeManager.getInstance().getDeviceType(dg.getGuid());

        String dispalyType = device.getDispalyType();
        baseViewHolder.setText(R.id.txtDeviceType, dispalyType);
        baseViewHolder.setText(R.id.txtDevice, device.getCategoryName() + cx.getString(R.string.detail));
        baseViewHolder.setText(R.id.txtBid, device.getBid());
        baseViewHolder.setText(R.id.txtOtaVer, String.valueOf(device.getVersion()));


        TextView tvDelete = baseViewHolder.getView(R.id.tv_delete);
        if (Utils.isFan(device.getID())) {
            tvDelete.setVisibility(View.GONE);

        } else if (Utils.isStove(device.getID())) {
            tvDelete.setVisibility(View.VISIBLE);

        } else if (Utils.isPot(device.getID())) {
            tvDelete.setVisibility(View.VISIBLE);


        } else if (Utils.isGasSensor(device.getID())) {
            if (falg) {
                tvDelete.setVisibility(View.INVISIBLE);
            } else {
                tvDelete.setVisibility(View.VISIBLE);
            }

        }

        if (Utils.isSteam(device.getID())) {//蒸箱
            tvDelete.setVisibility(View.GONE);
        } else if (Utils.isMicroWave(device.getID())) {//微波炉
            tvDelete.setVisibility(View.GONE);
        } else if (Utils.isOven(device.getID())) {//烤箱
            tvDelete.setVisibility(View.GONE);

        } else if (Utils.isWaterPurifier(device.getID())) {//净水器
            tvDelete.setVisibility(View.GONE);
        } else if (Utils.isSterilizer(device.getID())) {//消毒柜
            tvDelete.setVisibility(View.GONE);
        } else if (Utils.isSteamOvenMsg(device.getID())) {//一体机
            tvDelete.setVisibility(View.GONE);
        } else if (Utils.isRikaMsg(device.getID())) {//RIKA
            tvDelete.setVisibility(View.GONE);
        }else if (Utils.isDishWasher(device.getID())){//洗碗机
            tvDelete.setVisibility(View.GONE);

        }else if (Utils.isHidKitMsg(device.getID())){
            tvDelete.setVisibility(View.GONE);
        }

    }

    class DeviceAdapter extends BaseQuickAdapter<IDevice, BaseViewHolder> {

        public DeviceAdapter() {
            super(R.layout.view_device_detail);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, IDevice iDevice) {
            showData(baseViewHolder, iDevice);

        }
    }

    private void isDeleteToast(final int position) {
        final IRokiDialog deleteDialog = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_00);
        deleteDialog.setTitleText(R.string.is_delete_content);
        deleteDialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
                deviceAdapter.removeAt(position);

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

    class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

        public UserAdapter() {
            super(R.layout.view_device_user_item);
        }

        @Override
        protected void convert(@NonNull BaseViewHolder baseViewHolder, User user) {
            baseViewHolder.setText(R.id.txtUserName, user.name);
            baseViewHolder.setText(R.id.txtDesc, user.phone);
        }

    }

}
