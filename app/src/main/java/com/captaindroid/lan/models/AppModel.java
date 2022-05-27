package com.captaindroid.lan.models;

import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;

public class AppModel{
    private String name;
    private String packageName;
    private Drawable icon;
    private Bitmap bitmap;
    private Bitmap forG;
    private Bitmap backG;
    private AdaptiveIconDrawable ad;

    public AppModel(String name, String packageName, Drawable icon, Bitmap bitmap, AdaptiveIconDrawable ad, Bitmap forG, Bitmap backG){
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.bitmap = bitmap;
        this.forG = forG;
        this.backG = backG;
        this.ad = ad;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPackageName(){
        return packageName;
    }

    public void setPackageName(String packageName){
        this.packageName = packageName;
    }

    public Drawable getIcon(){
        return icon;
    }

    public void setIcon(Drawable icon){
        this.icon = icon;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public AdaptiveIconDrawable getAd(){
        return ad;
    }

    public void setAd(AdaptiveIconDrawable ad){
        this.ad = ad;
    }

    public Bitmap getForG(){
        return forG;
    }

    public void setForG(Bitmap forG){
        this.forG = forG;
    }

    public Bitmap getBackG(){
        return backG;
    }

    public void setBackG(Bitmap backG){
        this.backG = backG;
    }
}
