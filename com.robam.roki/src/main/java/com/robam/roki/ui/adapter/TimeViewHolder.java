package com.robam.roki.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.robam.roki.R;


/**
 * Created by Dell on 2018/4/10.
 */

public class TimeViewHolder extends RecyclerView.ViewHolder {
    public final TextView tv;

    public TimeViewHolder(View itemView){
        super(itemView);
      tv = itemView.findViewById(R.id.tv);
    }
}
