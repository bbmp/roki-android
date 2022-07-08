package com.robam.roki.ui.activity3.device.steamoven;

import android.view.View;
import android.widget.Button;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback;
import com.legent.utils.TimeUtils;
import com.robam.common.events.DishWasherAlarmEvent;
import com.robam.common.events.DisherWasherStatusChangeEvent;
import com.robam.common.events.SteamOvenOneStatusChangedEvent;
import com.robam.common.pojos.device.dishWasher.AbsDishWasher;
import com.robam.common.pojos.device.dishWasher.DishWasherStatus;
import com.robam.common.pojos.device.integratedStove.ChuGouEnum;
import com.robam.common.pojos.device.integratedStove.IntegStoveStatus;
import com.robam.common.pojos.device.integratedStove.SteamOvenModeEnum;
import com.robam.common.pojos.device.integratedStove.SteamOvenSteamEnum;
import com.robam.common.pojos.device.steameovenone.AbsSteameOvenOneNew;
import com.robam.roki.R;
import com.robam.roki.factory.RokiDialogFactory;
import com.robam.roki.listener.IRokiDialog;
import com.robam.roki.ui.activity3.device.base.DeviceWorkActivity;
import com.robam.roki.ui.activity3.device.base.adapter.bean.WorkModeBean;
import com.robam.roki.ui.activity3.device.base.table.LocalRecipeEntity;
import com.robam.roki.ui.activity3.device.base.view.WorkTimeView;
import com.robam.roki.ui.activity3.device.dishwasher.bean.DishModeEnum;
import com.robam.roki.utils.DateUtil;
import com.robam.roki.utils.DialogUtil;
import com.robam.roki.utils.StringUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/30
 *     desc   : 一体机工作界面（预约 ，P档菜谱）
 *     version: 1.0
 * </pre>
 */
public class SteamOvenWorkActivity extends DeviceWorkActivity {

    AbsSteameOvenOneNew steamOvenOne;
    /**
     * 中间显示状态的view
     */
    private WorkTimeView workTimeView;

    /**
     * 一体机状态变更事件
     *
     * @param event
     */
    @Subscribe
    public void onEvent(SteamOvenOneStatusChangedEvent event) {
        if (mDevice == null || !Objects.equal(mDevice.getID(), event.pojo.getID())) {
            return;
        }
        if (event.pojo instanceof AbsSteameOvenOneNew) {
            this.steamOvenOne = (AbsSteameOvenOneNew) event.pojo;
            setData(steamOvenOne);
        }

    }

    /**
     * 加载数据
     */
    @Override
    protected void dealData() {
        super.dealData();
        if (mDevice != null && mDevice instanceof AbsSteameOvenOneNew) {
            steamOvenOne = (AbsSteameOvenOneNew) mDevice;
            workTimeView = new WorkTimeView(this);

            setData(steamOvenOne);
        }
    }

