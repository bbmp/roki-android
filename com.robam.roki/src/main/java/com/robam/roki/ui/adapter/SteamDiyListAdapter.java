package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.common.pojos.device.Steamoven.AbsSteamoven;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.listener.OnRecyclerViewItemLongClickListener;
import com.robam.roki.model.bean.DeviceSteamDiyParams;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

public class SteamDiyListAdapter extends RecyclerView.Adapter<SteamViewHolder> {
    private LayoutInflater mInflater;
    private Context context;
    private List<DiyCookbookList> diyCookbookLists;
    private List<Integer> keyList;
    private List<DeviceSteamDiyParams> deviceList;
    private AbsSteamoven steam;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener mOnRecyclerViewItemLongClickListener;


    public SteamDiyListAdapter(Context context,
                               AbsSteamoven steam,
                               List<DiyCookbookList> data,
                               List<DeviceSteamDiyParams> deviceList,
                               List<Integer> keyList,
                               OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener,
                               OnRecyclerViewItemLongClickListener mOnRecyclerViewItemLongClickListener) {
        this.context = context;
        this.diyCookbookLists = data;
        this.deviceList = deviceList;
        this.keyList = keyList;
        this.steam = steam;
        this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
        this.mOnRecyclerViewItemLongClickListener = mOnRecyclerViewItemLongClickListener;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public SteamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_oven_bake_diy_list, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        SteamViewHolder steamViewHolder = new SteamViewHolder(view);
        return steamViewHolder;
    }

    @Override
    public void onBindViewHolder(SteamViewHolder holder, final int position) {
        DiyCookbookList diyCookbookList = diyCookbookLists.get(position);
        holder.tv_name.setText(diyCookbookList.name);
        String modeCode = diyCookbookList.modeCode;
        String s = name2Img(modeCode);
        Glide.with(context).load(s).into(holder.iv_img);

        String codeName = code2Name(diyCookbookList.modeCode);
        holder.tv_oven_mode_name.setText(codeName);
        holder.tv_temp.setText(diyCookbookList.temp + "℃");
        holder.tv_time.setText(diyCookbookList.minute + "分钟");
        holder.ll_item_bake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(position);
                mOnRecyclerViewItemClickListener.onItemClick(v);
            }
        });

        holder.ll_item_bake.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.setTag(position);
                mOnRecyclerViewItemLongClickListener.onItemLongClick(v);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return diyCookbookLists.size() == 0 ? 0 : diyCookbookLists.size();
    }


    private String code2Name(String modeCode) {
        String codeName = null;
        for (int i = 0; i < keyList.size(); i++) {
            if (modeCode.equals(keyList.get(i) + "")) {
                codeName = deviceList.get(i).getValue();

            }
        }


        return codeName;

    }


    private String name2Img(String code) {
        String img = null;
        for (int i = 0; i < keyList.size(); i++) {
            if (code.equals(keyList.get(i)+"")) {
                img = deviceList.get(i).getImg();
            }
        }
        return img;

    }
}


class SteamViewHolder extends RecyclerView.ViewHolder {
    LinearLayout ll_item_bake;
    TextView tv_name;
    ImageView iv_img;
    TextView tv_oven_mode_name;
    TextView tv_temp;
    TextView tv_time;

    public SteamViewHolder(View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        ll_item_bake = itemView.findViewById(R.id.ll_item_bake);
        tv_name = itemView.findViewById(R.id.tv_name);
        iv_img = itemView.findViewById(R.id.iv_img);
        tv_oven_mode_name = itemView.findViewById(R.id.tv_oven_mode_name);
        tv_temp = itemView.findViewById(R.id.tv_temp);
        tv_time = itemView.findViewById(R.id.tv_time);
    }
}
