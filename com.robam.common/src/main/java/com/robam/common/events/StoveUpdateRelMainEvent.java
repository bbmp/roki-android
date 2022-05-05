package com.robam.common.events;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/4/20.
 */
public class StoveUpdateRelMainEvent {
    Context context;
    View relMainView;
    int stepIndex;

    public StoveUpdateRelMainEvent(Context context, View relMainView, int stepIndex) {
        this.context = context;
        this.relMainView = relMainView;
        this.stepIndex = stepIndex;
    }

    public Context getContext() {
        return context;
    }

    public View getRelMainView() {
        return relMainView;
    }

    public int getStepIndex() {
        return stepIndex;
    }
}
