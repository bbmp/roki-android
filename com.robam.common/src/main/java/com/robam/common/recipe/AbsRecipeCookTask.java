package com.robam.common.recipe;

import android.widget.Toast;

import com.google.common.collect.Lists;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.services.AbsService;
import com.legent.utils.LogUtils;
import com.legent.utils.StringUtils;
import com.legent.utils.api.ToastUtils;
import com.robam.common.Utils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.PlatformCode;
import com.robam.common.pojos.Recipe;
import com.robam.common.recipe.inter.IAbsRecipeCookInterface;
import com.robam.common.recipe.inter.ICookingCallBack;
import com.robam.common.recipe.inter.IRecipe;
import com.robam.common.recipe.step.AbsRStepCook;
import com.robam.common.recipe.step.RecipeException;
import com.robam.common.recipe.step.inter.callback.IStepCallback;
import com.robam.common.recipe.step.inter.callback.IStepCallback_KZW;
import com.robam.common.recipe.step.inter.callback.IStepCallback_Stove;
import com.robam.common.recipe.step.inter.callto.IRecipeStep;
import com.robam.common.recipe.step.inter.callto.IRecipeStepKZW;
import com.robam.common.recipe.step.kzw.RStepMicroRecipe;
import com.robam.common.recipe.step.kzw.RStepOvenRecipe;
import com.robam.common.recipe.step.kzw.RStepSteamOvenOneRecipe;
import com.robam.common.recipe.step.kzw.RStepSteamRecipe;
import com.robam.common.recipe.step.stove.RStepStovePad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.robam.common.util.RecipeUtils.ifRecipeContainStep;

/**
 * Created by as on 2017-07-12.
 */

public class AbsRecipeCookTask extends AbsService implements IAbsRecipeCookInterface, Runnable {
   // Recipe mReciep;
    int mCookInstanceIndex;//做菜实例对象号
    ArrayList<CookStep> cookStepsTemp;
    @Override
    public int getmCurrentIndex() {
        return mCurrentIndex;
    }

    int mCurrentIndex = -1;//当前步骤号
    ICookingCallBack mViewCallBack;

    protected AbsRecipeCookTask(ArrayList<CookStep> cookSteps, ICookingCallBack callBack) {
        if (callBack == null || cookSteps.size()<1)
            throw new NullPointerException();
        this.cookStepsTemp = cookSteps;
        this.mViewCallBack = callBack;
        mCookInstanceIndex = addCookTaskToLIst(this);
    }

    public AbsRecipeCookTask(){}


    /**
     * 同时可支持多少道菜烹饪 默认 2,手机端和pad端可根据具体情况覆盖
     */
    protected static int getMaxCookinstance() {
        return 1;
    }

    /**
     * 当前正在做菜的实例集合
     */
    private static List<IAbsRecipeCookInterface> mRecipeCookingList = new ArrayList<IAbsRecipeCookInterface>();

    /**
     * 获取菜谱实例集合
     */
    public static List<IAbsRecipeCookInterface> getCookTaskLIst() {
        if (mRecipeCookingList == null)
            mRecipeCookingList = new ArrayList<IAbsRecipeCookInterface>();
        return mRecipeCookingList;
    }

    /**
     * 添加菜谱实例,并返回做菜实例索引值 步骤
     **/
    public synchronized int addCookTaskToLIst(IAbsRecipeCookInterface absRecipeCookTask) {
        mRecipeCookingList.add(absRecipeCookTask);
        return mRecipeCookingList.size() - 1;
    }


    ICookingCallBack mStepCallback;

    @Override
    public void run() {
        mCurrentIndex = 0;
        recipeDispatch(mCurrentIndex);
    }

    IRecipeStep mCurrentStep;//当前菜谱步骤对象

    //步骤切换
    protected void recipeDispatch(int stepIndex) {
        this.mCurrentIndex = stepIndex;
        mViewCallBack.onCurrentIndexStart(mCurrentIndex);
        List<CookStep> cookSteps = cookStepsTemp;
        CookStep cookStep = cookSteps.get(stepIndex);
        if (StringUtils.isNullOrEmpty(cookStep.getDc())
                || cookStep.getjs_PlatformCodes() == null
                || cookStep.getjs_PlatformCodes().size() <= 0) {
            LogUtils.i("20180330","cookStep::"+cookStep.getDc());
            if (mCurrentStep != null)
                mCurrentStep.close(false);
            //当前步骤没有设备
            mViewCallBack.ondisplay(mCurrentIndex,null);
        } else {
            LogUtils.i("20180330","cookStep11::"+cookStep.getDc());

            if (mCurrentStep != null)
                mCurrentStep.close(false);

            checkDeviceBefore(cookStep, stepIndex);
        }
    }

    protected Map<String, Object> mPreRunMap = new HashMap<String, Object>();

