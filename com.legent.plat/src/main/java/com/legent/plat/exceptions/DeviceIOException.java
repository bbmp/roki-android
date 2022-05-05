package com.legent.plat.exceptions;
import com.legent.plat.services.ResultCodeManager;

/**
 * 设备通讯调用异常
 *
 * @author sylar
 *
 */
@SuppressWarnings("serial")
 public class DeviceIOException extends PlatException {

    public DeviceIOException(String errorMsg) {
        super(ResultCodeManager.EC_DeviceIOError, errorMsg);
    }
}
