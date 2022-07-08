package com.robam.roki.ui.activity3.device.base.table;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/08
 *     desc   : 设备功能数据对象
 *     version: 1.0
 * </pre>
 */
public class FunctionEntity extends LitePalSupport {
    /**
     * 主键
     */
    @Column
    public int id ;
    /**
     * 设备类型
     */
    @Column
    public String deviceType;
    /**
     * 版本号
     */
    @Column
    public String version;
    /**
     * 设备功能参数
     */
    @Column
    public String param;
}
