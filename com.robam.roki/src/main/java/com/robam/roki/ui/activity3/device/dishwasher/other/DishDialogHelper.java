package com.robam.roki.ui.activity3.device.dishwasher.other;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.JsonUtils;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.base.other.DialogUtils;
import com.robam.roki.ui.activity3.device.dishwasher.adapter.RvDishParamPickerAdapter;
import com.robam.roki.ui.activity3.device.dishwasher.bean.ParamBean;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuncSelectAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.mdialog.PickerLayoutManager;

import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/07/05
 *     desc   : 洗碗机相关dialog
 *     version: 1.0
 * </pre>
 */
public class DishDialogHelper extends DialogUtils {
    public DishDialogHelper(Activity activity) {
        super(activity);
    }

    /**
     * 自动换气
     */
    public void autoVen(OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_dis_auto_ven);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());

        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {

                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    onSelectListener.onReset();
                    dialog.dismiss();
                }
            }
        }, R.id.btn_cancel, R.id.btn_complete);
    }

    /**
     * 首页辅助选择
     *
     * @param funcs
     */
    public void funcSelectDialog(DeviceConfigurationFunctions funcs, OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_device_ventilation);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        TextView tvDesc = (TextView) dialog.findViewById(R.id.tv_desc);
        TextView tv_1 = (TextView) dialog.findViewById(R.id.tv_1);
        tv_1.setText("辅助功能");
        RecyclerView rvDeviceFun = (RecyclerView) dialog.findViewById(R.id.rv_device_func);
        RvDishParamPickerAdapter rvDishParamPickerAdapter = new RvDishParamPickerAdapter(activity);
        PickerLayoutManager manager = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvDishParamPickerAdapter.setIndex(position);
                    }
                })
                .build();
        assert rvDeviceFun != null;
        rvDeviceFun.setLayoutManager(manager);
        rvDeviceFun.setAdapter(rvDishParamPickerAdapter);
        ParamBean paramBean = null;
        //设置数据
        try {
            String functionParams = funcs.functionParams;
             paramBean = JsonUtils.json2Pojo(functionParams, ParamBean.class);
            rvDishParamPickerAdapter.setData(paramBean.assist_func);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dialog.show();
        ParamBean finalParamBean = paramBean;
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                    ParamBean.AssistFuncDTO item = rvDishParamPickerAdapter.getItem(rvDishParamPickerAdapter.getIndex());
                    int downWsshSw = 0 ;
                    int autoVentiSw = 0 ;
                    int drySw = 0 ;
                    switch (item.functionName) {
                        case "under_wash" :
                            downWsshSw = 1 ;
                            break ;
                        case "assist_gzhq" :
                            autoVentiSw = 1 ;
                            break ;
                        case "assist_jqhg" :
                            drySw = 1 ;
                            break ;
                    }
                    onSelectListener.onSelectFunc(finalParamBean.model ,downWsshSw , autoVentiSw , drySw);
                }
            }
        }, R.id.btn_complete, R.id.btn_cancel);
    }

}
