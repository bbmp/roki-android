package com.robam.roki.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.robam.roki.R;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnItemSelectedListenerCenter;
import com.robam.roki.listener.OnItemSelectedListenerFrone;
import com.robam.roki.listener.OnItemSelectedListenerRear;
import com.robam.roki.ui.view.wheelview.LoopView;

import java.util.List;

/**
 * 这个类会实现IRokiDialog的所有方法，这样任何一种类型的对话框调用都不会出错
 * 不支持的部分调用相应的方法时无效，不会导致崩溃
 */
public abstract class BaseDialog implements IRokiDialog {

    protected CoreDialog mDialog = null;
    protected Context mContext;
    protected TextView tv_title;
    protected TextView mTitleTv;
    protected TextView mTitleAralmCodeTv;
    protected LoopView mLoopViewCenter;
    protected LoopView mLoopViewFront;
//    protected LoopView mLoopViewFront2;
    protected LoopView mLoopViewRear;
    protected List<DeviceConfigurationFunctions> mListFunctions;
    protected String descText;

    protected View.OnClickListener mCancelOnClickListener;
    protected View.OnClickListener mOkOnClickListener;
    protected OnItemSelectedListenerFrone mOnItemSelectedListenerFrone;
    protected OnItemSelectedListenerCenter mOnItemSelectedListenerCenter;
    protected OnItemSelectedListenerRear mOnItemSelectedListenerRear;

    public abstract void initDialog();

    public abstract void bindAllListeners();

    public BaseDialog(Context context) {
        mContext = context;
        initDialog();
        bindAllListeners();
    }

    protected void createDialog(View rootView) {
        if (mDialog == null) {
            mDialog = new CoreDialog(mContext, R.style.dialog, rootView, true);
            mDialog.setPosition(Gravity.CENTER, 0, 0);
        }
    }

    public void onCancelClick(View v) {
        dismiss();
        if (mCancelOnClickListener != null) mCancelOnClickListener.onClick(v);
    }

    public void onOkClick(View v) {
        if (mOkOnClickListener != null) mOkOnClickListener.onClick(v);
    }


    public void onTouchWheelSelectedFrone(String content) {

        if (mOnItemSelectedListenerFrone != null)
            mOnItemSelectedListenerFrone.onItemSelectedFront(content);
    }

    public void onTouchWheelSelectedCenter(String content) {
        if (mOnItemSelectedListenerCenter != null)
            mOnItemSelectedListenerCenter.onItemSelectedCenter(content);

    }

    public void onTouchWheelSelectedRear(String content) {
        if (mOnItemSelectedListenerRear != null)
            mOnItemSelectedListenerRear.onItemSelectedRear(content);

    }


    @Override
    public void setTitleText(int titleStrId) {
        if (mTitleTv != null) mTitleTv.setText(titleStrId);
    }

    @Override
    public void setTitleText(CharSequence titleStr) {
        if (mTitleTv != null) mTitleTv.setText(titleStr);
    }
    @Override
    public void setTitle(int titleStrId) {
        if (tv_title != null) tv_title.setText(titleStrId);
    }

    @Override
    public void setTitle(CharSequence titleStr) {
        if (tv_title != null) tv_title.setText(titleStr);
    }

    @Override
    public void setTitleAralmCodeText(CharSequence titleAralmCodeStr) {
        if (mTitleAralmCodeTv != null) mTitleAralmCodeTv.setText(titleAralmCodeStr);
    }


    @Override
    public void setTitleAralmCodeText(int titleAralmCodeStrId) {
        if (mTitleAralmCodeTv != null) mTitleAralmCodeTv.setText(titleAralmCodeStrId);
    }

    @Override
    public void setProgress(int por) {

    }

    @Override
    public void setContentText(int contentStrId) {
    }

    @Override
    public void setContentText(CharSequence contentStr) {
    }

    @Override
    public void setCancelBtn(int textId, View.OnClickListener cancelOnClickListener) {
    }

    @Override
    public void setCancelBtn(CharSequence text, View.OnClickListener cancelOnClickListener) {
    }

    @Override
    public void setCanBtnTextColor(int color) {

    }

    @Override
    public void setOkBtn(int textId, View.OnClickListener okOnClickListener) {
    }

