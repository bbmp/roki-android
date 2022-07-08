package com.legent.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.common.eventbus.Subscribe;
import com.legent.events.ConnectionModeChangedEvent;
import com.legent.services.ConnectivtyService;
import com.legent.utils.EventUtils;
import com.legent.utils.api.MemoryUtils;
import com.legent.utils.api.SoftInputUtils;

import java.lang.ref.WeakReference;
@SuppressLint("NewApi")
abstract public class AbsPage extends Fragment implements IPage {


    static protected class MyHandler extends Handler {
        WeakReference<Fragment> frm;

        public MyHandler() {

        }

        MyHandler(Fragment frm) {
            this.frm = new WeakReference<Fragment>(frm);
        }
    }

    protected String pageKey;
    protected String title;


//    private MyHandler handler;
    protected FragmentActivity activity;
    protected Context cx;
    protected Resources r;
    protected View rootView;

    @Override
    public String getPageKey() {
        return pageKey;
    }

    @Override
    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    @Override
    public void setPageTitle(String pageTitle) {
        this.title = pageTitle;
    }

    @Override
    public void onPageActivated() {

    }

    @Override
    public void onPageInActivated() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventUtils.regist(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            activity = getActivity();
        }
        cx = activity;
        r = cx.getResources();

//        handler = new MyHandler(this) {
//            @Override
//            public void handleMessage(Message msg) {
//                if (frm.get() == null) {
//                    return;
//                }
//                processMessage(msg);
//            }
//
//        };
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootView = view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventUtils.unregist(this);
//        handler = null;
        if (rootView != null) {
            MemoryUtils.disposeView(rootView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (rootView != null) {
            rootView.setOnTouchListener(null);
        }

        SoftInputUtils.hide(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (rootView != null) {
            rootView.setOnTouchListener(new OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    return true;
                }
            });
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
//        handler = null;
//        try {
//            java.lang.reflect.Field childFragmentManager = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
//                childFragmentManager = Fragment.class
//                        .getDeclaredField("mChildFragmentManager");
//            }
//            if (childFragmentManager != null) {
//                childFragmentManager.setAccessible(true);
//                childFragmentManager.set(this, null);
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Subscribe
    public void onEvent(ConnectionModeChangedEvent event) {
        int mode = event.connectionMode;
        switch (mode) {

            case ConnectivtyService.ConnectionMode_Broken:
                onConnectionBroken();
                break;
            case ConnectivtyService.ConnectionMode_Wifi:
                onConnectedByWifi();
                break;
            case ConnectivtyService.ConnectionMode_Mobil:
                onConnectedByMobil();
                break;

            default:
                break;
        }
    }

    protected void onConnectionBroken() {
    }

    protected void onConnectedByWifi() {
    }

    protected void onConnectedByMobil() {
    }


//    protected void sendEmptyMessage(int what) {
//        if (handler != null) {
//            handler.sendEmptyMessage(what);
//        }
//    }
//
//    protected void sendMessage(Message msg) {
//        if (handler != null) {
//            handler.sendMessage(msg);
//        }
//    }

    protected void processMessage(Message msg) {

    }

    protected void postEvent(Object event) {
        EventUtils.postEvent(event);
    }



}
