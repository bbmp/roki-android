package com.robam.roki.ui.activity3.device.base.other;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.base.BaseDialog;
import com.robam.roki.R;
import com.robam.roki.ui.activity3.device.fan.adapter.RvFuncSelectAdapter;
import com.robam.roki.ui.activity3.device.fan.adapter.RvPickerAdapter;
import com.robam.roki.ui.activity3.device.fan.adapter.RvPickerStringAdapter;
import com.robam.roki.ui.extension.GlideApp;
import com.robam.roki.ui.mdialog.PickerLayoutManager;

import java.util.ArrayList;
import java.util.List;

import skin.support.utils.SkinPreference;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/13
 *     desc   : 设备相关utils
 *     version: 1.0
 * </pre>
 */
public class DialogUtils {

    public final BaseDialog dialog;
    public Activity activity ;
    /**
     * 添加时间选择监听
     */
    protected OnSelectListener onSelectListener ;



    public DialogUtils(Activity activity) {
        this.activity = activity;
        dialog = new BaseDialog(activity);
    }

    public interface OnSelectListener{
        /**
         * 返回分钟
         * @param data
         */
        default void onSelectTime(int data){};
        default void onSelectHourMinu(int hour , int minu){};
        default void onSelect(String data){};
        default void onSelectPosition(int index){};
        default void onSelectFunc(DeviceConfigurationFunctions func){};
        default void onReset(){};
        default void onSelectFunc(int workMode , int downWsshSw, int autoVentiSw, int drySw){};
    }

