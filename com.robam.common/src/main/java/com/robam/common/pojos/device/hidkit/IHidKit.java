package com.robam.common.pojos.device.hidkit;

import com.legent.VoidCallback;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/30.
 * @PS:公共藏宝盒接口
 */
public interface IHidKit {


    /**
     * 设置藏宝盒组合模式
     * @param argumentNumber 参数个数
     * @param key            唯一的键
     * @param len            可变的长度
     * @param value          不同的值
     * @param callback       回调
     */
    void setHidKitStatusCombined(short argumentNumber, short key, short len,
                                 short value, VoidCallback callback);

}
