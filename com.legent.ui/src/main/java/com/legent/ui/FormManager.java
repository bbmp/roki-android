package com.legent.ui;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentManager.OnBackStackChangedListener;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;

import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.legent.events.PageChangedEvent;
import com.legent.ui.events.MenuToggledEvent;
import com.legent.ui.pojos.FormInfo;
import com.legent.utils.EventUtils;

public class FormManager {

    private String formKey, homePageKey, curPageKey;
    private ILayoutLoader loader;
    private FragmentActivity main;
    private FragmentManager frmMng;
    private boolean isShownMenu;

    public FormManager(FormInfo formInfo) {
        formKey = formInfo.id;
        homePageKey = formInfo.homePageKey;
        loader = formInfo.getLoader();
    }

    public FragmentActivity getActivity() {
        return main;
    }

    public ILayoutLoader getLoader() {
        return loader;
    }

    public String getFormKey() {
        return formKey;
    }

    public String getHomeKey() {
        return homePageKey;
    }

    public String getCurrentPageKey() {
        return curPageKey;
    }

    public IPage getCurrentPage() {
        return getCachedPage(curPageKey);
    }

    public boolean isHome() {
        return isHomePage(curPageKey);
    }

    public boolean isHomePage(String pageKey) {
        return homePageKey.equals(pageKey);
    }

    public boolean isShownMenu() {
        return isShownMenu;
    }

    public boolean toggleMenu() {
        isShownMenu = loader.toggleMenu();
        EventUtils.postEvent(new MenuToggledEvent(isShownMenu));
        return isShownMenu;
    }

    public void attachActivity(FragmentActivity main, String firstPageKey) {

        this.main = main;
        frmMng = this.main.getSupportFragmentManager();

        int backStackCount = frmMng.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            frmMng.popBackStackImmediate();
        }

        frmMng.removeOnBackStackChangedListener(backStackListener);
        frmMng.addOnBackStackChangedListener(backStackListener);

        Log.i("app", "stack count of page:" + frmMng.getBackStackEntryCount());

        if (loader instanceof AbsLoader) {
            ((AbsLoader) loader).init(main);
        }

        loader.layout(main);

        postPage(homePageKey, null);
        if (!Strings.isNullOrEmpty(firstPageKey)) {
            postPage(firstPageKey, null);
        }

    }

    public void detachActivity() {

    }

    synchronized public void postPage(String pageKey) {
        postPage(pageKey, null);
    }

    synchronized public void postPage(String pageKey, Bundle args) {
        loader.switchContent(pageKey, args);
    }

    synchronized public void popBack() {
        int count = frmMng.getBackStackEntryCount();
        if (count > 1) {
            Log.d("fragment", String.format("page removed: %s", curPageKey));
//            FragmentTransaction ft = frmMng.beginTransaction().setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
            FragmentTransaction ft = frmMng.beginTransaction();
            IPage page = getCachedPage(curPageKey);
            if (page instanceof Fragment) {
                removePage(curPageKey, ft, (Fragment) page);
            }
            ft.commitAllowingStateLoss();
            try {
                frmMng.popBackStack();
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            main.finish();
        }
    }

    synchronized public void returnHome() {

        if (isHome())
            return;

        int count = frmMng.getBackStackEntryCount();

        // 回退至主界面，并释放其它界面
        if (count > 0) {
            FragmentTransaction ft = frmMng.beginTransaction();
            String frmTag;
            for (int i = count - 1; i >= 0; i--) {
                frmTag = frmMng.getBackStackEntryAt(i).getName();
                if (Strings.isNullOrEmpty(frmTag))
                    continue;
                if (Objects.equal(homePageKey, frmTag))
                    continue;

                Fragment frm = frmMng.findFragmentByTag(frmTag);
                removePage(frmTag, ft, frm);
            }
            ft.commitAllowingStateLoss();
            // 主界面不弹出，仍留在堆栈中
            frmMng.popBackStack(homePageKey, 0);
        }

        // 若堆栈中无主界面，则清空堆栈，并加载主界面
        if (frmMng.findFragmentByTag(homePageKey) == null) {
            frmMng.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            postPage(homePageKey);
        }
    }


    /**
     * rent 添加 20160624 不影响其他代码
     */
    synchronized public void returnHomeAndaddRragment(String pageKey, Bundle args) {

        if (isHome())
            return;

        int count = frmMng.getBackStackEntryCount();

        // 回退至主界面，并释放其它界面
        if (count > 0) {
            FragmentTransaction ft = frmMng.beginTransaction();
            String frmTag;
            for (int i = count - 1; i >= 0; i--) {
                frmTag = frmMng.getBackStackEntryAt(i).getName();
                if (Strings.isNullOrEmpty(frmTag))
                    continue;
                if (Objects.equal(homePageKey, frmTag))
                    continue;

                Fragment frm = frmMng.findFragmentByTag(frmTag);
                removePage(frmTag, ft, frm);
            }
            loader.returnAndSwitchContent(pageKey, ft, args);
            ft.commitAllowingStateLoss();
            // 主界面不弹出，仍留在堆栈中
            frmMng.popBackStack(homePageKey, 0);
        }

        // 若堆栈中无主界面，则清空堆栈，并加载主界面
        if (frmMng.findFragmentByTag(homePageKey) == null) {
            frmMng.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            postPage(homePageKey);
        }

    }

    private FragmentManager.OnBackStackChangedListener backStackListener = new FragmentManager.OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {
            onFragmentChanged();
        }
};

    private void onFragmentChanged() {

        int count = frmMng.getBackStackEntryCount();
        Log.d("fragment", String.format("page count: %s", count));

        if (count == 0)
            return;

        String pageKey = frmMng.getBackStackEntryAt(count - 1).getName();
        Log.d("fragment", String.format("page current: %s", pageKey));

        loader.onPageInActivated(curPageKey);
        IPage prePage = getCachedPage(curPageKey);
        if (prePage != null) {
            prePage.onPageInActivated();
        }
        curPageKey = pageKey;
        loader.onPageActivated(curPageKey);
        IPage curPage = getCachedPage(curPageKey);
        if (curPage == null) {
            UIService.getInstance().returnHome();
        }
        if (curPage != null) {
            try {//任涛 09 29日添加
                curPage.onPageActivated();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EventUtils.postEvent(new PageChangedEvent(curPageKey));

    }

    private IPage getCachedPage(String pageKey) {
        return UIService.getInstance().getPage(pageKey);
    }

    private void removePage(String pageKey, FragmentTransaction ft, Fragment frm) {
        if (frm != null) {
            if (frm instanceof IPage) {
                ((IPage) frm).onPageInActivated();
            }
            ft.remove(frm);
            frm = null;
        }
        UIService.getInstance().removePage(pageKey);
    }
}
