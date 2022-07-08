package com.robam.roki.ui.activity3.device.base.table;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/07/06
 *     desc   : 本地菜谱对应关系表
 *     version: 1.0
 * </pre>
 */
public class LocalRecipeEntity extends LitePalSupport {
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
     * 菜谱mode
     */
    @Column
    public int mode;
    /**
     * 菜谱pkey
     */
    @Column
    public String pKey;
    /**
     * 菜谱名
     */
    @Column
    public String value;
}
