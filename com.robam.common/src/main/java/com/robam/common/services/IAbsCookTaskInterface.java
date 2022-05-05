package com.robam.common.services;

import com.robam.common.pojos.CookStep;
import com.robam.common.pojos.Recipe;
import com.robam.common.pojos.device.Stove.Stove;

import java.util.ArrayList;

/**
 * Created by as on 2017-04-07.
 */

public interface IAbsCookTaskInterface {
    boolean isRunning();

    boolean isPause();

    int getStepCount();

    int getStepIndex();

    int getRemainTime();

    void start(Stove.StoveHead stoveHead, ArrayList<CookStep> steps,Long id);

    void start(Stove.StoveHead stoveHead, Recipe book,String str);

    void next();

    void stop();
}
