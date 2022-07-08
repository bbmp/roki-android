package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.robam.roki.R;
import com.robam.roki.model.bean.GasParamsTemp;

import java.util.List;

import static com.legent.ContextIniter.cx;

/**
 * Created by Dell on 2018/5/30.
 */

public class GasSensorAdapter extends RecyclerView.Adapter<GasSensorAdapterViewHolder>  implements View.OnClickListener{
    private LayoutInflater mInflater;
    private Context mContext;
    private List<GasParamsTemp>  date;
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

    public GasSensorAdapter(Context context,List<GasParamsTemp> date){
        mInflater = LayoutInflater.from(context);
        this.date = date;
    }

    @Override
    public GasSensorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);
        GasSensorAdapterViewHolder gasSensorAdapterViewHolder = new GasSensorAdapterViewHolder(view);
        return gasSensorAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(GasSensorAdapterViewHolder holder, int position) {
        holder.mImageView.setImageResource(date.get(position).img);
        holder.mTvName.setText(date.get(position).title);
        holder.mTvDesc.setText(date.get(position).desc);
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



    public GasSensorAdapterViewHolder(View itemView) {
        super(itemView);
        mTvName = itemView.findViewById(R.id.tv_name);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mItemView = itemView.findViewById(R.id.itemView);
        mStateShow = itemView.findViewById(R.id.iv_state_show);
    }

    public void startAnimation() {

        mAlphaAnimation = null;
        if (mAlphaAnimation == null) {
            mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(cx, R.anim.device_rika_dot_alpha);
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