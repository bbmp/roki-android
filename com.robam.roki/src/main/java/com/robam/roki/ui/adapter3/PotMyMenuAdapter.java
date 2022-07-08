package com.robam.roki.ui.adapter3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.legent.ui.ext.views.RoundImageView;
import com.robam.common.util.RecipeUtils;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.extension.GlideApp;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

import jp.wasabeef.glide.transformations.MaskTransformation;

/**
 * 无人锅我的最爱adapter
 */
public class PotMyMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MAIN_VIEW = 1;
    public static final int OTHER_VIEW = 2;

    private Context mContext;
    private LayoutInflater mInflater;
    List<String> mDatas;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;

    public PotMyMenuAdapter(Context mContext, List<String> mDatas, OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
        this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
    }

    private RequestOptions maskOption = new RequestOptions()
            .centerCrop()
            .placeholder(R.mipmap.icon_recipe_default) //预加载图片
            .error(R.mipmap.icon_recipe_default) //加载失败图片
            .priority(Priority.HIGH) //优先级
            .skipMemoryCache(true)
            .format(DecodeFormat.PREFER_RGB_565)
            .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存
            .override((int) (167*2), (int) (160*2));

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (OTHER_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.item_my_menu_foot, viewGroup, false);
            ScreenAdapterTools.getInstance().loadView(view);
            PotMyMenuFootViewHolder potMyMenuFootViewHolder = new PotMyMenuFootViewHolder(view);
            return potMyMenuFootViewHolder;
        } else if (MAIN_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.item_pot_my_menu, viewGroup, false);
            ScreenAdapterTools.getInstance().loadView(view);
            final PotMyMenuCommonViewHolder potMyMenuCommonViewHolder = new PotMyMenuCommonViewHolder(view);

            return potMyMenuCommonViewHolder;
        }
        return null;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof PotMyMenuFootViewHolder) {
            PotMyMenuFootViewHolder potMyMenuFootViewHolder =(PotMyMenuFootViewHolder) viewHolder;
            if(mDatas.size()>=6){
                potMyMenuFootViewHolder.img_add_menu.setImageResource(R.mipmap.img_menu_replace);
                potMyMenuFootViewHolder.tv_add.setText("替换更多菜谱");
            }else{
                potMyMenuFootViewHolder.img_add_menu.setImageResource(R.mipmap.img_menu_add);
                potMyMenuFootViewHolder.tv_add.setText("添加更多菜谱");
            }
            potMyMenuFootViewHolder.img_add_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(position);
                    mOnRecyclerViewItemClickListener.onItemClick(v);
                }
            });
        } else if (viewHolder instanceof PotMyMenuCommonViewHolder) {
            PotMyMenuCommonViewHolder potMyMenuCommonViewHolder =(PotMyMenuCommonViewHolder) viewHolder;

            ImageView imageView=(ImageView) potMyMenuCommonViewHolder.iv_tag_recipe;
            GlideApp.with(mContext)
                    .load(R.mipmap.img_chufangzhishi)
                    .apply(maskOption)
                    .into(imageView);

            potMyMenuCommonViewHolder.iv_tag_recipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setTag(position);
                    mOnRecyclerViewItemClickListener.onItemClick(v);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (mDatas.size() == 0 || position == mDatas.size() - 1) {
            return OTHER_VIEW;
        } else {
            return MAIN_VIEW;
        }
    }

}

class PotMyMenuCommonViewHolder extends RecyclerView.ViewHolder {
    RoundImageView iv_tag_recipe;
    TextView tv_recipe_name;

    public PotMyMenuCommonViewHolder(@NonNull View itemView) {
        super(itemView);
        iv_tag_recipe = itemView.findViewById(R.id.iv_tag_recipe);
        tv_recipe_name = itemView.findViewById(R.id.tv_recipe_name);
    }
}

class PotMyMenuFootViewHolder extends RecyclerView.ViewHolder {
    RoundImageView img_add_menu;
    TextView tv_add;
    public PotMyMenuFootViewHolder(@NonNull View itemView) {
        super(itemView);
        img_add_menu = itemView.findViewById(R.id.img_add_menu);
        tv_add = itemView.findViewById(R.id.tv_add);
    }
}