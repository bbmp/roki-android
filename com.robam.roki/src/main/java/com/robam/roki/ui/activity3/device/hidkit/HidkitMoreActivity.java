package com.robam.roki.ui.activity3.device.hidkit;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.services.RestfulService;
import com.legent.utils.EventUtils;
import com.legent.utils.FileUtils;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.PreferenceUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.events.NewestVersionEvent;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.model.bean.HidKitUpdateBean;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.activity3.device.base.DeviceMoreActivity;
import com.robam.roki.utils.AlarmDataUtils;
import com.robam.roki.utils.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/30
 *     desc   : 藏宝盒更多界面
 *     version: 1.0
 * </pre>
 */
public class HidkitMoreActivity extends DeviceMoreActivity {

    private String KC306_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KC306.json";
    private String KM310_downloadUrl = "http://roki.oss-cn-hangzhou.aliyuncs.com/KM310.json";
    private String KC306_fileName = "KC306.json";
    private String KM310_fileName = "KM310.json";
    private IRokiDialog dialog;
    private IRokiDialog downDialog;

    @Override
    protected List<String> initMoreData() {
        ArrayList<String> data = new ArrayList<>();
        data.add("设备名称");
        data.add("留言咨询");
        data.add("一键售后");
        data.add("产品信息");
        data.add("检查更新");
        data.add("重新联网");
        return data;
    }


    @Override
    protected void otherOnClick(String more) {
        switch (more) {
            case "检查更新":
//                checkUpDate();
                downJsonVersion();
                break;
            case "重新联网":
                break;
        }
    }

    private void downJsonVersion() {
        if (mDevice instanceof AbsHidKit) {
            AbsHidKit hidKit = (AbsHidKit) this.mDevice;
            String downloadUrl = "", fileName = "";
            if (mGuid.contains("KC306")) {
                downloadUrl = KC306_downloadUrl;
                fileName = KC306_fileName;
            } else if (mGuid.contains("KM310")) {
                downloadUrl = KM310_downloadUrl;
                fileName = KM310_fileName;
            }
            RestfulService.getInstance().downFile(downloadUrl, fileName, new Callback<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    LogUtils.i("20201111", " uri:" + uri);
                    if (uri != null) {
                        String kc306Json = AlarmDataUtils.getFileFromSD(uri.getPath());
                        try {
                            HidKitUpdateBean hidKitUpdateBean = JsonUtils.json2Pojo(kc306Json, HidKitUpdateBean.class);
                            String versionNum = hidKitUpdateBean.getVersion().getValue();
                            final String desc = hidKitUpdateBean.getDesc().getValue();
                            LogUtils.i("20201111", " versionNum:" + versionNum);
                            LogUtils.i("20201111", " desc:" + desc);
                            FileUtils.deleteFile(uri.getPath());
                            int version = hidKit.getVersion();
                            boolean bool = PreferenceUtils.getBool(PageArgumentKey.isShow, false);
                            long saveTime = PreferenceUtils.getLong(PageArgumentKey.time, 0);
                            long timeMillis = System.currentTimeMillis();
                            long time = timeMillis - saveTime;
                            LogUtils.i("20201111", " time:" + time);
                            long longHours = time / (60 * 60 * 1000); //根据时间差来计算小时数
                            LogUtils.i("20201111", " longHours:" + longHours);
                            if (!mDevice.isConnected()) {
                                ToastUtils.showShort(R.string.oven_dis_con);
                                return;
                            }
                            if ((version < Integer.parseInt(versionNum))) {
                                dialog = RokiDialogFactory.createDialogByType(HidkitMoreActivity.this, DialogUtil.DIALOG_DISCOVER_NEW_VERSION);
                                dialog.setContentText(desc);
                                dialog.setTitleText(R.string.dialog_discover_new_version);
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelBtn(R.string.dialog_ignore_text, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                });
                                dialog.setOkBtn(R.string.dialog_update_text, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        downDialog = RokiDialogFactory.createDialogByType(HidkitMoreActivity.this, DialogUtil.DIALOG_DOWN_NEW_VERSION);
                                        downDialog.setTitleText(R.string.dialog_discover_new_version);
                                        downDialog.setContentText(desc);
                                        downDialog.setCanceledOnTouchOutside(false);
                                        downDialog.show();

                                        hidKit.setHidKitStatusCombined((short) 1, (short) 2, (short) 1, (short) 1, new VoidCallback() {
                                            @Override
                                            public void onSuccess() {
                                                LogUtils.i("20201111", " onSuccess:");
                                            }

                                            @Override
                                            public void onFailure(Throwable t) {
                                                LogUtils.i("20201111", " onFailure:" + t);
                                            }
                                        });

                                    }
                                });
                                if (bool) {
                                    if (time >= 604800000L) {
                                        dialog.show();
                                    }
                                } else {
                                    dialog.show();
                                }
                                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        long currentTimeMillis = System.currentTimeMillis();
                                        PreferenceUtils.setLong(PageArgumentKey.time, currentTimeMillis);
                                        PreferenceUtils.setBool(PageArgumentKey.isShow, true);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    LogUtils.i("20190614", " t:" + t.toString());
                }
            });
        }
    }



}
