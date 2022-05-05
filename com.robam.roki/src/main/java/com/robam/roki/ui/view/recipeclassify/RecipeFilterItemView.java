package com.robam.roki.ui.view.recipeclassify;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.legent.utils.LogUtils;
import com.robam.roki.R;

import java.util.HashMap;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhoudingjun on 2016/12/30.
 */

public class RecipeFilterItemView extends FrameLayout {
    @InjectView(R.id.img_shaixuan)
    ImageView img_shaixuan;
    @InjectView(R.id.filtername)
    TextView filtername;
    @InjectView(R.id.rel_filter)
    RelativeLayout rel_filter;
    public boolean isClickble = false;

    public RecipeFilterItemView(Context context, String filterData,HashMap<String,Boolean> tempSet) {
        super(context);
        init(context, null, filterData,tempSet);
    }

    public RecipeFilterItemView(Context context, AttributeSet attrs, String filterData, HashMap<String,Boolean> tempSet) {
        super(context, attrs);
        init(context, attrs, filterData,tempSet);
    }

    public RecipeFilterItemView(Context context, AttributeSet attrs, int defStyle, String filterData,HashMap<String,Boolean> tempSet) {
        super(context, attrs, defStyle);
        init(context, attrs, filterData,tempSet);
    }

    public void init(Context context, AttributeSet attrs, String filterData,HashMap<String,Boolean> tempSet) {
        View view = LayoutInflater.from(context).inflate(R.layout.recipe_filter_item,
                this, true);

        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            filtername.setText(filterData);
            LogUtils.i("20170420","temset:"+tempSet.size());
          /*  Iterator it=tempSet.iterator();
            while(it.hasNext())
            {
                Object o=it.next();
                String s=o.toString();
                LogUtils.i("20170420","s:"+s.toString());
                if (filtername.getText().toString().equals(s)){
                    rel_filter.setBackgroundColor(Color.parseColor("#fbc8be"));
                    img_shaixuan.setVisibility(VISIBLE);
                    isClickble = true;
                }
            }*/
            Iterator it = tempSet.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                Boolean value = tempSet.get(key);
                LogUtils.i("20170508","key::"+key+" "+"value::"+value);
                if (filtername.getText().toString().equals(key) && value == true) {
                    rel_filter.setBackgroundColor(Color.parseColor("#fbc8be"));
                    img_shaixuan.setVisibility(VISIBLE);
                    isClickble=true;
                }
            }

        }


    }

    /**
     * 点击事件变化背景色
     */
    @OnClick(R.id.rel_filter)
    void OnClickrel_filter() {
        if (!isClickble) {
            rel_filter.setBackgroundColor(Color.parseColor("#fbc8be"));
            img_shaixuan.setVisibility(VISIBLE);
            isClickble = true;
        } else {
            rel_filter.setBackgroundColor(Color.parseColor("#e5ddd3"));
            img_shaixuan.setVisibility(GONE);
            isClickble = false;
        }

    }

    public void restoreFilterItemView() {
        rel_filter.setBackgroundColor(Color.parseColor("#e5ddd3"));
        img_shaixuan.setVisibility(GONE);
        isClickble = false;
    }


    public String getFilterValue() {
        LogUtils.i("20170303","filtername.getText().toString():"+filtername.getText().toString());
        return filtername.getText().toString();
    }
}
