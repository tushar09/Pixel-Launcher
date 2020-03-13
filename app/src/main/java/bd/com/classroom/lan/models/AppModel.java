package bd.com.classroom.lan.models;

import android.graphics.drawable.Drawable;

public class AppModel{
    private String name;
    private String packageName;
    private Drawable icon;

    public AppModel(String name, String packageName, Drawable icon){
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
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
}
