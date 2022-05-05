package com.robam.roki.ui.view.interf;

/**
 * Created by Administrator on 2017/7/7.
 */

public interface OnRefreshListener {

    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
