package com.robam.roki.net.request.api;

import com.robam.roki.net.OnRequestListener;
import com.robam.roki.net.base.BaseApi;
import com.robam.roki.net.base.BaseBean;
import com.robam.roki.net.request.bean.LinkageConfig;
import com.robam.roki.net.request.param.GetDeviceParamsParam;
import com.robam.roki.net.request.param.SetDeciceNameParam;
import com.robam.roki.net.request.param.SetLinkageConfigParam;

import java.util.HashMap;

/**
 * <pre>
 *     author : huxw
 *     e-mail : xwhu93@163.com
 *     time   : 2022/05/07
 *     desc   : 设备相关接口
 *     version: 1.0
 * </pre>
 */
public class DeviceApi extends BaseApi {
    /**
     * 设置设备名称code
     */
    public static int setDeviceNameCode = 1001 ;
    /**
     * 获取设备参数（功能）code
     */
    public static int getDeviceByParamsCode = 1002 ;
    /**
     * 获取烟机绑定联动配置
     */
    public static int getLinkageConfigCode = 1003 ;
    /**
     * 配置烟机联动信息
     */
    public static int setLinkageConfigCode = 1004 ;


    /**
     * 设置设备名称
     */
    String setDeviceName= "/rest/dms/api/device/name/update";
    /**
     * 获取设备参数（功能）
     */
    String getDeviceByParams = "/rest/dms/api/device-configuration/get-by-params/new";

    /**
     * 获取烟机绑定联动配置
     */
    String getLinkageConfig = "/rest/dms/api/linkage/config/%s";

    /**
     * 配置烟机联动信息
     */
    String setLinkageConfig = "/rest/dms/api/linkage/config";



    public DeviceApi(OnRequestListener mOnRequestListener) {
        super(mOnRequestListener);
    }

    /**
     * 设置设备名称
     * @param userId
     * @param guid
     * @param name
     */
    public void setDeciceName(long userId , String guid , String name){
        doPost(setDeviceNameCode
                , setDeviceName
                ,new SetDeciceNameParam(userId , guid , name )
                , BaseBean.class);
    }

    /**
     * 获取设备参数
     * @param userId
     * @param deviceType
     * @param deviceCategory
     */
    public void getDeviceByParams(long userId , String deviceType , String deviceCategory){
        doPost(getDeviceByParamsCode
                , getDeviceByParams
                ,new GetDeviceParamsParam(userId , deviceType , deviceCategory )
                , BaseBean.class);
    }


    /**
     * 获取设备参数
     * @param userId
     * @param deviceId
     */
    public void getLinkageConfig(long userId , String deviceId ){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId" ,userId);
        String format = String.format(getLinkageConfig, deviceId);
        doGet(getLinkageConfigCode
                , format
                , map
                , LinkageConfig.class);
    }

    /**
     * 设置联动
     * @param param
     */
    public void setLinkageConfig(SetLinkageConfigParam param){
        param.targetDeviceName = null;
        doPost(setLinkageConfigCode , setLinkageConfig , param , BaseBean.class);
    }
}
