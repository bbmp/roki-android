package com.robam.roki.ui.view;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;
import com.robam.roki.model.bean.GasParamsTemp;
import com.robam.roki.ui.adapter.GasSensorAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/5/30.
 */

public class GasItemDisplayView extends FrameLayout {

    @InjectView(R.id.ry_other_func)
    RecyclerView ryOtherFunc;
    @InjectView(R.id.view_t)
    LinearLayout viewT;

    GasSensorAdapter gasSensorAdapter;
    List<GasParamsTemp> gasParamList= new ArrayList<>();
    Context cx;

    public GasItemDisplayView(Context context) {
        super(context);
        initView(context, null);
    }

    public void setGasParamList(List<GasParamsTemp> gasParamList) {
        this.gasParamList = gasParamList;
    }

    public GasItemDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.gas_item_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void onRefresh(Context cx,List<GasParamsTemp> gasParamList){
        gasSensorAdapter=new GasSensorAdapter(cx,gasParamList);
        ryOtherFunc.setAdapter(gasSensorAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        ryOtherFunc.setLayoutManager(linearLayoutManager);
        gasSensorAdapter.setOnItemClickListener(new GasSensorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                // ToastUtils.show(""+postion, Toast.LENGTH_SHORT);
                if (onRecycleItemClickLister!=null){
                    onRecycleItemClickLister.itemClick(view,postion);
                }
            }
        });
    }

    public OnRecycleItemClick onRecycleItemClickLister;

    public void setOnRecycleItemClick(OnRecycleItemClick onRecycleItemClickLister){
        this.onRecycleItemClickLister = onRecycleItemClickLister;
    }

    public interface OnRecycleItemClick{
        void itemClick(View view,int position);
    }
}
