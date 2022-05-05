package com.robam.common.recipe.step.stove;

import androidx.annotation.NonNull;

import com.google.common.eventbus.Subscribe;
import com.legent.VoidCallback4;
import com.legent.plat.pojos.device.IDevice;
import com.robam.common.events.PotStatusChangedEvent;
import com.robam.common.pojos.CookStep;
import com.robam.common.recipe.step.AbsRStepCook;
import com.robam.common.recipe.step.inter.callback.IStepCallback;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Dell on 2018/4/16.
 */

public class RStepPot  {

    public tempCall tempCall;

    public void setTempCallLister(RStepPot.tempCall tempCall) {
        this.tempCall = tempCall;
    }

    @Subscribe
    public void onEvent(PotStatusChangedEvent event) {
        if (tempCall!=null){
            tempCall.tempureCall(event.pojo.tempUp);
        }
    }

    public interface tempCall{
        void tempureCall(float tempture);
    }

}
