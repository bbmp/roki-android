package com.robam.roki.ui.activity3.device.dishwasher;

import com.legent.utils.api.PreferenceUtils;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.DeviceBaseFuntionActivity;
import com.robam.widget.view.SwitchButton;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/29
 *     desc   : 洗碗机耗材提醒通知
 *     version: 1.0
 * </pre>
 */
public class DishConsNotifiActivity extends DeviceBaseFuntionActivity {

    private SwitchButton sbNotifi1;
    private SwitchButton sbNotifi2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cons_notifi;
    }

    @Override
    protected void initView() {
        sbNotifi1 = findViewById(R.id.sb_notifi_1);
        sbNotifi2 = findViewById(R.id.sb_notifi_2);
        sbNotifi1.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean checked) {
                PreferenceUtils.setBool(DishWssherFunctionActivity.RINSE , checked);
            }
        });
        sbNotifi2.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton button, boolean checked) {
                PreferenceUtils.setBool(DishWssherFunctionActivity.SALF , checked);
            }
        });
    }

    @Override
    protected void dealData() {
        //设置title
        setTitle("耗材通知");

        sbNotifi1.setChecked(PreferenceUtils.getBool(DishWssherFunctionActivity.RINSE , true));
        sbNotifi2.setChecked(PreferenceUtils.getBool(DishWssherFunctionActivity.SALF , true));

    }



}
