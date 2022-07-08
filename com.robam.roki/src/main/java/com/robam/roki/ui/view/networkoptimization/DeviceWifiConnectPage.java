package com.robam.roki.ui.view.networkoptimization;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.robam.base.BaseDialog;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.pojos.device.DeviceInfo;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.popoups.Dp2PxUtils;
import com.legent.utils.EventUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.DeviceEasylinkCompletedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.NetWorkingSteps;
import com.robam.roki.R;
import com.robam.roki.service.IanSend;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.form.MainActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/14.
 */

public class DeviceWifiConnectPage extends MyBasePage<MainActivity> {
    String NetImgUrls;
    String displayType;
    String NetTips;
    ImageView img_device;
    TextView tip1;
    TextView cannotfinishperform;
    Button button;
    TextView tip2;
    TextView tip3;
    String[] text;
    StringBuilder sb = new StringBuilder();
    ImageView imgTip;
    String txt;
    String strDeviceName;
    String chnName;

    List<NetWorkingSteps> steps;
    int step = 0;
    /**
     * 发现这杯提示
     */
    private TextView tv_add_device_name;
    private ImageView ivBack;

    IanSend send;
    BaseDialog baseDialog;
    private boolean isDismiss = false;

    @OnClick(R.id.next)
    public void onClickNext() {
        LogUtils.i("20170227", "txt:" + txt);
        if (steps == null) {
            return;
        }
        if (steps.size() > 1) {
            if ((step + 1) == steps.size()) {
                UIService.getInstance().postPage(PageKey.WifiConnect);
                return;
            }
            step++;
            Glide.with(getContext()).load(steps.get(step).netImgUrl).into(img_device);
            button.setText(steps.get(step).buttonDesc);
            txt = steps.get(step).buttonDesc;
            RichText.from(steps.get(step).netRichText).fix(new ImageFixCallback() {
                @Override
                public void onInit(ImageHolder holder) {
                    holder.setHeight(Dp2PxUtils.dp2px(getActivity(), 16));
                    holder.setWidth(Dp2PxUtils.dp2px(getActivity(), 16));
                }

                @Override
                public void onLoading(ImageHolder holder) {

                }

                @Override
                public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {

                }

                @Override
                public void onImageReady(ImageHolder holder, int width, int height) {

                }

                @Override
                public void onFailure(ImageHolder holder, Exception e) {

                }
            }).into(tip1);
            return;
        }
        if ("确定".equals(txt)) {
            UIService.getInstance().popBack();
        } else {
            if ("5915ST".equals(displayType)  ) {
                Bundle bundle = new Bundle();
                bundle.putString("displayType", displayType);
                UIService.getInstance().postPage(PageKey.WifiSoftapConnect, bundle);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("displayType", displayType);
                UIService.getInstance().postPage(PageKey.WifiConnect, bundle);
            }
//            Bundle bundle = new Bundle();
//            bundle.putString("displayType", displayType);
//            UIService.getInstance().postPage(PageKey.WifiConnect, bundle);
//            LogUtils.i("获取网关信息", "ip=========="+NetworkUtils.getLocalIp());
//            LogUtils.i("获取网关信息", "Macip=========="+NetworkUtils.getMacByIp(NetworkUtils.getLocalIp()));

        }

    }

