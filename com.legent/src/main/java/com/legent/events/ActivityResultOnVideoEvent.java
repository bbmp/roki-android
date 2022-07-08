package com.legent.events;

import android.content.Intent;

public class ActivityResultOnVideoEvent {

    public int requestCode;
    public int resultCode;
    public Intent intent;

    public ActivityResultOnVideoEvent(int requestCode, int resultCode,
                                     Intent intent) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.intent = intent;
    }
}
