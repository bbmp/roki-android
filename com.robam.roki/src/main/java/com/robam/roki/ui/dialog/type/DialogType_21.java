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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by  2020年5月21日 09:34:46
 * 左上角按钮,右上角按钮，两个滑动滚轮，有内容描述，从底部弹出
 * 新增不会左右滚轮出现index都为0的情况 适用于“小时 分钟”弹出框
 *
 */
public class DialogType_21 extends BaseDialog {

    private TextView mTvCancel;
    private TextView mTvConfirm;
    private TextView mTvDesc;

    public DialogType_21(Context context) {
        super(context);
    }

    @Override
    public void initDialog() {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.common_dialog_layout_type_17, null);
        mTvCancel = (TextView) rootView.findViewById(R.id.tv_cancel);
        mTvConfirm = (TextView) rootView.findViewById(R.id.tv_confirm);
        mTvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
        mLoopViewFront = (LoopView) rootView.findViewById(R.id.wheel_view_front);
        mLoopViewRear = (LoopView) rootView.findViewById(R.id.wheel_view_rear);
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
    List<String>listFrnot=new ArrayList<>();
    List<String>listRear=new ArrayList<>();
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
        this.listFrnot=listFrnot;
        this.listRear=listRear;
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

    String frontText;
    String rearText;

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
                frontText = getNumeric(contentFront);
                if ("0".equals(frontText) && "0".equals(rearText)){
                    onTouchWheelSelectedRear(listRear.get(1));
                    mLoopViewRear.setCurrentPosition(1);
                }
                onTouchWheelSelectedFrone(contentFront);
            }
        });

        mLoopViewRear.setListenerRear(new OnItemSelectedListenerRear() {
            @Override
            public void onItemSelectedRear(String contentRear) {
                rearText = getNumeric(contentRear);
                if ("0".equals(frontText) && "0".equals(rearText)){
                    mLoopViewFront.setCurrentPosition(1);
                    onTouchWheelSelectedFrone(listFrnot.get(1));
                }

                onTouchWheelSelectedRear(contentRear);
            }
        });

    }

    public static String getNumeric(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
