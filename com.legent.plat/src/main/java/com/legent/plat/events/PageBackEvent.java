package com.legent.plat.events;

import com.legent.events.AbsEvent;
import com.legent.plat.pojos.User;

/**
 * 界面返回需要重新变更数据event
 */
public class PageBackEvent  {

    private String pageName ;

    public PageBackEvent(String pageName) {
        this.pageName = pageName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
