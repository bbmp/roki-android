package com.legent.ui.ext.loaders;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

import com.legent.ui.AbsLoader;
import com.legent.ui.IPage;
import com.legent.ui.R;
import com.legent.ui.UIService;
import com.legent.ui.ext.HeadPage;
import com.legent.ui.ext.views.TitleBar;

abstract public class AbsTitleBarLoader extends AbsLoader {

    abstract protected void onPageActivated(TitleBar bar, String pageKey);

    @Override
    public boolean toggleMenu() {
        return false;
    }

    @Override
    public IPage switchContent(String pageKey, Bundle args) {
        return addFragment(R.id.main_fragment, pageKey, args);
    }

    /**
     * rent添加 20160624 不印象其他代码
     */
    @Override
    public IPage returnAndSwitchContent(String pageKey, FragmentTransaction ft, Bundle args) {
        return returnHomeAndaddFragment(R.id.main_fragment, ft,pageKey, args);
    }

    @Override
    public void onPageActivated(String pageKey) {
        super.onPageActivated(pageKey);
        IPage page = UIService.getInstance().getPage(pageKey);
        if (page != null && page instanceof HeadPage) {

            HeadPage hp = (HeadPage) page;
            TitleBar bar = hp.getTitleBar();
            if (bar != null) {
                onPageActivated(bar, pageKey);
            }
        }
    }


}