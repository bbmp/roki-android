package com.legent.plat.events;

import android.os.Bundle;

/**
 * 界面返回需要重新变更数据event
 */
public class FloatHelperEvent {

    public Bundle bd;
    public long id;
    /**
     * 第几步
     */
    public int step;
    public String url;
    public String guid;
    public int stoveflag;
    public int pageKey;
    /**
     * 0 :无设备 1：一体机 2：灶具 3：无人锅 4:菜谱详情 5 ：随机菜谱
     */
    public int form;
    public boolean isHiden;//隐藏

    public FloatHelperEvent(long id, Bundle bd, int step, String url) {
        this.id = id;
        this.bd = bd;
        this.step = step;
        this.url = url;
    }

    public FloatHelperEvent(long id, Bundle bd, int step, String url, String guid, int form) {
        this.id = id;
        this.bd = bd;
        this.step = step;
        this.url = url;
        this.guid = guid;
        this.form = form;
    }

    public FloatHelperEvent(long id, Bundle bd, int step, String url, String guid, int stoveflag, int form) {
        this.id = id;
        this.bd = bd;
        this.step = step;
        this.url = url;
        this.guid = guid;
        this.form = form;
        this.stoveflag = stoveflag;
    }

    public FloatHelperEvent(long id, Bundle bd, String url, int form, int pageKey) {
        this.id = id;
        this.bd = bd;
        this.step = step;
        this.url = url;
        this.guid = guid;
        this.form = form;
        this.pageKey = pageKey;
    }

    public FloatHelperEvent(int form, String url) {
        this.form = form;
        this.url = url;
    }
    public FloatHelperEvent(boolean isHiden,int pageKey) {
        this.isHiden = isHiden;
        this.pageKey = pageKey;
    }
}
