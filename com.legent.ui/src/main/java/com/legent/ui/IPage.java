package com.legent.ui;

import android.view.KeyEvent;

public interface IPage {

    String getPageKey();

    void setPageKey(String pageKey);

    String getPageTitle();

    void setPageTitle(String pageTitle);

    void onPageInActivated();

    void onPageActivated();

    boolean onKeyDown(int keyCode, KeyEvent event);

}