package com.robam.common.io.device;

import com.robam.common.Utils;

public class TerminalType {
    public final static short CLOUD = 10;
    public final static short APP = 11;
    public final static short PAD = 12;

    static public short getType() {
        return Utils.isMobApp() ? APP : PAD;
    }
}