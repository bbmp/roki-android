package com.robam.roki.ui.activity3.device.dishwasher.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.common.pojos.device.Pot.Pot;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 无人锅功能模块adapter
 */
public class RvDishWssherFuntionAdapter extends BaseQuickAdapter<List<DeviceConfigurationFunctions>, BaseViewHolder> {
    /**
     * 点击事件监听
     */
    FuntionOnClickListener funtionOnClickListener ;
    /**
     * 主功能区adapter
     */
    private RvDishWssherFuntionMainAdapter rvMainAdapter;
    /**
     * 模块1 adapter
     */
    private RvDishWssherFuntionOtherAdapter rvOtherAdapter;


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
     * @param dishWasher
     */
    public void potNotify(AbsDishWasher dishWasher){
        rvMainAdapter.potNotify(dishWasher);
        rvOtherAdapter.potNotify(dishWasher);
    }



    public RvDishWssherFuntionAdapter() {
        super(R.layout.item_funtion);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, List<DeviceConfigurationFunctions> item) {
        int itemPosition = getItemPosition(item);
        if (itemPosition == 0){
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new GridLayoutManager(getContext() , item.size()));
             rvMainAdapter = new RvDishWssherFuntionMainAdapter();
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
            RelativeLayout rlBg = holder.getView(R.id.rl_bg);
            rlBg.setBackgroundResource(R.color.transparent);
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
            rvFunc.addItemDecoration(new VerticalItemDecoration(16 , getContext()));
             rvOtherAdapter = new RvDishWssherFuntionOtherAdapter();
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
        }
    }


}
