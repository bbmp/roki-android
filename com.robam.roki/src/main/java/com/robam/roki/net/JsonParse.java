package com.robam.roki.net;

import java.lang.reflect.Type;

public abstract interface JsonParse
{
    public abstract <T> T fromJson(String paramString, Class<T> paramClass);

    public abstract <T> T fromJson(String paramString, Type paramType);

    public abstract String toJson(Object paramObject);
}
