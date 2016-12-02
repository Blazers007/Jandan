package com.blazers.jandan.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Blazers on 2015/9/8 v0.1.
 */
public class TimeHelper {

    public static long ONE_DAY = 24 * 60 * 60 * 1000;    //24H
    public static long ONE_HOUR = 60 * 60 * 1000;      //1H
    public static long ONE_WHILE = 60 * 5 * 1000;      //5MIN
    public static long ONE_MIN = 60 * 1000;      //5MIN

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
    }

    public static Date stringToDateTime(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateString);
    }

    public static int dayAwayFromNowByString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(dateString);
        return (int) ((new Date().getTime() - date.getTime()) / ONE_DAY);
    }

    /**
     * @param dateString 日期字符串
     * @return 返回的是换算过后的时间指数
     */
    public static String getSocialTime(String dateString) {
        try {
            Date date = stringToDateTime(dateString);
            long time = date.getTime();
            long before = new Date().getTime() - time;
            if (before > 7 * ONE_DAY) // 7天前不做处理
                return dateString;
            if (before > ONE_DAY) {
                return before / ONE_DAY + "天前  " + date.getHours() + ":" + date.getMinutes();
            }
            if (before < ONE_WHILE) {
                return "刚刚";
            }
            if (before > ONE_HOUR) {
                return before / ONE_HOUR + "小时前";
            }
            return before / ONE_MIN + "分钟前";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }

    /**
     * @param hours 偏差的小时数量 之前为负数
     * @return 返回的毫秒数
     */
    public static long getTimeByOffsetHour(int hours) {
        long now = new Date().getTime();
        return ONE_HOUR * hours + now;
    }

    public static String getDate() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return String.format("%02d-%02d", (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTime(new Date(time));
        return String.format("%02d-%02d", (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static String getTime() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static String getTime(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(time));
        return String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
    }

    public static String getTimeWithSecond() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return String.format("%02d:%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
    }

    public static long currentTime() {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        return calendar.getTime().getTime();
    }

    public static boolean isTimeEnoughForRefreshing(String dateTime) {
        try {
            long time = stringToDateTime(dateTime).getTime();
//            Log.e("TIME", ">>>>>" + (currentTime() - time)/1000/60);
            return currentTime() - time > 10 * ONE_MIN;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static boolean isTimeEnoughForRefreshing(long time) {
        if (time == 0)
            return false;
        return currentTime() - time > 10 * ONE_MIN;
    }
}
