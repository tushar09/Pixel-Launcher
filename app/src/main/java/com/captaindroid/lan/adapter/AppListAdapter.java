package com.captaindroid.lan.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.captaindroid.lan.MainActivity;
import com.captaindroid.lan.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import com.captaindroid.lan.databinding.RowAppAdaptiveBinding;
import com.captaindroid.lan.databinding.RowAppBinding;
import com.captaindroid.lan.models.AppModel;

public class AppListAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<AppModel> applIst;

    private int xPos;
    private int yPos;
    private PopupMenu popup;
    private boolean popupDismissed = false;

    public AppListAdapter(Context context, List<AppModel> applIst){
        this.context = context;
        this.applIst = applIst;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if(viewType == 0){
            RowAppBinding binding = RowAppBinding.inflate(LayoutInflater.from(context), parent, false);
            return new Holder(binding);
        }else {
            RowAppAdaptiveBinding binding = RowAppAdaptiveBinding.inflate(LayoutInflater.from(context), parent, false);
            return new HolderCircle(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
        if(holder.getItemViewType() == 0){
            Log.e("type", holder.getItemViewType() + "");
            Holder h = (Holder) holder;
            h.binding.ivIcon.setImageDrawable(applIst.get(position).getIcon());
            h.binding.tvName.setText(applIst.get(position).getName());

            h.binding.getRoot().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = context.getPackageManager().getLaunchIntentForPackage(applIst.get(position).getPackageName());
                    if(i != null){
                        context.startActivity(i);
                    }

                }
            });
        }else {
            Log.e("type", holder.getItemViewType() + "");
            HolderCircle h = (HolderCircle) holder;
            h.binding.ivBack.setImageBitmap(applIst.get(position).getBackG());
            h.binding.ivFor.setImageBitmap(applIst.get(position).getForG());
            h.binding.tvName.setText(applIst.get(position).getName());

            xPos = (int) h.binding.cvIcon.getX();
            yPos = (int) h.binding.cvIcon.getY();

            h.binding.cvIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    MainActivity.ma.vibe.vibrate(20);
                    xPos = MainActivity.ma.posX;
                    yPos = MainActivity.ma.posY;
                    MainActivity.ma.appDrawerCanScroll = false;

                    popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.pop_up, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {

                            return true;
                        }
                    });
                    popup.show();
                    popupDismissed = false;
                    return false;
                }
            });

            h.binding.cvIcon.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        xPos = 0;
                        yPos = 0;
                        MainActivity.ma.appDrawerCanScroll = true;
                        MainActivity.ma.bottomSheetBehavior.setDraggable(true);
                        MainActivity.ma.binding.ivFloat.setVisibility(View.GONE);
                    }else {
                        if(xPos != 0){
                            int difx = Math.abs(Math.abs(Math.abs(xPos) - Math.abs(MainActivity.ma.posX)));
                            int difY = Math.abs(Math.abs(Math.abs(yPos) - Math.abs(MainActivity.ma.posY)));
                            if(difx > 30 || difY > 30){
                                if(popupDismissed == false){
                                    popupDismissed = true;
                                    popup.dismiss();
                                    MainActivity.ma.binding.ivFloat.setVisibility(View.VISIBLE);
                                    MainActivity.ma.binding.ivFloat.setImageBitmap(applIst.get(position).getBackG());
                                    MainActivity.ma.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    MainActivity.ma.bottomSheetBehavior.setDraggable(false);
                                    Log.e("diff", difx + "pop" + difY);
                                }

                            }
                        }

                    }

                    return false;
                }
            });

//            h.binding.si.setShapeAppearanceModel(h.binding.si.getShapeAppearanceModel()
//                    .toBuilder()
//                    .setTopLeftCorner(CornerFamily.ROUNDED, 65)
//                    .setTopRightCorner(CornerFamily.ROUNDED, 65)
//                    .setBottomLeftCorner(CornerFamily.ROUNDED, 65)
//                    .setBottomRightCorner(CornerFamily.ROUNDED, 20)
//                    .build()
//            );
//            h.binding.si2.setShapeAppearanceModel(h.binding.si2.getShapeAppearanceModel()
//                    .toBuilder()
//                    .setTopLeftCorner(CornerFamily.ROUNDED, 65)
//                    .setTopRightCorner(CornerFamily.ROUNDED, 65)
//                    .setBottomLeftCorner(CornerFamily.ROUNDED, 65)
//                    .setBottomRightCorner(CornerFamily.ROUNDED, 20)
//                    .build()
//            );
//            h.binding.si.setImageBitmap(applIst.get(position).getBackG());
//            h.binding.si2.setImageBitmap(applIst.get(position).getForG());

            h.binding.getRoot().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent i = context.getPackageManager().getLaunchIntentForPackage(applIst.get(position).getPackageName());
                    if(i != null){
                        context.startActivity(i);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount(){
        return applIst.size();
    }

    @Override
    public int getItemViewType(int position){
        if(applIst.get(position).getAd() == null){
            return 0;
        }else {
            return 1;
        }
    }

    private class Holder extends RecyclerView.ViewHolder{

        RowAppBinding binding;

        public Holder(@NonNull RowAppBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class HolderCircle extends RecyclerView.ViewHolder{

        RowAppAdaptiveBinding binding;

        public HolderCircle(@NonNull RowAppAdaptiveBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
