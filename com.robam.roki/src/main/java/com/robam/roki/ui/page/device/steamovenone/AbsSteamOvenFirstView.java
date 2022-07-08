package com.robam.roki.ui.page.device.steamovenone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Objects;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.SteamOvenOpenDoorSteamEvent;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.OvenCommonAdapter;
import com.robam.roki.ui.adapter.SteamOvenCommonAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/7/12.
 */

public class AbsSteamOvenFirstView extends FrameLayout{
    Context cx;
    @InjectView(R.id.oven_offline_txt)
    TextView ovenOfflineTxt;
    @InjectView(R.id.oven_func)
    GridView ovenFunc;
    @InjectView(R.id.oven_func_show)
    RecyclerView ovenFuncShow;

    List<DeviceConfigurationFunctions> mainList;
    List<DeviceConfigurationFunctions> otherList;
    SteamOvenCommonAdapter steamOvenCommonAdapter;


    public AbsSteamOvenFirstView(Context context, List<DeviceConfigurationFunctions> mainList, List<DeviceConfigurationFunctions> otherList) {
        super(context);
        this.cx = context;
        this.mainList = mainList;
        this.otherList = otherList;
        initView();
    }

    public AbsSteamOvenFirstView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }



    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.steamoven_first_view, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }


        steamOvenCommonAdapter = new SteamOvenCommonAdapter(cx,mainList,otherList);
        ovenFuncShow.setAdapter(steamOvenCommonAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(cx, 1);
        ovenFuncShow.setLayoutManager(layoutManager);
        steamOvenCommonAdapter.setGridViewOnclickLister(new SteamOvenCommonAdapter.GridViewOnclick() {
            @Override
            public void onGridClick(String pos) {
                if (onclickMainLister!=null){
                    onclickMainLister.onclickMain(pos);
                }
            }
        });

        steamOvenCommonAdapter.setItemViewOnclickLister(new SteamOvenCommonAdapter.ItemViewOnclick() {

            @Override
            public void onItemClick(String pos) {
                if (onclickMainLister!=null){
                    onclickMainLister.onclickOther(pos);
                }
            }
        });
    }

    protected void setUpData(List<DeviceConfigurationFunctions> moreList){
        LogUtils.i("20180831","first moreList:" + moreList.size());
        steamOvenCommonAdapter.upMoreView(moreList);
    }





    protected void removeMoreView(){
        steamOvenCommonAdapter.removeMoreView();
    }

    protected void disConnect(boolean isCon){
        if (isCon){
            ovenOfflineTxt.setVisibility(View.VISIBLE);
        }else{
            ovenOfflineTxt.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnClickMian{

        void  onclickMain(String str);

        void  onclickOther(String str);
    }

    public OnClickMian onclickMainLister;

    public void setOnclickMainLister(OnClickMian onclickMainLister) {
        this.onclickMainLister = onclickMainLister;
    }



}
