package com.robam.common.recipe.step;

import androidx.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback4;
import com.legent.plat.events.DeviceConnectionChangedEvent;
import com.legent.plat.pojos.device.IDevice;
import com.legent.services.AbsService;
import com.legent.services.TaskService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.recipe.step.inter.callback.IStepCallback;
import com.robam.common.recipe.step.inter.callto.IRecipeStep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by as on 2017-07-13.
 */

public abstract class AbsRStepCook extends AbsService implements IRecipeStep {
    protected Recipe mRecipe;
    protected CookStep mCookStep;
    protected int mRecipeInstance_Index;//菜谱实例号
    protected int mStepIndex;//菜谱步骤号
    protected boolean mIsCounting;//是否在运行(倒计时)
    protected IStepCallback mServiceCallback;//步骤实例回调
    protected boolean mIsPolling;//是否开启轮训
    protected int IFPREFLAG = 0;//临时变量 开发完成后删除 0 立即执行 1 预设 2 预设并生效或已经完全运行
    protected boolean mWaitFlag;//等待标志位

    protected final int MAX_SENDPARAM_NUM = 3;//最大下发次数
    protected final int MAX_SENDPARAM_INTERVAL = 800;//每次下发次数间隔
    protected int mFailureNum = 0;//下发失败计数
    protected Map<String, Object> mPreRunMap = new HashMap<String, Object>();//
    /**
     * 设备与菜谱实例索引对应
     */
    protected static Map<String, Integer> mDevice_RecipeMap = new HashMap<String, Integer>();

    public static Map<String, Integer> getDeviceRecipeMap() {
        if (mDevice_RecipeMap == null)
            return mDevice_RecipeMap = new HashMap<String, Integer>();
        return mDevice_RecipeMap;
    }

    /**
     * @param guid    设备 guid
     * @param cookIns 菜谱实例
     */
    public synchronized void addDeviceRecipeMap(String guid, int cookIns) {
        if (mDevice_RecipeMap == null)
            mDevice_RecipeMap = new HashMap<String, Integer>();
        if (mDevice_RecipeMap != null && mDevice_RecipeMap.size() > 0) {
            Iterator iterator = mDevice_RecipeMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                if (cookIns == (Integer) entry.getValue()) {
                    if (!guid.equals(entry.getKey())) {
                        iterator.remove();
                    }
                }
            }
        }
        mDevice_RecipeMap.put(guid, cookIns);
    }

    /**
     * 当前菜谱步骤实例集合
     */
    private static Map<Integer, IRecipeStep> mStep_CookingMap = new HashMap<Integer, IRecipeStep>();

    /**
     * 获取菜谱步骤实例集合
     */
    public Map<Integer, IRecipeStep> getCookTaskLIst() {
        if (mStep_CookingMap == null)
            mStep_CookingMap = new HashMap<Integer, IRecipeStep>();
        return mStep_CookingMap;
    }

    /**
     * 添加菜谱步骤实例,并返回步骤实例索引值
     **/
    public synchronized void addStepTaskToLIst(int cookIns, IRecipeStep stepins) {
        if (mStep_CookingMap == null)
            mStep_CookingMap = new HashMap<Integer, IRecipeStep>();
        mStep_CookingMap.put(cookIns, stepins);
        listToTask();
    }

    void listToTask() {
        if (mStep_CookingMap == null || mStep_CookingMap.size() <= 0) {
            Log.i("20171110", "步骤实例集合null");
        } else {
            Iterator iterator = mStep_CookingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                //遍历设备菜谱表 若有遗留设备则清空
                Log.i("20171110", "步骤实例集合:" + entry.getKey() + "  " + entry.getValue());
            }
        }
    }

    /**
     * @param cookIns   菜谱实例号码
     * @param cookSteps    烹饪步骤 不能为空
     * @param stepindex 菜谱步骤号
     */
    List<CookStep> steps;
    public AbsRStepCook(int cookIns, ArrayList<CookStep> cookSteps, int stepindex, @NonNull IStepCallback callback2) {
        if (cookSteps != null && stepindex >= 0) {
             steps = cookSteps;
            if (steps != null && steps.size() > 0 && steps.size() > stepindex) {
                this.mCookStep = steps.get(stepindex);
                this.mStepIndex = stepindex;
            }
        }
        //菜谱实例与步骤实例映射
        synchronized (this) {
            addStepTaskToLIst(cookIns, this);
            Map map = getDeviceRecipeMap();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                //遍历设备菜谱表 若有遗留设备则清空
                if (cookIns == (Integer) entry.getValue())
                    iterator.remove();
            }
        }
        this.mRecipeInstance_Index = cookIns;
        this.mServiceCallback = callback2;
    }

    @Override
    public void run(String id) {
        LogUtils.i("20180408","id::"+id);
        if (StringUtils.isNullOrEmpty(id))
            return;
        String[] ids = id.split("_");
        Log.i("20182018", "run:" + ids[0] + "  " + (ids.length > 1 ? ids[1] : null));
        addDevice(ids[0], ids.length > 1 ? ids[1] : null);
        setDeviceCommand(new VoidCallback4() {
            @Override
            public void onSuccess() {
                mIsPolling = true;
                mWaitFlag = true;
                mServiceCallback.onStart((short) 0);
                Log.i("stove_sts", "下发指令成功");
                LogUtils.i("20180414","下发指令成功:");
            }

            @Override
            public void onFailure(Object t) {
                mWaitFlag = false;
                mServiceCallback.onStart(t);
                LogUtils.i("20180414","下发指令失败:"+t.toString());
                Log.i("stove_sts", "下发指令失败:"+t.toString());
            }
        });
    }

    /**
     * 根据ID获取 设备信息，若无设备 返回null
     */
    protected abstract void addDevice(String id, String... headId);


    @Override
    public void close(boolean isOff) {
        if ("RRQZ".equals(mCookStep.getDc())){
            if (isOff){
                closeRRQZ();
            }else{
                closeDevice();
            }
        }else{
            closeDevice();
        }
        getCookTaskLIst().remove(mRecipeInstance_Index);
        Map device_Recipe = getDeviceRecipeMap();
        Iterator iterator = device_Recipe.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            if (mRecipeInstance_Index == (Integer) entry.getValue()) {
                iterator.remove();
            }
        }
        stopCountdown();
        //mIsPolling = false;
        IFPREFLAG = -1;
        try {
            this.dispose();
        } catch (Exception e) {

        }
