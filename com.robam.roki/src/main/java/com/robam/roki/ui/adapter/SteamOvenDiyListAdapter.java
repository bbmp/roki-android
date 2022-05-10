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
import com.legent.utils.LogUtils;
import com.robam.common.pojos.DiyCookbookList;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.listener.OnRecyclerViewItemLongClickListener;
import com.robam.roki.model.bean.DeviceOvenDiyParams;

import java.util.List;

/**
 * Created by Administrator on 2019/9/3.
 */

public class SteamOvenDiyListAdapter extends RecyclerView.Adapter<SteamOvenDiyViewHolder> {

    private LayoutInflater mInflater;
    private Context context;
    private List<DiyCookbookList> diyCookbookLists;
    private List<Integer> keyList;
    private List<DeviceOvenDiyParams> deviceList;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    private OnRecyclerViewItemLongClickListener mOnRecyclerViewItemLongClickListener;


    public SteamOvenDiyListAdapter(Context context,
                                   List<DiyCookbookList> data,
                                   List<DeviceOvenDiyParams> deviceList,
                                   List<Integer> keyList,
                                   OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener,
                                   OnRecyclerViewItemLongClickListener mOnRecyclerViewItemLongClickListener) {
        this.context = context;
        this.diyCookbookLists = data;
        this.deviceList = deviceList;
        this.keyList = keyList;
        this.mOnRecyclerViewItemClickListener = mOnRecyclerViewItemClickListener;
        this.mOnRecyclerViewItemLongClickListener = mOnRecyclerViewItemLongClickListener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SteamOvenDiyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_oven_bake_diy_list, parent, false);

        return new SteamOvenDiyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SteamOvenDiyViewHolder holder, final int position) {
        DiyCookbookList diyCookbookList = diyCookbookLists.get(position);
        holder.tv_name.setText(diyCookbookList.name);
        LogUtils.i("20200628", "diyCookbookList.modeCode:::" + diyCookbookList.modeCode);
        String img = name2Img(diyCookbookList.modeCode == null ? "" : diyCookbookList.modeCode);
        Glide.with(context).load(img).into(holder.iv_img);
        holder.tv_oven_mode_name.setText(code2Name(diyCookbookList.modeCode) == null ? "" : code2Name(diyCookbookList.modeCode));

        if ("10".equals(diyCookbookList.modeCode)) {
            holder.tv_temp_down.setVisibility(View.VISIBLE);
            String tempDown = diyCookbookList.tempDown.contains("℃") ? diyCookbookList.tempDown : diyCookbookList.tempDown + "℃";
            holder.tv_temp_down.setText("下：" + tempDown);
            String tempUp = diyCookbookList.temp.contains("℃") ? diyCookbookList.temp : diyCookbookList.temp + "℃";
            if (tempUp.contains("下")) {
                tempUp = tempUp.substring(0, tempUp.indexOf("下"));
            }
            holder.tv_temp.setText("上：" + tempUp);
        } else {
            holder.tv_temp_down.setVisibility(View.GONE);
            holder.tv_temp.setText(diyCookbookList.temp.contains("℃") ? diyCookbookList.temp : diyCookbookList.temp + "℃");
        }

        holder.tv_time.setText(diyCookbookList.minute.contains("分钟") || diyCookbookList.minute.contains("min") ? diyCookbookList.minute : diyCookbookList.minute + "分钟");
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

    private String code2Name(String modeCode) {
        if (modeCode == null || "".equals(modeCode)) {
            return "";
        }
        String codeName = null;
        for (int i = 0; i < keyList.size(); i++) {
            if (modeCode.equals(keyList.get(i) + "")) {
                codeName = deviceList.get(i).getValue();

            }
        }
        return codeName;

    }


    @Override
    public int getItemCount() {
        return diyCookbookLists.size() == 0 ? 0 : diyCookbookLists.size();
    }


    private String name2Img(String code) {
        if (code == null || code.equals("")) {
            return "";
        }
        String img = null;
        for (int i = 0; i < keyList.size(); i++) {
            if (code.equals(keyList.get(i) + "")) {
                LogUtils.i("2020062801", "deviceList.get(i).getImg()::" + deviceList.get(i).getImg());
                img = deviceList.get(i).getImg();
            }
        }
        return img;

    }


}

class SteamOvenDiyViewHolder extends RecyclerView.ViewHolder {
    LinearLayout ll_item_bake;
    TextView tv_name;
    ImageView iv_img;
    TextView tv_oven_mode_name;
    TextView tv_temp;
    TextView tv_temp_down;
    TextView tv_time;


    SteamOvenDiyViewHolder(View itemView) {
        super(itemView);
        this.setIsRecyclable(false);
        ll_item_bake = itemView.findViewById(R.id.ll_item_bake);
        tv_name = itemView.findViewById(R.id.tv_name);
        iv_img = itemView.findViewById(R.id.iv_img);
        tv_oven_mode_name = itemView.findViewById(R.id.tv_oven_mode_name);
        tv_temp = itemView.findViewById(R.id.tv_temp);
        tv_temp_down = itemView.findViewById(R.id.tv_temp_down);
        tv_time = itemView.findViewById(R.id.tv_time);
    }


}