    /**
     * 这是处理数据
     *
     * @param steamOvenOne
     */
    private void setData(AbsSteameOvenOneNew steamOvenOne) {
        if (steamOvenOne.recipeId != 0) {
            LocalRecipeEntity localRecipe = LitePal
                    .where("dt = ? and  mode = ?", mDevice.getDt(), steamOvenOne.recipeId + "")
                    .findFirst(LocalRecipeEntity.class);
            List<WorkModeBean> workModeBeans = new ArrayList<>();
            WorkModeBean workModeBean = new WorkModeBean();
            workModeBean.key = "菜谱";
            workModeBean.value = localRecipe.pKey + "" + localRecipe.value;
            workModeBeans.add(workModeBean);
            workModeBean = new WorkModeBean();
            workModeBean.key = "时间";
            workModeBean.value = String.valueOf(steamOvenOne.recipeSetMinutes);
            workModeBean.unit = "min";
            workModeBeans.add(workModeBean);
            setTopMode(workModeBeans);

        } else {
            //工作模式
            short mode = steamOvenOne.mode;
            SteamOvenModeEnum modeEnum = SteamOvenModeEnum.match(mode);
            //设置的工作时间 (秒)
            int setTime = steamOvenOne.setTimeH * 256 + steamOvenOne.setTime;
            //设置温度
            int setUpTemp = steamOvenOne.setUpTemp;
            int setDownTemp = steamOvenOne.setDownTemp;
            //蒸汽量
            int steam = steamOvenOne.steam;

            List<WorkModeBean> workModeBeans = new ArrayList<>();
            WorkModeBean workModeBean = new WorkModeBean();
            workModeBean.key = "模式";
            workModeBean.value = modeEnum.getValue();
            workModeBeans.add(workModeBean);
            //除垢模式
            if (modeEnum == SteamOvenModeEnum.CHUGOU) {
                setTopMode(workModeBeans);
                chugouState();
                return;
            }

            if (modeEnum == SteamOvenModeEnum.EXP) {    //EXP
                workModeBean = new WorkModeBean();
                workModeBean.key = "上温度";
                workModeBean.value = String.valueOf(setUpTemp);
                workModeBean.unit = "℃";
                workModeBeans.add(workModeBean);

                workModeBean = new WorkModeBean();
                workModeBean.key = "下温度";
                workModeBean.value = String.valueOf(setDownTemp);
                workModeBean.unit = "℃";
                workModeBeans.add(workModeBean);
            } else if (modeEnum == SteamOvenModeEnum.ZHIKONGZHENG
                    || mode == 22 || mode == 23 || mode == 24) { //带蒸汽的模式

                workModeBean = new WorkModeBean();
                workModeBean.key = "蒸汽量";
                workModeBean.value = SteamOvenSteamEnum.match(steam).getValue();
                workModeBeans.add(workModeBean);

                workModeBean = new WorkModeBean();
                workModeBean.key = "温度";
                workModeBean.value = String.valueOf(setUpTemp);
                workModeBean.unit = "℃";
                workModeBeans.add(workModeBean);
            } else {
                workModeBean = new WorkModeBean();
                workModeBean.key = "温度";
                workModeBean.value = String.valueOf(setUpTemp);
                workModeBean.unit = "℃";
                workModeBeans.add(workModeBean);
            }

            workModeBean = new WorkModeBean();
            workModeBean.key = "时间";
            workModeBean.value = String.valueOf(setTime / 60);
            workModeBean.unit = "min";
            workModeBeans.add(workModeBean);
            setTopMode(workModeBeans);
        }
        if (steamOvenOne.setTime != 0) {
            setProgressMax(steamOvenOne.setTimeH * 256 + steamOvenOne.setTime);
            setProgressCur(steamOvenOne.restTimeH * 256 + steamOvenOne.restTime);
        }

        switch (steamOvenOne.workState) {
            case IntegStoveStatus.workState_order://预约中
                orderState();
                break;
            case IntegStoveStatus.workState_preheat://预热中
            case IntegStoveStatus.workState_preheat_time_out://预热暂停中
                preheatState();
                break;
            case IntegStoveStatus.workState_work://工作中
            case IntegStoveStatus.workState_work_time_out://工作暂停
                //工作中
                workState();
                break;
            default:
                finish();
                break;
        }


    }

    /**
     * 除垢状态
     */
    private void chugouState() {
        short chugouType = steamOvenOne.chugouType;
        String chugouMessage = "";
        chugouMessage = ChuGouEnum.match(chugouType) == null ? "" : ChuGouEnum.match(chugouType).getValue();
        workTimeView.setMessage("除垢中");
        hideButton();
        if (steamOvenOne.workState == IntegStoveStatus.workState_work) {
            if (ChuGouEnum.match(chugouType) != ChuGouEnum.CHUGOU1) {
                String[] split = chugouMessage.split("&");
                if (StringUtil.isEmpty(chugouMessage) || split == null || split.length < 2) {
                    return;
                }
                setMessage("", workTimeView, split[1]);
            } else {
                setMessage("", workTimeView, chugouMessage);
            }
        } else if (steamOvenOne.workState == IntegStoveStatus.workState_work_time_out) {
            if (ChuGouEnum.match(chugouType) != ChuGouEnum.CHUGOU1) {
                String[] split = chugouMessage.split("。");
                if (StringUtil.isEmpty(chugouMessage) || split == null || split.length < 2) {
                    return;
                }
                setMessage("", workTimeView, split[0]);
            } else {
                setMessage("", workTimeView, chugouMessage);
            }
        }
    }

    /**
     * 预热中 预热暂停中
     */
    private void preheatState() {
        int outTime = 0;
        if (steamOvenOne.recipeId != 0) {
            outTime = steamOvenOne.totalRemain;
        } else {
            outTime = steamOvenOne.restTimeH * 256 + steamOvenOne.restTime;
        }
        int outTimeMinu = outTime / 60;
        if (outTimeMinu < 60) {
            workTimeView.setTvMinu(outTimeMinu);
        } else {
            int hour = outTimeMinu / 60;
            int minu = outTimeMinu % 60;
            workTimeView.setTvHourMinu(hour, minu);
        }
        setProgressCur(outTime);
        setMessage("剩余工作时长"
                , workTimeView
                , steamOvenOne.workState == IntegStoveStatus.workState_preheat ? "预热中" : "暂停中");
        setButtonText("结束工作", steamOvenOne.workState == IntegStoveStatus.workState_preheat ? "暂停" : "继续");
    }

