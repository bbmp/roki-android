package com.robam.roki.ui.activity3.device.fan.adapter;

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
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 烟机功能模块adapter
 */
public class RvFuntionAdapter extends BaseQuickAdapter<List<DeviceConfigurationFunctions>, BaseViewHolder> {
    /**
     * 点击事件监听
     */
    FuntionOnClickListener funtionOnClickListener ;
    /**
     * 主功能区adapter
     */
    private RvFuntionMainItemAdapter rvFuntionMainItemAdapter;
    /**
     * 模块1 adapter
     */
    private RvFuntionOtherItemAdapter rvFuntionOtherItemAdapter;
    /**
     * 模块2 adapter
     */
    private RvFuntionOtherItemAdapter rvFuntionOtherItemAdapter2;

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
     * @param fan
     */
    public void fanNotify(AbsFan fan){
        rvFuntionMainItemAdapter.fanNotify(fan);
        rvFuntionOtherItemAdapter.fanNotify(fan);
        rvFuntionOtherItemAdapter2.fanNotify(fan);
    }



    public RvFuntionAdapter() {
        super(R.layout.item_funtion);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, List<DeviceConfigurationFunctions> item) {
        int itemPosition = getItemPosition(item);
        if (itemPosition == 0){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new GridLayoutManager(getContext() , item.size()));
             rvFuntionMainItemAdapter = new RvFuntionMainItemAdapter();
            rvFunc.setAdapter(rvFuntionMainItemAdapter);
            rvFuntionMainItemAdapter.setNewInstance(item);
            rvFuntionMainItemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvFuntionMainItemAdapter.getItem(i);
                    if (funtionOnClickListener != null){
                        funtionOnClickListener.onClick(func);

                    }
                }
            });
        }else if (itemPosition == 1){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
             rvFuntionOtherItemAdapter = new RvFuntionOtherItemAdapter();
            rvFunc.setAdapter(rvFuntionOtherItemAdapter);
            rvFuntionOtherItemAdapter.setNewInstance(item);
            rvFuntionOtherItemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvFuntionOtherItemAdapter.getItem(i);
                    if (funtionOnClickListener != null){
                        funtionOnClickListener.onClick(func);
                    }
                }
            });
        }else if (itemPosition == 2){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
             rvFuntionOtherItemAdapter2 = new RvFuntionOtherItemAdapter();
            rvFunc.setAdapter(rvFuntionOtherItemAdapter2);
            rvFuntionOtherItemAdapter2.setNewInstance(item);
            rvFuntionOtherItemAdapter2.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvFuntionOtherItemAdapter2.getItem(i);
                    if (funtionOnClickListener != null){
                        funtionOnClickListener.onClick(func);
                    }
                }
            });
        }
    }


}
