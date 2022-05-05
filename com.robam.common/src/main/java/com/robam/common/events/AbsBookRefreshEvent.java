package com.robam.common.events;

public class AbsBookRefreshEvent {
    public long bookId;
    public boolean flag;

    public AbsBookRefreshEvent(long bookId, boolean flag) {
        this.bookId = bookId;
        this.flag = flag;
    }
}