    @Override
    public void setOkBtn(CharSequence text, View.OnClickListener okOnClickListener) {
    }

    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 boolean isLoop, int froneIndex, int centerIndex, int RearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {

        if (mLoopViewCenter != null && listCenter != null && listCenter.size() != 0) {
            mLoopViewCenter.setItems(listCenter);
        } else if (mLoopViewFront != null && mLoopViewRear != null && listFrnot != null &&
                listFrnot.size() != 0 && listRear != null && listRear.size() != 0) {
            mLoopViewFront.setItems(listFrnot);
            mLoopViewRear.setItems(listRear);
        } else if (mLoopViewFront != null && mLoopViewRear != null && listFrnot != null &&
                listFrnot.size() != 0 && listRear != null && listRear.size() != 0 && mLoopViewCenter
                != null && listCenter != null && listCenter.size() != 0) {
            mLoopViewFront.setItems(listFrnot);
            mLoopViewCenter.setItems(listCenter);
            mLoopViewRear.setItems(listRear);
        }

    }

    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 List<DeviceConfigurationFunctions> listFunctions, boolean isLoop,
                                 int froneIndex, int centerIndex, int RearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {


        if (mLoopViewCenter != null && listCenter != null && listCenter.size() != 0) {
            mLoopViewCenter.setItems(listCenter);
        } else if (mLoopViewFront != null && mLoopViewRear != null && listFrnot != null &&
                listFrnot.size() != 0 && listRear != null && listRear.size() != 0) {
            mLoopViewFront.setItems(listFrnot);
            mLoopViewRear.setItems(listRear);
        } else if (mLoopViewFront != null && mLoopViewRear != null && listFrnot != null &&
                listFrnot.size() != 0 && listRear != null && listRear.size() != 0 && mLoopViewCenter
                != null && listCenter != null && listCenter.size() != 0) {
            mLoopViewFront.setItems(listFrnot);
            mLoopViewCenter.setItems(listCenter);
            mLoopViewRear.setItems(listRear);
        }

        if (listFunctions != null && listFunctions.size() != 0) {
            mListFunctions = listFunctions;
        }

    }


    @Override
    public void setWheelViewData(List<String> listFrnot, List<String> listCenter, List<String> listRear,
                                 String desc, boolean isLoop, int froneIndex, int centerIndex, int rearIndex,
                                 OnItemSelectedListenerFrone onItemSelectedListenerFrone,
                                 OnItemSelectedListenerCenter onItemSelectedListenerCenter,
                                 OnItemSelectedListenerRear onItemSelectedListenerRear) {
        if (mLoopViewCenter != null && listCenter != null && listCenter.size() != 0) {
            mLoopViewCenter.setItems(listCenter);
        } else if (mLoopViewFront != null && mLoopViewRear != null && listFrnot != null &&
                listFrnot.size() != 0 && listRear != null && listRear.size() != 0) {
            mLoopViewFront.setItems(listFrnot);
            mLoopViewRear.setItems(listRear);
        } else if (mLoopViewFront != null && mLoopViewRear != null && listFrnot != null &&
                listFrnot.size() != 0 && listRear != null && listRear.size() != 0 && mLoopViewCenter
                != null && listCenter != null && listCenter.size() != 0) {
            mLoopViewFront.setItems(listFrnot);
            mLoopViewCenter.setItems(listCenter);
            mLoopViewRear.setItems(listRear);
        }
        if (!"".equals(desc)) {
            descText = desc;
        }


    }

    public void setmOnItemSelectedListenerFrone(OnItemSelectedListenerFrone mOnItemSelectedListenerFrone) {
        this.mOnItemSelectedListenerFrone = mOnItemSelectedListenerFrone;
    }

    public void setmOnItemSelectedListenerCenter(OnItemSelectedListenerCenter mOnItemSelectedListenerCenter) {
        this.mOnItemSelectedListenerCenter = mOnItemSelectedListenerCenter;
    }

    public void setmOnItemSelectedListenerRear(OnItemSelectedListenerRear mOnItemSelectedListenerRear) {
        this.mOnItemSelectedListenerRear = mOnItemSelectedListenerRear;
    }

    public void setOnCancelClickListener(View.OnClickListener cancelClickListener) {
        mCancelOnClickListener = cancelClickListener;
    }

    public void setOnOkClickListener(View.OnClickListener okClickListener) {
        mOkOnClickListener = okClickListener;
    }

    @Override
    public void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        if (mDialog != null) mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        if (mDialog != null) mDialog.setOnDismissListener(listener);
    }

    @Override
    public void setOnShowListener(DialogInterface.OnShowListener listener) {
        if (mDialog != null) mDialog.setOnShowListener(listener);
    }

    @Override
    public void setCancelable(boolean b) {
        if (mDialog != null) mDialog.setCancelable(b);
    }

    @Override
    public void setInitTaskData(int randomNumber, int readyTime, long timingTime) {

    }

    @Override
    public void setToastShowTime(int time) {

    }

    @Override
    public boolean isShow() {
        if (mDialog == null)
            return false;
        return mDialog.isShowing();
    }

    @Override
    public void show() {
        if (mDialog != null) {
            /*WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            display.getMetrics(dm);

            Window win = mDialog.getWindow();
            WindowManager.LayoutParams wl = win.getAttributes();
           // wl.width = (int) (dm.widthPixels*0.6);
            wl.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 1.0);
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(wl);*/

            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (!activity.isFinishing()) {
                    mDialog.show();
                }
            } else {
                mDialog.show();
            }
        }
    }

    @Override
    public void dismiss() {
        if (mDialog != null) mDialog.dismiss();
    }


    @Override
    public CoreDialog getCoreDialog() {
        return null;
    }

    public LoopView getmLoopViewCenter() {
        return mLoopViewCenter;
    }

    public LoopView getmLoopViewFront() {
        return mLoopViewFront;
    }

    public LoopView getmLoopViewRear() {
        return mLoopViewRear;
    }
}