    /**
     * 运行前检查
     *
     * @param cookStep
     * @return
     */
    public void checkDeviceBefore(CookStep cookStep, final int stepIndex) {
        mViewCallBack.onPreStartCheck(prerun(cookStep), stepIndex, new RecipeDeviceSelect<String>() {
            @Override
            public void selectguid(String id, String... para) {
                if (StringUtils.isNullOrEmpty(id))
                    return;
                IDevice device = DeviceService.getInstance().lookupChild(id);
                if (!device.isConnected()){
                    ToastUtils.show("设备已离线", Toast.LENGTH_SHORT);
                    return;
                }
               // checkDeviceStatus(device);
                if (Utils.isStove(id)) {
                    LogUtils.i("20180413","id:"+para[0]);
                    mCurrentStep = new RStepStovePad(mCookInstanceIndex, cookStepsTemp, stepIndex, new RecipeStepCall_Stove());
                    mCurrentStep.run(id + "_" + para[0]);
                } else if (Utils.isOven(id)) {
                    mCurrentStep = new RStepOvenRecipe(mCookInstanceIndex, cookStepsTemp, stepIndex, new RecipeStepCall_KZW());
                    mCurrentStep.run(id);
                } else if (Utils.isSteam(id)) {
                    mCurrentStep = new RStepSteamRecipe(mCookInstanceIndex, cookStepsTemp, stepIndex, new RecipeStepCall_KZW());
                    mCurrentStep.run(id);
                } else if (Utils.isMicroWave(id)) {
                    mCurrentStep = new RStepMicroRecipe(mCookInstanceIndex, cookStepsTemp, stepIndex, new RecipeStepCall_KZW());
                    mCurrentStep.run(id);
                } else if (Utils.isSteamOvenMsg(id)) {
                    mCurrentStep = new RStepSteamOvenOneRecipe(mCookInstanceIndex, cookStepsTemp, stepIndex, new RecipeStepCall_KZW());
                    mCurrentStep.run(id);
                }
            }

            @Override
            public void cancelselect() {
//                recipeStep.refreshInit();
            }
        });
    }

    /*private void checkDeviceStatus(IDevice device){
        boolean deviceIsCon = DeviceStatusCheck.getInstance().getDeviceConnect(device.getDc());
        if (deviceIsCon){
            ToastUtils.showShort(getDeviceName(device.getDc())+"离线，连接后才可自动烹饪");
            UIService.getInstance().popBack();
            return;
        }
        //判断设备是否被占用
        //boolean occupyFlag = DeviceStatusCheck.getInstance().getStatus(device.getDc(),head[0]);
        if (occupyFlag){
            ToastUtils.showShort(getDeviceName(device.getDc())+"被占用，停止后才可自动烹饪");
            UIService.getInstance().popBack();
            return;
        }
    }*/

    private String getDeviceName(String dc){
        if ("RZQL".equals(dc)){
            return "电蒸箱";
        }else if ("RDKX".equals(dc)){
            return "电烤箱";
        }else if ("RWBL".equals(dc)){
            return "微波炉";
        }else if ("RZKY".equals(dc)){
            return "蒸烤一体机";
        }
        return null;
    }


    public Map<String, Object> prerun(CookStep cookStep) {
        if (mPreRunMap == null)
            mPreRunMap = new HashMap<String, Object>();
        mPreRunMap.clear();
        if (StringUtils.isNullOrEmpty(cookStep.getDc())
                || cookStep.getjs_PlatformCodes() == null
                || cookStep.getjs_PlatformCodes().size() <= 0) {
            mPreRunMap.put(IRecipe.RECIPE_STEP_DC, false);
            LogUtils.i("20180402","dc is null");
            return mPreRunMap;
        }
        List<IDevice> deviceList = Lists.newArrayList();
        deviceList.add(Utils.getDefaultStove());
        deviceList.add(Utils.getDefaultOven());
        deviceList.add(Utils.getDefaultSteam());
        deviceList.add(Utils.getDefaultMicrowave());
        deviceList.add(Utils.getDefaultSteameOven());

        if (deviceList.size() <= 0) {
            mPreRunMap.put(IRecipe.RECIPE_STEP_DC, true);
            mPreRunMap.put(IRecipe.DEVICE_IFHAS, false);
            return mPreRunMap;
        }
        Set<IDevice> devices = new HashSet<IDevice>();
        for (PlatformCode platformCode : cookStep.getjs_PlatformCodes()) {
            if (platformCode == null) continue;
            for (IDevice device : deviceList) {
                if (device == null) continue;
                if (device.getDc().equals(platformCode.deviceCategory)
                        && device.getDp().equals(platformCode.platCode)) {
                    devices.add(device);
                }
            }
        }

        if (devices.size() <= 0) {
            mPreRunMap.put(IRecipe.RECIPE_STEP_DC, true);
            mPreRunMap.put(IRecipe.DEVICE_IFHAS, false);
            return mPreRunMap;
        }
        mPreRunMap.put(IRecipe.RECIPE_STEP_DC, true);
        mPreRunMap.put(IRecipe.DEVICE_IFHAS, true);


        List<String> occu_list = new ArrayList<String>();
        List<String> avai_list = new ArrayList<String>();
        for (IDevice device : devices) {
            if (ifContainDevice(device))
                occu_list.add(device.getID());
            else
                avai_list.add(device.getID());
        }
        mPreRunMap.put(IRecipe.DEVICE_OCCUPY, occu_list);
        mPreRunMap.put(IRecipe.DEVICE_AVAILB, avai_list);
        return mPreRunMap;
    }

