package com.robam.roki.ui.adapter;

import static com.legent.ContextIniter.cx;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.ui.UIService;
import com.legent.utils.EventUtils;
import com.legent.utils.TimeUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.pojos.DeviceType;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.pojos.device.Stove.StoveStatus;
import com.robam.roki.R;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 14807 on 2018/5/18.
 */

public class StoveOtherFuncHasCurveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MAIN_VIEW = 1;
    public static final int OTHER_VIEW = 2;
    private LayoutInflater mInflater;
    private Context mContext;
    Stove mStove;
    List<DeviceConfigurationFunctions> mDatas;
    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener;
    short leftTime;
    short leftStatus;
    short rightTime;
    short rightStatus;

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        if (mStove == null || !Objects.equal(mStove.getID(), event.pojo.getID())) return;
        leftTime = mStove.leftHead.time;
        leftStatus = mStove.leftHead.status;
        rightTime = mStove.rightHead.time;
        rightStatus = mStove.rightHead.status;
        this.notifyDataSetChanged();
    }

    public StoveOtherFuncHasCurveAdapter(Context context, Stove stove, List<DeviceConfigurationFunctions> data,
                                 OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        mContext = context;
        mDatas = data;
        mStove = stove;
        mOnRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        mInflater = LayoutInflater.from(context);
        EventUtils.regist(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = mInflater.inflate(R.layout.item_otherfunc_has_curve_page, parent, false);
        ScreenAdapterTools.getInstance().loadView(view);
        StoveOtherFuncHasCurveViewHolder stoveOtherFuncViewHolder = new StoveOtherFuncHasCurveViewHolder(view);
        //灶具下面的监听
        stoveOtherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemEvent(v);
            }
        });
        return stoveOtherFuncViewHolder;
    }

    private void onItemEvent(View v) {
        if (!mStove.isConnected()) {
            DeviceOfflinePrompt();
            return;
        }
        String tag = v.getTag().toString();
        if ("auxiliaryShutdown".equals(tag)) {
            onClickOffFire.onClickOffFire();
        } else if ("timedOff".equals(tag)) {
            for (int i = 0; i < mDatas.size(); i++) {
                if ("timedOff".equals(mDatas.get(i).functionCode)) {
                    String functionName = mDatas.get(i).functionName;
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = mDatas.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;
                    Bundle bd = new Bundle();
                    bd.putSerializable(PageArgumentKey.Bean, mStove);
                    bd.putString(PageArgumentKey.text, functionName);
                    bd.putSerializable(PageArgumentKey.List, (Serializable) deviceConfigurationFunctions);
                    UIService.getInstance().postPage(PageKey.DeviceStoveTimingOffHeat, bd);
                }
            }
            //防干烧设定
        } else if ("remindingFire".equals(tag)) {

            for (int i = 0; i < mDatas.size(); i++) {
                if ("remindingFire".equals(mDatas.get(i).functionCode)) {
                    String subViewTitle = mDatas.get(i).subView.title;
                    List<DeviceConfigurationFunctions> deviceConfigurationFunctions = mDatas.get(i)
                            .subView
                            .subViewModelMap
                            .subViewModelMapSubView
                            .deviceConfigurationFunctions;


                    Bundle bd = new Bundle();
                    bd.putSerializable(PageArgumentKey.Bean, mStove);
                    bd.putString(PageArgumentKey.title, subViewTitle);
                    bd.putSerializable(PageArgumentKey.List, (Serializable) deviceConfigurationFunctions);
                    UIService.getInstance().postPage(PageKey.StoveBurnWithoutWater, bd);
                }
            }
        } else if ("automaticCooking".equals(tag)) {
            String params = null;
            String platformCode = "";
            for (int i = 0; i < mDatas.size(); i++) {
                if ((mDatas.get(i).functionCode).equals("automaticCooking")) {
                    params = mDatas.get(i).functionParams;
                }
            }
            try {
                if (params != null && !"".equals(params)) {
                    JSONObject jsonObject = new JSONObject(params);
                    platformCode = jsonObject.optString("platformCode");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Bundle bundle = new Bundle();
            bundle.putString(PageArgumentKey.RecipeId, DeviceType.RRQZ);
            bundle.putString(PageArgumentKey.platformCode, platformCode);
            bundle.putString(PageArgumentKey.Guid, mStove.getGuid().getGuid());
            UIService.getInstance().postPage(PageKey.RecipeCategoryList, bundle);
        } else if ("curve".equals(tag)) {
            onClickOpenCurve.onClickOpenCurve();
        }
        else {
            //TODO
        }
    }
    private void DeviceOfflinePrompt() {
        ToastUtils.showLong(R.string.device_connected);
    }

    public interface OnClickOpenCurve {
        void onClickOpenCurve();
    }
    public OnClickOpenCurve onClickOpenCurve;

    public void setOnClickOpenCurve(OnClickOpenCurve onClickOpenCurve) {
        this.onClickOpenCurve = onClickOpenCurve;
    }
    public interface OnClickOffFire {
        void onClickOffFire();
    }
    public OnClickOffFire onClickOffFire;
    public void setOnClickOffFire(OnClickOffFire onClickOffFire) {
        this.onClickOffFire = onClickOffFire;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoveOtherFuncHasCurveViewHolder) {
            StoveOtherFuncHasCurveViewHolder stoveOtherFuncHasCurveViewHolder = (StoveOtherFuncHasCurveViewHolder) holder;
            if (mDatas != null && mDatas.size() > 0) {
                if ("auxiliaryShutdown".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncHasCurveViewHolder.mImageView);
                    stoveOtherFuncHasCurveViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncHasCurveViewHolder.mTvDesc.setText(mDatas.get(position).msg);

                } else if ("timedOff".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncHasCurveViewHolder.mImageView);

//                    LogUtils.i("20180807", " leftTime:" + leftTime + " leftLevel:" + leftLevel + " rightTime:" + rightTime + " rightLevel:" + rightLevel);
                    if (leftTime >0 && rightTime > 0 && leftStatus == StoveStatus.Working && rightStatus == StoveStatus.Working) {//两边都定时
                        String strLeftTime = TimeUtils.secToHourMinSec(leftTime);
                        stoveOtherFuncHasCurveViewHolder.startAnimation();
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setText(strLeftTime + cx.getString(R.string.device_stove_off_left_burner));
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                        stoveOtherFuncHasCurveViewHolder.rl_default_text.setVisibility(View.GONE);
                        stoveOtherFuncHasCurveViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));

                        stoveOtherFuncHasCurveViewHolder.tv_work_name_tow.setVisibility(View.VISIBLE);
                        String strRightTime = TimeUtils.secToHourMinSec(rightTime);
                        stoveOtherFuncHasCurveViewHolder.tv_work_name_tow.setText(strRightTime + cx.getString(R.string.device_stove_off_right_burner));

                    } else if (leftTime == 0 && rightTime == 0 || leftStatus == StoveStatus.Off && rightStatus == StoveStatus.Off
                            || leftStatus == StoveStatus.StandyBy && rightStatus == StoveStatus.StandyBy) {//两边都待机
                        stoveOtherFuncHasCurveViewHolder.stopAnimation();
                        stoveOtherFuncHasCurveViewHolder.mTvName.setText(mDatas.get(position).functionName);
                        stoveOtherFuncHasCurveViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setVisibility(View.GONE);
                        stoveOtherFuncHasCurveViewHolder.rl_default_text.setVisibility(View.VISIBLE);
                        stoveOtherFuncHasCurveViewHolder.mStateShow.setBackground(null);
                        stoveOtherFuncHasCurveViewHolder.tv_work_name_tow.setVisibility(View.GONE);
                    } else if (leftTime > 0 && leftStatus == StoveStatus.Working && rightTime <= 0) {//左定时，右待机
                        String strLeftTime = TimeUtils.secToHourMinSec(leftTime);
                        stoveOtherFuncHasCurveViewHolder.startAnimation();
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setText(strLeftTime + cx.getString(R.string.device_stove_off_left_burner));
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                        stoveOtherFuncHasCurveViewHolder.rl_default_text.setVisibility(View.GONE);
                        stoveOtherFuncHasCurveViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                        stoveOtherFuncHasCurveViewHolder.tv_work_name_tow.setVisibility(View.GONE);
                    } else if (rightTime > 0 && rightStatus == StoveStatus.Working && leftTime <= 0) {//右定时，左待机
                        String strRightTime = TimeUtils.secToHourMinSec(rightTime);
                        stoveOtherFuncHasCurveViewHolder.startAnimation();
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setText(strRightTime + cx.getString(R.string.device_stove_off_right_burner));
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                        stoveOtherFuncHasCurveViewHolder.rl_default_text.setVisibility(View.GONE);
                        stoveOtherFuncHasCurveViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                        stoveOtherFuncHasCurveViewHolder.tv_work_name_tow.setVisibility(View.GONE);
                    } else {
                        stoveOtherFuncHasCurveViewHolder.stopAnimation();
                        stoveOtherFuncHasCurveViewHolder.mTvName.setText(mDatas.get(position).functionName);
                        stoveOtherFuncHasCurveViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                        stoveOtherFuncHasCurveViewHolder.mTvWorkName.setVisibility(View.GONE);
                        stoveOtherFuncHasCurveViewHolder.rl_default_text.setVisibility(View.VISIBLE);
                        stoveOtherFuncHasCurveViewHolder.tv_work_name_tow.setVisibility(View.GONE);
                    }

                } else if ("remindingFire".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncHasCurveViewHolder.mImageView);
                    stoveOtherFuncHasCurveViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncHasCurveViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                } else if ("automaticCooking".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncHasCurveViewHolder.mImageView);
                    stoveOtherFuncHasCurveViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncHasCurveViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                    stoveOtherFuncHasCurveViewHolder.img_right_head_tip.setVisibility(View.VISIBLE);
                }else if ("curve".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncHasCurveViewHolder.mImageView);
                    stoveOtherFuncHasCurveViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncHasCurveViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                    stoveOtherFuncHasCurveViewHolder.img_right_head_tip.setVisibility(View.VISIBLE);
                }
            }
            stoveOtherFuncHasCurveViewHolder.mItemView.setTag(mDatas.get(position).functionCode);
            stoveOtherFuncHasCurveViewHolder.mItemView.setTag(R.id.tag_stove_other_func_key, mDatas.get(position).functionParams);
        } else if (holder instanceof StoveMainFuncViewHolder) {
//            StoveMainFuncViewHolder stoveMainFuncViewHolder = (StoveMainFuncViewHolder) holder;
//            stoveMainFuncViewHolder.mTvModelName.setText(mDatas.get(position).functionName);
//            if (mDatas != null && mDatas.size() > 0) {
//                Glide.with(cx)
//                        .load(mDatas.get(position).backgroundImg)
////                        .crossFade()
//                        .into(stoveMainFuncViewHolder.mIvModelImg);
//                if (mStove.isLock) {
//                    stoveMainFuncViewHolder.mIvModelImg.setVisibility(View.GONE);
//                } else {
//                    stoveMainFuncViewHolder.mIvModelImg.setVisibility(View.VISIBLE);
//                }
//            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        String functionCode = mDatas.get(position).functionCode;
        if ("lock".equals(functionCode)) {
            return MAIN_VIEW;
        } else {
            return OTHER_VIEW;
        }
    }

}

class StoveOtherFuncHasCurveViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    TextView mTvWorkName;
    TextView tv_work_name_tow;
    LinearLayout mItemView;
    RelativeLayout rl_default_text;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;
    ImageView img_right_head_tip;
    public StoveOtherFuncHasCurveViewHolder(View itemView) {
        super(itemView);

        mTvName = itemView.findViewById(R.id.tv_name);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvWorkName = itemView.findViewById(R.id.tv_work_name);
        tv_work_name_tow = itemView.findViewById(R.id.tv_work_name_tow);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        rl_default_text = itemView.findViewById(R.id.rl_default_text);
        mItemView = itemView.findViewById(R.id.itemView);
        mStateShow = itemView.findViewById(R.id.iv_state_show);
        img_right_head_tip = itemView.findViewById(R.id.img_right_head_tip);
    }

    public void startAnimation() {

        mAlphaAnimation = null;
        if (mAlphaAnimation == null) {
            mAlphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(cx, R.anim.device_rika_dot_alpha);
            LinearInterpolator lin = new LinearInterpolator();
            mAlphaAnimation.setInterpolator(lin);
            mStateShow.startAnimation(mAlphaAnimation);
            mStateShow.setVisibility(View.VISIBLE);
        }
    }

    public void stopAnimation() {
        mStateShow.setVisibility(View.GONE);
        if (mAlphaAnimation != null) {
            mAlphaAnimation.cancel();
            mAlphaAnimation = null;
        }
    }
}

