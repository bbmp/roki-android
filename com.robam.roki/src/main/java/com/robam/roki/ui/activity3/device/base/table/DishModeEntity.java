package com.robam.roki.ui.activity3.device.base.table;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/07/06
 *     desc   : 洗碗机模式对应关系表
 *     version: 1.0
 * </pre>
 */
public class DishModeEntity extends LitePalSupport {
    /**
     * 主键
     */
    @Column
    public int id ;
    /**
     * 设备deviceType
     */
    @Column
    public String dt;
    /**
     * 模式
     */
    @Column
    public int mode;
    /**
     * 名称
     */
    @Column
    public String name;
    /**
     * 温度
     */
    @Column
    public int temp;
    /**
     * 时间
     */
    @Column
    public int time;
}