//        this.dispose();

    }


    protected abstract void closeDevice();

    //关闭灶具
    protected void closeRRQZ(){

    }


    /**
     * 叶敏重新初始化
     */
    @Override
    public void refreshInit() {
        stopCountdown();
        mWaitFlag = false;
        mIsPolling = false;
        IFPREFLAG = 0;
        mServiceCallback.onRefresh();
    }

    /**
     * 正式开始前 对设备运行情况判断
     * 被占用设备集合字段 IRecipe.DEVICE_OCCUPY
     * 可用的设备 IRecipe.DEVICE_AVAILB
     */
    protected abstract Map<String, Object> prerun();

    /**
     * 下发参数
     */
    protected abstract void setDeviceCommand(VoidCallback4 callback);

    /**
     * 获取设备
     */
    protected abstract IDevice getDevice();

    /**
     * 倒计时完成时候 调用设备关闭程序
     */
    protected void onCountComplete() {
        mIsPolling = false;
    }

    /**
     * 发生异常(当前菜谱步骤已经停止)  index 具体值根据具体子类设计，，此处异常 不影响做菜进程，只是用来提醒等
     */
    protected abstract void onException(int index);

    /**
     * 发生报警  id 报警号 具体值根据具体子类设计
     */
    protected abstract void onWarn(int n);

    /**
     * 报警恢复
     */
    protected abstract void onWarnRecovery(int alarmId);


    protected ScheduledFuture<?> mFuture;//计时器
    protected int mRemainTime;

    /**
     * 开始计时
     */
    protected void startCountdown(final int needTime) {
        if (mFuture != null)
            return;
        mRemainTime = needTime;
        mFuture = TaskService.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.i("20171108", "startCountdown mRemainTime " + mRemainTime);
                mIsCounting = true;
                if (mRemainTime <= 0) {
                    stopCountdown();
                    onCountComplete();
                }
                onCount(mRemainTime);
                mRemainTime--;

            }
        }, 500, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 结束计时
     */
    protected void stopCountdown() {
        mIsCounting = false;
        Log.i("20171108", "stopCountdown " + mFuture);
        if (mFuture != null) {
            mFuture.cancel(true);
            mFuture = null;
        }
    }

    protected void onCount(final int count) {
        Log.i("20171106", "startCountdown " + count);
        mServiceCallback.onRunning(count);
    }

    /**
     * 设备联网情况
     */
    @Subscribe
    public void onEvent(DeviceConnectionChangedEvent event) {
        String id = getDevice().getID();
        if (id != null && Objects.equal(id, event.device.getID())) {
            if (event.isConnected)
                mServiceCallback.onConnect();
            else
                mServiceCallback.onDisconnect(event.device);
        }
    }

    /**
     * @param guid
     * @return 查看当前菜谱设备是否包含 guid
     */
    protected boolean ifContainDevice(String guid) {
        return getDeviceRecipeMap().get(guid) != null;
    }

    /**
     * @return 返回这道菜 需要的 全部时间
     */
    protected abstract int getNeedTime();
}
