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
import com.robam.roki.R;
import com.robam.roki.model.device.rika.CookBookTag;

import java.util.ArrayList;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/5/28.
 * @PS:
 */
public class LocalRikaBookAdapter extends RecyclerView.Adapter<RikaLocalBookHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private ArrayList<CookBookTag> tags;
    private OnItemRecyclerClick onItemRecyclerClick;


    public interface OnItemRecyclerClick {
        void onItemClick(View v, int position);
    }

    public void setOnItemRecyclerClick(OnItemRecyclerClick onItemRecyclerClick) {
        this.onItemRecyclerClick = onItemRecyclerClick;
    }

    public LocalRikaBookAdapter(Context context, ArrayList<CookBookTag> tags) {

        this.context = context;
        this.tags = tags;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public RikaLocalBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.localbook_rika_item_view, parent, false);
        return new RikaLocalBookHolder(view);
    }

    @Override
    public void onBindViewHolder(RikaLocalBookHolder holder, final int position) {
        CookBookTag cookBookTag = tags.get(position);
        if (cookBookTag.backgroundImg != null) {
            Glide.with(context).load(cookBookTag.backgroundImg).into(holder.imgRecipe);
        }


        if (position % 2 == 0) {
            holder.imgRecipe.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500));
        } else {
            holder.imgRecipe.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 600));
        }
        if (cookBookTag.functionName != null) {
            holder.tvTitle.setText(cookBookTag.functionName);
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
        return tags.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}

class RikaLocalBookHolder extends RecyclerView.ViewHolder {
    ImageView imgRecipe;
    TextView tvTitle;


    public RikaLocalBookHolder(View itemView) {
        super(itemView);
        imgRecipe = itemView.findViewById(R.id.img_recipe);
        tvTitle = itemView.findViewById(R.id.tv_title);


    }


}
