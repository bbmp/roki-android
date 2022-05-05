package com.robam.roki.ui.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.ui.UIService;
import com.legent.ui.ext.BasePage;
import com.robam.common.events.FanStatusChangedEvent;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.PageArgumentKey;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Rent on 2016/6/28.
 */
public class DeviceFan8700OilPage extends BasePage {
    private AbsFan fan8700;
    private View contentView;

    @InjectView(R.id.fan8700_oil_btn_clean)//按钮
            Button fan8700_oil_btn_clean;
    @InjectView(R.id.fan8700_oil_status)//提示文字
            TextView fan8700_oil_status;
    @InjectView(R.id.fan8700_oil_circleimg)//图片
            ImageView fan8700_oil_circleimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        String guid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        fan8700 = Plat.deviceService.lookupChild(guid);
        Preconditions.checkNotNull(fan8700, "id is null");
        if (inflater == null)
            inflater = LayoutInflater.from(cx);
        contentView = inflater.inflate(R.layout.page_device_fan8700_oil, container, false);
        ButterKnife.inject(this, contentView);
        initView();
        return contentView;
    }

    private void initView() {
        initCleanMode();
    }

    /**
     * 返回
     */
    @OnClick(R.id.fan8700_oil_return)
    public void OnClickReturn() {
        UIService.getInstance().popBack();
    }

    /**
     * 清洗按钮
     */
    @OnClick(R.id.fan8700_oil_btn_clean)
    public void OnClickClean() {
        fan8700.restFanCleanTime(new VoidCallback() {
            @Override
            public void onSuccess() {
                fan8700_oil_circleimg.setImageResource(R.mipmap.img_fan8700oill_oilcircle_black);
                fan8700_oil_status.setText("烟机油网目前较干净");
                fan8700_oil_btn_clean.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @OnClick(R.id.fan8700_oil_buy)
    public void OnClickShop() {
        /*Uri uri = Uri.parse("http://wechat.robam.com/weixin/menubutton/10_wxsc");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);*/
    }

    /**
     * 干净状态模式
     */
    private void initCleanMode() {
        fan8700_oil_btn_clean.setVisibility(View.GONE);
        fan8700_oil_status.setText("烟机油网目前已经干净");
        fan8700_oil_circleimg.setImageResource(R.mipmap.img_fan8700oill_oilcircle_black);
    }

    /**
     * 需要清理状态模式
     */
    private void initNeedCleanMode() {
        fan8700_oil_btn_clean.setVisibility(View.VISIBLE);
        fan8700_oil_status.setText("烟机油网需要清洁");
        fan8700_oil_circleimg.setImageResource(R.mipmap.img_fan8700oill_oilcircle_red);
    }

    @Subscribe
    public void onEvent(FanStatusChangedEvent event) {
        if (fan8700 == null || !Objects.equal(fan8700.getID(), event.pojo.getID()))
            return;
        if (fan8700.clean)
            initNeedCleanMode();
        else
            initCleanMode();
    }
}