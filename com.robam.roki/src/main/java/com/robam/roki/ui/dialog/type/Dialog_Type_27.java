package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.adapter.AbsSteamProfessionalAdapter;
import com.robam.roki.ui.adapter.DeviceModelAdapter;
import com.robam.roki.ui.adapter3.Rv610ModeAdapter;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;
import com.robam.roki.ui.page.device.oven.MyGridView;

import java.util.List;

/**
 * 类型27 Dialog
 * 上半部分模式列表 下半部分2个wheel（温度和时间）
 */

public class Dialog_Type_27 extends BaseDialog {
    private TextView mCancelTv;
    private TextView mOkTv;
    private TextView tvTopTemp;
    private View rootView;
    private RecyclerView myGridView;
    private List<DeviceConfigurationFunctions> deviceConfigurationFunctionsList;
    public Rv610ModeAdapter mDeviceModelAdapter;
    private AbsSteamProfessionalAdapter modeGridAdapter;
    private OnItemSelectOkListener onItemSelectOkListener ;
    private LinearLayout ll_temp_2;
    private TextView tv_temp_2;
    private TextView tv_unit1;

    public Dialog_Type_27(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctions) {
        super(context);
        this.deviceConfigurationFunctionsList = deviceConfigurationFunctions;
//        modeGridAdapter = new AbsSteamProfessionalAdapter(deviceConfigurationFunctions, context);
//        mDeviceModelAdapter = new DeviceModelAdapter(context, deviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
////                modelSelectItemEvent(view);
//            }
//        });
        mDeviceModelAdapter.addData(deviceConfigurationFunctions);
    }
    public Dialog_Type_27(Context context, List<DeviceConfigurationFunctions> deviceConfigurationFunctions,OnItemClickListener onItemClickListener) {
        super(context);
        this.deviceConfigurationFunctionsList = deviceConfigurationFunctions;
        modeGridAdapter = new AbsSteamProfessionalAdapter(deviceConfigurationFunctions, context);
        mDeviceModelAdapter.addData(deviceConfigurationFunctions);
        mDeviceModelAdapter.setOnItemClickListener(onItemClickListener);
    }
    @Override
    public void initDialog() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_27, null);
        mCancelTv = rootView.findViewById(R.id.tv_cancel);
        mOkTv = rootView.findViewById(R.id.tv_confirm);
        mLoopViewFront = rootView.findViewById(R.id.wheel_view_front);
        ll_temp_2 = rootView.findViewById(R.id.ll_temp_2);
        tv_temp_2 = rootView.findViewById(R.id.tv_temp_2);
        tvTopTemp = rootView.findViewById(R.id.tv_top_temp);
        tv_unit1 = rootView.findViewById(R.id.tv_unit1);
        mLoopViewCenter = rootView.findViewById(R.id.wheel_view_front_2);
        mLoopViewRear = rootView.findViewById(R.id.wheel_view_rear);
        mLoopViewFront.setDividerColor(Color.parseColor("#e1e1e1"));
        mLoopViewRear.setDividerColor(Color.parseColor("#e1e1e1"));
        mLoopViewCenter.setDividerColor(Color.parseColor("#e1e1e1"));
        myGridView = rootView.findViewById(R.id.mode_list);
        myGridView.setLayoutManager(new LinearLayoutManager(mContext ,   RecyclerView.HORIZONTAL , false));
        createDialog(rootView);
        mDeviceModelAdapter = new Rv610ModeAdapter();
        myGridView.setAdapter(mDeviceModelAdapter);
    }

    @Override
    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void bindAllListeners() {
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOkClick(v);
            }
        });

        mLoopViewFront.setListenerFrone(new OnItemSelectedListenerFrone() {
            @Override
            public void onItemSelectedFront(String contentFront) {
                onTouchWheelSelectedFrone(contentFront);
            }
        });

        mLoopViewRear.setListenerRear(new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                onTouchWheelSelectedRear(contentRear);
            }
        });
    }

    public void setGridClickListener (AdapterView.OnItemClickListener listener) {
//        myGridView.setOnItemClickListener(listener);
    }

    public void set2Visible(boolean visible){
        if (ll_temp_2 != null){
            ll_temp_2.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        if (tvTopTemp != null){
            tvTopTemp.setText(visible ? "上管温度" : "温度");
        }
    }
    public void setVisible(boolean visible){
        if (ll_temp_2 != null){
            ll_temp_2.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        if (tvTopTemp != null){
            tvTopTemp.setText(visible ? "蒸汽量" : "温度");
            tv_temp_2.setText("温度");
            tv_unit1.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mOkTv.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    public void setOkBtn(int textId,   OnItemSelectOkListener onItemSelectOk) {
        mOkTv.setText(textId);
        this.onItemSelectOkListener = onItemSelectOk ;
//        setOnOkClickListener(okOnClickListener);
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemSelectOkListener != null){
                    onItemSelectOkListener.onOkListener(mLoopViewFront.getItemsContent(mLoopViewFront.getSelectedItem())
                            , mLoopViewRear.getItemsContent(mLoopViewRear.getSelectedItem()) ,
                                mLoopViewCenter != null ? mLoopViewCenter.getItemsContent(mLoopViewCenter.getSelectedItem()) : "0");
                }
            }
        });
    }
    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mCancelTv.setText(text);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 String desc, boolean isLoop, int froneIndex, int centerIndex, int rearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear){
        if (!isLoop){
            mLoopViewFront.setNotLoop();
            mLoopViewRear.setNotLoop();
        }
        mLoopViewFront.setItems(listFrnot);
        mLoopViewCenter.setItems(listCenter);
        mLoopViewRear.setItems(listRear);
        mLoopViewFront.setInitPosition(froneIndex);
        mLoopViewFront.setCurrentPosition(froneIndex);
        mLoopViewRear.setInitPosition(rearIndex);
        mLoopViewRear.setCurrentPosition(rearIndex);
        mLoopViewCenter.setInitPosition(centerIndex);
        mLoopViewCenter.setCurrentPosition(centerIndex);
        setmOnItemSelectedListenerFrone(onItemSelectedListenerFrone);
        setmOnItemSelectedListenerRear(onItemSelectedListenerRear);
    }

    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                          List<DeviceConfigurationFunctions> listFunctions, boolean isLoop,
                          int froneIndex, int centerIndex, int rearIndex,
                          OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                          OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                          OnItemSelectedListenerRear onItemSelectedListenerRear){
        if (!isLoop){
            mLoopViewFront.setNotLoop();
            mLoopViewRear.setNotLoop();
        }
        mLoopViewFront.setItems(listFrnot);
        if (mLoopViewCenter != null ){
            mLoopViewRear.setItems(listCenter);
        }
        mLoopViewRear.setItems(listRear);
        mLoopViewFront.setInitPosition(froneIndex);
        mLoopViewFront.setCurrentPosition(froneIndex);
        mLoopViewRear.setInitPosition(rearIndex);
        mLoopViewRear.setCurrentPosition(rearIndex);
        setmOnItemSelectedListenerFrone(onItemSelectedListenerFrone);
        setmOnItemSelectedListenerRear(onItemSelectedListenerRear);
    }


    public interface OnItemSelectOkListener{
        void onOkListener(String temperature, String time ,String temp2);
    }


}
