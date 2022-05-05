package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.legent.plat.pojos.device.ConsumablesList;
import com.robam.roki.R;

import java.util.List;

public class ConsumablesBuyAdapter extends RecyclerView.Adapter<ConsumablesBuyViewHolder> {
    private LayoutInflater mInflater;
    private Context cx;
    private ConsumablesBuyAdapter.OnItemClickListener onItemClickListener;
    private List<ConsumablesList>consumablesLists;


    public ConsumablesBuyAdapter(Context cx, List<ConsumablesList>consumablesLists) {
        this.mInflater = LayoutInflater.from(cx);
        this.cx = cx;
        this.consumablesLists = consumablesLists;
    }

    @Override
    public ConsumablesBuyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_consumables_buy, parent, false);
        return new ConsumablesBuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ConsumablesBuyViewHolder holder, int position) {
        holder.tv_title.setText(consumablesLists.get(position).accessoryName);
        String imgUrl = consumablesLists.get(position).imgUrl;
        if (!"".equals(imgUrl)) {
            Glide.with(cx).load(imgUrl).into(holder.iv_bg);
        }

        holder.tv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return consumablesLists==null?0:consumablesLists.size();
    }

    /**
     * 设置回调监听
     *
     * @param listener
     */
    public void setOnItemClickListener(ConsumablesBuyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


}

class ConsumablesBuyViewHolder extends RecyclerView.ViewHolder {
    ImageView iv_bg;
    TextView tv_title;
    TextView tv_buy;

    public ConsumablesBuyViewHolder(View itemView) {
        super(itemView);
        iv_bg = itemView.findViewById(R.id.iv_bg);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_buy = itemView.findViewById(R.id.tv_buy);
    }
}
