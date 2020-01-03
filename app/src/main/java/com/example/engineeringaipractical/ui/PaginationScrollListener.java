package com.example.engineeringaipractical.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {


    private RecyclerView.LayoutManager layoutManager;

    PaginationScrollListener(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int findVisibleItemPosition;
        if (layoutManager instanceof LinearLayoutManager)
            findVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        else if (layoutManager instanceof GridLayoutManager)
            findVisibleItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        else
            findVisibleItemPosition = 0;
        if (visibleItemCount + findVisibleItemPosition >= totalItemCount) {
            loadMoreItems();
        }
    }

    protected abstract void loadMoreItems();
}
