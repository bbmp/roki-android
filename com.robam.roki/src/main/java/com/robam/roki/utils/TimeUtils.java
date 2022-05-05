package com.robam.roki.utils;

import com.legent.utils.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dell on 2018/3/26.
 */

public class TimeUtils {
    public static synchronized TimeUtils getInstance() {
        return new TimeUtils();
    }

    public static String getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(now.getTime());
    }

    public static String timeToStr(short time) {
        String strTime = getTime(time);
        return strTime;
    }


    public static String getTime(int second) {
        if (second < 10) {
            return "00:00:0" + second;
        }
        if (second < 60) {
            return "00:00:" + second;
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return "00:0" + minute + ":0" + second;
                }
                return "00:0" + minute + ":" + second;
            }
            if (second < 10) {
                return "00:" + minute + ":0" + second;
            }
            return "00:" + minute + ":" + second;
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return "0" + hour + ":0" + minute + ":0" + second;
                }
                return "0" + hour + ":0" + minute + ":" + second;
            }
            if (second < 10) {
                return "0" + hour + ":" + minute + ":0" + second;
            }
            return "0" + hour + ":" + minute + ":" + second;
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + ":0" + minute + ":0" + second;
            }
            return hour + ":0" + minute + ":" + second;
        }
        if (second < 10) {
            return hour + ":" + minute + ":0" + second;
        }
        return hour + ":" + minute + ":" + second;
    }


    public static String timeforStr(short time) {
        int min = time;
        int hour = min / 60;
        if (hour != 0) {
            min = min % 60;
        }
        String strTime = String.format("%s:%s" + ":00", String.format("%02d", hour), String.format("%02d", min));
        return strTime;
    }

    public static String timeStr(short time) {
        int min = time / 60;
        int hour = min / 60;
        String strTime = String.format("%s:%s", String.format("%02d", hour), String.format("%02d", min));
        return strTime;
    }


    public static String timeFor(short time) {
        int min = time;
        int hour = time / 60;
        String strTime = String.format("%s:%s", String.format("%02d", hour), String.format("%02d", min));
        return strTime;
    }

    public static String min2HourM(short time) {
        String strTime;
        int min = time;
        int hour = min / 60;
        if (hour != 0) {
            min = min % 60;
        }

        if (hour == 0) {
            strTime = min+"分钟";
        } else {
            if (min == 0) {
                strTime = hour + "小时";
            } else {

                strTime = hour + "小时" + min+"分钟";
            }

        }

        return strTime;

    }

    public static String getNowTime(Date d) {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(d);
    }

    public static boolean getTimeCompare(String time1, String time2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(time1);
            dt2 = sdf.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtils.i("20180525", "dt1:" + dt1.getTime() + " dt2:" + dt2.getTime());
        return dt1.getTime() < dt2.getTime();
    }


    public static String se2HourMin(short time) {
        //小时
        int h = time / 3600;
        int vh = h;
        //分钟
        int m = time % 3600 / 60;
        int vm = m;

        //秒
        int s = time % 3600 % 60;
        if (s != 0) {
            vm += 1;
            if (vm % 60 != 0) {
                vh += 1;
            }
        }
        //小时str
        String sh = String.valueOf(h);
        String vsh = String.valueOf(vh);

        if (h < 10) {
            sh = "0" + h;
        }

        if (vh < 10) {
            vsh = "0" + vh;
        }


        //分Str
        String sm = String.valueOf(m);
        String vsm = String.valueOf(vm);

        if (m < 10) {
            sm = "0" + sm;
        }
        if (vm < 10) {
            vsm = "0" + vm;
        }

        //秒Str
        String ss = String.valueOf(s);
        if (s < 10) {
            ss = "0" + s;
        }
        return sh + ":" + vsm;

    }
}
