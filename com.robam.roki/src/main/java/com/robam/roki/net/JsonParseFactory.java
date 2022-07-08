package com.robam.roki.net;

import com.robam.roki.net.Impl.JsonParseImpl;

public class JsonParseFactory {
    private static JsonParse mInstance;

    public static JsonParse getInstance()
    {
        try
        {
            if (mInstance == null) {
                mInstance = new JsonParseImpl();
            }
            return mInstance;
        }
        finally {}
    }
}
