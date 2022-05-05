package com.legent.plat.events;

/**
 * 界面返回需要重新变更数据event
 */
public class PageBack2Event {

    private String pageName ;
    private int step ;

    public PageBack2Event(String pageName) {
        this.pageName = pageName;
    }

    public PageBack2Event(String pageName, int step) {
        this.pageName = pageName;
        this.step = step;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }
}
