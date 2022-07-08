package com.robam.roki.net.base;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.robam.roki.MobApp;
import com.robam.roki.net.OnRequestCallback;
import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.RequestFactory;
import com.robam.roki.net.model.ResponseInfo;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Map;

public class BaseApi implements OnRequestCallback {

    protected WeakReference<OnRequestListener> mListener;
    public BaseApi(OnRequestListener mOnRequestListener)
    {
        this.mListener = new WeakReference(mOnRequestListener);
    }
    @Override
    public void onFailure(int paramInt1, int paramInt2, @Nullable String msg, @Nullable Object data) {
        if (mListener.get()!=null)
        mListener.get().onFailure(paramInt1,paramInt2,msg,data);
    }
    private boolean checkValid()
    {
        WeakReference localWeakReference = this.mListener;
        if ((localWeakReference != null) && (localWeakReference.get() != null))
        {
            if ((this.mListener.get() instanceof Activity)) {
                return ((Activity)this.mListener.get()).isFinishing() ^ true;
            }
            if ((this.mListener.get() instanceof Fragment)) {
                return ((Fragment)this.mListener.get()).isAdded();
            }
            return true;
        }
        return false;
    }
    @Override
    public void onSuccess(int paramInt1, int paramInt2, @Nullable Object data) {

        if (mListener.get()!=null)
        mListener.get().onSuccess(paramInt1,paramInt2,data);
    }

    private static int mRequestId = 0;
    public int generateRequestId()
    {
        int i = mRequestId;
        int j = i + 1;
        mRequestId = j;
        if (j > 2147483632) {
            mRequestId = 0;
        }
        return i;
    }
    protected int doPost(int mRequestId, String url, BaseParam paramBaseParam, Class<?> paramType){
        return RequestFactory.getRequest(MobApp.getInstance()).doPost(mRequestId,url, paramBaseParam, paramType, this, ResponseInfo.class);
    }



    protected int doGet(int requestCode, String url, Map<String,Object> request, Class<?> paramType){
        return RequestFactory.getRequest(MobApp.getInstance()).doGet(requestCode,url, request,paramType,this, ResponseInfo.class);
    }
    protected int doDel(int requestCode, String url, Map<String,Object> request, Class<?> paramType){
        return RequestFactory.getRequest(MobApp.getInstance()).doDel(requestCode,url, request,paramType,this, ResponseInfo.class);
    }

    protected int doFilePost(int requestCode, String url, Map<String,Object> request,Class<?> paramType){
        return RequestFactory.getRequest(MobApp.getInstance()).doFilePost(requestCode,url,request,paramType,this, ResponseInfo.class);
    }
//    public  int doPost(int paramInt1, int paramInt2, String paramString,
//                               HashMap<String, String> paramHashMap1, HashMap<String, String> paramHashMap2,
//                               Class<?> paramClass, Type paramType, OnRequestCallback paramOnRequestCallback){
//
//
//
//    }

}
