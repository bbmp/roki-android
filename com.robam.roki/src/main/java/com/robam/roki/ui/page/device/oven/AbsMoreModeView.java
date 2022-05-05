package com.robam.roki.ui.page.device.oven;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.ui.adapter.OvenGridApater;
import com.robam.roki.ui.adapter.OvenMoreGridAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dell on 2018/7/18.
 */

public class AbsMoreModeView extends FrameLayout {
    Context cx;
    List<DeviceConfigurationFunctions> moreList;
    @InjectView(R.id.gv_show)
    MyGridView gvShow;

    public AbsMoreModeView(Context cx, List<DeviceConfigurationFunctions> moreList) {
        super(cx);
        this.cx = cx;
        this.moreList = moreList;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(cx).inflate(R.layout.oven_mygrid_show, this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
        }
        OvenMoreGridAdapter moreGridAdapter = new OvenMoreGridAdapter(cx, moreList);
        gvShow.setAdapter(moreGridAdapter);
        gvShow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(moreList.get(position).functionCode);
                if (moreViewClickLister!=null){
                    moreViewClickLister.onfirm(view,position);
                }
            }
        });
    }

    private MoreViewClick moreViewClickLister;

    public void setMoreViewClickLister(MoreViewClick moreViewClickLister){
        this.moreViewClickLister = moreViewClickLister;
    }


    public interface MoreViewClick{
        void onfirm(View view,int pos);
    }



}
