package com.robam.roki.ui.adapter.helper;


import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    private int currentPage = 1;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    protected EndlessRecyclerOnScrollListener(StaggeredGridLayoutManager linearLayoutManager) {
        this.staggeredGridLayoutManager = linearLayoutManager;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy == 0) {
            return;
        }

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = staggeredGridLayoutManager.getItemCount();
        int[] lastCompletelyVisiableItemPositions = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null);


        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if (!loading && (visibleItemCount > 0) &&
                (lastCompletelyVisiableItemPositions[0] >= totalItemCount - 1)) {
            currentPage++;
            onLoadMore(currentPage);
            loading = true;
        }
    }


    public void refresh() {
        loading = true;
        previousTotal = 0;
        currentPage = 1;
    }

    public abstract void onLoadMore(int currentPage);
}