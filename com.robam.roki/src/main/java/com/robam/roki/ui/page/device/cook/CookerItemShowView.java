package com.robam.roki.ui.page.device.cook;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.api.ToastUtils;
import com.robam.roki.R;

import java.util.List;

/**
 * Created by Dell on 2018/7/18.
 */

public class CookerItemShowView extends FrameLayout{
    Context cx;
    List<DeviceConfigurationFunctions> paramList;
    public CookerItemShowView(Context context,List<DeviceConfigurationFunctions> paramList) {
        super(context);
        this.cx = context;
        this.paramList = paramList;
        initView();
    }

    public CookerItemShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.cx = context;
        initView();
    }

    public interface ItemOnShowClick{
        void onfirm(View v,int pos);
    }

    private ItemOnShowClick itemOnShowClickLister;

    protected void  setItemOnShowClick(ItemOnShowClick itemOnShowClick){
        this.itemOnShowClickLister = itemOnShowClick;
    }

    private void initView() {
        View viewItem = LayoutInflater.from(cx).inflate(R.layout.abs_device_cooker_item_show, this, true);
        CookItemDisplayView itemShow = viewItem.findViewById(R.id.item_show);
        itemShow.onRefresh(cx, paramList);
        itemShow.setOnRecycleItemClick(new CookItemDisplayView.OnRecycleItemClick() {
            @Override
            public void itemClick(View view, int position) {
                //ToastUtils.show("" + position, Toast.LENGTH_SHORT);
                if (itemOnShowClickLister!=null){
                    itemOnShowClickLister.onfirm(view,position);
                }
                //点击处理事件
            }
        });
    }
}
