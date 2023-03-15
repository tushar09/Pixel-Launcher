package com.captaindroid.lan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.captaindroid.lan.MainActivity;
import com.captaindroid.lan.R;
import com.captaindroid.lan.databinding.RowAppAdaptiveBinding;
import com.captaindroid.lan.databinding.RowAppBinding;
import com.captaindroid.lan.databinding.RowDesktopAppBinding;
import com.captaindroid.lan.interfaces.ItemFinder;
import com.captaindroid.lan.models.AppModel;
import com.captaindroid.lan.models.DesktopAppModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

public class DesktopAppListAdapter extends RecyclerView.Adapter implements ItemFinder{

    private Context context;
    private List<DesktopAppModel> applIst;

    private int xPos;
    private int yPos;
    private PopupMenu popup;
    private boolean popupDismissed = false;
    private int highlightedPosition = -1;

    public DesktopAppListAdapter(Context context, List<DesktopAppModel> applIst){
        this.context = context;
        this.applIst = applIst;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        RowDesktopAppBinding binding = RowDesktopAppBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
        Holder h = (Holder) holder;
        h.binding.cv.setTag(position);
        if(applIst.get(position).isHightlight()){
            h.binding.cv.setStrokeColor(context.getResources().getColor(R.color.colorAccent));
        }else{
            h.binding.cv.setStrokeColor(context.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public int getItemCount(){
        return applIst.size();
    }

    @Override
    public void findItem(int newHighlightPosition) {
        if(newHighlightPosition == -1 && highlightedPosition != -1){
            applIst.get(highlightedPosition).setHightlight(false);
            notifyItemChanged(highlightedPosition);
            highlightedPosition = -1;
            return;
        }
        if(highlightedPosition != newHighlightPosition){
            if(highlightedPosition != -1){
                applIst.get(highlightedPosition).setHightlight(false);
            }
            applIst.get(newHighlightPosition).setHightlight(true);
            notifyItemChanged(highlightedPosition);
            notifyItemChanged(newHighlightPosition);
            highlightedPosition = newHighlightPosition;
        }

    }

    private class Holder extends RecyclerView.ViewHolder{

        RowDesktopAppBinding binding;

        public Holder(@NonNull RowDesktopAppBinding binding){
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
