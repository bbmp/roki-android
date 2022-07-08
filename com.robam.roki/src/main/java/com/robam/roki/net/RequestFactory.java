package com.robam.roki.net;


import android.content.Context;

import com.robam.roki.net.Impl.ConnectorImpl;

public class RequestFactory
{
    private static Connector sInstance;

    public static final Connector getRequest(Context paramContext)
    {
        try
        {
            if (sInstance == null) {
                sInstance = ConnectorImpl.getConnector(paramContext);
            }
            return sInstance;
        }
        finally {}
    }
}