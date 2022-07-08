package com.legent.plat.events;

/**
 * 是否在HomePage event
 */
public class PageIsHomekEvent {

    private boolean isHome ;

    public PageIsHomekEvent(boolean isHome) {
        this.isHome = isHome;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }
}
