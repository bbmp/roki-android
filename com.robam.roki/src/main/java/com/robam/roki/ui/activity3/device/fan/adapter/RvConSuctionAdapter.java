package com.robam.roki.ui.activity3.device.fan.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.model.bean.DeviceFanStoveLinkage;
import com.robam.roki.model.bean.FanMainParams;
import com.robam.widget.view.SwitchButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author r210190
 * des 智感恒吸功能adapter
 */
public class RvConSuctionAdapter extends BaseQuickAdapter<DeviceConfigurationFunctions, BaseViewHolder> {

    private AbsFan fan ;

    /**
     * 更新烟机状态
     * @param absFan
     */
    public void fanNotify(AbsFan absFan){
        fan = absFan ;
        notifyDataSetChanged();
    }

    /**
     * checkBox监听
     */
    OnLinkageChangeLinstener onLinkageChangeLinstener ;
    /**
     * 添加checkBox监听
     */
    public void addOnLinkageChangeLinstener(OnLinkageChangeLinstener onLinkageChangeLinstener) {
        this.onLinkageChangeLinstener = onLinkageChangeLinstener;
    }
    /**
     * checkBox监听接口
     */
    public interface OnLinkageChangeLinstener{
        void linkChange( boolean checked , DeviceConfigurationFunctions func );
    }

    public RvConSuctionAdapter() {
        super(R.layout.item_fan_con_suction);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void convert(@NotNull BaseViewHolder holder, DeviceConfigurationFunctions item) {
        if(item != null){
            holder.setText(R.id.tv_func , item.functionName);
            SwitchButton sbLinkage = holder.getView(R.id.sb_linkage);
            if (fan != null){
                sbLinkage.setChecked(fan.cruise == 1);
            }

            //从功能中找到相关描述参数
            List<DeviceConfigurationFunctions> functions = item
                    .subView
                    .subViewModelMap
                    .subViewModelMapSubView
                    .deviceConfigurationFunctions;

            if (functions != null && functions.size() != 0) {
                String functionParams = functions.get(0).functionParams;
                try {
                    DeviceFanStoveLinkage fanStoveLinkage = JsonUtils.json2Pojo(functionParams, DeviceFanStoveLinkage.class);
                    String tips = fanStoveLinkage.getParam().getTips().getValue();
                    holder.setText(R.id.tv_desc , tips);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sbLinkage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onLinkageChangeLinstener != null){
                        onLinkageChangeLinstener.linkChange(sbLinkage.isChecked() , item );
                    }
                }
            });
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFan(AbsFan fan ){
        this.fan = fan ;
        notifyDataSetChanged();
    }
}
