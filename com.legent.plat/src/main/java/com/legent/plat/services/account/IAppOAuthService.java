package com.legent.plat.services.account;

import android.content.Context;

import com.legent.IDispose;
import com.legent.Initialization;

/**
 * Created by sylar on 15/8/17.
 */
public interface IAppOAuthService extends Initialization, IDispose {

    String TAG = "oauth";
    int PlatId_Self = 0;
    int PlatId_QQ = 1;
    int PlatId_Sina = 2;
    int PlatId_Ali = 3;

    void authorize(Context cx, int platId, AuthCallback callback);
    void removeAuth(int platId);
}