    /**
     * 分钟选择
     * @param tims
     */
    public void minSelectDialog( List<Integer> tims , int index ,OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_device_delayed_shut);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvDevice = (RecyclerView) dialog.findViewById(R.id.rv_device);
        RvPickerAdapter rvPickerAdapter = new RvPickerAdapter(activity);
        PickerLayoutManager manager = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvPickerAdapter.setIndex(position);
                    }
                })
                .build();
        assert rvDevice != null;
        rvDevice.setLayoutManager(manager);

        rvDevice.setAdapter(rvPickerAdapter);
        manager.scrollToPosition(index);
        rvPickerAdapter.setIndex(index);
        //设置数据
        rvPickerAdapter.setData(tims);
        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                    onSelectListener.onSelectTime(rvPickerAdapter.getItem(rvPickerAdapter.getIndex()));
                }
            }
        }, R.id.btn_complete, R.id.btn_cancel);
    }

    /**
     * 厨房净化通风换气
     * @param funcs
     */
    public void funcSelectDialog( List<DeviceConfigurationFunctions> funcs ,OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_device_ventilation);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        TextView tvDesc = (TextView) dialog.findViewById(R.id.tv_desc);
        RecyclerView rvDeviceFun = (RecyclerView) dialog.findViewById(R.id.rv_device_func);
        RvFuncSelectAdapter rvFuncSelectAdapter = new RvFuncSelectAdapter(activity);
        PickerLayoutManager manager = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvFuncSelectAdapter.setIndex(position);
                    }
                })
                .build();
        assert rvDeviceFun != null;
        rvDeviceFun.setLayoutManager(manager);
        rvDeviceFun.setAdapter(rvFuncSelectAdapter);
        //设置数据
        rvFuncSelectAdapter.setData(funcs);
        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                    onSelectListener.onSelectFunc(rvFuncSelectAdapter.getItem(rvFuncSelectAdapter.getIndex()));
                }
            }
        }, R.id.btn_complete, R.id.btn_cancel);
    }

    /**
     * 油网检测
     */
    public void checkUpOilNetDialog( boolean clean ,OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_device_oil_net_det);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        View tvReset = dialog.findViewById(R.id.tv_reset);
      TextView tvClear = dialog.findViewById(R.id.tv_clear);
      TextView tvCheckMessage =  dialog.findViewById(R.id.tv_check_message);
        ImageView ivGif = (ImageView) dialog.findViewById(R.id.iv_gif);
        GlideApp.with(ivGif)
                .asGif()
                .load(SkinPreference.getInstance().getSkinName().equals("night") ?
                        R.drawable.icon_youwang_night : R.drawable.icon_youwang)
                .into(ivGif);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvClear.setVisibility(View.VISIBLE);
                tvReset.setVisibility(View.VISIBLE);
                if (clean ){
                    GlideApp.with(ivGif)
                            .asGif()
                            .load(SkinPreference.getInstance().getSkinName().equals("night") ?
                                    R.drawable.icon_youwang_red_night : R.drawable.icon_youwang_red)
                            .into(ivGif);
                    tvClear.setText(R.string.fan_oil_detection_desc_bad);
                    tvCheckMessage.setText(R.string.fan_oil_detection_text_bad);
                }else {
                    tvClear.setText(R.string.fan_oil_detection_desc_good);
                    tvCheckMessage.setText(R.string.fan_oil_detection_text_good);
                }
            }
        } , 3000);

        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.tv_reset) {
                    //重置油网
                    onSelectListener.onReset();
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                }
            }
        }, R.id.tv_reset, R.id.btn_complete);
    }


    /**
     * 假日模式选择
     * @param data
     */
    public void holidayDialog( String funcName ,List<String> data , int index , OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_device_delayed_shut);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvDevice = (RecyclerView) dialog.findViewById(R.id.rv_device);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_1);
        tvTitle.setText(funcName);
        RvPickerStringAdapter rvPickerAdapter = new RvPickerStringAdapter(activity ,"");
        PickerLayoutManager manager = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvPickerAdapter.setIndex(position);
                    }
                })
                .build();
        assert rvDevice != null;
        rvDevice.setLayoutManager(manager);

        rvDevice.setAdapter(rvPickerAdapter);
        manager.scrollToPosition(index);
        rvPickerAdapter.setIndex(index);
        //设置数据
        rvPickerAdapter.setData(data);
        dialog.show();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                    onSelectListener.onSelectPosition(rvPickerAdapter.getIndex());
                }
            }
        }, R.id.btn_complete, R.id.btn_cancel);
    }


    /**
     * 小时时间选择（传入小时时间）
     * @param hours
     * @param minus
     * @param onSelectListener
     */
    public void hourMinuSelect( List<String> hours , List<String> minus , int indexHour , int indexMinus ,OnSelectListener onSelectListener) {
        dialog.setContentView(R.layout.dialog_device_hour_min);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvHours = (RecyclerView) dialog.findViewById(R.id.rv_hours);
        RecyclerView rvMinus = (RecyclerView) dialog.findViewById(R.id.rv_minus);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_1);
        RvPickerStringAdapter rvHoursAdapter = new RvPickerStringAdapter(activity , "h" );

        PickerLayoutManager manager = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvHoursAdapter.setIndex(position);
                    }
                })
                .build();
        rvHours.setLayoutManager(manager);
        rvHours.setAdapter(rvHoursAdapter);
        manager.scrollToPosition(indexHour);
        rvHoursAdapter.setIndex(indexHour);
        //设置小时数据
        rvHoursAdapter.setData(hours);

        RvPickerStringAdapter rvMinusAdapter = new RvPickerStringAdapter(activity  ,"min");
        PickerLayoutManager managerMinus = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvMinusAdapter.setIndex(position);
                    }
                })
                .build();
        rvMinus.setLayoutManager(managerMinus);
        rvMinus.setAdapter(rvMinusAdapter);
        managerMinus.scrollToPosition(indexMinus);
        rvMinusAdapter.setIndex(indexMinus);
        //设置数据
        rvMinusAdapter.setData(minus);

        dialog.show();

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                    String hour = rvHoursAdapter.getItem(rvHoursAdapter.getIndex());
                    String minu = rvMinusAdapter.getItem(rvMinusAdapter.getIndex());
                    int min = Integer.parseInt(hour) * 60 + Integer.parseInt(minu);
                    onSelectListener.onSelectTime(min);
                }
            }
        }, R.id.btn_complete, R.id.btn_cancel);
    }


    /**
     * 选中的时间
     * @param indexHour
     * @param indexMinus
     * @param onSelectListener
     */
    public void timeSelect(  int indexHour , int indexMinus ,OnSelectListener onSelectListener) {

        // 生产小时
        ArrayList<String> hourData = new ArrayList<>(24);
        for (int i = 0; i <= 23; i++) {
            hourData.add((i < 10 ? "0" : "") + i + "");
        }

        // 生产分钟
        ArrayList<String> minuteData = new ArrayList<>(60);
        for (int i = 0; i <= 59; i++) {
            minuteData.add((i < 10 ? "0" : "") + i + "");
        }
        dialog.setContentView(R.layout.dialog_device_date_select);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setGravity(Gravity.BOTTOM);
        dialog.setWidth(activity.getWindowManager().getDefaultDisplay().getWidth());
        RecyclerView rvHours = (RecyclerView) dialog.findViewById(R.id.rv_hours);
        RecyclerView rvMinus = (RecyclerView) dialog.findViewById(R.id.rv_minus);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_1);
        tvTitle.setText("时间选择");
        RvPickerStringAdapter rvHoursAdapter = new RvPickerStringAdapter(activity , "" );

        PickerLayoutManager manager = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvHoursAdapter.setIndex(position);
                    }
                })
                .build();
        rvHours.setLayoutManager(manager);
        rvHours.setAdapter(rvHoursAdapter);
        manager.scrollToPosition(indexHour);
        rvHoursAdapter.setIndex(indexHour);
        //设置小时数据
        rvHoursAdapter.setData(hourData);

        RvPickerStringAdapter rvMinusAdapter = new RvPickerStringAdapter(activity  ,"");
        PickerLayoutManager managerMinus = new PickerLayoutManager.Builder(activity)
                .setMaxItem(3)
                .setScale(0.5f)
                .setOnPickerListener(new PickerLayoutManager.OnPickerListener() {
                    @Override
                    public void onPicked(RecyclerView recyclerView, int position) {
                        Log.e("position", "---------" + position);
                        rvMinusAdapter.setIndex(position);
                    }
                })
                .build();
        rvMinus.setLayoutManager(managerMinus);
        rvMinus.setAdapter(rvMinusAdapter);
        managerMinus.scrollToPosition(indexMinus);
        rvMinusAdapter.setIndex(indexMinus);
        //设置数据
        rvMinusAdapter.setData(minuteData);

        dialog.show();

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_cancel) {
                    dialog.dismiss();
                } else if (view.getId() == R.id.btn_complete) {
                    dialog.dismiss();
                    String hour = rvHoursAdapter.getItem(rvHoursAdapter.getIndex());
                    String minu = rvMinusAdapter.getItem(rvMinusAdapter.getIndex());
                    int min = Integer.parseInt(hour) * 60 + Integer.parseInt(minu);
                    onSelectListener.onSelectHourMinu(Integer.parseInt(hour) ,  Integer.parseInt(minu));
                }
            }
        }, R.id.btn_complete, R.id.btn_cancel);
    }
}
