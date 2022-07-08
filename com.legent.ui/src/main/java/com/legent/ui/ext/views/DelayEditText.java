package com.legent.ui.ext.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class DelayEditText extends EditText {

    static final int MSGCODE = 0;

    public interface OnTextChangedCallback {
        void onTextChanged(String value);

        void onTextChangedWithoutDelay(String value);
    }

    int msgCount = 0;
    long delayMillis = 1800;
    OnTextChangedCallback callabck;

    public DelayEditText(Context context) {
        super(context);
        init();
    }

    public DelayEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public DelayEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
    }

    public void setDelay(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public void setOnTextChangedCallback(OnTextChangedCallback callabck) {
        this.callabck = callabck;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        if (!isInEditMode() && handler != null) {
            msgCount++;
            handler.sendEmptyMessageDelayed(MSGCODE, delayMillis);
        }

        if (callabck != null) {
            callabck.onTextChangedWithoutDelay(text.toString());
        }
    }

    MyHandler handler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            if (refObj.get() == null) return;
            if (msg.what == MSGCODE) {
                if (msgCount == 1) {
                    msgCount = 0;

                    if (callabck != null) {
                        callabck.onTextChanged(DelayEditText.this.getText()
                                .toString());
                    }

                } else {
                    msgCount--;
                }

            }
        }
    };


    static class MyHandler extends Handler {
        WeakReference<Object> refObj;

        MyHandler(Object obj) {
            this.refObj = new WeakReference<Object>(obj);
        }
    }


}
