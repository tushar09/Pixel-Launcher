package bd.com.classroom.lan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.pm.ResolveInfo;
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

//        binding.slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener(){
//            @Override
//            public void onPanelSlide(View panel, float slideOffset){
////                binding.rvAppList.setAlpha(slideOffset);
////                //binding.main.setAlpha(slideOffset);
////                binding.nai.setAlpha(slideOffset);
////                binding.llQuick.setAlpha(1 - slideOffset);
////                Log.e("sc", binding.llQuick.getAlpha() + "");
//            }
//
//            @Override
//            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState){
//
//            }
//        });
    }

    private List<AppModel> getAppList(){
        List<AppModel> appList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
        for(ResolveInfo ri : apps){
            String appName = ri.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = ri.activityInfo.packageName;
            Drawable appIcon = ri.activityInfo..loadIcon(getPackageManager());
            appList.add(new AppModel(appName, appPackageName, appIcon));
        }
        Collections.sort(appList, new Comparator<AppModel>(){
            @Override
            public int compare(AppModel o1, AppModel o2){
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        return appList;
    }
}