    @OnClick(R.id.cannotfinishperform)
    public void onClickCantfinishPerform() {
        LogUtils.i("20170804", "str::" + displayType);
        Bundle bundle = new Bundle();
        bundle.putString("strDeviceName", strDeviceName);
        UIService.getInstance().postPage(PageKey.CantFinish, bundle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_device_connect_desc;
    }

    @Override
    protected void initView() {
        NetImgUrls = getArguments().getString("NetImgUrl");
        NetTips = getArguments().getString("NetTips");
        strDeviceName = getArguments().getString("strDeviceName");
        displayType = getArguments().getString("displayType");
        chnName = getArguments().getString("chnName");

        img_device = findViewById(R.id.img_device);
        ivBack = findViewById(R.id.img_back);
        tv_add_device_name = findViewById(R.id.tv_add_device_name);
        button = findViewById(R.id.next);
        tip1 = findViewById(R.id.tip1);
        tip2 = findViewById(R.id.tip2);
        tip3 = findViewById(R.id.tip3);
        imgTip = findViewById(R.id.img_tip);
        if (!TextUtils.isEmpty(strDeviceName) && (strDeviceName.contains("J312") || strDeviceName.contains("J321") || strDeviceName.contains("J320"))) {
            imgTip.setImageResource(R.mipmap.img_312_network);
        }
        tv_add_device_name.setText(strDeviceName);
        if ("DB610".equals(strDeviceName)) {
//            if ("DB610".equals(strDeviceName)) {
            button.setVisibility(View.INVISIBLE);
//            }
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
        }
        try {
            if (NetTips != null) {
                text = NetTips.split("\n");
                if (text.length > 0) {
                    if (strDeviceName.contains("9B37")) { //9b37特殊处理
                        if (text.length == 1) {
                            sb.append(text[text.length - 1]);
                            button.setText("确定");
                        }
                    } else {
                        button.setText(text[text.length - 1]);
                    }
                    for (int i = 0; i < text.length - 1; i++) {
                        sb.append(text[i] + "\n");
                    }
                    txt = button.getText().toString();
                    if ("确定".equals(txt)) {
                        tip1.setText(sb.toString());
                        tip2.setVisibility(View.GONE);
                        tip3.setVisibility(View.GONE);
                        imgTip.setVisibility(View.GONE);
                    } else {
                        tip1.setText(text[0]);
                        tip2.setText(text[1]);
                        tip3.setText(text[2]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cannotfinishperform = findViewById(R.id.cannotfinishperform);
        cannotfinishperform.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        cannotfinishperform.getPaint().setAntiAlias(true);//抗锯齿
        if (NetImgUrls != null) {
            Glide.with(getContext()).load(NetImgUrls).into(img_device);
        }

        if (TextUtils.equals(chnName, getActivity().getString(R.string.device_stove_name)) || TextUtils.equals(chnName, getActivity().getString(R.string.stove_weishi))) {
            cannotfinishperform.setVisibility(View.VISIBLE);
        } else {
            cannotfinishperform.setVisibility(View.GONE);
        }

        setOnClickListener(R.id.img_back);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_back)
            UIService.getInstance().popBack();
    }

    @Override
    protected void initData() {
        RokiRestHelper.getNetworkDeviceStepsRequest(displayType, new Callback<List<NetWorkingSteps>>() {

            @Override
            public void onSuccess(List<NetWorkingSteps> netWorkingSteps) {
                if(netWorkingSteps == null || netWorkingSteps.size() == 0){
                    return;
                }
                steps = netWorkingSteps;
                String imgUrl = netWorkingSteps.get(0).netImgUrl;
                if (imgUrl.endsWith("gif")) {
                    Glide.with(getContext())
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                            .load(netWorkingSteps.get(0).netImgUrl)
                            .into(img_device);
                } else {
                    Glide.with(getContext()).load(netWorkingSteps.get(0).netImgUrl).into(img_device);
                }

                button.setText(netWorkingSteps.get(0).buttonDesc);
                String text = netWorkingSteps.get(0).netRichText;
                txt = netWorkingSteps.get(0).buttonDesc;
                text = text.replace("background", " ");
                RichText.from(text).fix(new ImageFixCallback() {
                    @Override
                    public void onInit(ImageHolder holder) {
                        holder.setHeight(Dp2PxUtils.dp2px(getActivity(), 16));
                        holder.setWidth(Dp2PxUtils.dp2px(getActivity(), 16));
                    }

                    @Override
                    public void onLoading(ImageHolder holder) {

                    }

                    @Override
                    public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {

                    }

                    @Override
                    public void onImageReady(ImageHolder holder, int width, int height) {

                    }

                    @Override
                    public void onFailure(ImageHolder holder, Exception e) {

                    }
                }).into(tip1);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (send != null) {
            send.close();
        }
    }

    private void showDialog(String guid) {
        if (isDismiss) {
            return;
        }
        if (baseDialog == null) {
            baseDialog = new BaseDialog(activity);
            baseDialog.setContentView(R.layout.dialog_device_wifi_610);
            TextView tvDeviceName = (TextView) baseDialog.findViewById(R.id.tv_device_name);
            String deviceName = guid.substring(0, 5);
            if ("DB610".equals(deviceName) ) {
                tvDeviceName.setText("大厨多功能蒸烤一体机DB610");
            } else if ("B610D".equals(deviceName) ) {
                tvDeviceName.setText("大厨多功能蒸烤一体机DB610D");
            } else {
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
                        UIService.getInstance().popBack().popBack();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.showThrowable(t);
                    }
                });

    }
}
