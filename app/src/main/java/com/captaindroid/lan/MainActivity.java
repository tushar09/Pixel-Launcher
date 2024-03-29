package com.captaindroid.lan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.captaindroid.lan.R;
import com.captaindroid.lan.databinding.BottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.captaindroid.lan.adapter.AppListAdapter;
import com.captaindroid.lan.databinding.ActivityMainBinding;
import com.captaindroid.lan.models.AppModel;
import com.captaindroid.lan.views.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;

    private List<AppModel> appList;

    private GridLayoutManager gridLayoutManager;

    private NotificationReceiver nReceiver;

    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        getSupportActionBar().hide();

        appList = getAppList();

//        Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver,filter);



        gridLayoutManager = new GridLayoutManager(this, 5);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        int firstRowTopMargin = getResources().getDimensionPixelSize(R.dimen.margin_top_30);
        binding.container.rvAppList.addItemDecoration(new SpacesItemDecoration(spacingInPixels, firstRowTopMargin));
        binding.container.rvAppList.setAdapter(new AppListAdapter(this, appList));
        binding.container.rvAppList.setLayoutManager(gridLayoutManager);
        binding.container.rvAppList.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        binding.container.getRoot().setAlpha(0);

        bottomSheetBehavior = BottomSheetBehavior.from(binding.container.getRoot());
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState){
                View decor = getWindow().getDecorView();
                if(newState == BottomSheetBehavior.STATE_EXPANDED){
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }else{
                    decor.setSystemUiVisibility(0 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset){
                //binding.container.pick.setAlpha(1 - slideOffset);
                binding.container.search.setAlpha(slideOffset);
                binding.mainHolder.getRoot().setAlpha(1 - slideOffset);
                //binding.mainHolder.dockPick.setAlpha(1 - slideOffset);
                binding.container.getRoot().setAlpha(slideOffset);
            }
        });
//
        for(int t = 0; t < appList.size(); t++){
            if(appList.get(t).getName().toLowerCase().equals("chrome")){
                binding.mainHolder.iv3.setImageDrawable(appList.get(t).getIcon());
            }
            if(appList.get(t).getName().toLowerCase().equals("camera")){
                binding.mainHolder.iv5.setImageDrawable(appList.get(t).getIcon());
            }
            if(appList.get(t).getName().toLowerCase().equals("my contacts")){
                binding.mainHolder.iv4.setImageDrawable(appList.get(t).getIcon());
            }
            if(appList.get(t).getName().toLowerCase().equals("google")){
                binding.mainHolder.iv1.setImageDrawable(appList.get(t).getIcon());
            }
            if(appList.get(t).getName().toLowerCase().equals("play store")){
                binding.mainHolder.iv2.setImageDrawable(appList.get(t).getIcon());
            }
        }
//        binding.container.iv1.setImageDrawable(appList.get(0).getIcon());
//        binding.container.iv2.setImageDrawable(appList.get(1).getIcon());
//        binding.container.iv3.setImageDrawable(appList.get(2).getIcon());
//        binding.container.iv4.setImageDrawable(appList.get(3).getIcon());
//        binding.container.iv5.setImageDrawable(appList.get(4).getIcon());

    }

    private List<AppModel> getAppList(){
        List<AppModel> appList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
        for(ResolveInfo ri : apps){
            String appName = ri.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = ri.activityInfo.packageName;
            Drawable appIcon = ri.activityInfo.loadIcon(getPackageManager());
            Bitmap icon = drawableToBitmap(appIcon);
            AdaptiveIconDrawable d;
            Bitmap backG = null;
            Bitmap forG = null;
            Bitmap background = null;
            Bitmap forground = null;
            try{
                d = (AdaptiveIconDrawable) getApplicationContext().getPackageManager().getDrawable(ri.activityInfo.packageName, ri.activityInfo.getIconResource(), ri.activityInfo.applicationInfo);
                background = drawableToBitmap(d.getBackground());
                forground = drawableToBitmap(d.getForeground());

                if(background.getWidth() == 1){
                    backG = background;
                }else {
                    backG = Bitmap.createBitmap(background, 40, 40, background.getWidth() - 80, background.getHeight() - 80);
                }

                if(forground.getWidth() == 1){
                    forG = forground;
                }else {
                    forG = Bitmap.createBitmap(forground, 40, 40, forground.getWidth() - 80, forground.getHeight() - 80);
                }

            }catch(Exception eg){
                //Log.e("err", background.getWidth() + " " + eg.toString());
                d = null;
            }

            appList.add(new AppModel(appName, appPackageName, appIcon, icon, d, forG, backG));
        }
        Collections.sort(appList, new Comparator<AppModel>(){
            @Override
            public int compare(AppModel o1, AppModel o2){
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        return appList;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
        //return Bitmap.createBitmap(bitmap, 40, 40, bitmap.getWidth() - 80, bitmap.getHeight() - 80);
    }

    @Override
    protected void onStop(){
        unregisterReceiver(nReceiver);
        super.onStop();
    }

    @Override
    public void onBackPressed(){
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            super.onBackPressed();
        }

    }

    class NotificationReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event");

        }
    }

    class Pager extends FragmentPagerAdapter{

        public Pager(@NonNull FragmentManager fm, int behavior){
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position){
            return null;
        }

        @Override
        public int getCount(){
            return 0;
        }
    }
}
