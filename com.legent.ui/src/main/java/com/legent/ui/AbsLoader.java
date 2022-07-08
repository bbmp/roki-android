package com.legent.ui;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.legent.services.AbsService;
import com.legent.ui.pojos.PageInfo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

abstract public class AbsLoader extends AbsService implements ILayoutLoader {

    static final String TAG = UI.TAG;

    protected FragmentActivity main;
    protected FragmentManager fm;
    protected Resources r;
    protected String pageKey;
    protected String loaderKey;

    public void setKey(String key) {
        this.loaderKey = key;
    }

    @Override
    public void layout(FragmentActivity main) {
        this.main = main;
        this.fm = main.getSupportFragmentManager();
        this.r = main.getResources();
    }

    synchronized protected IPage addFragment(int containerResId,
                                             String pageKey, Bundle args) {

        Preconditions.checkNotNull(pageKey);
        IPage page = UIService.getInstance().createPage(pageKey);
        Fragment frm = null;
        frm = (Fragment) page;
        String frmTag = pageKey;
//        if (fm.findFragmentByTag(frmTag) != null && !"DeviceSubCooker".equals(frmTag)) {
//            Log.d(TAG, String.format("page exist: %s", pageKey));
//            return page;
//        }

        if (args != null) {
            frm.setArguments(args);
        }

        FragmentTransaction ft = null;
        ft = fm.beginTransaction();
        setPageAnim(pageKey, ft);

        boolean isAdded = false;
        isAdded = frm.isAdded();
        if (isAdded) {
            ft.detach(frm).add(containerResId, frm, frmTag)
                    .addToBackStack(frmTag).attach(frm)
                    .commitAllowingStateLoss();

            Log.d(TAG,
                    String.format("page added: %s", pageKey));
        } else {
            ft.add(containerResId, frm, frmTag).addToBackStack(frmTag)
                    .commitAllowingStateLoss();

            Log.d(TAG, String.format("page add: %s", pageKey));
        }
        Log.i("onViewCreated", "page" + page);
        return page;
    }

    /**
     * rent添加 20160624 不影响其他代码
     *
     * @param containerResId
     * @param pageKey
     * @param args
     * @return
     */
    synchronized protected IPage returnHomeAndaddFragment(int containerResId,
                                                          FragmentTransaction ft, String pageKey, Bundle args) {

        Preconditions.checkNotNull(pageKey);
        IPage page = UIService.getInstance().createPage(pageKey);
        Fragment frm = null;
        frm = (Fragment) page;

        String frmTag = pageKey;

        if (fm.findFragmentByTag(frmTag) != null) {
            Log.d(TAG, String.format("page exist: %s", pageKey));
            return page;
        }

        if (args != null) {
            frm.setArguments(args);
        }

        //ft = fm.beginTransaction();
        setPageAnim(pageKey, ft);

        boolean isAdded = false;
        isAdded = frm.isAdded();
        if (isAdded) {
            ft.detach(frm).add(containerResId, frm, frmTag)
                    .addToBackStack(frmTag).attach(frm)
                    .commitAllowingStateLoss();

            Log.d(TAG, String.format("page added: %s", pageKey));
        } else {
            ft.add(containerResId, frm, frmTag).addToBackStack(frmTag)
                    .commitAllowingStateLoss();

            Log.d(TAG, String.format("page add: %s", pageKey));
        }

        return page;
    }

    @Override
    public void onPageInActivated(String pageKey) {

    }

    @Override
    public void onPageActivated(String pageKey) {
        this.pageKey = pageKey;
    }

    @Override
    public String toString() {
        if (!Strings.isNullOrEmpty(loaderKey))
            return loaderKey;
        else
            return getClass().getName();
    }

    @SuppressLint("ResourceType")
    void setPageAnim(String pageKey, FragmentTransaction ft) {
        PageInfo pi = UIService.getInstance().getPageInfo(pageKey);
//        int pageIn = pi.getAnimInResId(cx);
//        int pageOut = pi.getAnimOutResId(cx);

//        int animIn = pageIn > 0 ? pageIn : android.R.anim.fade_in;
//        int animOut = pageOut > 0 ? pageOut : android.R.anim.fade_out;

//        ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.setCustomAnimations(animIn, animOut);
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
    }

}
