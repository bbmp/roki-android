package com.robam.roki.ui.view.networkoptimization;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

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
import com.robam.base.BaseDialog;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.DeviceGroupList;
import com.robam.common.pojos.DeviceItemList;
import com.robam.common.pojos.Group;
import com.robam.common.pojos.Tag;
import com.robam.roki.R;
import com.robam.roki.model.bean.RecipeTagGroupItem;
import com.robam.roki.service.IanSend;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.bean3.DeviceTagGroupItem;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.ClassifyTagRecipePage;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.linkrecipetag.LinkageRecyclerView;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkagePrimaryViewHolder;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkageSecondaryFooterViewHolder;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkageSecondaryHeaderViewHolder;
import com.robam.roki.ui.view.linkrecipetag.adapter.viewholder.LinkageSecondaryViewHolder;
import com.robam.roki.ui.view.linkrecipetag.bean.BaseGroupedItem;
import com.robam.roki.ui.view.linkrecipetag.contract.ILinkagePrimaryAdapterConfig;
import com.robam.roki.ui.view.linkrecipetag.contract.ILinkageSecondaryAdapterConfig;
import com.robam.roki.ui.view.recipeclassify.RecipeClassifyPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import butterknife.ButterKnife;
import butterknife.OnClick;
import skin.support.content.res.SkinCompatResources;

/**
 * Created by sylar on 15/6/14.
 */
public class DeviceAddPage extends MyBasePage<MainActivity> {
    private final String TAG = DeviceAddPage.this.getClass().getName();
    /**
     * Called when the activity is first created.
     */
    private LinkageRecyclerView linkageRecyclerView;
    private ImageView ivBack;
    private static final int SPAN_COUNT_FOR_GRID_MODE = 3;
    private static final int MARQUEE_REPEAT_LOOP_MODE = -1;
    private static final int MARQUEE_REPEAT_NONE_MODE = 0;

    IanSend send;
    private int form;


