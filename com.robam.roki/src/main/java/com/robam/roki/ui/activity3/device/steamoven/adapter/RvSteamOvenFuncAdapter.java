package com.robam.roki.ui.activity3.device.steamoven.adapter;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuntionOtherItemAdapter;
import com.robam.roki.ui.activity3.device.pot.adapter.RvPotFuntionMainAdapter;
import com.robam.roki.ui.activity3.device.pot.adapter.RvPotFuntionOtherAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 一体机功能模块adapter
 */
public class RvSteamOvenFuncAdapter extends BaseQuickAdapter<List<DeviceConfigurationFunctions>, BaseViewHolder> {
    /**
     * 点击事件监听
     */
    FuntionOnClickListener funtionOnClickListener ;
    /**
     * 主功能区adapter
     */
    private RvSteamOvenFuncMainAdapter rvMainAdapter;
    /**
     * 模块1 adapter
     */
    private RvSteamOvenFuncOtherAdapter rvOtherAdapter;
    /**
     * 模块2 adapter
     */
    private RvSteamOvenFuncOtherAdapter rvOtherAdapter2;

    /**
     * 添加监听事件
     * @param funtionOnClickListener
     */
    public void addFuntionOnClickListener(FuntionOnClickListener funtionOnClickListener) {
        this.funtionOnClickListener = funtionOnClickListener;
    }

    /**
     * 监听接口
     */
    public interface FuntionOnClickListener{
        void onClick(DeviceConfigurationFunctions func);
    }

    /**
     * 功能区更新
     * @param steamOven
     */
    public void potNotify(AbsSteameOvenOneNew steamOven){
        rvMainAdapter.notify(steamOven);
        rvOtherAdapter.notify(steamOven);
    }



    public RvSteamOvenFuncAdapter() {
        super(R.layout.item_funtion);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, List<DeviceConfigurationFunctions> item) {
        int itemPosition = getItemPosition(item);
        if (itemPosition == 0){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new GridLayoutManager(getContext() , Math.min(item.size(), 4)));
             rvMainAdapter = new RvSteamOvenFuncMainAdapter();
            rvFunc.setAdapter(rvMainAdapter);
            rvMainAdapter.setNewInstance(item);
            rvMainAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvMainAdapter.getItem(i);
                    if (funtionOnClickListener != null){
                        funtionOnClickListener.onClick(func);

                    }
                }
            });
        }else if (itemPosition == 1){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
             rvOtherAdapter = new RvSteamOvenFuncOtherAdapter();
            rvFunc.setAdapter(rvOtherAdapter);
            rvOtherAdapter.setNewInstance(item);
            rvOtherAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvOtherAdapter.getItem(i);
                    if (funtionOnClickListener != null){
                        funtionOnClickListener.onClick(func);
                    }
                }
            });
        }else if (itemPosition == 2){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
            rvOtherAdapter2 = new RvSteamOvenFuncOtherAdapter();
            rvFunc.setAdapter(rvOtherAdapter2);
            rvOtherAdapter2.setNewInstance(item);
            rvOtherAdapter2.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvOtherAdapter2.getItem(i);
                    if (funtionOnClickListener != null){
                        funtionOnClickListener.onClick(func);
                    }
                }
            });
        }
    }


}
