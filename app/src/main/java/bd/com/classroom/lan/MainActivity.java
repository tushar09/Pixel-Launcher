package bd.com.classroom.lan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ComponentName;
import android.content.Intent;
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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import bd.com.classroom.lan.adapter.AppListAdapter;
import bd.com.classroom.lan.databinding.ActivityMainBinding;
import bd.com.classroom.lan.models.AppModel;
import bd.com.classroom.lan.views.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;

    private List<AppModel> appList;

    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        appList = getAppList();

        for(int t = 0; t < appList.size(); t++){
            //Log.e("app name", appList.get(t).getName());
            //Bitmap b = appList.get(t).getBitmap();
            if(appList.get(t).getAd() == null){
                Log.e(appList.get(t).getName(), "null");
            }
//            if(b.getPixel(0, 0) == Color.TRANSPARENT){
//                //Log.e("app name", appList.get(t).getName() + " has");
//            }else {
//                //Log.e("app name", appList.get(t).getName() + " not has");
//            }
//            for(int x = 0; x<b.getWidth(); x++){
//                for(int y = 0; y<b.getHeight(); y++){
//                    if(b.getPixel(x, y) == Color.TRANSPARENT){
//
//                    }
//                }
//            }
        }


        gridLayoutManager = new GridLayoutManager(this, 5);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        binding.container.rvAppList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        binding.container.rvAppList.setAdapter(new AppListAdapter(this, appList));
        binding.container.rvAppList.setLayoutManager(gridLayoutManager);
        binding.container.rvAppList.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(binding.container.getRoot());
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback(){
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState){

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset){
                Log.e("slider", slideOffset + "");
                binding.container.pick.setAlpha(1 - slideOffset);
                binding.container.search.setAlpha(slideOffset);
                binding.mainHolder.getRoot().setAlpha(slideOffset);
            }
        });
//
        binding.container.iv1.setImageDrawable(appList.get(0).getIcon());
        binding.container.iv2.setImageDrawable(appList.get(1).getIcon());
        binding.container.iv3.setImageDrawable(appList.get(2).getIcon());
        binding.container.iv4.setImageDrawable(appList.get(3).getIcon());
        binding.container.iv5.setImageDrawable(appList.get(4).getIcon());

//        PackageManager pm = getPackageManager();
//        Intent launchIntentForPackage = pm.getLaunchIntentForPackage("sadfg");
//        String fullPathToActivity = launchIntentForPackage.getComponent().getClassName();
//        ActivityInfo activityInfo = null;
//        try{
//            activityInfo = pm.getActivityInfo(new ComponentName("sadfg", fullPathToActivity), 0);
//        }catch(PackageManager.NameNotFoundException e){
//            e.printStackTrace();
//        }
//        int iconRes = activityInfo.icon;
//        Drawable drawable = pm.getDrawable("asdf", iconRes, activityInfo.applicationInfo); // will be AdaptiveIconDrawable, if the app has it
//        AdaptiveIconDrawable d = (AdaptiveIconDrawable) drawable;
//        //d.get
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
}
