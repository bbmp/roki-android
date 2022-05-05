package com.robam.roki.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.common.pojos.KuCookingSteps;
import com.robam.roki.R;

import java.util.List;

/**
 * Created by Dell on 2018/6/6.
 */

public class DeviceCookerApdater extends RecyclerView.Adapter<DeviceCookerApdater.CookerRecipeViewHolder> {
    List<KuCookingSteps> listStep;
    LayoutInflater mInflater;
    Context cx;
    public DeviceCookerApdater(Context cx,List<KuCookingSteps> listStep){
        this.cx = cx;
        this.listStep = listStep;
        mInflater = LayoutInflater.from(cx);
    }

    @Override
    public CookerRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cooker_recipe_desc_show_item, parent, false);
        CookerRecipeViewHolder cookerRecipeViewHolder = new CookerRecipeViewHolder(view);
        return cookerRecipeViewHolder;
    }

    public void upData(List<KuCookingSteps> listStep){
        this.listStep = listStep;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(CookerRecipeViewHolder holder, int position) {
        if (pos==position){
            holder.stepPro.setVisibility(View.VISIBLE);
            holder.stepTxt.setTextColor(Color.parseColor("#ffb714"));
            holder.stepTxt.setTextSize(30);
            holder.stepTxt.setText((position+1)+"."+listStep.get(position).description);
        }else{
            holder.stepPro.setVisibility(View.GONE);
            holder.stepTxt.setTextColor(Color.parseColor("#ffffff"));
            holder.stepTxt.setTextSize(24);
            holder.stepTxt.setText((position+1)+"."+listStep.get(position).description);
        }
    }

    int pos;
    public void setSelect(int position){
        pos = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (listStep!=null&&listStep.size()>0)?listStep.size():0;
    }

    class CookerRecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView stepPro;
        TextView stepTxt;
        public CookerRecipeViewHolder(View itemView) {
            super(itemView);
            stepPro = itemView.findViewById(R.id.step_pro);
            stepTxt = itemView.findViewById(R.id.txt_step);
        }
    }
}