    /**
     * 工作中 暂停中
     */
    private void workState() {
        int outTime = 0;
        if (steamOvenOne.recipeId != 0) {
            outTime = steamOvenOne.totalRemain;
        } else {
            outTime = steamOvenOne.restTimeH * 256 + steamOvenOne.restTime;
        }
        int outTimeMinu = outTime / 60;
        if (outTimeMinu < 60) {
            workTimeView.setTvMinu(outTimeMinu == 0 ? 1 : outTimeMinu);
        } else {
            int hour = outTimeMinu / 60;
            int minu = outTimeMinu % 60;
            workTimeView.setTvHourMinu(hour, minu);
        }
        setProgressCur(outTime);
        //设置进度条暂停
        if (steamOvenOne.workState == IntegStoveStatus.workState_work) {
            setProgressStart();
        } else {
            setProgressPause();
        }

        SteamOvenModeEnum match = SteamOvenModeEnum.match(steamOvenOne.workModel);
        if (match == SteamOvenModeEnum.CHUGOU) {
            setMessage("剩余工作时长"
                    , workTimeView
                    , steamOvenOne.workState == IntegStoveStatus.workState_work ? "工作中" : "暂停中");
        } else {
            setMessage("剩余工作时长"
                    , workTimeView
                    , steamOvenOne.workState == IntegStoveStatus.workState_work ? "工作中" : "暂停中");
        }
        setButtonText("结束工作", steamOvenOne.workState == IntegStoveStatus.workState_work ? "暂停" : "继续");
    }

    /**
     * 预约中
     */
    private void orderState() {
        int orderLeftTime = steamOvenOne.orderLeftTime;
        long orderData = TimeUtils.getCurrentTimeInLong() + orderLeftTime * 1000L;
        String time = TimeUtils.getTime(orderData, TimeUtils.SDF_TIME_2);
        workTimeView.setMessage(time);
        setMessage("开始工作时间", workTimeView, "预约中");
        setButtonText("结束预约", "立即启动");
    }

    /**
     * 左按钮点击事件
     *
     * @param btnLeft
     */
    @Override
    protected void onControlLeftClick(Button btnLeft) {
        toast("left");
        finishWork();
    }

    /**
     * 右边按钮点击事件
     *
     * @param btnRight
     */
    @Override
    protected void onControlRightClick(Button btnRight) {
        toast("right");
        pauseAndContinueWork();
    }

    /**
     * 一个按钮的点击事件
     *
     * @param one
     */
    @Override
    protected void onControlOneClick(Button one) {
        toast("one");
    }

    /**
     * 结束工作
     */
    private void finishWork() {
        IRokiDialog closedialog = RokiDialogFactory.createDialogByType(this, DialogUtil.DIALOG_TYPE_10);
        closedialog.setTitleText(R.string.close_work);
        closedialog.setContentText(R.string.is_close_work);
        closedialog.show();
        closedialog.setOkBtn(R.string.ok_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }

                steamOvenOne.setSteamWorkStatus(IntegStoveStatus.workCtrl_stop, (short) 4, new VoidCallback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(Throwable t) {
                    }
                });
            }
        });
        closedialog.setCancelBtn(R.string.can_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (closedialog.isShow()) {
                    closedialog.dismiss();
                }
            }
        });
    }

    /**
     * 开始或者暂停
     */
    private void pauseAndContinueWork() {
        if ((steamOvenOne.workState == IntegStoveStatus.workState_order)) {
            steamOvenOne.setSteamWorkStatus(IntegStoveStatus.workCtrl_start, (short) 4, new VoidCallback() {
                @Override
                public void onSuccess() {
                    toast("启动成功");
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        } else {
            short ctrl = 0;
            if ((steamOvenOne.workState == IntegStoveStatus.workState_preheat_time_out)
                    || (steamOvenOne.workState == IntegStoveStatus.workState_work_time_out)
            ) {
                ctrl = IntegStoveStatus.workCtrl_continue;
            } else {
                ctrl = IntegStoveStatus.workCtrl_time_out;
            }

            steamOvenOne.setSteamWorkStatus(ctrl, (short) 4, new VoidCallback() {
                @Override
                public void onSuccess() {
                    toast("启动成功");
                }

                @Override
                public void onFailure(Throwable t) {
                }
            });
        }
    }
}
