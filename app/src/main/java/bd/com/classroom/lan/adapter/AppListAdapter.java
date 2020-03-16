package bd.com.classroom.lan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import bd.com.classroom.lan.R;
import bd.com.classroom.lan.databinding.RowAppBinding;
import bd.com.classroom.lan.models.AppModel;

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
        LayoutInflater li = LayoutInflater.from(context);
        RowAppBinding binding = RowAppBinding.inflate(li, parent, false);
        return new Holder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position){
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
    }

    @Override
    public int getItemCount(){
        return applIst.size();
    }

    private class Holder extends RecyclerView.ViewHolder{

        RowAppBinding binding;

        public Holder(@NonNull RowAppBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
