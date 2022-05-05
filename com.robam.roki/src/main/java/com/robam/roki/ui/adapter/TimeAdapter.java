package com.robam.roki.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;

import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceOvenDiyParams;
import com.robam.roki.ui.wheel.WheelView;
import com.robam.roki.utils.LoginUtil;

import org.eclipse.jetty.http.Parser;

import java.util.List;



/**
 * Created by Dell on 2018/4/10.
 */

public class TimeAdapter<T> extends WheelView.WheelAdapter<TimeViewHolder> {
    private List<T> timelist;
    private String str;
    public TimeAdapter(List<T> timelist,String str){
        this.timelist = timelist;
        this.str = str;
    }

    private int pos=0;

    @Override
    public int getItemCount() {
        return (timelist!=null&&timelist.size()>0)?timelist.size():0;
    }

    @Override
    public TimeViewHolder onCreateViewHolder(LayoutInflater inflater, int viewType) {
        return new TimeViewHolder(inflater.inflate(R.layout.timeview_item, null, false));
    }

    @Override
    public void onBindViewHolder(TimeViewHolder holder, int position) {
        LogUtils.i("20180810","position::"+position+"   pos:"+pos);
        if (str!=null){
            holder.tv.setText(timelist.get(position)+"");
            if (position==pos){
                holder.tv.setTextColor(Color.parseColor("#000000"));
            }else{
                holder.tv.setTextColor(Color.GRAY);
            }
            if (timelist.get(position) instanceof DeviceOvenDiyParams) {
                holder.tv.setText(((DeviceOvenDiyParams) timelist.get(position)).getValue());
            } else {
                holder.tv.setText(timelist.get(position) + str);
            }
        }else{
            if (timelist.get(position) instanceof DeviceOvenDiyParams) {
                holder.tv.setText(((DeviceOvenDiyParams) timelist.get(position)).getValue());
            } else {
                holder.tv.setText(timelist.get(position) + "");
            }
            if (position==pos){
                holder.tv.setTextColor(Color.parseColor("#000000"));
            }else{
                holder.tv.setTextColor(Color.GRAY);
            }


        }
    }

    @Override
    public void getSelect(int pos) {
        LogUtils.i("20180810","pos::"+pos);
        this.pos = pos;
        notifyDataSetChanged();
    }

}
