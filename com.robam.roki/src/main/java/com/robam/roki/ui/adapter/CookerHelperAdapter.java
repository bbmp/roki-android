package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.robam.common.pojos.PayLoad;
import com.robam.roki.R;
import com.robam.roki.ui.view.RoundTransformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2018/6/5.
 */

public class CookerHelperAdapter extends RecyclerView.Adapter<CookerHelperAdapter.CookerHelperViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    List<PayLoad> cookerList=new ArrayList<>();
    Context cx;
    private OnItemClickListener mClickListener;

    public CookerHelperAdapter(Context cx, List<PayLoad> cookerList) {
        this.cx = cx;
        this.cookerList = cookerList;
        mInflater = LayoutInflater.from(cx);
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    @Override
    public CookerHelperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_cooker_helper, parent, false);
        CookerHelperViewHolder cookerHelperViewHolder = new CookerHelperViewHolder(view);
        return cookerHelperViewHolder;
    }

    @Override
    public void onBindViewHolder(CookerHelperViewHolder holder, int position) {
        holder.reName.setText(cookerList.get(position).name);
        holder.reTime.setText("用时："+timeStr(cookerList.get(position).totalTime)+"min");
        Glide.with(cx).load(cookerList.get(position).image).into(holder.helperImg);
//        Glide.with(cx).load(cookerList.get(position).image).asBitmap().transform(new CenterCrop(cx),new RoundTransformation(cx,10))
//                .into(holder.helperImg);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    private String timeStr(int time){
        int ti=time/60;
        if (time%60!=0){
            ti+=1;
        }
        return ti+"";
    }

    @Override
    public int getItemCount() {
        return (cookerList!=null&&cookerList.size() > 0) ? cookerList.size() : 0;
    }


    @Override
    public void onClick(View v) {
        if (mClickListener!=null){
            mClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    class CookerHelperViewHolder extends RecyclerView.ViewHolder {
        ImageView helperImg;
        TextView reName;
        TextView reTime;

        public CookerHelperViewHolder(View itemView) {
            super(itemView);
            helperImg = itemView.findViewById(R.id.helper_img);
            reName = itemView.findViewById(R.id.re_name);
            reTime = itemView.findViewById(R.id.re_time);
        }
    }
}
