package com.robam.roki.net.request.param;

import com.robam.roki.net.base.BaseParam;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/05/07
 *     desc   : 设置烟机一体机联动
 *     version: 1.0
 * </pre>
 */
public class SetLinkageConfigParam extends BaseParam {

    public String deviceGuid;
    public boolean doorOpenEnabled;
    public boolean enabled;
    public String targetGuid;
    public String targetDeviceName ;


}
