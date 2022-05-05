package com.legent.ui;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public interface ILayoutLoader {

    void layout(FragmentActivity main);

    boolean toggleMenu();

    IPage switchContent(String pageKey, Bundle args);

    void onPageInActivated(String pageKey);

    void onPageActivated(String pageKey);
    //rent 添加20160624 不影响其他代码
    IPage returnAndSwitchContent(String pageKey, FragmentTransaction ft, Bundle args);
}
