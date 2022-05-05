package com.legent.plat.pojos.dictionary.msg;

/**
 * Created by sylar on 15/7/28.
 */
public enum ePT {

    /**
     * 1byte
     */
    BYTE,

    /**
     * 1byte
     * true    - 0x01
     * false   - 0x00
     */
    BOOL,

    /**
     * 1byte 或 2byte
     */
    SHORT,

    /**
     * 4byte
     */
    INT,

    /**
     * 4byte
     */
    FLOAT,

    /**
     * 8byte
     */
    LONG,

    /**
     * 8byte
     */
    DOUBLE,

    /**
     * n byte
     */
    BYTES,

    /**
     * n byte
     */
    STRING,

    /**
     * nested param
     */
    LIST,

}
