package com.captaindroid.lan.views;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    private int space;
    private int firstRowTopMargin;

    public SpacesItemDecoration(int space, int firstRowTopMargin) {
        this.space = space;
        this.firstRowTopMargin = firstRowTopMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space / 2;
        outRect.right = space / 2;
        outRect.bottom = space;
        //outRect.top = space;

//        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) < 5) {
            outRect.top = firstRowTopMargin;
        } else {
            outRect.top = space;
        }
    }
}
