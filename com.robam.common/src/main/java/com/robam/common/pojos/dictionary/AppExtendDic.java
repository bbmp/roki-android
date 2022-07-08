package com.robam.common.pojos.dictionary;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IDeviceType;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.dictionary.DeviceType;
import com.legent.plat.pojos.dictionary.ResultCode;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.plat.services.ResultCodeManager;
import com.legent.pojos.IJsonPojo;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ResourcesUtils;
import com.robam.common.R;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.services.StoveAlarmManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppExtendDic implements IJsonPojo {

    @JsonProperty("deviceTypes")
    public List<DeviceType> deviceTypes;

    @JsonProperty("resultCodes")
    public List<ResultCode> resultCodes;

    @JsonProperty("stoveAlarms")
    public List<StoveAlarm> stoveAlarms;

    static public void init(Context cx) {

        AppExtendDic dic = null;
        String dicContent = ResourcesUtils.raw2String(R.raw.app_dic_extend);
        Preconditions.checkNotNull(dicContent);
        try {
            dic = JsonUtils.json2Pojo(dicContent, AppExtendDic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Preconditions.checkNotNull(dic, "加载 app dic 失败");

        final AppExtendDic finalDic = dic;
        Plat.deviceService.getAllDeviceType(new Callback<Reponses.DeviceTypeResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceTypeResponse deviceTypeResponse) {
                LogUtils.i("20180716","sponce:::"+deviceTypeResponse.toString());
                Map types = deviceTypeResponse.deviceTypes;
                // 获取所有键值对对象的集合
                Set<Map.Entry<String, LinkedHashMap<String, String>>> set = types.entrySet();
                // 遍历键值对对象的集合，得到每一个键值对对象
                for (Map.Entry<String, LinkedHashMap<String, String>> me : set) {
                    DeviceType deviceType = new DeviceType();
                    // 根据键值对对象获取键和值
                    String key = me.getKey();
                    deviceType.setId(key);
                    LinkedHashMap<String, String> linkedHashMap = me.getValue();
//                    LogUtils.i("20180612", "key:" + key + "  value:" + linkedHashMap);
                    Set<Map.Entry<String, String>> entries = linkedHashMap.entrySet();
                    for (Map.Entry<String, String> hashMap: entries){
                        String hashMapKey = hashMap.getKey();
                        String hashMapValue = hashMap.getValue();
//                        LogUtils.i("20180612", "hashMapKey:" + hashMapKey + "  hashMapValue:" + hashMapValue);
                        //新品类在这里配置IDeviceType和名字
                            if ("dc".equals(hashMapKey)){
                                if (IDeviceType.RYYJ.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RYYJ_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RRQZ.equals(hashMapValue)
                                        || IDeviceType.RDCZ.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RRQZ_AND_RDCZ_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RZQL.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RZQL_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RDKX.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RDKX_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RWBL.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RWBL_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RXDG.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RXDG_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RZKY.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RZKY_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RJSQ.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RJSQ_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RIKA.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RIKA_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RJCZ.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RJCZ_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RXWJ.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RXWJ_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RPOT.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RPOT_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RQCG.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RQCG_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.KZNZ.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.KZNZ_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RCBH.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RCBH_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }else if (IDeviceType.RZNG.equals(hashMapValue)){
                                    deviceType.setName(IDeviceType.RPOT_ZN);
                                    finalDic.deviceTypes.add(deviceType);
                                }
                            }
                    }

                }
                DeviceTypeManager.getInstance().batchAdd(finalDic.deviceTypes);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });

        // common
        ResultCodeManager.getInstance().batchAdd(dic.resultCodes);
        StoveAlarmManager.getInstance().batchAdd(dic.stoveAlarms);

    }

}