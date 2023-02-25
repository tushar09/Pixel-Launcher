package com.captaindroid.lan.models;

import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;

public class DesktopAppModel {
    private boolean hightlight;

    public DesktopAppModel(boolean hightlight) {
        this.hightlight = hightlight;
    }

    public boolean isHightlight() {
        return hightlight;
    }

    public void setHightlight(boolean hightlight) {
        this.hightlight = hightlight;
    }
}
