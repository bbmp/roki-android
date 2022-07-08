package com.robam.roki.net.request.param;

import com.robam.roki.net.base.BaseParam;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/05/07
 *     desc   : 设置设备名称
 *     version: 1.0
 * </pre>
 */
public class SetDeciceNameParam extends BaseParam {
    public long userId ;
    public String guid ;
    public String name ;

    public SetDeciceNameParam(long userId, String guid, String name) {
        this.userId = userId;
        this.guid = guid;
        this.name = name;
    }
}
