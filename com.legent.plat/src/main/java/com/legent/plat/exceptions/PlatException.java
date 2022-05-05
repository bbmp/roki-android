package com.legent.plat.exceptions;

import com.google.common.base.Strings;
import com.legent.plat.services.ResultCodeManager;

/**
 * 平台异常
 *
 * @author sylar
 */
@SuppressWarnings("serial")
public class PlatException extends Exception {

    /**
     * 错误码
     */
    public int errorCode;

    /**
     * 异常详情
     */
    public String errorMsg;

    protected Object tag;

    public PlatException(int errorCode) {
        this(errorCode, null);
    }

    public PlatException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        String errInfo = ResultCodeManager.getInstance().getDescription(errorCode);

        String errString;
        if (!Strings.isNullOrEmpty(errInfo)
                && !Strings.isNullOrEmpty(errorMsg)) {
            errString = String.format("%s\t%s", errInfo, errorMsg);
        } else {
            errString = !Strings.isNullOrEmpty(errInfo) ? errInfo
                    : errorMsg;
        }

        if (Strings.isNullOrEmpty(errString)) {
            errString = "出了点小状况";
        }

        return errString;
    }

    public <T> T getTag() {
        return (T) tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}