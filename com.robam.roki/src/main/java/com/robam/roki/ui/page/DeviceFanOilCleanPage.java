//package com.robam.roki.ui.page;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.common.base.Objects;
//import com.google.common.base.Preconditions;
//import com.google.common.eventbus.Subscribe;
//import com.legent.VoidCallback;
//import com.legent.plat.Plat;
//import com.legent.ui.UIService;
//import com.legent.ui.ext.BasePage;
//import com.robam.common.events.FanStatusChangedEvent;
//import com.robam.common.pojos.device.fan.AbsFan;
//import com.robam.roki.R;
//import com.robam.roki.ui.PageArgumentKey;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import butterknife.OnClick;
//
//
///**
// * Created by Rent on 2016/6/28.
// */
//public class DeviceFanOilCleanPage extends BasePage {
//    private AbsFan fan8700;
//    private View contentView;
//
//    @InjectView(R.id.fanoil_oil_btn_clean)//按钮
//            Button fanoil_oil_btn_clean;
//    @InjectView(R.id.fanoil_oil_status)//提示文字
//            TextView fanoil_oil_status;
//    @InjectView(R.id.fanoil_oil_circleimg)//图片
//            ImageView fanoil_oil_circleimg;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Bundle bd = getArguments();
//        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
//        fan8700 = Plat.deviceService.lookupChild(guid);
//        Preconditions.checkNotNull(fan8700, "id is null");
//        if (inflater == null)
//            inflater = LayoutInflater.from(cx);
//        contentView = inflater.inflate(R.layout.page_device_fan_oil_clean, container, false);
//        ButterKnife.inject(this, contentView);
//        initListeren();
//        initView();
//        return contentView;
//    }
//
//    private void initView() {
//        initCleanMode();
//    }
//
//    private View view_return;
//    private View view_lock;
//
//    private void initListeren() {
//        if (contentView == null) return;
//        if (fanoil_oil_btn_clean != null) return;
//        fanoil_oil_btn_clean = contentView.findViewById(R.id.fanoil_oil_btn_clean);
//        fanoil_oil_status = contentView.findViewById(R.id.fanoil_oil_status);
//        fanoil_oil_circleimg = contentView.findViewById(R.id.fanoil_oil_circleimg);
//        //返回
//        view_return = contentView.findViewById(R.id.fanoil_oil_return);
//        view_return.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UIService.getInstance().popBack();
//            }
//        });
//        //清洗锁定
//        view_lock = contentView.findViewById(R.id.fan8700_oil_btn_clean);
//        view_lock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fan8700.restFanCleanTime(new VoidCallback() {
//                    @Override
//                    public void onSuccess() {
//                        fanoil_oil_circleimg.setImageResource(R.mipmap.img_fan8700oill_oilcircle_black);
//                        fanoil_oil_status.setText("烟机油网目前较干净");
//                        fanoil_oil_btn_clean.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//
//                    }
//                });
//            }
//        });
//
//    }
//
//    @OnClick(R.id.fanoil_oil_return)
//    public void OnClickReturn() {
//        UIService.getInstance().popBack();
//    }
//
//    /**
//     * 干净状态模式
//     */
//    private void initCleanMode() {
//        fanoil_oil_btn_clean.setVisibility(View.GONE);
//        fanoil_oil_status.setText("烟机油网目前已经干净");
//        fanoil_oil_circleimg.setImageResource(R.mipmap.img_fan8700oill_oilcircle_black);
//    }
//
//    /**
//     * 需要清理状态模式
//     */
//    private void initNeedCleanMode() {
//        fanoil_oil_btn_clean.setVisibility(View.VISIBLE);
//        fanoil_oil_status.setText("烟机油网需要清洁");
//        fanoil_oil_circleimg.setImageResource(R.mipmap.img_fan8700oill_oilcircle_red);
//    }
//
//    @Subscribe
//    public void onEvent(FanStatusChangedEvent event) {
//        if (fan8700 == null || !Objects.equal(fan8700.getID(), event.pojo.getID()))
//            return;
//        if (fan8700.clean)
//            initNeedCleanMode();
//        else
//            initCleanMode();
//    }
//}