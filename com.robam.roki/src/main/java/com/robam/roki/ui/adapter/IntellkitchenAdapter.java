package com.robam.roki.ui.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.common.pojos.CookingKnowledge;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

/**
 * Created by 14807 on 2018/4/11.
 */

public class IntellkitchenAdapter extends RecyclerView.Adapter<KitchenHolder> {

    private Context mContext;
    private List<CookingKnowledge> mKtchens;
    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    public IntellkitchenAdapter(Context context, List<CookingKnowledge> kitchens,
                                OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        super();
        mContext = context;
        mKtchens = kitchens;
        mInflater = LayoutInflater.from(context);
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;

    }

    @Override
    public KitchenHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_kitchen_view, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        final KitchenHolder kitchenHolder = new KitchenHolder(view);

        kitchenHolder.itemView.setOnClickListener(v -> mOnRecyclerViewItemClickListener.onItemClick(v));
        return kitchenHolder;
    }

    @Override
    public void onBindViewHolder(KitchenHolder holder, int position) {
        for (int i = 0; i < mKtchens.size(); i++) {
            if (mKtchens.get(position).contentType == 1){
                holder.img.setImageResource(R.mipmap.img_intelligence_knowledge_vode);
            }else if (mKtchens.get(position).contentType == 0){
                holder.img.setImageResource(R.mipmap.img_intelligence_knowledge_text);
            }
            holder.tv_dec.setText(mKtchens.get(position).title);
            holder.itemView.setTag(mKtchens.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mKtchens.size();
    }

}

class KitchenHolder extends RecyclerView.ViewHolder {

    LinearLayout linearLayout;
    ImageView img;
    TextView tv_dec;

    public KitchenHolder(View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.ll_kitchen);
        img = itemView.findViewById(R.id.iv_img);
        tv_dec = itemView.findViewById(R.id.tv_dec);
    }
}

