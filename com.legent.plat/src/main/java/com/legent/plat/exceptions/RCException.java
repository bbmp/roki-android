package com.legent.plat.exceptions;

/**
 * RC码异常
 *
 * @author sylar
 */
@SuppressWarnings("serial")
public class RCException extends PlatException {

    public RCException(int errorCode) {
        super(errorCode);
    }

}
