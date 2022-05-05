package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;
import com.robam.roki.ui.view.wheelview.LoopView;

import java.util.List;


/**
 * Created by  2017/8/14.
 * 左上角按钮,右上角按钮，两个滑动滚轮，有内容描述，从底部弹出
 */
public class DialogType_Time extends BaseDialog {

    private TextView mTvCancel;
    private TextView mTvConfirm;
    private TextView mTvDesc;
    private LoopView mLoopViewFront;
    private LoopView mLoopViewRear;

    public DialogType_Time(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_time, null);
        mTvCancel = rootView.findViewById(R.id.tv_cancel);
        mTvConfirm = rootView.findViewById(R.id.tv_confirm);
        mTvDesc = rootView.findViewById(R.id.tv_desc);
        mLoopViewFront = rootView.findViewById(R.id.wheel_view_front);
        mLoopViewRear = rootView.findViewById(R.id.wheel_view_rear);
        createDialog(rootView);
    }

    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 0);
        }
    }

    public LoopView getmLoopViewFront() {
        return mLoopViewFront;
    }

    public LoopView getmLoopViewRear() {
        return mLoopViewRear;
    }

    public void setDest(String dest) {
        if(mTvDesc != null) {
            mTvDesc.setText(dest);
        }
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvConfirm.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mTvConfirm.setText(text);
        setOnOkClickListener(okOnClickListener);
    }



    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 List<DeviceConfigurationFunctions> listFunctions, boolean isLoop,
                                 int froneIndex, int centerIndex, int rearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {
        if (!isLoop) {
            mLoopViewFront.setNotLoop();
            mLoopViewRear.setNotLoop();
        }
        mLoopViewFront.setItems(listFrnot);
        mLoopViewRear.setItems(listRear);
        mLoopViewFront.setInitPosition(froneIndex);
        mLoopViewFront.setCurrentPosition(froneIndex);
        mLoopViewRear.setInitPosition(rearIndex);
        mLoopViewRear.setCurrentPosition(rearIndex);
        setmOnItemSelectedListenerFrone(onItemSelectedListenerFrone);
        setmOnItemSelectedListenerRear(onItemSelectedListenerRear);
        if (listFunctions != null && listFunctions.size() != 0) {
            mListFunctions = listFunctions;
            mTvDesc.setText(mListFunctions.get(0).msg);
        }
    }
    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 String desc, boolean isLoop, int froneIndex, int centerIndex,
                                 int rearIndex, OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {
        if (!isLoop) {
            mLoopViewFront.setNotLoop();
            mLoopViewRear.setNotLoop();
        }
        mLoopViewFront.setItems(listFrnot);
        mLoopViewRear.setItems(listRear);
        mLoopViewFront.setInitPosition(froneIndex);
        mLoopViewFront.setCurrentPosition(froneIndex);
        mLoopViewRear.setInitPosition(rearIndex);
        mLoopViewRear.setCurrentPosition(rearIndex);
        setmOnItemSelectedListenerFrone(onItemSelectedListenerFrone);
        setmOnItemSelectedListenerRear(onItemSelectedListenerRear);
        if (!"".equals(desc)) {
            mTvDesc.setText(desc);
        }


    }


    @Override
    public void bindAllListeners() {

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelClick(v);
            }
        });

        mTvConfirm.setOnClickListener(new View.OnClickListener() {
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

}
