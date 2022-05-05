package com.legent.plat.exceptions;


import com.legent.plat.services.ResultCodeManager;

/**
 * Created by sylar on 15/8/7.
 */
public class ExceptionHelper {
    static public Exception newPlatException(int errorCode) {
        return new PlatException(errorCode);
    }

    static public Exception newConnectException() {
        return new PlatException(ResultCodeManager.EC_ConnectError);
    }

    static public Exception newDeviceIOException(String errorInfo) {
        return new DeviceIOException(errorInfo);
    }

    static public Exception newRestfulException(String errorInfo) {
        return new RestfulException(errorInfo);
    }
    static public Exception newRestfulException(int rcCode ,String errorInfo) {
        return new RestfulException( rcCode,errorInfo);
    }
    static public Exception newRCException(int rcCode) {
        return new RCException(rcCode);
    }

    static public Exception newRestfulNullException() {
        return new RestfulException("restful调用返回空值");
    }
}
