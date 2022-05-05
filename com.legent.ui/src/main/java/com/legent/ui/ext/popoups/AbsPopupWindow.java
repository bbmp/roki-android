package com.legent.ui.ext.popoups;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

import com.legent.events.PageChangedEvent;
import com.legent.utils.EventUtils;

/**
 * Created by as on 2017-01-04.
 */

public class AbsPopupWindow extends PopupWindow {
    public AbsPopupWindow(Context context) {
        super(context);
        init();
    }

    public AbsPopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbsPopupWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AbsPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public AbsPopupWindow() {
        init();
    }

    public AbsPopupWindow(View contentView) {
        super(contentView);
    }

    public AbsPopupWindow(int width, int height) {
        super(width, height);
        init();
    }

    public AbsPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        init();
    }

    public AbsPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        init();
    }

    void init() {
        EventUtils.postEvent(new PageChangedEvent("dialogshow"));
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
            }
        });
    }
}