    public void setGroupListAndDeviceList() {
        RokiRestHelper.getNetworkDeviceInfoRequest("roki", null, null, new Callback<List<DeviceGroupList>>() {
            @Override
            public void onSuccess(List<DeviceGroupList> deviceGroupLists) {
                List<DeviceTagGroupItem> deviceTagGroupItemList = new ArrayList<>();
                if (deviceGroupLists != null) {
                    for (DeviceGroupList group : deviceGroupLists) {

                        DeviceTagGroupItem deviceTagGroupItem = new DeviceTagGroupItem(true, group.name);
                        deviceTagGroupItem.isHeader = true;
                        deviceTagGroupItem.header = group.name;
                        deviceTagGroupItemList.add(deviceTagGroupItem);
                        LogUtils.i(TAG, "group name:" + group.name + " group toString :" + group.toString());
                        for (DeviceItemList deviceItem : group.getDeviceItemLists()) {
                            DeviceTagGroupItem.ItemInfo itemInfo = new DeviceTagGroupItem.ItemInfo(group.name, group.name, deviceItem.getNetImgUrl(), deviceItem.getName());
                            itemInfo.setTitle(group.name);
                            itemInfo.setGroup(group.name);
                            itemInfo.setIconUrl(deviceItem.iconUrl);
                            itemInfo.setDp(deviceItem.getDp());
                            itemInfo.setDisplayType(deviceItem.displayType);
                            itemInfo.setNetTips(deviceItem.getNetTips());
                            DeviceTagGroupItem deviceTagGroupItem1 = new DeviceTagGroupItem(itemInfo);
                            deviceTagGroupItem1.isHeader = false;
                            deviceTagGroupItemList.add(deviceTagGroupItem1);
                        }
                    }
                }
                linkageRecyclerView.init(deviceTagGroupItemList, new ElemeLinkagePrimaryAdapterConfig(), new ElemeLinkageSecondaryAdapterConfig());
                linkageRecyclerView.setGridMode(true);

                for (int i = 0; i < deviceGroupLists.size(); i++) {
                    LogUtils.i("20170214", "deviceGroupLists" + deviceGroupLists.get(i).getDc());
                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }


    @OnClick(R.id.layoutSearch)
    public void onClickLayoutSearch() {
        UIService.getInstance().postPage(PageKey.DeviceSearch);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.expandablelistview;
    }

    @Override
    protected void initView() {
        form = getArguments() == null ? 0 : getArguments().getInt("form" , 0);
        linkageRecyclerView = findViewById(R.id.linkage_rv);
        ivBack = findViewById(R.id.img_back);

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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack();
            }
        });
    }

    @Override
    protected void initData() {
        setGroupListAndDeviceList();

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

//    @Override
//    protected void setStateBarFixer() {
//        if (form == 1){
//            super.setStateBarFixer();
//        }else {
//            super.setStateBarFixer();
//        }
//
//    }
private class ElemeLinkagePrimaryAdapterConfig implements ILinkagePrimaryAdapterConfig {

    private Context mContext;

    @Override
    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.default_adapter_linkage_primary;
    }

    @Override
    public int getGroupTitleViewId() {
        return R.id.tv_group;
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_group;
    }

    @Override
    public void onBindViewHolder(LinkagePrimaryViewHolder holder, boolean selected, String title) {
        LogUtils.i(TAG, "onBindViewHolde title:" + title);
        TextView tvTitle = ((TextView) holder.mGroupTitle);
//            tvTitle.setTextSize(16);
        tvTitle.setText(title);
//            tvTitle.setBackgroundColor(mContext.getResources().getColor(
//                    selected ? com.robam.roki.ui.view.linkrecipetag.R.color.colorPurple : com.robam.roki.ui.view.linkrecipetag.R.color.colorWhite));
        int target = SkinCompatResources.getInstance().getTargetResId(mContext, R.color.text_color_net_err);
        target = (target != 0)?target:R.color.text_color_net_err;
        tvTitle.setTextColor(ContextCompat.getColor(mContext,
                selected ? R.color.text_select_color : target));
        tvTitle.setEllipsize(selected ? TextUtils.TruncateAt.MARQUEE : TextUtils.TruncateAt.END);
        tvTitle.setFocusable(selected);
        tvTitle.setFocusableInTouchMode(selected);
        tvTitle.setMarqueeRepeatLimit(selected ? MARQUEE_REPEAT_LOOP_MODE : MARQUEE_REPEAT_NONE_MODE);
    }

    @Override
    public void onItemClick(LinkagePrimaryViewHolder holder, View view, String title) {
        //TODO
    }

}

    private class ElemeLinkageSecondaryAdapterConfig implements
            ILinkageSecondaryAdapterConfig<DeviceTagGroupItem.ItemInfo> {

        private Context mContext;

        @Override
        public void setContext(Context context) {
            mContext = context;
        }

        @Override
        public int getGridLayoutId() {
            return R.layout.item_device_tag_adapter_secondary_grid;
        }

        @Override
        public int getLinearLayoutId() {
            return R.layout.item_recipe_tag_adapter_secondary_linear;
        }

        @Override
        public int getHeaderLayoutId() {
            return R.layout.default_adapter_linkage_secondary_header;
        }

        @Override
        public int getFooterLayoutId() {
            return 0;
        }

        @Override
        public int getHeaderTextViewId() {
            return R.id.secondary_header;
        }

        @Override
        public int getSpanCountOfGridMode() {
            return SPAN_COUNT_FOR_GRID_MODE;
        }

        @Override
        public void onBindViewHolder(final LinkageSecondaryViewHolder holder,
                                     final BaseGroupedItem<DeviceTagGroupItem.ItemInfo> item) {

            LogUtils.i(TAG, "item header:" + item.header + " item.info.getName:" + item.info.getName() + " item Title:" + item.info.getTitle());
            ((TextView) holder.getView(R.id.tv_device_tag_name)).setText(item.info.getName());
            ImageView imageView = holder.getView(R.id.iv_device_tag);
            GlideApp.with(cx).load(item.info.getIconUrl()).into(imageView);
            holder.getView(R.id.iv_goods_item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(item.info.getName())) {

                        return;
                    }

                    if ("YYJ02".equals(item.info.getDp())) {//有屏
                        Bundle bundle = new Bundle();
                        bundle.putString("NetImgUrl", item.info.getImgUrl());
                        bundle.putString("displayType", item.info.getDisplayType());
                        bundle.putString("strDeviceName", item.info.getName());
                        UIService.getInstance().postPage(PageKey.DeviceScan, bundle);
                    } else if ("YYJ01".equals(item.info.getDp())) {//无屏
                        SwitchToWiFiConnectPage(item);
                    }
//                    else if (strDeviceName.contains("找不到")) {
//                        UIService.getInstance().postPage(PageKey.CantfindDevice);
//                    }
                    else {
                        SwitchToWiFiConnectPage(item);
                    }
                }
            });

        }

        @Override
        public void onBindHeaderViewHolder(LinkageSecondaryHeaderViewHolder holder,
                                           BaseGroupedItem<DeviceTagGroupItem.ItemInfo> item) {

            ((TextView) holder.getView(R.id.secondary_header)).setText(item.header);
        }

        @Override
        public void onBindFooterViewHolder(LinkageSecondaryFooterViewHolder holder,
                                           BaseGroupedItem<DeviceTagGroupItem.ItemInfo> item) {

        }
    }
    private void SwitchToWiFiConnectPage(BaseGroupedItem<DeviceTagGroupItem.ItemInfo> item) {
        Bundle bundle = new Bundle();
        bundle.putString("NetImgUrl", item.info.getImgUrl());
        bundle.putString("NetTips", item.info.getNetTips());
        bundle.putString("displayType", item.info.getDisplayType());
        bundle.putString("chnName", item.info.getGroup());
        bundle.putString("strDeviceName", item.info.getName());
        LogUtils.i("20170802", "strDeviceName::" + item.info.getName());
        LogUtils.i("20170227", "NetTips:" + item.info.getNetTips());

       /* if ("9W70".equals(strDeviceName)||"9B39".equals(strDeviceName)){
            UIService.getInstance().postPage(PageKey.DeviceZJWifiConnect, bundle);
        }else{*/
        UIService.getInstance().postPage(PageKey.DeviceWifiConnect, bundle);
        //}


    }
}
