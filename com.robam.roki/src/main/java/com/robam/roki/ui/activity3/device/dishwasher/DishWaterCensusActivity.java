package com.robam.roki.ui.activity3.device.dishwasher;

import android.os.Message;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.io.cloud.CloudHelper;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.PayloadBean;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.fan.AbsFan;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.roki.ui.activity3.device.base.other.VerticalItemDecoration;
import com.robam.roki.ui.activity3.device.fan.adapter.RvConSuctionAdapter;

/**
 * author : huxw
 * time   : 2022/06/13
 * desc   : 节水统计
 */
public final class DishWaterCensusActivity extends DeviceBaseFuntionActivity {
    /**
     * 洗碗机
     */
    private AbsDishWasher dishWasher ;
    /**
     * 累计节约水量
     */
    private TextView tvWaterVol;
    /**
     * 换算水量
     */
    private TextView tvWaterVol2;
    /**
     * 节约次数
     */
    private TextView tvNum;
    /**
     * 日期
     */
    private TextView tvDate;
    /**
     * 最后一次节水量
     */
    private TextView tvWaterVol3;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_dish_water_census;
    }

    @Override
    protected void initView() {

        tvWaterVol = findViewById(R.id.tv_water_vol);
        tvWaterVol2 = findViewById(R.id.tv_water_vol_2);
        tvNum = findViewById(R.id.tv_num);
        tvDate = findViewById(R.id.tv_date);
        tvWaterVol3 = findViewById(R.id.tv_water_vol_3);
    }

    /**
     * 显示数据
     */
    @Override
    protected void dealData() {
        //设置title
        setTitle(deviceConfigurationFunction.functionName);

        getHistoryData(mGuid);
    }


    private void getHistoryData(String guid) {
        CloudHelper.getHistroyData(guid, new Callback<Reponses.GetHistoryDataResponse>() {
            @Override
            public void onSuccess(Reponses.GetHistoryDataResponse getHistoryDataResponse) {

                try {
                    PayloadBean payload = getHistoryDataResponse.payload;
                    tvWaterVol.setText(payload.totalWaterSaved +"");
                    tvWaterVol2.setText("≈" + payload.totalWaterSaved / 0.5 +"瓶水");
                    tvNum.setText(payload.totalWaterUsed +"次");
                    tvWaterVol3.setText(payload.lastWaterCost +"L");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}