package com.robam.roki.net.request.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robam.roki.net.base.BaseParam;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/06
 *     desc   : 获取设备参数
 *     version: 1.0
 * </pre>
 */
public class GetDeviceParamsParam extends BaseParam {
    public long userId ;
    public String deviceType;
    public String deviceCategory;

    public GetDeviceParamsParam(long userId, String deviceType, String deviceCategory) {
        this.userId = userId;
        this.deviceType = deviceType;
        this.deviceCategory = deviceCategory;
    }
}
