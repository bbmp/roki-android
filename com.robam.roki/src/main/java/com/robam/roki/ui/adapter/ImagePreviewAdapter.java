package com.robam.roki.ui.adapter;

import static com.robam.roki.ui.view.nineview.NineCookedViewKt.getNameByPosition;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.PagerAdapter;


import com.bm.library.PhotoView;
import com.robam.roki.R;
import com.robam.roki.ui.extension.GlideApp;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * @Description:
 * @Author: Liangchaojie
 * @Create On 2018/3/30 10:33
 */
public class ImagePreviewAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageList;
    private int itemPosition;
    private PhotoView photoView;
    public ImagePreviewAdapter(Context context, List<String> imageList, int itemPosition) {
        this.context = context;
        this.imageList = imageList;
        this.itemPosition = itemPosition;
    }

    @Override
    public int getCount() {
        return imageList==null?0:imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imageview, null);
        final PhotoView photoView = view.findViewById(R.id.photo_view);
//        photoView.ise();
//        Picasso.with(context).load(imageList.get(position)).into(photoView);
        GlideApp.with(context).load(imageList.get(position)).into(photoView);


        photoView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               photoView.setEnabled(false);
               if (context!=null)
               ((Activity)context).onBackPressed();
           }
       });
        if(photoView.getParent()!=null){
            ((RelativeLayout)photoView.getParent()).removeAllViews();
        }
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        photoView = (PhotoView) object;
        photoView.setTag(getNameByPosition(itemPosition,position));
        photoView.setTransitionName(getNameByPosition(itemPosition,position));
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public PhotoView getPhotoView() {
        return photoView;
    }

}
