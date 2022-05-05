package com.robam.roki.ui.page;

import android.os.Bundle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.roki.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by yinwei on 2016/12/20.
 */

public class DeviceWaterPurifiyInstall extends BasePage {
   @InjectView(R.id.waterpurifier_viewpager)
    ViewPager waterpurifier_viewpager;//viewpager对象
    //子View1中的返回控件
    ImageView water_purifiy_install_return1;
    //子View2中的返回控件
    ImageView  water_purifiy_install_return2;

    private View contentView;
    private ArrayList<View> pageViews;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView=inflater.inflate(R.layout.view_waterpurifier_lvxin_install,container,false);
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.page_waterpurifiy_install, null));
        pageViews.add(inflater.inflate(R.layout.page_waterpurifiy_install2,null));
        ButterKnife.inject(this,contentView);
         init();
        return contentView;
    }
    //初始化布局
    public void init() {
        waterpurifier_viewpager.setAdapter(new Guidadapter());
        water_purifiy_install_return1= pageViews.get(0).findViewById(R.id.water_purifiy_install_return1);
        water_purifiy_install_return1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(cx,"我可以返回",Toast.LENGTH_SHORT).show();
                UIService.getInstance().popBack();
            }
        });
        water_purifiy_install_return2= pageViews.get(1).findViewById(R.id.water_purifiy_install_return2);
        water_purifiy_install_return2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIService.getInstance().popBack();
            }
        });

    }
    class Guidadapter extends PagerAdapter {

        @Override
        public void destroyItem(View v, int position, Object object) {
            super.destroyItem(v, position, object);
            ((ViewPager) v).removeView(pageViews.get(position));
        }

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public Object instantiateItem(View v, int position) {
            ((ViewPager) v).addView(pageViews.get(position));
            return pageViews.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

    }

}
