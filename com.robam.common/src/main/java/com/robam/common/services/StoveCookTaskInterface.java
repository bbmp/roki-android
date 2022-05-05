package com.robam.common.services;

import com.legent.VoidCallback;

/**
 * Created by as on 2017-04-07.
 */

interface StoveCookTaskInterface {
    void pause();

    void pause(VoidCallback callback);

    void onPause();

    void restore();

    void restore(VoidCallback callback);

    void onRestore();


    void back();

    void onBack();
}
