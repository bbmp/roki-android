package com.legent.plat.pojos.dictionary;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.legent.plat.R;
import com.legent.plat.services.DeviceTypeManager;
import com.legent.plat.services.ResultCodeManager;
import com.legent.pojos.AbsPojo;
import com.legent.utils.JsonUtils;
import com.legent.utils.api.ResourcesUtils;

import java.util.List;

public class PlatDic extends AbsPojo {

    @JsonProperty("deviceTypes")
    public List<DeviceType> deviceTypes;

    @JsonProperty("resultCodes")
    public List<ResultCode> resultCodes;

    static public void loadPlatDic(Context cx) {

        PlatDic dic = null;
        String dicContent = ResourcesUtils.raw2String(R.raw.plat_dic);
        Preconditions.checkNotNull(dicContent);

        try {
            dic = JsonUtils.json2Pojo(dicContent, PlatDic.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Preconditions.checkNotNull(dic, "加载 plat_dic 失败");

        // init
        DeviceTypeManager.getInstance().init(cx);
        ResultCodeManager.getInstance().init(cx);

        //batchAdd
        DeviceTypeManager.getInstance().batchAdd(dic.deviceTypes);
        ResultCodeManager.getInstance().batchAdd(dic.resultCodes);

    }

}