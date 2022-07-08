package com.robam.roki.ui.page.device.stove;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.legent.Callback;
import com.legent.VoidCallback;
import com.legent.plat.Plat;
import com.legent.plat.constant.IPlatRokiFamily;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.events.DeviceNameChangeEvent;
import com.legent.plat.io.cloud.Reponses;
import com.legent.plat.pojos.device.BackgroundFunc;
import com.legent.plat.pojos.device.DeviceConfigurationFunctions;
import com.legent.plat.pojos.device.MainFunc;
import com.legent.plat.pojos.device.ModelMap;
import com.legent.plat.pojos.device.OtherFunc;
import com.legent.ui.UIService;
import com.legent.utils.JsonUtils;
import com.legent.utils.LogUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.events.StoveAlarmEvent;
import com.robam.common.events.StoveStatusChangedEvent;
import com.robam.common.io.cloud.RokiRestHelper;
import com.robam.common.pojos.device.IRokiFamily;
import com.robam.common.pojos.device.Stove.Stove;
import com.robam.common.services.StoveAlarmManager;
import com.robam.roki.MobApp;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.listener.OnRecyclerViewItemClickListener;
import com.robam.roki.model.bean.StoveLockParams;
import com.robam.roki.ui.PageArgumentKey;
import com.robam.roki.ui.PageKey;
import com.robam.roki.ui.adapter.DividerItemDecoration;
import com.robam.roki.ui.adapter.StoveBackgroundFuncAdapter;
import com.robam.roki.ui.adapter.StoveOtherFuncAdapter;
import com.robam.roki.ui.dialog.type.DialogType_30;
import com.robam.roki.ui.form.CookCurveActivity;
import com.robam.roki.ui.page.device.DeviceCatchFilePage;
import com.robam.roki.ui.view.SlideLockView;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.ToolUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.content.Context.VIBRATOR_SERVICE;


