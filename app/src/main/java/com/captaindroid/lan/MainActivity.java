package com.captaindroid.lan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.captaindroid.lan.adapter.PagerAdapter;
import com.captaindroid.lan.utils.GridLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.captaindroid.lan.adapter.AppListAdapter;
import com.captaindroid.lan.databinding.ActivityMainBinding;
import com.captaindroid.lan.models.AppModel;
import com.captaindroid.lan.views.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity {

    public static MainActivity ma;

    public int posX;
    public int posY;
    public boolean canScroll = true;
    public ImageView v;

    public ActivityMainBinding binding;

    private List<AppModel> appList;

    private GridLayoutManager gridLayoutManager;

    private NotificationReceiver nReceiver;

    public BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ma = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        v = binding.ivFloat;
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        appList = getAppList();

//        Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);

        binding.mainHolder.pager.setAdapter(new PagerAdapter(colors, this));
        binding.mainHolder.pager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                TextView v = page.findViewById(R.id.tv_planet_name);
                ImageView i = page.findViewById(R.id.iv_planet_pic);
                if (position <= 1 && position >= -1) {
                    v.setTranslationX(position * page.getWidth() / 100f);
                    i.setTranslationX(position * page.getWidth());
                    //planet.translationX = position * (width / 2f)
                    //name.translationX = - position * (width / 4f)
                /* If user drags the page right to left :
                   Planet : 0.5 of normal speed
                   Name : 1.25 of normal speed

                   If the user drags the page left to right :
                   Planet: 1.5 of normal speed
                   Name: 0.75 of normal speed
                 */
                }
            }
        });

        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(nReceiver, filter);


        gridLayoutManager = new GridLayoutManager(this, 5);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        int firstRowTopMargin = getResources().getDimensionPixelSize(R.dimen.margin_top_30);
        binding.container.rvAppList.addItemDecoration(new SpacesItemDecoration(spacingInPixels, firstRowTopMargin));
        binding.container.rvAppList.setAdapter(new AppListAdapter(this, appList));
        binding.container.rvAppList.setLayoutManager(gridLayoutManager);
        binding.container.rvAppList.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        //binding.container.getRoot().setAlpha(0);

        bottomSheetBehavior = BottomSheetBehavior.from(binding.container.getRoot());
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                View decor = getWindow().getDecorView();
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                } else {
                    decor.setSystemUiVisibility(0 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //binding.container.pick.setAlpha(1 - slideOffset);
                //binding.container.search.setAlpha(slideOffset);

                binding.container.flBottomRoot.setAlpha(slideOffset);
                binding.container.llPeak.setAlpha(1 - slideOffset);
                //binding.container.cvSearchHolder.setAlpha(slideOffset);
                //binding.container.rvAppList.setAlpha(slideOffset);
                //binding.mainHolder.getRoot().setAlpha(1 - slideOffset);
                //binding.mainHolder.dockPick.setAlpha(1 - slideOffset);
                //binding.container.getRoot().setAlpha(slideOffset);
            }
        });
//
        for (int t = 0; t < appList.size(); t++) {
            if (appList.get(t).getName().toLowerCase().equals("chrome")) {
                binding.container.iv3.setImageDrawable(appList.get(t).getIcon());
            }
            if (appList.get(t).getName().toLowerCase().equals("camera")) {
                binding.container.iv5.setImageDrawable(appList.get(t).getIcon());
            }
            if (appList.get(t).getName().toLowerCase().equals("my contacts")) {
                binding.container.iv4.setImageDrawable(appList.get(t).getIcon());
            }
            if (appList.get(t).getName().toLowerCase().equals("google")) {
                binding.container.iv1.setImageDrawable(appList.get(t).getIcon());
            }
            if (appList.get(t).getName().toLowerCase().equals("play store")) {
                binding.container.iv2.setImageDrawable(appList.get(t).getIcon());
            }
        }

        binding.container.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "asdf", Toast.LENGTH_SHORT).show();
            }
        });
//        binding.container.iv1.setImageDrawable(appList.get(0).getIcon());
//        binding.container.iv2.setImageDrawable(appList.get(1).getIcon());
//        binding.container.iv3.setImageDrawable(appList.get(2).getIcon());
//        binding.container.iv4.setImageDrawable(appList.get(3).getIcon());
//        binding.container.iv5.setImageDrawable(appList.get(4).getIcon());

    }

    private List<AppModel> getAppList() {
        List<AppModel> appList = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo ri : apps) {
            String appName = ri.activityInfo.loadLabel(getPackageManager()).toString();
            String appPackageName = ri.activityInfo.packageName;
            Drawable appIcon = ri.activityInfo.loadIcon(getPackageManager());
            Bitmap icon = drawableToBitmap(appIcon);
            AdaptiveIconDrawable d;
            Bitmap backG = null;
            Bitmap forG = null;
            Bitmap background = null;
            Bitmap forground = null;
            try {
                d = (AdaptiveIconDrawable) getApplicationContext().getPackageManager().getDrawable(ri.activityInfo.packageName, ri.activityInfo.getIconResource(), ri.activityInfo.applicationInfo);
                background = drawableToBitmap(d.getBackground());
                forground = drawableToBitmap(d.getForeground());

                if (background.getWidth() == 1) {
                    backG = background;
                } else {
                    backG = Bitmap.createBitmap(background, 40, 40, background.getWidth() - 80, background.getHeight() - 80);
                }

                if (forground.getWidth() == 1) {
                    forG = forground;
                } else {
                    forG = Bitmap.createBitmap(forground, 40, 40, forground.getWidth() - 80, forground.getHeight() - 80);
                }

            } catch (Exception eg) {
                //Log.e("err", background.getWidth() + " " + eg.toString());
                d = null;
            }

            appList.add(new AppModel(appName, appPackageName, appIcon, icon, d, forG, backG));
        }
        Collections.sort(appList, new Comparator<AppModel>() {
            @Override
            public int compare(AppModel o1, AppModel o2) {
                return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
            }
        });
        return appList;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
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
    protected void onStop() {
        unregisterReceiver(nReceiver);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }

    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event");

        }
    }

    class Pager extends FragmentPagerAdapter {

        public Pager(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //Log.e("Touch", "Touch dsi");
        posX = (int) event.getX();
        posY = (int) event.getY();
        if(v != null){
            v.setX(event.getX());
            v.setY(event.getY());
            Log.e("gettin", "there");
        }
        return super.dispatchTouchEvent(event);
    }
}
