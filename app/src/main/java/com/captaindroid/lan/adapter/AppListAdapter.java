package com.captaindroid.lan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.CornerFamily;

import java.util.List;

import com.captaindroid.lan.databinding.RowAppAdaptiveBinding;
import com.captaindroid.lan.databinding.RowAppBinding;
import com.captaindroid.lan.models.AppModel;

public class AppListAdapter extends RecyclerView.Adapter{

    private Context context;
    private List<AppModel> applIst;

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
