package com.legent.plat.services.account;


import com.legent.plat.pojos.PlatformUser;

/**
 * Created by sylar on 15/6/3.
 */
public interface AuthCallback {
    void onAuthCompleted(PlatformUser platUser);

    void onError(Throwable t);

    void onCancel();
}
