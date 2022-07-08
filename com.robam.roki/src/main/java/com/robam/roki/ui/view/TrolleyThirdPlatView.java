package com.robam.roki.ui.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.common.collect.Lists;
import com.legent.ui.UIService;
import com.legent.ui.ext.adapters.ExtBaseAdapter;
import com.legent.utils.api.DisplayUtils;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TrolleyThirdPlatView extends FrameLayout {

    @InjectView(R.id.gridView)
    GridView gridView;

    int imgHeight;

    public TrolleyThirdPlatView(Context context) {
        super(context);
        init(context, null);
    }

    public TrolleyThirdPlatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TrolleyThirdPlatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context cx, AttributeSet attrs) {
        View view = LayoutInflater.from(cx).inflate(R.layout.view_trolley_third_plat,
                this, true);
        if (!view.isInEditMode()) {
            ButterKnife.inject(this, view);
            computeHeight();

            Adapter adapter = new Adapter();
            gridView.setAdapter(adapter);
            adapter.loadData(getPlats());
        }
    }

    void computeHeight() {
        int screenWidth = DisplayUtils.getScreenWidthPixels(getContext());
        int imgWidth = (screenWidth - DisplayUtils.dip2px(getContext(), 11 * 2 + 5 * 2)) / 3;
        imgHeight = imgWidth * 71 / 190;
    }

    List<Plat> getPlats() {
        List<Plat> plats = Lists.newArrayList();
        plats.add(new Plat("中粮我买", R.mipmap.img_thirdplat_link_2, "www.womai.com"));
        plats.add(new Plat("味库", R.mipmap.img_thirdplat_link_4, "www.wecook.cn"));
        plats.add(new Plat("新味", R.mipmap.img_thirdplat_link_1, "www.xinweicook.com"));
        plats.add(new Plat("窝里快购", R.mipmap.img_thirdplat_link_3, "www.wlkgo.com"));
        plats.add(new Plat("下厨房", R.mipmap.img_thirdplat_link_5, "www.xiachufang.com"));

        return plats;
    }


    class Plat {
        int imgResid;
        String url;
        String title;

        public Plat(String title, int imgResid, String url) {
            this.title = title;
            this.imgResid = imgResid;
            this.url = url;
        }
    }

    class Adapter extends ExtBaseAdapter<Plat> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Plat plat = getEntity(position);
            ViewHolder vh = null;
            if (convertView == null) {
                ImageView img = getImage();
                vh = new ViewHolder(img);
                convertView = img;
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            vh.showData(plat);

            return convertView;
        }

        ImageView getImage() {
            ImageView img = new ImageView(getContext());
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);

            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, imgHeight);
            img.setLayoutParams(lp);

            return img;
        }

        class ViewHolder {

            ImageView img;

            public ViewHolder(ImageView view) {
                this.img = view;
                img.setTag(this);
            }

            void showData(final Plat plat) {
                img.setImageResource(plat.imgResid);
                img.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bd = new Bundle();
                        bd.putString(PageArgumentKey.WebTitle, plat.title);
                        bd.putString(PageArgumentKey.Url, plat.url);
                        UIService.getInstance().postPage(PageKey.WebClient, bd);
                    }
                });
            }
        }
    }


}
