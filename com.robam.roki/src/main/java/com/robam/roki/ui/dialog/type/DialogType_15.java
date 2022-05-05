package com.robam.roki.ui.dialog.type;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.utils.LogUtils;
import com.robam.roki.R;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.dialog.BaseDialog;
import com.robam.roki.ui.dialog.CoreDialog;
import com.robam.roki.ui.view.wheelview.LoopView;

import java.util.List;


/**
 * Created by Administrator on 2017/8/14.
 * 左上角按钮,右上角按钮，中部一个滑动滚轮，从底部弹出
 */
public class DialogType_15 extends BaseDialog {


    private TextView mTvCancel;
    private TextView mTvConfirm;
    public TextView mTvDesc;

    public DialogType_15(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {

        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_15, null);
        mTvCancel = rootView.findViewById(R.id.tv_cancel);
        mTvConfirm = rootView.findViewById(R.id.tv_confirm);
        mTvDesc = rootView.findViewById(R.id.tv_desc);
        mLoopViewCenter = rootView.findViewById(R.id.wheel_view);
        createDialog(rootView);
    }

    @Override
    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.Dialog_Microwave_professtion_bottom, rootView, true);
            mDialog.setPosition(Gravity.BOTTOM, 0, 0);
        }
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
        mTvConfirm.setText(text);
        setOnOkClickListener(okOnClickListener);
    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
        mTvConfirm.setText(textId);
        setOnOkClickListener(okOnClickListener);
    }


    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 List<DeviceConfigurationFunctions> listFunctions, boolean isLoop, int froneIndex, int centerIndex, int rearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {
        if (!isLoop) {
            mLoopViewCenter.setNotLoop();
        }
        mLoopViewCenter.setItems(listCenter);
        mLoopViewCenter.setInitPosition(centerIndex);
        mLoopViewCenter.setCurrentPosition(centerIndex);
        setmOnItemSelectedListenerCenter(onItemSelectedListenerCenter);
        if (listFunctions != null && listFunctions.size() != 0){
            mListFunctions = listFunctions;
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

        mLoopViewCenter.setListenerCenter(new OnItemSelectedListenerCenter() {

            @Override
            public void onItemSelectedCenter(String contentCenter) {

                if (mListFunctions != null && mListFunctions.size() > 0) {
                    for (int i = 0; i < mListFunctions.size(); i++) {
                        if (contentCenter.equals(mListFunctions.get(i).functionName)) {
                            mTvDesc.setText(mListFunctions.get(i).msg);
                        }
                    }
                }
                onTouchWheelSelectedCenter(contentCenter);
            }
        });

    }

    @Override
    public CoreDialog getCoreDialog() {
        return this.mDialog;
    }

}
