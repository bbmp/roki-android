package com.legent.plat.exceptions;


import com.legent.plat.services.ResultCodeManager;

/**
 * restful调用异常
 *
 * @author sylar
 *
 */
@SuppressWarnings("serial")
 public class RestfulException extends PlatException {

    public RestfulException(String errorMsg) {
        super(ResultCodeManager.EC_RestfulError, errorMsg);
    }
    public RestfulException(int rcCode ,String errorMsg) {
        super(rcCode, errorMsg);
    }
}
