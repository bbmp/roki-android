package com.legent.plat.services;

import com.legent.plat.pojos.dictionary.ResultCode;
import com.legent.pojos.AbsPojoManagerWithMap;
import com.legent.utils.LogUtils;

public class ResultCodeManager extends AbsPojoManagerWithMap<ResultCode, Integer> {


    /**
     * 成功
     */
    static public final int EC_RC_Success = 0;

    /**
     * 失败
     */
    static public final int EC_RC_Failure = 1;

    /**
     * 通路异常
     */
    static public final int EC_ConnectError = -1;

    /**
     * 设备通讯异常
     */
    static public final int EC_DeviceIOError = -2;


    /**
     * 云端访问异常
     */
    static public final int EC_RestfulError = -3;

    /**
     * 第三方登录已取消
     */
    static public final int EC_CancelAuth3rd = -4;


    static public boolean isSuccessRC(int rc) {
        return rc == EC_RC_Success;
    }


    static private ResultCodeManager instance = new ResultCodeManager();

    synchronized static public ResultCodeManager getInstance() {
        return instance;
    }

    private ResultCodeManager() {
    }

    public String getDescription(int code) {
        ResultCode rc = queryById(code);
        return rc == null ? null : rc.getName();
    }
}
