package com.robam.roki.ui.activity3.device.base;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.legent.Callback;
import com.legent.plat.Plat;
import com.legent.plat.io.cloud.Reponses;
import com.legent.utils.JsonUtils;
import com.robam.roki.ui.activity3.device.base.table.FunctionEntity;
import com.robam.roki.utils.DataUtils;

import org.litepal.LitePal;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/06/07
 *     desc   : 获取设备参数类
 *     version: 1.0
 * </pre>
 */
public class DeviceParamManage {
    private OnParamListener onParamListener ;
    private Context context ;
    public interface OnParamListener{
        void onLoadData(Reponses.DeviceResponse deviceResponse);
        void onFail();
    }


    public DeviceParamManage(OnParamListener onParamListener) {
        this.onParamListener = onParamListener;
    }

    /**
     * 缓存基类获取数据
     */
    protected void getDataMethod(long userId , String mDt , String mDc) {
        //从缓存中获取设备参数
//        FunctionEntity functionEntity = LitePal.where("deviceType = ? ", mDt).findFirst(FunctionEntity.class);
//        if (functionEntity != null){
//            try {
//                Reponses.DeviceResponse deviceResponse = JsonUtils.json2Pojo(functionEntity.param, Reponses.DeviceResponse.class);
//                if (onParamListener != null){
//                    onParamListener.onLoadData(deviceResponse);
//                }
//                Log.i("DeviceParamManage" , "FunctionEntity Success") ;
//                return;
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.i("DeviceParamManage" , e.getMessage()) ;
//            }
//        }
        Plat.deviceService.getDeviceByParams(userId, mDt, mDc, new Callback<Reponses.DeviceResponse>() {
            @Override
            public void onSuccess(Reponses.DeviceResponse deviceResponse) {
                if (deviceResponse == null) return;
                //保存当前功能数据
                FunctionEntity functionEntity = new FunctionEntity();
                functionEntity.param = deviceResponse.toString();
                functionEntity.version = deviceResponse.version ;
                functionEntity.deviceType = mDt ;
                functionEntity.save() ;
                Log.i("DeviceParamManage" , "http Success") ;
                //回调请求的设备功能对象
                if (onParamListener != null){
                   onParamListener.onLoadData(deviceResponse);
               }

            }

            @Override
            public void onFailure(Throwable t) {
                if (onParamListener != null){
                    onParamListener.onFail();
                }
            }
        });
    }
}
