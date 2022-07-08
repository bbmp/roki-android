package com.robam.roki.ui.activity3.recipedetail;



import android.app.SharedElementCallback;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.robam.base.BaseActivity;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.AppActivity;
import com.robam.roki.ui.adapter.CustomViewPager;
import com.robam.roki.ui.adapter.ImagePreviewAdapter;

import java.util.List;
import java.util.Map;

/**
 * 图片预览 Activity
 */
public class ImagePreviewActivity extends AppActivity {

    private int itemPosition;
    private List<String> imageList;
    private CustomViewPager viewPager;
    private LinearLayout main_linear;
    private boolean      mIsReturning;
    private int            mStartPosition;
    private int            mCurrentPosition;
    private ImagePreviewAdapter adapter;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_preview);
//        initView();
//        initShareElement();
//        getIntentData();
//
//        renderView();
//        getData();
//        setListener();
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.imageBrowseViewPager);
        main_linear = findViewById(R.id.main_linear);
    }

    private void initShareElement() {
        setEnterSharedElementCallback(mCallback);
    }
    private void setListener() {
        main_linear=findViewById(R.id.main_linear);
        main_linear.getChildAt(mCurrentPosition).setEnabled(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideAllIndicator(position);
                main_linear.getChildAt(position).setEnabled(true);
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);
                page.setScaleX(normalizedposition/2 + 0.5f);
                page.setScaleY(normalizedposition/2 + 0.5f);

            }
        });
    }
    private void  hideAllIndicator(int position){
        for(int i=0;i<imageList.size();i++){
            if(i!=position){
                main_linear.getChildAt(i).setEnabled(false);
            }
        }
    }


    @Override
    protected void initData() {
        initView();
        initShareElement();
        getIntentData();

        renderView();
        getData();
        setListener();
    }

    private void renderView() {
        if(imageList==null) return;
        if(imageList.size()==1){
            main_linear.setVisibility(View.GONE);
        }else {
            main_linear.setVisibility(View.VISIBLE);
        }
        adapter = new ImagePreviewAdapter(this,imageList,itemPosition);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mCurrentPosition);
    }

    private void getIntentData() {
        if(getIntent()!=null){
            mStartPosition = getIntent().getIntExtra(P.START_IAMGE_POSITION, 0);
            mCurrentPosition = mStartPosition;
            itemPosition = getIntent().getIntExtra(P.START_ITEM_POSITION, 0);
            imageList = getIntent().getStringArrayListExtra("imageList");
            //删除空的
            for (int i = 0; i < imageList.size(); i++) {
                if (TextUtils.isEmpty(imageList.get(i)))
                    imageList.remove(i);
            }
        }
    }

    /**
     * 获取数据
     */
    private void getData() {

        View view;
        for (String pic : imageList) {

            //创建底部指示器(小圆点)
            view = new View(ImagePreviewActivity.this);
            view.setBackgroundResource(R.drawable.indicator);
            view.setEnabled(false);
            //设置宽高
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            //设置间隔
            if (!pic.equals(imageList.get(0))) {
                layoutParams.leftMargin = 20;
            }
            //添加到LinearLayout
            main_linear.addView(view, layoutParams);
        }
    }

    public interface P {
        String START_ITEM_POSITION   = "start_item_position";//初始的Item位置
        String START_IAMGE_POSITION = "start_item_image_position"; //初始的图片位置

        String CURRENT_ITEM_POSITION   = "current_item_position";
        String CURRENT_IAMGE_POSITION = "current_item_image_position";
    }
    @Override
    public void finishAfterTransition() {
        mIsReturning = true;
        Intent data = new Intent();
        data.putExtra(P.START_IAMGE_POSITION, mStartPosition);
        data.putExtra(P.CURRENT_IAMGE_POSITION, mCurrentPosition);
        data.putExtra(P.CURRENT_ITEM_POSITION, itemPosition);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }


    private final SharedElementCallback mCallback = new SharedElementCallback() {

        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            if (mIsReturning) {
                ImageView sharedElement = adapter.getPhotoView();
                if (sharedElement == null) {
                    names.clear();
                    sharedElements.clear();
                } else if (mStartPosition != mCurrentPosition) {
                    names.clear();
                    names.add(sharedElement.getTransitionName());
                    sharedElements.clear();
                    sharedElements.put(sharedElement.getTransitionName(), sharedElement);
                }
            }
        }
    };
}

