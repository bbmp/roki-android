package com.robam.roki.ui.page.login.action;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

public interface ActivityAction {
    Context getContext();

    default Activity getActivity() {
        Context context = getContext();
        while (true) {
            if (context instanceof Activity)
                return (Activity)context;
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper)context).getBaseContext();
            } else {
                return null;
            }
            if (context == null)
                return null;
        }
    }

    default void startActivity(Class<? extends Activity> clazz) {
        startActivity(new Intent(getContext(), clazz));
    }

    default void startActivity(Intent intent) {
        if (!(getContext() instanceof Activity))
            intent.addFlags(268435456);
        getContext().startActivity(intent);
    }
}
