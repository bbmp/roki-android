package com.robam.roki.net.Impl;

import com.google.gson.Gson;
import com.robam.roki.net.JsonParse;

import java.lang.reflect.Type;

public class JsonParseImpl implements JsonParse {
    private Gson mGson = new Gson();

    public <T> T fromJson(String paramString, Class<T> paramClass)
    {
        return (T)this.mGson.fromJson(paramString, paramClass);
    }

    public <T> T fromJson(String paramString, Type paramType)
    {
        return (T)this.mGson.fromJson(paramString, paramType);
    }

    public String toJson(Object paramObject)
    {
        return this.mGson.toJson(paramObject);
    }
}
