package com.robam.roki.ui.page.device.cook;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.model.bean.GasParamsTemp;



import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/6/19.
 */

public class CookItemDisplayView extends FrameLayout {
    @InjectView(R.id.ry_other_func)
    RecyclerView ryOtherFunc;
    @InjectView(R.id.view_t)
    LinearLayout viewT;

    CookAdapter gasSensorAdapter;
    List<GasParamsTemp> gasParamList= new ArrayList<>();
    Context cx;

    public CookItemDisplayView(Context context) {
        super(context);
        initView(context, null);
    }

    public void setGasParamList(List<GasParamsTemp> gasParamList) {
        this.gasParamList = gasParamList;
    }

    public CookItemDisplayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.cook_item_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
    }

    public void onRefresh(Context cx,List<DeviceConfigurationFunctions> gasParamList){
        gasSensorAdapter=new CookAdapter(cx,gasParamList);
        ryOtherFunc.setAdapter(gasSensorAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager
                (cx, LinearLayoutManager.VERTICAL, false);
        ryOtherFunc.setLayoutManager(linearLayoutManager);
        gasSensorAdapter.setOnItemClickListener(new CookAdapter.OnItemClickListener(){
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
