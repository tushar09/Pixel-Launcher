package com.captaindroid.lan.utils;

import android.content.Context;

import com.captaindroid.lan.MainActivity;

public class GridLayoutManager extends androidx.recyclerview.widget.GridLayoutManager {
    public GridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public boolean canScrollVertically() {
        return MainActivity.ma.appDrawerCanScroll && super.canScrollVertically();
    }
}
