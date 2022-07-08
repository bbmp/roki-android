package com.robam.roki.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.robam.roki.ui.page.device.steamovenone.AbsSteamOvenWorkingCurve925VView;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

import static com.legent.ContextIniter.cx;

/**
 * Created by 14807 on 2018/5/18.
 */

public class StoveOtherFuncAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public StoveOtherFuncAdapter(Context context, Stove stove, List<DeviceConfigurationFunctions> data,
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

        if (OTHER_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.item_otherfunc_page, parent, false);
            ScreenAdapterTools.getInstance().loadView(view);
            StoveOtherFuncViewHolder stoveOtherFuncViewHolder = new StoveOtherFuncViewHolder(view);
            //灶具下面的监听
            stoveOtherFuncViewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemEvent(v);
                }
            });
            return stoveOtherFuncViewHolder;
        } else if (MAIN_VIEW == viewType) {
            View view = mInflater.inflate(R.layout.item_stove_mainfunc_page, parent, false);
            ScreenAdapterTools.getInstance().loadView(view);
            final StoveMainFuncViewHolder stoveMainFuncViewHolder = new StoveMainFuncViewHolder(view);
            //童锁的监听
            stoveMainFuncViewHolder.mIvModelImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecyclerViewItemClickListener.onItemClick(v);
                }
            });

            return stoveMainFuncViewHolder;
        }
        return null;


    }

    private void onItemEvent(View v) {
//        if (!mStove.isConnected()) {
//            DeviceOfflinePrompt();
//            return;
//        }
        String tag = v.getTag().toString();
        if ("auxiliaryShutdown".equals(tag)) {
            for (int i = 0; i < mDatas.size(); i++) {
                if ("auxiliaryShutdown".equals(mDatas.get(i).functionCode)) {
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
                    UIService.getInstance().postPage(PageKey.DeviceStoveQuicklyOffHeat, bd);
                }
            }

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
    public interface OnClickOpenCurve {

        void onClickOpenCurve();

    }
    public OnClickOpenCurve onClickOpenCurve;

    public void setOnClickOpenCurve(OnClickOpenCurve onClickOpenCurve) {
        this.onClickOpenCurve = onClickOpenCurve;
    }
    private void DeviceOfflinePrompt() {
        ToastUtils.showShort(R.string.device_connected);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof StoveOtherFuncViewHolder) {
            StoveOtherFuncViewHolder stoveOtherFuncViewHolder = (StoveOtherFuncViewHolder) holder;
            if (mDatas != null && mDatas.size() > 0) {
                if ("auxiliaryShutdown".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncViewHolder.mImageView);
                    stoveOtherFuncViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncViewHolder.mTvDesc.setText(mDatas.get(position).msg);

                } else if ("timedOff".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncViewHolder.mImageView);

//                    LogUtils.i("20180807", " leftTime:" + leftTime + " leftLevel:" + leftLevel + " rightTime:" + rightTime + " rightLevel:" + rightLevel);

                    if (leftTime != 0 && rightTime != 0 && leftStatus == StoveStatus.Working && rightStatus == StoveStatus.Working) {

                        if (leftTime < rightTime && leftStatus == StoveStatus.Working) {
                            String strLeftTime = TimeUtils.secToHourMinSec(leftTime);
                            stoveOtherFuncViewHolder.startAnimation();
                            stoveOtherFuncViewHolder.mTvWorkName.setText(strLeftTime + cx.getString(R.string.device_stove_off_left_burner));
                            stoveOtherFuncViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                            stoveOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                            stoveOtherFuncViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                        } else if (rightTime < leftTime && rightStatus == StoveStatus.Working) {
                            String strRightTime = TimeUtils.secToHourMinSec(rightTime);
                            stoveOtherFuncViewHolder.startAnimation();
                            stoveOtherFuncViewHolder.mTvWorkName.setText(strRightTime + cx.getString(R.string.device_stove_off_right_burner));
                            stoveOtherFuncViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                            stoveOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                            stoveOtherFuncViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                        }
                    } else if (leftTime == 0 && rightTime == 0 || leftStatus == StoveStatus.Off && rightStatus == StoveStatus.Off
                            || leftStatus == StoveStatus.StandyBy && rightStatus == StoveStatus.StandyBy) {
                        stoveOtherFuncViewHolder.stopAnimation();
                        stoveOtherFuncViewHolder.mTvName.setText(mDatas.get(position).functionName);
                        stoveOtherFuncViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                        stoveOtherFuncViewHolder.mTvWorkName.setVisibility(View.GONE);
                        stoveOtherFuncViewHolder.mLlDefaultText.setVisibility(View.VISIBLE);
                        stoveOtherFuncViewHolder.mStateShow.setBackground(null);
                    } else if (leftTime != 0 && leftStatus == StoveStatus.Working && rightTime == 0) {
                        String strLeftTime = TimeUtils.secToHourMinSec(leftTime);
                        stoveOtherFuncViewHolder.startAnimation();
                        stoveOtherFuncViewHolder.mTvWorkName.setText(strLeftTime + cx.getString(R.string.device_stove_off_left_burner));
                        stoveOtherFuncViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                        stoveOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                        stoveOtherFuncViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    } else if (rightTime != 0 && rightStatus == StoveStatus.Working && leftTime == 0) {
                        String strRightTime = TimeUtils.secToHourMinSec(rightTime);
                        stoveOtherFuncViewHolder.startAnimation();
                        stoveOtherFuncViewHolder.mTvWorkName.setText(strRightTime + cx.getString(R.string.device_stove_off_right_burner));
                        stoveOtherFuncViewHolder.mTvWorkName.setVisibility(View.VISIBLE);
                        stoveOtherFuncViewHolder.mLlDefaultText.setVisibility(View.GONE);
                        stoveOtherFuncViewHolder.mStateShow.setBackground(cx.getResources().getDrawable(R.drawable.shape_rika_round_yellow_dot));
                    } else {
                        stoveOtherFuncViewHolder.stopAnimation();
                        stoveOtherFuncViewHolder.mTvName.setText(mDatas.get(position).functionName);
                        stoveOtherFuncViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                        stoveOtherFuncViewHolder.mTvWorkName.setVisibility(View.GONE);
                        stoveOtherFuncViewHolder.mLlDefaultText.setVisibility(View.VISIBLE);
                    }

                } else if ("remindingFire".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncViewHolder.mImageView);
                    stoveOtherFuncViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                } else if ("automaticCooking".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncViewHolder.mImageView);
                    stoveOtherFuncViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                }else if ("curve".equals(mDatas.get(position).functionCode)) {
                    Glide.with(cx)
                            .load(mDatas.get(position).backgroundImg)
//                            .crossFade()
                            .into(stoveOtherFuncViewHolder.mImageView);
                    stoveOtherFuncViewHolder.mTvName.setText(mDatas.get(position).functionName);
                    stoveOtherFuncViewHolder.mTvDesc.setText(mDatas.get(position).msg);
                }
            }
            stoveOtherFuncViewHolder.mItemView.setTag(mDatas.get(position).functionCode);
            stoveOtherFuncViewHolder.mItemView.setTag(R.id.tag_stove_other_func_key, mDatas.get(position).functionParams);
        } else if (holder instanceof StoveMainFuncViewHolder) {
            StoveMainFuncViewHolder stoveMainFuncViewHolder = (StoveMainFuncViewHolder) holder;
            stoveMainFuncViewHolder.mTvModelName.setText(mDatas.get(position).functionName);
            if (mDatas != null && mDatas.size() > 0) {
                Glide.with(cx)
                        .load(mDatas.get(position).backgroundImg)
//                        .crossFade()
                        .into(stoveMainFuncViewHolder.mIvModelImg);
                if (mStove.isLock) {
                    stoveMainFuncViewHolder.mIvModelImg.setVisibility(View.GONE);
                } else {
                    stoveMainFuncViewHolder.mIvModelImg.setVisibility(View.VISIBLE);
                }
            }
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

class StoveOtherFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    TextView mTvDesc;
    TextView mTvName;
    TextView mTvWorkName;
    LinearLayout mItemView;
    LinearLayout mLlDefaultText;
    ImageView mStateShow;
    AlphaAnimation mAlphaAnimation;

    public StoveOtherFuncViewHolder(View itemView) {
        super(itemView);

        mTvName = itemView.findViewById(R.id.tv_name);
        mImageView = itemView.findViewById(R.id.iv_view);
        mTvWorkName = itemView.findViewById(R.id.tv_work_name);
        mTvDesc = itemView.findViewById(R.id.tv_desc);
        mLlDefaultText = itemView.findViewById(R.id.ll_default_text);
        mItemView = itemView.findViewById(R.id.itemView);
        mStateShow = itemView.findViewById(R.id.iv_state_show);
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

class StoveMainFuncViewHolder extends RecyclerView.ViewHolder {

    ImageView mIvModelImg;
    TextView mTvModelName;
    LinearLayout mItemView;

    public StoveMainFuncViewHolder(View itemView) {
        super(itemView);

        mItemView = itemView.findViewById(R.id.itemView);
        mIvModelImg = itemView.findViewById(R.id.iv_model_img);
        mTvModelName = itemView.findViewById(R.id.tv_model_name);
    }
}
