package com.robam.roki.ui.page;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.legent.plat.constant.IAppType;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.adapters.ExtPageAdapter;
import com.legent.utils.api.NetworkUtils;
import com.legent.utils.graphic.ImageUtils;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.services.StoreService;
import com.robam.roki.R;

import com.robam.roki.ui.form.WizardActivity;
import com.robam.roki.ui.page.login.MyBasePage;
import com.robam.roki.ui.view.WizardView;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WizardPage extends MyBasePage<WizardActivity> {

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.indicator)
    CirclePageIndicator indicator;
    private String[] images;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.page_wizard, container, false);
        ButterKnife.inject(this, view);
        boolean connect = NetworkUtils.isConnect(cx);
        if (connect){
            inidData();
        } else {
            ExtPageAdapter adapter = new ExtPageAdapter();
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
            List<View> views = buildViews();
            adapter.loadViews(views);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (images != null){
                    if (position == images.length -1){
                        indicator.setVisibility(View.GONE);
                    }else {
                        indicator.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.page_wizard;
    }

    @Override
    protected void initView() {
        boolean connect = NetworkUtils.isConnect(cx);
        if (connect){
            inidData();
        } else {
            ExtPageAdapter adapter = new ExtPageAdapter();
            pager.setAdapter(adapter);
            indicator.setViewPager(pager);
            List<View> views = buildViews();
            adapter.loadViews(views);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length -1){
                    indicator.setVisibility(View.GONE);
                }else {
                    indicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    private void inidData() {
        StoreService.getInstance().getAppStartImages(IAppType.RKDRD, new Callback<Reponses.AppStartImgResponses>() {

            @Override
            public void onResponse(Call<Reponses.AppStartImgResponses> call, Response<Reponses.AppStartImgResponses> response) {
                Reponses.AppStartImgResponses appStartImgResponses = response.body();
                if (null != appStartImgResponses && null != appStartImgResponses.images) {
                    images = appStartImgResponses.images;
                    List<View> views = buildViews(images);
                    ExtPageAdapter adapter = new ExtPageAdapter();
                    pager.setAdapter(adapter);
                    indicator.setViewPager(pager);
                    adapter.loadViews(views);
                }
            }

            @Override
            public void onFailure(Call<Reponses.AppStartImgResponses> call, Throwable throwable) {

            }
        });
    }

    List<View> buildViews(String[] images) {

        List<View> views = new ArrayList<View>();
        WizardView wizardView = new WizardView(cx, images[images.length - 1] ,activity);
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(cx);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            ImageUtils.displayImage(cx, images[i], imageView);
            if (images.length - 1 == i) {
                views.add(wizardView);
            } else {
                views.add(imageView);
            }
        }

        return views;
    }

    List<View> buildViews() {
        List<View> views = new ArrayList<View>();
        ImageView view1 = new ImageView(cx);
        ImageView view2 = new ImageView(cx);
        WizardView view3 = new WizardView(cx,activity);

        view1.setScaleType(ScaleType.CENTER_CROP);
        view2.setScaleType(ScaleType.CENTER_CROP);

        ImageUtils.displayImage(cx, R.mipmap.img_wizard_1, view1);
        ImageUtils.displayImage(cx, R.mipmap.img_wizard_2, view2);

        views.add(view1);
        views.add(view2);
        views.add(view3);

        return views;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

   /* @OnClick(R.id.txtLogin)
    public void onTxtLoginClicked() {
        UserActivity.start((Activity) getContext());
    }

    @OnClick(R.id.txtStroll)
    public void onTxtStrollClicked() {
        MainActivity.start((Activity) getContext());
    }*/
}
