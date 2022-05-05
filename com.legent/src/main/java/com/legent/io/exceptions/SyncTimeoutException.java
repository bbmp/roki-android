package com.legent.io.exceptions;

import java.util.concurrent.TimeoutException;

/**
 * Created by sylar on 15/8/7.
 */
public class SyncTimeoutException extends TimeoutException {

    public SyncTimeoutException() {
        super("通讯超时");
    }

    public SyncTimeoutException(String msg) {
        super(msg);
    }

}