package com.robam.roki.ui.page.login.action;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public interface BundleAction {
    @Nullable
    Bundle getBundle();

    default int getInt(String name) {
        return getInt(name, 0);
    }

    default int getInt(String name, int defaultValue) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return defaultValue;
        return bundle.getInt(name, defaultValue);
    }

    default long getLong(String name) {
        return getLong(name, 0);
    }

    default long getLong(String name, int defaultValue) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return defaultValue;
        return bundle.getLong(name, defaultValue);
    }

    default float getFloat(String name) {
        return getFloat(name, 0);
    }

    default float getFloat(String name, int defaultValue) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return defaultValue;
        return bundle.getFloat(name, defaultValue);
    }

    default double getDouble(String name) {
        return getDouble(name, 0);
    }

    default double getDouble(String name, int defaultValue) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return defaultValue;
        return bundle.getDouble(name, defaultValue);
    }

    default boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    default boolean getBoolean(String name, boolean defaultValue) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return defaultValue;
        return bundle.getBoolean(name, defaultValue);
    }

    default String getString(String name) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return null;
        return bundle.getString(name);
    }

    default <P extends android.os.Parcelable> P getParcelable(String name) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return null;
        return (P)bundle.getParcelable(name);
    }

    default <S extends java.io.Serializable> S getSerializable(String name) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return null;
        return (S)bundle.getSerializable(name);
    }

    default ArrayList<String> getStringArrayList(String name) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return null;
        return bundle.getStringArrayList(name);
    }

    default ArrayList<Integer> getIntegerArrayList(String name) {
        Bundle bundle = getBundle();
        if (bundle == null)
            return null;
        return bundle.getIntegerArrayList(name);
    }
}