    protected boolean ifContainDevice(IDevice device) {
        /*if (device!=null){
            LogUtils.i("20180402","  d:"+AbsRStepCook.getDeviceRecipeMap().get(device.getID()));
          return DeviceStatusCheck.getInstance().getStatus(device.getDc(),null);
        }
        return false*/
        return AbsRStepCook.getDeviceRecipeMap().get(device.getID()) != null;
    }

    /**
     * 返回值： 0 成功 1 菜谱数据结构异常
     */
    @Override
    public int start() {
       /* if (!RecipeCheck.checkOnStart(mReciep)) {//菜谱数据结构简单判断
            //删除菜谱实例
            getCookTaskLIst().remove(this);
            return 1;
        }*/
        new Thread(this).run();
        return 0;
    }

    @Override
    public void pause() {
        if (mCurrentStep != null)
            mCurrentStep.pause();
    }

    @Override
    public void restore() {
        if (mCurrentStep != null)
            mCurrentStep.restore();
    }

    @Override
    public void stop() {
        if (mCurrentStep != null)
            mCurrentStep.refreshInit();
    }

    @Override
    public void close() {
        if (mCurrentStep != null){
            mCurrentStep.close(true);
        }

        getCookTaskLIst().remove(this);
    }

    @Override
    public void changeStep(int index) {
        LogUtils.i("20180409","index::"+index);
        if (cookStepsTemp.size() > index && index >= 0) {
            recipeDispatch(index);
        }
    }


    /**
     * 灶具菜谱逻辑回调
     */
    protected class RecipeStepCall_Stove extends RecipeStepCall implements IStepCallback_Stove {


    }

    /**
     * 烤蒸微菜谱逻辑回调
     */
    protected class RecipeStepCall_KZW extends RecipeStepCall implements IStepCallback_KZW {

        @Override
        public void onWarn(int n) {
            mViewCallBack.onWarn(n);
        }

        @Override
        public void onWarnRecovery(int n) {
            mViewCallBack.onWarnRecovery(n);

        }
    }

    public interface RecipeDeviceSelect<T extends String> {
        /**
         * 选择设备 t 具体guid
         */
        void selectguid(T t, String... head);

        /**
         * 取消选择
         */
        void cancelselect();
    }

    /**
     * 菜谱步骤回调
     */
    protected class RecipeStepCall implements IStepCallback {

        @Override
        public void oncheck(Map<String, Object> map, final IRecipeStep recipeStep) {
            mViewCallBack.onPreStartCheck(map, mCurrentIndex, new RecipeDeviceSelect<String>() {
                @Override
                public void selectguid(String id, String... para) {
                    if (para != null && para.length > 0) {
                        recipeStep.run(id + "_" + para[0]);
                    } else {
                        recipeStep.run(id);
                    }
                }

                @Override
                public void cancelselect() {
                    recipeStep.refreshInit();
                }
            });
        }

        @Override
        public void onStart(Object t) {
            if (t instanceof RecipeException.RecipeStepStartException) {
                mViewCallBack.onStart(1);
            } else
                mViewCallBack.onStart(t);
        }


        @Override
        public void onSet(Throwable t) {
            mViewCallBack.onPreSet(t, t != null ? null : new RecipeDeviceSelect() {
                @Override
                public void selectguid(String s, String... head) {//s值在此处可null
                    if (mCurrentStep instanceof IRecipeStepKZW) {
                        ((IRecipeStepKZW) mCurrentStep).setrun();
                    }
                }

                @Override
                public void cancelselect() {
                    mCurrentStep.refreshInit();
                }
            });
        }

        @Override
        public void onPolling(IDevice device) {
            mViewCallBack.onPolling(device);
        }

        @Override
        public void onRunning(int count) {
            mViewCallBack.onRunning(count);
        }

        @Override
        public void onDisconnect(IDevice device) {
            mViewCallBack.onDisconnect(device);
        }

        @Override
        public void onConnect() {
            mViewCallBack.onConnect();
        }

        @Override
        public void onException(int n) {
            mViewCallBack.onException(n);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            } finally {
                //mViewCallBack.onClose();
            }
        }

        @Override
        public void onPause(Throwable t) {
            mViewCallBack.onPause(t);
        }

        @Override
        public void onRestore(Throwable t) {
            mViewCallBack.onRestore(t);
        }

        @Override
        public void onComplete(int param) {
            mViewCallBack.onComplete(param);
        }

        @Override
        public void onRefresh() {
            mViewCallBack.onRefresh();
        }

        @Override
        public void onClose(int error) {
            mViewCallBack.onClose(error);
        }

    }

    protected static class RecipeCheck {
        static boolean checkOnStart(Recipe recipe) {
            return ifRecipeContainStep(recipe);
        }
    }
}