public class AbsDeviceStovePage<Device extends Stove> extends DeviceCatchFilePage
        implements View.OnTouchListener {

    Device stove;
    String mGuid;
    String mDeviceCategory;
    @InjectView(R.id.iv_back)
    ImageView mIvBack;
    @InjectView(R.id.tv_device_model_name)
    TextView mTvDeviceModelName;
    @InjectView(R.id.iv_device_more)
    ImageView mIvDeviceMore;
    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @InjectView(R.id.layout_bg)
    FrameLayout mLayoutBg;
    @InjectView(R.id.rv_background_func)
    RecyclerView mBackgroundFuncRecyclerview;
    @InjectView(R.id.iv_bg)
    ImageView mIvBg;
    @InjectView(R.id.tv_off_line_text)
    TextView mTvOffLineText;
    @InjectView(R.id.iv_lock_bg)
    ImageView mIvLockBg;
    @InjectView(R.id.slv_lock)
    SlideLockView mSlvLock;
    @InjectView(R.id.tv_child_lock)
    TextView mTvChildLock;
    @InjectView(R.id.rl_lock)
    RelativeLayout mRlLock;
    @InjectView(R.id.btn_curve_entry)
    Button btn_curve_entry;


    private IRokiDialog mStoveLevelAlarm_03;
    Vibrator mVibrator;
    private StoveOtherFuncAdapter mStoveOtherFuncAdapter;
    private StoveBackgroundFuncAdapter mStoveBackgroundFuncAdapter;
    private List<DeviceConfigurationFunctions> mDeviceConfigurationFunctions = new ArrayList<>();
    private boolean mIsLock;
    private List<DeviceConfigurationFunctions> mBackgroundFuncDeviceConfigurationFunctions;
    private List<DeviceConfigurationFunctions> mMainFunList;
    private long curveId = 0;
    DialogType_30 rokiDialog=null;

    @Subscribe
    public void onEvent(DeviceNameChangeEvent event){
        if (mGuid.equals(event.device.getGuid().getGuid())){
            String name = event.device.getName();
            mTvDeviceModelName.setText(name);
        }
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mStoveOtherFuncAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    setDataToView((Reponses.DeviceResponse) msg.obj);
                    break;
            }
        }
    };

    @Subscribe
    public void onEvent(StoveAlarmEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.stove.getID()))
            return;
        switch (event.alarm.getID()) {
            case StoveAlarmManager.E_1:
                if (IRokiFamily.RQZ02.equals(event.stove.getDp())) {
                    mStoveLevelAlarm_03.setContentText(R.string.device_alarm_stove_9b39_E1_content);
                    mStoveLevelAlarm_03.setToastShowTime(DialogUtil.LENGTH_LONG);
                    mStoveLevelAlarm_03.show();
                } else if (IRokiFamily.RQZ01.equals(event.stove.getDp())) {
                    //mStoveLevelAlarm_03.setContentText(R.string.device_alarm_stove_9w70_E1_content);
                }

                break;
        }
    }

    @Subscribe
    public void onEvent(StoveStatusChangedEvent event) {
        LogUtils.i("20180518", event.pojo.getID());
        if (stove == null || !Objects.equal(stove.getID(), event.pojo.getID()))
            return;
        mTvOffLineText.setVisibility(View.GONE);
        mIsLock = stove.isLock;
        if (mIsLock) {
            mIvLockBg.setVisibility(View.VISIBLE);
            mRlLock.setVisibility(View.VISIBLE);
        } else {
            mIvLockBg.setVisibility(View.GONE);
            mRlLock.setVisibility(View.GONE);
        }
        mStoveBackgroundFuncAdapter.onEvent(event);
        rokiDialog.setStatus(stove);
    }

    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        if (stove == null || !Objects.equal(stove.getID(), event.device.getID()))
            return;
        if (!event.isConnected) {
            ToastUtils.showLong(R.string.device_new_connected);
            mTvOffLineText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bd = getArguments();
        mGuid = bd == null ? null : bd.getString(PageArgumentKey.Guid);
        mDeviceCategory = bd == null ? null : bd.getString(PageArgumentKey.deviceCategory);
        stove = Plat.deviceService.lookupChild(mGuid);
        View view = inflater.inflate(R.layout.page_device_stoveh, container, false);
        ButterKnife.inject(this, view);
        initData();
        initWidget();
        rokiDialog= RokiDialogFactory.createDialogType_30(cx);
        rokiDialog.setCancelable(true);
        btn_curve_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rokiDialog.setCancelBtn(0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ToastUtils.show("左灶开启烹饪曲线", Toast.LENGTH_LONG);
                        query(0);
                    }
                });
                rokiDialog.setOkBtn(0, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ToastUtils.show("右灶开启烹饪曲线", Toast.LENGTH_LONG);
                        rokiDialog.dismiss();
                        query(1);
                    }
                });
                rokiDialog.show();
            }
        });
        return view;
    }

    //童锁开的监听
    protected void initWidget() {
        mStoveLevelAlarm_03 = RokiDialogFactory.createDialogByType(cx, DialogUtil.DIALOG_TYPE_09);
        mIvLockBg.setOnTouchListener(this);
        mVibrator = (Vibrator) cx.getSystemService(VIBRATOR_SERVICE);
        mSlvLock.setLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {

                mVibrator.vibrate(100);
                stove.setStoveLock(false, new VoidCallback() {
                    @Override
                    public void onSuccess() {
                        mIvLockBg.setVisibility(View.GONE);
                        mRlLock.setVisibility(View.GONE);
                    }

                    @Override

                    public void onFailure(Throwable t) {


                    }
                });

            }
        });
    }

    //童锁关的监听
    protected void setDataToView(Reponses.DeviceResponse deviceResponse) {

        if (mDevice instanceof Stove) {
            stove = (Device) mDevice;
        }

        if (!stove.isConnected()) {
            ToastUtils.showLong(R.string.device_new_connected);
            if (mTvOffLineText != null) {
                mTvOffLineText.setVisibility(View.VISIBLE);
            }
        }

        try {
            ModelMap modelMap = deviceResponse.modelMap;
//            mTvDeviceModelName.setText(deviceResponse.title);
            mTvDeviceModelName.setText(stove.getName() == null ||  stove.getName().equals(stove.getCategoryName()) ? stove.getDispalyType() : stove.getName());
            Glide.with(cx).load(deviceResponse.viewBackgroundImg).into(mIvBg);
            //档位，炉头，模式
            BackgroundFunc backgroundFunc = modelMap.backgroundFunc;
            mBackgroundFuncDeviceConfigurationFunctions = backgroundFunc.deviceConfigurationFunctions;
            mStoveBackgroundFuncAdapter = new StoveBackgroundFuncAdapter(cx, stove, mBackgroundFuncDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    //TODO
                }
            });

            mBackgroundFuncRecyclerview.setAdapter(mStoveBackgroundFuncAdapter);
            LinearLayoutManager backgroundFuncLinerLayout = new LinearLayoutManager(cx, LinearLayoutManager.HORIZONTAL, false);

            mBackgroundFuncRecyclerview.setLayoutManager(backgroundFuncLinerLayout);
            mBackgroundFuncRecyclerview.addItemDecoration(new DividerItemDecoration(cx, DividerItemDecoration.HORIZONTAL_LIST));


            MainFunc mainFunc = modelMap.mainFunc;
            mMainFunList = mainFunc.deviceConfigurationFunctions;
            LogUtils.i("20180724", " mMainFunList:" + mMainFunList.size());
            mDeviceConfigurationFunctions.addAll(mMainFunList);
            //主功能区
            OtherFunc otherFunc = modelMap.otherFunc;
            List<DeviceConfigurationFunctions> otherFuncList = otherFunc.deviceConfigurationFunctions;
            mDeviceConfigurationFunctions.addAll(otherFuncList);
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(cx, 1);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = mRecyclerview.getAdapter().getItemViewType(position);
                    if (viewType == StoveOtherFuncAdapter.OTHER_VIEW) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
            //童锁关的监听
            mStoveOtherFuncAdapter = new StoveOtherFuncAdapter(cx, stove, mDeviceConfigurationFunctions, new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view) {
                    if (mMainFunList == null || mMainFunList.size() == 0) return;
                    for (int i = 0; i < mMainFunList.size(); i++) {
                        String functionParams = mMainFunList.get(i).functionParams;
                        try {
                            StoveLockParams stoveLockParams = JsonUtils.json2Pojo(functionParams, StoveLockParams.class);
                            String lockValue = stoveLockParams.getParam().getPower().getValue();
                            short lock = Short.parseShort(lockValue);
                            if (lock == 1) {
                                mIsLock = true;
                            }
                            if (!stove.isConnected()) {
                                ToastUtils.showShort(R.string.device_connected);
                                return;
                            }
                            if (IPlatRokiFamily.RQZ01.equals(stove.getDp()) || IPlatRokiFamily.RQZ05.equals(stove.getDp())) {
                                if (stove.leftHead.status == 0 && stove.rightHead.status == 0) {
                                    ToastUtils.showShort(R.string.device_stove_off_not_lock);
                                    return;
                                }

                                if (mIsLock) {
                                    ToolUtils.logEvent(stove.getDt(), "童锁:开", "roki_设备");
                                } else {
                                    ToolUtils.logEvent(stove.getDt(), "童锁:关", "roki_设备");
                                }

                                stove.setStoveLock(mIsLock, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            } else {
                                stove.setStoveLock(mIsLock, new VoidCallback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            mStoveOtherFuncAdapter.setOnClickOpenCurve(new StoveOtherFuncAdapter.OnClickOpenCurve() {
                @Override
                public void onClickOpenCurve() {

                    rokiDialog.setCancelBtn(0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.show("左灶开启烹饪曲线", Toast.LENGTH_LONG);
//                            cookingCurveSave(0);
                            query(0);
                        }
                    });
                    rokiDialog.setOkBtn(0, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtils.show("右灶开启烹饪曲线", Toast.LENGTH_LONG);
                            rokiDialog.dismiss();
//                            cookingCurveSave(1);
                            query(1);
//                            Bundle bd = new Bundle();
//                            bd.putString(PageArgumentKey.Guid,stove.getGuid().getGuid());
//                            bd.putInt(PageArgumentKey.HeadId,1);
//                            bd.putLong(PageArgumentKey.curveId, curveId);
//                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
                        }
                    });
                    rokiDialog.show();

                }
            });
            mRecyclerview.setLayoutManager(gridLayoutManager);
            mRecyclerview.setAdapter(mStoveOtherFuncAdapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    private void query(int headId) {
        //mGuid 暂时写死241
        RokiRestHelper.cookingCurvequery(mGuid,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveQueryRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveQueryRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload == null) {
                            cookingCurveSave(headId);
                        }else{
                            curveId = rcReponse.payload.curveCookbookId;
                            Bundle bd = new Bundle();
                            bd.putString(PageArgumentKey.Guid,stove.getGuid().getGuid());
                            bd.putInt(PageArgumentKey.HeadId,headId);
                            bd.putLong(PageArgumentKey.curveId, curveId);
//                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
                            CookCurveActivity.start(activity,bd);
//                            SteamOvenCookCurveActivity.start(activity,bd);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
    }
    private void cookingCurveSave(int headId) {
        String deviceGuid = stove.getGuid().getGuid();
        int id = 0;
        String model =  "";
        String setTemp =  "";
        String setTime = "";
        RokiRestHelper.cookingCurveSave(deviceGuid, id, model, setTemp, setTime,headId,
                new Callback<com.robam.common.io.cloud.Reponses.CookingCurveSaveRes>() {

                    @Override
                    public void onSuccess(com.robam.common.io.cloud.Reponses.CookingCurveSaveRes rcReponse) {
                        Log.d("20211129", rcReponse.toString());
                        if (rcReponse.payload != 0) {
                            curveId = rcReponse.payload;
                            Bundle bd = new Bundle();
                            bd.putString(PageArgumentKey.Guid,deviceGuid);
                            bd.putInt(PageArgumentKey.HeadId,headId);
                            bd.putLong(PageArgumentKey.curveId, curveId);
//                            UIService.getInstance().postPage(PageKey.DeviceStoveCurve,bd);
                            CookCurveActivity.start(activity,bd);
//                            SteamOvenCookCurveActivity.start(activity,bd);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        ToastUtils.show(t.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (stove == null) {
            return;
        }
        if (stove.getDt() != null) {
            FirebaseAnalytics firebaseAnalytics = MobApp.getmFirebaseAnalytics();
            firebaseAnalytics.setCurrentScreen(getActivity(), stove.getDt(), null);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.iv_back)
    public void onMIvBackClicked() {
        UIService.getInstance().popBack();
    }


    @OnClick(R.id.iv_device_more)
    public void onMIvDeviceMoreClicked() {
        Bundle bd = new Bundle();
        bd.putString(PageArgumentKey.Guid, stove.getID());
        bd.putString(PageArgumentKey.stove, "stove");
        UIService.getInstance().postPage(PageKey.StoveDeviceMore, bd);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
