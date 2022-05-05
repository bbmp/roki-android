package com.robam.roki.ui.dialog.type;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
 * Created by  2017/8/14.
 * 左上角按钮,右上角按钮，两个滑动滚轮，从底部弹出
 */
public class DialogType_04 extends BaseDialog {

    private TextView mTvCancel;
    private TextView mTvConfirm;

    public DialogType_04(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_04, null);
        mTvCancel = rootView.findViewById(R.id.tv_cancel);
        mTvConfirm = rootView.findViewById(R.id.tv_confirm);
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
                                 boolean isLoop, int froneIndex, int centerIndex, int rearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {
        if (!isLoop){
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
