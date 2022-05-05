package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;

import java.util.List;

public class LocalRecipeAdapter extends RecyclerView.Adapter<LocalRecipeHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<DeviceConfigurationFunctions> deviceConfigurationFunctions;

    private OnItemRecyclerClick onItemRecyclerClick;


    public interface OnItemRecyclerClick {
        void onItemClick(View v, int position);
    }

    public void setOnItemRecyclerClick(OnItemRecyclerClick onItemRecyclerClick) {
        this.onItemRecyclerClick = onItemRecyclerClick;
    }

    public LocalRecipeAdapter(Context context, List<DeviceConfigurationFunctions> tags) {

        this.context = context;
        this.deviceConfigurationFunctions = tags;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public LocalRecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.local_recipe_item_view, parent, false);
        return new LocalRecipeHolder(view);
    }


    @Override
    public void onBindViewHolder(LocalRecipeHolder holder, final int position) {

        DeviceConfigurationFunctions deviceConfigurationFunctions = this.deviceConfigurationFunctions.get(position);

        if (deviceConfigurationFunctions.backgroundImg != null) {
            Glide.with(context).load(deviceConfigurationFunctions.backgroundImg).into(holder.imgRecipe);
        }


        if (position == 0) {
            holder.imgRecipe.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        } else {
            holder.imgRecipe.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        }
        if (deviceConfigurationFunctions.functionName != null) {
            holder.tvTitle.setText(deviceConfigurationFunctions.functionName);
        }


        holder.imgRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRecyclerClick != null) {
                    onItemRecyclerClick.onItemClick(v, position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return deviceConfigurationFunctions.size();
    }


}

class LocalRecipeHolder extends RecyclerView.ViewHolder {
    ImageView imgRecipe;
    TextView tvTitle;


    public LocalRecipeHolder(View itemView) {
        super(itemView);
        imgRecipe = itemView.findViewById(R.id.img_recipe);
        tvTitle = itemView.findViewById(R.id.tv_title);


    }


}