package com.robam.roki.ui.page.device.cook;

import static com.blankj.utilcode.util.ViewUtils.runOnUiThread;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.hjq.toast.ToastUtils;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.pojos.RCReponse;
import com.legent.plat.pojos.device.IDevice;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.legent.ui.ext.dialogs.ProgressDialogHelper;
import com.legent.utils.EventUtils;
import com.robam.common.events.HomeRecipeViewEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.io.cloud.Reponses;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.PayLoadCookBook;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOne;
import com.robam.common.pojos.device.steameovenone.SteamOvenOnePowerOnStatus;
import com.robam.common.pojos.device.steameovenone.SteamOvenOneWorkStatus;
import com.robam.common.services.CookbookManager;
import com.robam.roki.R;
import com.robam.roki.model.bean.LineChartDataBean;
import com.robam.roki.ui.page.device.steamovenone.CurveCookBooksHView;
import com.robam.roki.ui.page.device.steamovenone.CurveCookBooksVView;
import com.robam.roki.ui.view.LineChartMarkView;
import com.robam.roki.ui.view.recipeclassify.GlideImageLoader;
import com.robam.roki.utils.chart.ChartDataReviseUtil;
import com.robam.roki.utils.chart.DynamicLineChartManager;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 烹饪曲线应用
 */

public class CurveCookbooksUserPage extends BasePage {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_device_model_name)
    TextView tvDeviceModelName;
    @InjectView(R.id.recipethemebanner)
    Banner banner;
    @InjectView(R.id.tv_voice_long)
    TextView tvVoiceLong;
    @InjectView(R.id.ll_voice_long)
    LinearLayout llVoiceLong;
    @InjectView(R.id.cook_chart)
    LineChart cookChart;
    @InjectView(R.id.btn_suspend)
    Button btnSuspend;
    @InjectView(R.id.btn_finish)
    Button btnFinish;
    @InjectView(R.id.img_screen)
    ImageView img_screen;
    @InjectView(R.id.contain)
    FrameLayout contain;

    private PayLoadCookBook mPayLoadCookBook;
    private DynamicLineChartManager dm;
    private final int GET_LAND_PARAMS = 888;// 横屏
    private final int GET_PORT_PARAMS = 999; // 竖屏

    private CurveCookBooksHView curveCookBooksHView;
    private CurveCookBooksVView curveCookBooksVView;
    Handler mHandler = new MyHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_LAND_PARAMS:
                    ToastUtils.show("我要换横屏");
//                    ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//                    contain.getChildAt(0).setVisibility(View.INVISIBLE);
//                    contain.getChildAt(1).setVisibility(View.VISIBLE);

                    break;
                case GET_PORT_PARAMS:
                    ToastUtils.show("我要换竖屏");
//                    ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

//                    contain.getChildAt(0).setVisibility(View.VISIBLE);
//                    contain.getChildAt(1).setVisibility(View.INVISIBLE);

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mPayLoadCookBook = bd == null ? null : (PayLoadCookBook) bd.getSerializable("Item");
        View view = inflater.inflate(R.layout.curve_cook_books_user_page_contain, container, false);
        ButterKnife.inject(this, view);
        dm = new DynamicLineChartManager(cookChart, cx);
        initData();
        initView();
        return view;
    }

    private void initData() {
//        CookbookManager.getInstance().cookingCurveSave(String deviceCategoryCode, String deviceGuid, String devicePlatformCode, String deviceTypeCode, int id, String mode, String modeName, String setTemp, String setTime,
//                new VoidCallback() {
//            @Override
//            public void onSuccess() {
//                ProgressDialogHelper.setRunning(cx, false);
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                ProgressDialogHelper.setRunning(cx, false);
//                com.legent.utils.api.ToastUtils.showThrowable(t);
//            }                UIService.getInstance().postPage(PageKey.AbsSteamOvenWorkingCurve925, bd);
//        });

    }

    private void initView() {
        curveCookBooksHView=new CurveCookBooksHView(cx,mPayLoadCookBook);
        contain.addView(curveCookBooksHView);
        curveCookBooksHView.setOnClickChangePageLister(
                new CurveCookBooksHView.OnClickChangePage() {
                    @Override
                    public void onclickChange(int VIndex) {
                        ToastUtils.show("1111111111111");
//                        mHandler.sendEmptyMessage(GET_PORT_PARAMS);
                        ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        contain.getChildAt(0).setVisibility(View.INVISIBLE);
                        contain.getChildAt(1).setVisibility(View.VISIBLE);

                    }
                }
        );
        curveCookBooksVView=new CurveCookBooksVView(cx,mPayLoadCookBook);
        contain.addView(curveCookBooksVView);
        curveCookBooksVView.setOnClickChangePageLister(new CurveCookBooksVView.OnClickChangePage() {
            @Override
            public void onclickChange(int VIndex) {
                ToastUtils.show("2222222");
//                mHandler.sendEmptyMessage(GET_LAND_PARAMS);
                ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                contain.getChildAt(0).setVisibility(View.VISIBLE);
                contain.getChildAt(1).setVisibility(View.INVISIBLE);
            }

        });


//        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_easy), entryList, false);
//        dm.initLineDataSet("烹饪曲线", getResources().getColor(R.color.line_chart_deep), drawList, true);
//
//        singleLine.add(new Entry(10, 0));
//        singleLine.add(new Entry(10, 150));
    }

    List<Entry> singleLine = new ArrayList<>();

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                UIService.getInstance().popBack();
                break;
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        ((Activity) cx).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {

        //预热   工作
        if ((event.pojo.workState == SteamOvenOneWorkStatus.PreHeat &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus) || (event.pojo.workState == SteamOvenOneWorkStatus.Working &&
                event.pojo.powerOnStatus == SteamOvenOnePowerOnStatus.WorkingStatus)) {
//            steameOven = (AbsSteameOvenOne) event.pojo;
//            new Entry(dataBeanList.get(i).xValue, dataBeanList.get(i).yValue)
            curveCookBooksHView.drawPoint((AbsSteameOvenOne) event.pojo);
            curveCookBooksVView.drawPoint((AbsSteameOvenOne) event.pojo);
        }
//        curveCookBooksHView.drawPoint((AbsSteameOvenOne) event.pojo);
//        curveCookBooksVView.drawPoint((AbsSteameOvenOne) event.pojo);
    }


}
