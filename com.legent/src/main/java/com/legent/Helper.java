package com.legent;

import com.legent.services.TaskService;

public class Helper {
    static TaskService ts = TaskService.getInstance();

    static public void onSuccess(final VoidCallback callback) {
        if (callback == null)
            return;

        ts.postUiTask(new Runnable() {

            @Override
            public void run() {
                callback.onSuccess();
            }
        });
    }


    static public void onFailure(final VoidCallback callback, final Throwable t) {
        if (callback == null)
            return;
        ts.postUiTask(new Runnable() {

            @Override
            public void run() {
                callback.onFailure(t);
            }
        });
    }

    static public <Result> void onSuccess(final Callback<Result> callback,
                                          final Result result) {
        if (callback == null)
            return;

        ts.postUiTask(new Runnable() {

            @Override
            public void run() {
                callback.onSuccess(result);
            }
        });
    }

    static public <Result> void onFailure(final Callback<Result> callback,
                                          final Throwable t) {
        if (callback == null)
            return;

        ts.postUiTask(new Runnable() {

            @Override
            public void run() {
                callback.onFailure(t);
            }
        });
    }

}
