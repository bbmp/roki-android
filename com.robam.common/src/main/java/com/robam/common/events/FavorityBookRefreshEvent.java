package com.robam.common.events;

/**
 * 收藏菜谱更新事件
 *
 * @author sylar
 */
public class FavorityBookRefreshEvent extends AbsBookRefreshEvent {

    public FavorityBookRefreshEvent(long bookId, boolean isToday) {
        super(bookId, isToday);
    }
}