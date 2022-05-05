package com.robam.roki.ui.page.device.sterilizer;

import android.os.Bundle;
import android.widget.Toast;

import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.pojos.device.Oven.OvenModeName;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.dialog.SerilizerDialog;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2018/10/18.
 */

public class SterilizerSubPage extends SterilizerBasePage {

    boolean falg = true;



    @Override
    public void clickMain(String str) {
        super.clickMain(str);
        if (judgeSter()) return;
        switch (str) {
            //更多
            case OvenModeName.more:
                if (falg) {
                    ovenFirstView.setUpData(moreList);
                    falg = false;
                } else {
                    ovenFirstView.removeMoreView();
                    falg = true;
                }
                break;
            //暖碟
            case SterilizerMode.warmDish:
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "暖碟", "roki_设备");
                    }
                }

                WarmParam(str);
                break;
            //保洁
            case SterilizerMode.cleanup:
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "保洁", "roki_设备");
                    }
                }

                dealParam(str);
                break;
            //消毒
            case SterilizerMode.disinfection:

                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "消毒", "roki_设备");
                    }
                }

                if ("alert".equals(paramMapMore.get(str).functionType)) {
                    Bundle bd = new Bundle();
                    bd.putString(PageArgumentKey.Guid, mGuid);
                    bd.putSerializable(PageArgumentKey.tag, paramMapMore.get(str).functionName);
                    bd.putSerializable(PageArgumentKey.functionParams, paramMapMore.get(str).functionParams);
                    UIService.getInstance().postPage(PageKey.SterilizerOrder, bd);
                } else {
                    dealParam(str);
                }
                break;
            //烘干
            case SterilizerMode.drying:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                bd.putSerializable(PageArgumentKey.tag, paramMapMore.get(str).functionName);
                bd.putSerializable(PageArgumentKey.functionParams, paramMapMore.get(str).functionParams);
                UIService.getInstance().postPage(PageKey.SterilizerOrder, bd);
                break;
            //快洁
            case SterilizerMode.fastCleaning:
                dealParam(str);
                break;
            default:
                dealParam(str);
                break;
        }
    }


    @Override
    public void clickOther(String str) {
        super.clickOther(str);
        if (judgeSter()) return;

        switch (str) {
            case SterilizerMode.peakValleyElectricDisinfection:
                Bundle bd = new Bundle();
                bd.putString(PageArgumentKey.Guid, mGuid);
                bd.putSerializable("settingParam", (Serializable) settingParam);
                UIService.getInstance().postPage(PageKey.DeviceOrderSer, bd);
                break;
            //母婴
            case SterilizerMode.motherInfant:

                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "母婴", "roki_设备");
                    }
                }

                Bundle bd2 = new Bundle();
                bd2.putString(PageArgumentKey.Guid, mGuid);
                bd2.putSerializable(PageArgumentKey.tag, paramMapOther.get(str).functionName);
                bd2.putSerializable(PageArgumentKey.functionParams, paramMapOther.get(str).functionParams);
                UIService.getInstance().postPage(PageKey.SterilizerOrder, bd2);

                break;
            //暖碟
            case SterilizerMode.warmDish:
                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "暖碟", "roki_设备");
                    }
                }
                Bundle bd3 = new Bundle();
                bd3.putString(PageArgumentKey.Guid, mGuid);
                bd3.putSerializable(PageArgumentKey.tag, paramMapOther.get(str).functionName);
                bd3.putSerializable(PageArgumentKey.functionParams, paramMapOther.get(str).functionParams);
                UIService.getInstance().postPage(PageKey.SterilizerOrder, bd3);

                break;
            //智能杀菌
            case SterilizerMode.cleanup:

                if (dt != null) {
                    if (!"".equals(dt)) {
                        ToolUtils.logEvent(dt, "智能杀菌", "roki_设备");
                    }
                }
                String functionParams = null;
                List<DeviceConfigurationFunctions> deviceConfigurationFunctions = paramMapOther.get(str).subView.subViewModelMap.subViewModelMapSubView.deviceConfigurationFunctions;
                for (int i = 0; i < deviceConfigurationFunctions.size(); i++) {
                    if (deviceConfigurationFunctions.get(i).functionCode.equals("Intelligence")) {
                        functionParams = deviceConfigurationFunctions.get(i).functionParams;
                    }
                }
                Bundle bd4 = new Bundle();
                bd4.putString(PageArgumentKey.Guid, mGuid);
                bd4.putSerializable(PageArgumentKey.functionParams, functionParams);
                UIService.getInstance().postPage(PageKey.SterilizerIntelligenceCleaning, bd4);

                break;

            default:
                break;
        }
    }

    private boolean judgeSter() {
        if (steri826 != null && !steri826.isConnected()) {
            ToastUtils.show("已离线", Toast.LENGTH_SHORT);
            return true;
        }
        if (steri829 != null && !steri829.isConnected()) {
            ToastUtils.show("已离线", Toast.LENGTH_SHORT);
            return true;
        }

        if (steri826 != null && steri826.doorLock == 0) {
            ToastUtils.show("门未关好，请检查并确认关好门。", Toast.LENGTH_SHORT);
            return true;
        }
        if (steri829 != null && steri829.AlarmStautus == 0) {
            ToastUtils.show("门未关好，请检查并确认关好门。", Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }


    String value;
    String cmd;
    String state;
    String msg;

    private void WarmParam(String str) {
        JSONObject obj = null;
        try {
            final Map<String, String> listMap = new HashMap<>();
            final List<String> listStr = new ArrayList<>();
            final List<String> listData = new ArrayList<>();
            obj = new JSONObject(paramMapMore.get(str).functionParams);
            cmd = obj.getString("cmd");
            state = obj.getString("state");
            msg = paramMapMore.get(str).msg;
            LogUtils.i("20191231","cmd:::"+cmd);
            String param = obj.getString("setTimeType");
            String parmType = obj.getJSONObject("setTimeParam").getJSONObject(param).getString("paramType");
            int defValue = obj.getJSONObject("setTimeParam").getJSONObject(param).getInt("defaultValue");
            if ("array".equals(parmType)) {
                JSONArray jarray = obj.getJSONObject("setTimeParam").getJSONObject(param).getJSONArray("value");
                JSONArray hiddenArray = obj.getJSONObject("setTimeParam").getJSONObject(param).getJSONArray("hiddenValue");

                for (int i = 0; i < hiddenArray.length(); i++) {
                    listData.add(String.valueOf(hiddenArray.get(i)));
                }

                for (int i = 0; i < jarray.length(); i++) {
                    listStr.add(String.valueOf(jarray.get(i)));
                }
                if (listData.size() != 0) {
                    for (int i = 0; i < listStr.size(); i++) {
                        listMap.put(listStr.get(i), listData.get(i));
                    }
                }
                serilizerDialog = new SerilizerDialog<String>(cx, listStr, "分钟", msg, defValue - 1);
                serilizerDialog.show(serilizerDialog);
                serilizerDialog.setListener(new SerilizerDialog.PickListener() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onConfirm(Object index1) {
                        if (listData.size() != 0) {
                            LogUtils.i("20181105", "value:" + listMap.get(index1));
                            value = listMap.get(index1);
                        } else {
                            value = (String) index1;
                        }
                        LogUtils.i("20181107", "cmd:" + cmd + "state:" + state + "value::" + value);
                        sw(cmd, state, value);
                    }
                });
            }
        } catch (Exception e) {
            LogUtils.i("20181105", "e::" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void dealParam(String str) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(paramMapMore.get(str).functionParams);

            cmd = obj.getString("cmd");
            LogUtils.i("20181024", "cmd::" + cmd);
            state = obj.getString("state");
            String param = obj.getString("setTimeType");
            if (!"1".equals(param)) {
                //说明参数不是一个
                final List<String> list = new ArrayList<>();
                String temp = obj.getJSONObject("setTimeParam").getJSONObject(param).getString("paramType");
                String msg = paramMapMore.get(str).msg;
                if ("array".equals(temp)) {
                    JSONArray listData = obj.getJSONObject("setTimeParam").getJSONObject(param)
                            .getJSONArray("value");

                    for (int i = 0; i < listData.length(); i++) {
                        list.add(listData.get(i) + "");
                    }
                    serilizerDialog = new SerilizerDialog<String>(cx, list, "分钟", msg, 0,"开始工作");
                    serilizerDialog.show(serilizerDialog);
                    serilizerDialog.setListener(new SerilizerDialog.PickListener() {
                        @Override
                        public void onCancel() {

                         }

                         @Override
                         public void onConfirm(Object index1) {
                             LogUtils.i("20181026","indexs:"+(String)index1+" "+state);
                             value = (String) index1;
                             sw(cmd,state,value);
                         }
                     });


                    for (int i = 0; i < listData.length(); i++) {
                        list.add(listData.get(i) + "");
                    }
                    serilizerDialog = new SerilizerDialog<String>(cx, list, "分钟", msg, 0);
                    serilizerDialog.show(serilizerDialog);
                    serilizerDialog.setListener(new SerilizerDialog.PickListener() {
                        @Override
                        public void onCancel() {

                        }

                        @Override
                        public void onConfirm(Object index1) {
                            LogUtils.i("20181026", "indexs:" + index1 + " " + state);
                            value = (String) index1;
                            sw(cmd, state, value);
                        }
                    });
                }
            } else {
                value = obj.getJSONObject("setTimeParam").getJSONObject(param).getString("value");
                LogUtils.i("20181024", "value::" + value);
                sw(cmd, state, value);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sw(String cmd, String state, String value) {
        LogUtils.i("2020060206","cmd::"+cmd);
        switch (cmd) {
            //消毒柜开关
            case "128":
                sendCommand(state, value);
                break;
                //消毒柜预约时间
            //暂未开发
            case "130":
                sendCommand130();
                break;
                //消毒柜烘干   还是  消毒
            case "132":
                sendCommand132(value);
                LogUtils.i("2019081600000000","暖碟模式");
                break;
                //消毒柜保洁
            case "134":
                sendCommand134(value);
                LogUtils.i("2019081600000000","保洁模式");
                break;
                //消毒柜暖碟
            case "136":
                sendCommand136(value);
                LogUtils.i("2019081600000000","消毒模式");
                break;
            default:
                break;
        }
    }


    private void sendCommand130() {
        ToastUtils.show("暂未开发", Toast.LENGTH_SHORT);
    }

    private void sendCommand(final String state, final String value) {
        steri826.setSteriPower((short) 1, (short) 0, (short) 0, new VoidCallback() {
            @Override
            public void onSuccess() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i("20181026", "state::" + state + " value:" + value);
                        steri826.setSteriPower(Short.parseShort(state), Short.parseShort(value), (short) 0, new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                if (!"XS855".equals(steri826.getDt())) {
                                    countDown();
                                }

                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }, 500);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    public void sendCommand132(final String value) {
        steri829.setSteriPower((short) 1, new VoidCallback() {
            @Override
            public void onSuccess() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        steri829.setSteriDrying(Short.parseShort(value), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                countDown();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }, 500);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }


    public void sendCommand134(final String value) {
        steri829.setSteriPower((short) 1, new VoidCallback() {
            @Override
            public void onSuccess() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        steri829.setSteriClean(Short.parseShort(value), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                countDown();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                ToastUtils.show("指令下发失败", Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }, 500);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    public void sendCommand136(final String value) {
        steri829.setSteriPower((short) 1, new VoidCallback() {
            @Override
            public void onSuccess() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        steri829.setSteriDisinfect(Short.parseShort(value), new VoidCallback() {
                            @Override
                            public void onSuccess() {
                                countDown();
                            }

                            @Override
                            public void onFailure(Throwable t) {

                            }
                        });
                    }
                }, 500);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }


    private void countDown() {
        IRokiDialog rokiCountDownDialog = RokiDialogFactory.createDialogByType(getContext(), DialogUtil.DIALOG_TYPE_07);
        rokiCountDownDialog.show();
    }

}
