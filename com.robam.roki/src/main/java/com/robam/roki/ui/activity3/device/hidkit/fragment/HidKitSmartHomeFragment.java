package com.robam.roki.ui.activity3.device.hidkit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.FunctionSmartHomeParams;
import com.robam.roki.ui.adapter.HidKitSmartDialogueAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class HidKitSmartHomeFragment extends Fragment {
    @InjectView(R.id.tv_model_dt)
    TextView tvModelDt;
    @InjectView(R.id.recycle_said_content)
    RecyclerView recycleSaidContent;
    @InjectView(R.id.tv_recipe_name)
    TextView tvRecipeName;

    public static Fragment instance(FunctionSmartHomeParams.DeviceInfoBean devInfos) {
        HidKitSmartHomeFragment fragment = new HidKitSmartHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("devInfo", devInfos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_smart_home_item, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            FunctionSmartHomeParams.DeviceInfoBean devInfo = (FunctionSmartHomeParams.DeviceInfoBean) bundle.getSerializable("devInfo");
            List<String> canSay = devInfo.getCanSay();
            tvRecipeName.setText(devInfo.getCookbooks());
            tvModelDt.setText(devInfo.getDt());
            HidKitSmartDialogueAdapter hidKitSmartDialogueAdapter = new HidKitSmartDialogueAdapter(getContext(), canSay);
            recycleSaidContent.setAdapter(hidKitSmartDialogueAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                    (getContext(), LinearLayoutManager.VERTICAL, false);
            recycleSaidContent.setLayoutManager(linearLayoutManager);
            LogUtils.i("20201021", "devInfo:" + devInfo);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
