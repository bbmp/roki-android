package com.legent.events;

import android.content.Intent;

/**
 * Created by sylar on 15/8/7.
 */
public class ActivityResultOnPageEvent {

    public int requestCode;
    public int resultCode;
    public Intent intent;

    public ActivityResultOnPageEvent(int requestCode, int resultCode,
                                     Intent intent) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.intent = intent;
    }

}