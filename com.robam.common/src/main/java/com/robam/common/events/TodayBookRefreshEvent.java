package com.robam.common.events;

/**
 * 今日菜单更新事件
 *
 * @author sylar
 */
public class TodayBookRefreshEvent extends AbsBookRefreshEvent {

    public TodayBookRefreshEvent(long bookId, boolean isFavority) {
        super(bookId, isFavority);
    }
}