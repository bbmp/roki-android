package com.robam.roki.ui.activity3.device.hidkit.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hjq.toast.ToastUtils;
import com.legent.VoidCallback;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.common.pojos.device.hidkit.AbsHidKit;
import com.robam.roki.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 藏宝盒功能模块adapter
 */
public class RvHidKitFuntionAdapter extends BaseQuickAdapter<List<DeviceConfigurationFunctions>, BaseViewHolder> implements SeekBar.OnSeekBarChangeListener {

    AbsHidKit hidKit;
    /**
     * 点击事件监听
     */
    FuntionOnClickListener funtionOnClickListener;

    /**
     * 模块1 adapter
     */
    private RvHidKitFuncOtherItemAdapter rvHidKitFuncOtherItemAdapter;
    /**
     * 模块2 adapter
     */
    private RvHidKitFuncOtherItemAdapter rvHidKitFuncOtherItemAdapter2;

    /**
     * 添加监听事件
     *
     * @param funtionOnClickListener
     */
    public void addFuntionOnClickListener(FuntionOnClickListener funtionOnClickListener) {
        this.funtionOnClickListener = funtionOnClickListener;
    }



    /**
     * 监听接口
     */
    public interface FuntionOnClickListener {
        void onClick(DeviceConfigurationFunctions func);
    }

    public void setHidKit(AbsHidKit hidKit) {
        this.hidKit = hidKit;
        notifyDataSetChanged();
    }

    public RvHidKitFuntionAdapter() {
        super(R.layout.item_hidkit_funtion);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, List<DeviceConfigurationFunctions> item) {
        int itemPosition = getItemPosition(item);
        if (itemPosition == 0) {
            holder.setVisible(R.id.rv_main_func, false);
            holder.setVisible(R.id.rl_voice, true);
            SeekBar seekBar = (SeekBar) holder.getView(R.id.seek_bar);
            seekBar.setMax(99);

            seekBar.setOnSeekBarChangeListener(this);
            if (hidKit == null){
                return;
            }
            if (!hidKit.isConnected()) {
                seekBar.setProgress(50);
            } else {
                seekBar.setProgress(hidKit.volume);
            }

        } else if (itemPosition == 1) {
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
            rvHidKitFuncOtherItemAdapter = new RvHidKitFuncOtherItemAdapter();
            rvFunc.setAdapter(rvHidKitFuncOtherItemAdapter);
            rvHidKitFuncOtherItemAdapter.setNewInstance(item);
            rvHidKitFuncOtherItemAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvHidKitFuncOtherItemAdapter.getItem(i);
                    if (funtionOnClickListener != null) {
                        funtionOnClickListener.onClick(func);
                    }
                }
            });
        } else if (itemPosition == 2) {
            RecyclerView rvFunc = holder.getView(R.id.rv_main_func);
            rvFunc.setLayoutManager(new LinearLayoutManager(getContext()));
            rvHidKitFuncOtherItemAdapter2 = new RvHidKitFuncOtherItemAdapter();
            rvFunc.setAdapter(rvHidKitFuncOtherItemAdapter2);
            rvHidKitFuncOtherItemAdapter2.setNewInstance(item);
            rvHidKitFuncOtherItemAdapter2.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter, @NonNull View view, int i) {
                    DeviceConfigurationFunctions func = rvHidKitFuncOtherItemAdapter2.getItem(i);
                    if (funtionOnClickListener != null) {
                        funtionOnClickListener.onClick(func);
                    }
                }
            });
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (!hidKit.isConnected()) {
//            .show(R.string.oven_dis_con);
            seekBar.setProgress(50);
            return;
        }
        hidKit.setHidKitStatusCombined((short) 1, (short) 1, (short) 1, (short)
                seekBar.getProgress(), new VoidCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show(R.string.device_volume_fader);
            }

            @Override
            public void onFailure(Throwable t) {
                ToastUtils.show(R.string.device_volume_fader_failed);
            }
        });
        seekBar.invalidate();
    }

}
