package com.robam.roki.ui.view.networkoptimization;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;
import com.robam.roki.R;
import com.robam.roki.service.IanSend;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.widget.base.BaseDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sylar on 15/6/14.
 */
public class DeviceAddPage extends HeadPage {
    /**
     * Called when the activity is first created.
     */
    private ExpandableListView expandableListView;
    List<DeviceGroupList> groupList = new ArrayList<DeviceGroupList>();
    List<List<DeviceItemList>> deviceList = new ArrayList<List<DeviceItemList>>();
    IanSend send;
    private int form;

    public void setGroupListAndDeviceList() {
        RokiRestHelper.getNetworkDeviceInfoRequest("roki", null, null, new Callback<List<DeviceGroupList>>() {
            @Override
            public void onSuccess(List<DeviceGroupList> deviceGroupLists) {

                for (int i = 0; i < deviceGroupLists.size(); i++) {
                    LogUtils.i("20170214", "deviceGroupLists" + deviceGroupLists.get(i).getDc());
                }
                groupList.clear();
                deviceList.clear();
                for (int i = 0; i < deviceGroupLists.size(); i++) {
                    deviceGroupLists.get(i).save2db();
                }
                for (int i = 0; i < deviceGroupLists.size(); i++) {
                    DeviceGroupList groupListBean = deviceGroupLists.get(i);
                    addGroupListBean(groupListBean, deviceGroupLists, i, i, R.mipmap.youyanji, groupListBean.name, groupListBean.englishName);
                }
                Comparator comp = new SortComparatorGroupListBean();
                Collections.sort(groupList, comp);
                for (int i = 0; i < groupList.size(); i++) {
                    LogUtils.i("20170214", "groupList:" + groupList.get(i).getDeviceItemLists());
                    deviceList.add(groupList.get(i).getDeviceItemLists());
                }
                setExpandableListAdapter();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    protected View onCreateContentView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.expandablelistview, viewGroup, false);
        ButterKnife.inject(this, view);
        form = getArguments() == null ? 0 : getArguments().getInt("form" , 0);
        expandableListView = view.findViewById(R.id.expandableListView);
        setGroupListAndDeviceList();
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int count = expandableListView.getExpandableListAdapter().getGroupCount();
                for (int j = 0; j < count; j++) {
                    if (j != groupPosition) {
                        expandableListView.collapseGroup(j);
                    }
                }
            }
        });
        send = new IanSend(activity, cx);
        send.addOnDevicelistener(new IanSend.OnDevicelistener() {
            @Override
            public void onDeviceGuid(String guid) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDialog(guid);
                    }
                });

            }
        });
        send.join();
        return view;
    }


    //创建ExpandableList的Adapter容器
    private void setExpandableListAdapter() {
      /*  for (int i = 0; i < deviceList.size(); i++) {
            LogUtils.i("20170214","deviceList:"+deviceList.get(i).toString());
        }*/
        LogUtils.i("20170214", "deviceList:" + deviceList.size());
        ExpandableListAdapter adapter = new ExpandableListAdapter(cx, groupList, deviceList);
        expandableListView.setAdapter(adapter);
    }

    @OnClick(R.id.layoutSearch)
    public void onClickLayoutSearch() {
        UIService.getInstance().postPage(PageKey.DeviceSearch);
    }


    //向设备组集合里面添加设备并排序
    private void addGroupListBean(DeviceGroupList groupListBean, List<DeviceGroupList> deviceGroupLists, int position, int sortId, int mipmap, String deviceName, String deviceEnglishName) {
        groupListBean = deviceGroupLists.get(position);
        groupListBean.setDeviceName(deviceName);
        groupListBean.setDeviceEnglishName(deviceEnglishName);
        groupListBean.setImage_device(mipmap);
        groupListBean.setSortId(sortId);
        groupList.add(groupListBean);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    BaseDialog baseDialog;
    private boolean isDismiss = false;

    private void showDialog(String guid) {

        if (isDismiss) {
            return;
        }
        if (!guid.contains("610")){
            return;
        }
        if (baseDialog == null) {
            if (!guid.contains("610")){
                return;
            }
            baseDialog = new BaseDialog(activity);
            baseDialog.setContentView(R.layout.dialog_device_wifi_610);
            TextView tvDeviceName = (TextView)baseDialog.findViewById(R.id.tv_device_name);
            String deviceName = guid.substring(0, 5);
            if ("DB610".equals(deviceName)){
                tvDeviceName.setText("大厨多功能蒸烤一体机DB610");
            }else if ("B610D".equals(deviceName)){
                tvDeviceName.setText("大厨多功能蒸烤一体机DB610D");
            }else {
                return;
            }

            baseDialog.setCanceledOnTouchOutside(true);
            baseDialog.setGravity(Gravity.BOTTOM);
            baseDialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
            baseDialog.show();
            baseDialog.findViewById(R.id.btn_add_device).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDevice(guid);

                }
            });
            baseDialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseDialog.dismiss();
                }
            });
            baseDialog.addOnDismissListener(new BaseDialog.OnDismissListener() {
                @Override
                public void onDismiss(BaseDialog baseDialog) {
                    isDismiss = true;
                }
            });
        } else {
            if (baseDialog.isShowing()) {

            } else {
                baseDialog.show();
            }
        }

    }

    private void addDevice(String guid) {
        DeviceInfo info = new DeviceInfo();
        info.ownerId = Plat.accountService.getCurrentUserId();
        info.name = DeviceTypeManager.getInstance().getDeviceType(
                guid).getName();
        info.guid = guid;
        Plat.deviceService.addWithBind(info.guid, info.name,
                true, new VoidCallback() {

                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort("添加完成");
                        EventUtils.postEvent(new DeviceEasylinkCompletedEvent(info));
//                        UIService.getInstance().returnHome();
                        baseDialog.dismiss();
//                        Bundle bd = new Bundle();
//                        bd.putString(PageArgumentKey.Guid, guid);
//                        bd.putShort(PageArgumentKey.from, (short) 1);
//                        UIService.getInstance().postPage(PageKey.AbsDeviceSteamOvenOne, bd);
                        UIService.getInstance().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (send != null) {
            send.close();
        }
    }

    @Override
    protected void setStateBarFixer() {
        if (form == 1){
            super.setStateBarFixer();
        }else {
            super.setStateBarFixer();
        }

    }
}
