package com.robam.roki.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.robam.roki.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2018/6/5.
 */

public class HidKitSmartDialogueAdapter extends RecyclerView.Adapter<HidKitSmartDialogueAdapter
        .HidKitSmartDialogueViewHolder> {

    private LayoutInflater mInflater;
    List<String> mCanSay = new ArrayList<>();
    Context cx;

    public HidKitSmartDialogueAdapter(Context cx, List<String> canSay) {
        this.cx = cx;
        this.mCanSay = canSay;
        mInflater = LayoutInflater.from(cx);
    }

    @Override
    public HidKitSmartDialogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_smart_dialogue, parent, false);
        HidKitSmartDialogueViewHolder hidKitSmartDialogueViewHolder = new HidKitSmartDialogueViewHolder(view);
        return hidKitSmartDialogueViewHolder;
    }

    @Override
    public void onBindViewHolder(HidKitSmartDialogueViewHolder holder, int position) {
        holder.txtMsg.setText(mCanSay.get(position));
    }


    @Override
    public int getItemCount() {
        return (mCanSay != null && mCanSay.size() > 0) ? mCanSay.size() : 0;
    }


    class HidKitSmartDialogueViewHolder extends RecyclerView.ViewHolder {
        TextView txtMsg;

        public HidKitSmartDialogueViewHolder(View itemView) {
            super(itemView);
            txtMsg = itemView.findViewById(R.id.txtMsg);
        }
    }
}
