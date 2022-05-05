package com.legent.utils.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.Set;

public class PreferenceUtils {

    static Context cx;

    static SharedPreferences prefs;


    public static void init(Context cx) {
        PreferenceUtils.cx = cx;
        prefs = getPrefs(cx);
    }

    static SharedPreferences getPrefs(Context cx) {
        return PreferenceManager.getDefaultSharedPreferences(cx);
    }

    public static boolean containKey(String key) {
        return prefs.contains(key);
    }

    public static boolean getBool(String key, boolean defValue) {
        return prefs.getBoolean(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return prefs.getFloat(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return prefs.getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return prefs.getLong(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return prefs.getString(key, defValue);
    }

    public static Set<String> getStrings(String key, Set<String> defValue) {
        return prefs.getStringSet(key, defValue);
    }


    public static void setBool(String key, boolean value) {
        Editor edit = prefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static void setFloat(String key, float value) {
        Editor edit = prefs.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static void setInt(String key, int value) {
        Editor edit = prefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public static void setLong(String key, long value) {
        Editor edit = prefs.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static void setString(String key, String value) {
        Editor edit = prefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public static void setStrings(String key, Set<String> value) {
        Editor edit = prefs.edit();
        edit.putStringSet(key, value);
        edit.commit();
    }


    public static void remove(String key) {
        Editor edit = prefs.edit();
        edit.remove(key);
        edit.commit();
    }

    public static void clear() {
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }


}
