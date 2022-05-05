package com.robam.roki.ui.holder;


import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.robam.roki.R;

/**
 * Created by yinwei on 2017/12/6.
 */

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private MyItemClickListener mListener;

    @Override
    public void onClick(View v) {
        if (mListener!=null){
            mListener.onItemClick(v,getPosition());
        }
    }


    public interface MyItemClickListener{
        void onItemClick(View view, int postion);
    }

    public ImageView deviceBg;
    public TextView deviceDesc;
    public TextView deviceName;
    public MyViewHolder(View view,MyItemClickListener listener) {
        super(view);
        this.mListener = listener;
        deviceBg = view.findViewById(R.id.device_bg);
        deviceName = view.findViewById(R.id.device_name);
        deviceDesc = view.findViewById(R.id.device_desc);
        view.setOnClickListener(this);
    }
}
