package com.robam.roki.ui.page.device.cook;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.model.bean.GasParamsTemp;
import com.robam.roki.ui.adapter.GasSensorAdapter;

import java.util.List;


/**
 * Created by Dell on 2018/6/19.
 */

public class CookAdapter extends RecyclerView.Adapter<GasSensorAdapterViewHolder> implements View.OnClickListener{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<DeviceConfigurationFunctions> date;
    private OnItemClickListener mClickListener;

    @Override
    public void onClick(View v) {
        if (mClickListener!=null){
            mClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int postion);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }

    public CookAdapter(Context context,List<DeviceConfigurationFunctions> date){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.date = date;
    }

    @Override
    public GasSensorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);
        GasSensorAdapterViewHolder gasSensorAdapterViewHolder = new GasSensorAdapterViewHolder(mContext, view);
        return gasSensorAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(GasSensorAdapterViewHolder holder, int position) {
        Glide.with(mContext).load(date.get(position).backgroundImg).into(holder.mImageView);
        holder.mTvName.setText(date.get(position).functionName);
        holder.mTvDesc.setText(date.get(position).msg);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return date.size()>0 ? date.size():0;
    }
}


class GasSensorAdapterViewHolder extends RecyclerView.ViewHolder{
    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    LinearLayout mItemView;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;
    Context mContext;


    public GasSensorAdapterViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mTvName = itemView.findViewById(R.id.tv_name);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mItemView = itemView.findViewById(R.id.itemView);
        mStateShow = itemView.findViewById(R.id.iv_state_show);
    }

    public void startAnimation() {

        mAlphaAnimation = null;
        if (mAlphaAnimation == null) {
            mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(mContext, R.anim.device_rika_dot_alpha);
            LinearInterpolator lin = new LinearInterpolator();
            mAlphaAnimation.setInterpolator(lin);
            mStateShow.startAnimation(mAlphaAnimation);
            mStateShow.setVisibility(View.VISIBLE);
        }
    }

    public void stopAnimation() {
        mStateShow.setVisibility(View.GONE);

        if (mAlphaAnimation != null) {
            mAlphaAnimation.cancel();
            mAlphaAnimation = null;
        }
    }

}
