package com.legent.ui.ext.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.legent.events.PageChangedEvent;
import com.legent.utils.EventUtils;

/**
 * Created by as on 2017-01-04.
 */

public class ParentDialog extends Dialog {
    public boolean dismissNoListeren;

    public ParentDialog(Context context) {
        super(context);
        init();
    }

    public ParentDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected ParentDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    void init() {
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                if (!dismissNoListeren)
                    EventUtils.postEvent(new PageChangedEvent("dialogshow"));
            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!dismissNoListeren)
                    EventUtils.postEvent(new PageChangedEvent("dialogdismiss"));
            }
        });
    }
}



