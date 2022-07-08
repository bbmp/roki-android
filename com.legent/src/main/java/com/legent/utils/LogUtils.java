package com.legent.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.base.Strings;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by sylar on 15/6/23.
 */
public class LogUtils {
    private static boolean debug = true;
    final static SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    final static SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    public static final String DEFAULT_PATH = "log";
    public static final String DEFAULT_File = "log";
    public static final String DEFAULT_SUFFIX = ".txt";
    public static final String UPLOAD_File = "upload_log";

    public static void logFIleWithTime(String content) {
        logFIleWithTime(getPath(), content);
    }
    public static void uploadLogFIleWithTime(String content) {
        logFIleWithTime(getUploadPath(), content);
    }
    public static void logFIleWithTime(String path, String content) {
        logFIleWithTime(path, content, true);
    }

    public static void logFIleWithTime(String path, String content, boolean isAppend) {

        String time = SDF_TIME.format(Calendar.getInstance().getTime());
        content = String.format("%s:\t %s\n", time, content);
        logFile(path, content, isAppend);
    }

    public static void logFile(String path, String content, boolean isAppend) {
        if (Strings.isNullOrEmpty(content)) return;
//        FileUtils.writeFile(path, content, isAppend);
        FileUtils.writeFile(path, content, isAppend);

    }


    static String getPath() {
        String path = Environment.getExternalStorageDirectory()
                .getPath()
                .concat(File.separator)
                .concat(DEFAULT_PATH)
                .concat(File.separator)
                .concat(DEFAULT_File)
                .concat(String.format("_%s", SDF_DATE.format(Calendar.getInstance().getTime())))
                .concat(DEFAULT_SUFFIX);
        return path;
    }
    public static String getUploadPath() {
        String path = Environment.getExternalStorageDirectory()
                .getPath()
                .concat(File.separator)
                .concat(DEFAULT_PATH)
                .concat(File.separator)
                .concat(UPLOAD_File)
                .concat(String.format("_%s", SDF_DATE.format(Calendar.getInstance().getTime())))
                .concat(DEFAULT_SUFFIX);
        return path;
    }
    public static String getUploadPath2() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE,-1);
        String date2= SDF_DATE.format(calendar.getTime());

        String path = Environment.getExternalStorageDirectory()
                .getPath()
                .concat(File.separator)
                .concat(DEFAULT_PATH)
                .concat(File.separator)
                .concat(UPLOAD_File)
                .concat(String.format("_%s", date2))
                .concat(DEFAULT_SUFFIX);
        return path;
    }
    public static void i(String key, String value) {
        if (!debug) return;
        if (!TextUtils.isEmpty(value)) {
            Log.i(key, value);
        }
    }

    public static void out(String value) {
        if (!debug) return;
        Log.i("roki_rent", value);
    }

    public static void var(String value) {
        if (!debug) return;
        Log.i("var:", value);
    }
}
