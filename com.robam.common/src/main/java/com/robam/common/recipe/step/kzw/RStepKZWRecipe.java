package com.robam.common.recipe.step.kzw;

import androidx.annotation.NonNull;

import com.legent.VoidCallback4;
import com.legent.plat.pojos.device.IDevice;
import com.legent.plat.services.DeviceService;
import com.legent.utils.StringUtils;
import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.recipe.step.AbsRStepCook;
import com.robam.common.recipe.step.inter.callback.IStepCallback;
import com.robam.common.recipe.step.inter.callback.IStepCallback_KZW;
import com.robam.common.recipe.step.inter.callto.IRecipeStepKZW;

import java.util.ArrayList;

/**
 * Created by as on 2017-07-17.
 */

public abstract class RStepKZWRecipe extends AbsRStepCook implements IRecipeStepKZW {
    protected String mGuid;
    private IDevice mDevice;

    /**
     * @param cookIns   菜谱实例号码
     * @param cookSteps  菜谱步骤 不能为空
     * @param stepindex 菜谱步骤号
     * @param callback2
     */
    public RStepKZWRecipe(int cookIns, ArrayList<CookStep> cookSteps, int stepindex, @NonNull IStepCallback callback2) {
        super(cookIns, cookSteps, stepindex, callback2);
    }

    @Override
    protected void addDevice(String id, String... tag) {
        if (StringUtils.isNullOrEmpty(this.mGuid)) {
            this.mGuid = id;
            addDeviceRecipeMap(id, mRecipeInstance_Index);
        }
    }

    @Override
    protected void setDeviceCommand(VoidCallback4 callback2) {
        setDeviceParam(callback2);
    }

    abstract void setDeviceParam(VoidCallback4 callback2);


    @Override
    protected IDevice getDevice() {
        if (mDevice != null)
            return mDevice;
        return DeviceService.getInstance().lookupChild(this.mGuid);
    }

    /**
     * 发生报警  id 报警号 具体值根据具体子类设计
     */
    @Override
    protected void onWarn(int alarmId) {
        ((IStepCallback_KZW) mServiceCallback).onWarn(alarmId);
    }

    /**
     * 报警恢复
     */
    @Override
    protected void onWarnRecovery(int alarmId) {
        ((IStepCallback_KZW) mServiceCallback).onWarnRecovery(alarmId);
    }

    @Override
    protected void onException(int index) {

    }
}
