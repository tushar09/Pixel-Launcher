package com.captaindroid.lan.views;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

import com.captaindroid.lan.MainActivity;

public class Grid extends GridLayoutManager {
    public Grid(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean canScrollVertically() {
        return MainActivity.ma.canScroll && super.canScrollVertically();
    }
}
