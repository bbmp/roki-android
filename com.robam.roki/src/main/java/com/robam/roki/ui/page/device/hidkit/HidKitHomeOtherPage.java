package com.robam.roki.ui.page.device.hidkit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.utils.JsonUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.HidKitHomeOtherParams;
import com.robam.roki.ui.PageArgumentKey;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * @author: lixin
 * @email: lx86@myroki.com
 * @date: 2020/10/26.
 * @PS:
 */
public class HidKitHomeOtherPage extends BasePage {


    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    private DeviceConfigurationFunctions functions;
    private List<HidKitHomeOtherParams.StepsBean> steps = new ArrayList<>();
    HidKitHomeOtherAdapter hidKitHomeOtherAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (null != bundle) {

            functions = (DeviceConfigurationFunctions) bundle.getSerializable(PageArgumentKey.Bean);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.page_hidkit_home_other, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            String functionName = functions.functionName;
            tvName.setText(functionName);
            String functionParams = functions.functionParams;
            HidKitHomeOtherParams hidKitHomeOtherParams = JsonUtils.json2Pojo(functionParams, HidKitHomeOtherParams.class);
            steps = hidKitHomeOtherParams.getSteps();
            hidKitHomeOtherAdapter = new HidKitHomeOtherAdapter();
            recyclerView.setAdapter(hidKitHomeOtherAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        UIService.getInstance().popBack();
    }


    class HidKitHomeOtherAdapter extends RecyclerView.Adapter<HidKitHomeOtherViewHolder> {


        @Override
        public HidKitHomeOtherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_hidkit_home_other, parent, false);

            HidKitHomeOtherViewHolder hidKitHomeOtherViewHolder = new HidKitHomeOtherViewHolder(view);
            return hidKitHomeOtherViewHolder;
        }

        @Override
        public void onBindViewHolder(HidKitHomeOtherViewHolder holder, int position) {

            if (null != steps && 0 < steps.size()){
                holder.mTvTag.setText(steps.get(position).getTag().getValue());
                holder.mTvTagName.setText(steps.get(position).getName().getValue());
                holder.mTvContent.setText(steps.get(position).getDesc().getValue());
            }

        }

        @Override

        public int getItemCount() {
            return (steps != null && steps.size() != 0) ? steps.size() : 0;
        }
    }


    class HidKitHomeOtherViewHolder extends RecyclerView.ViewHolder {

        TextView mTvTag;
        TextView mTvTagName;
        TextView mTvContent;

        public HidKitHomeOtherViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            mTvTag = itemView.findViewById(R.id.tv_tag);
            mTvTagName = itemView.findViewById(R.id.tv_tag_name);
            mTvContent = itemView.findViewById(R.id.tv_content);
        }


    }
}
