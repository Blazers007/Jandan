package com.blazers.jandan.util;

import android.support.v4.util.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Blazers on 2015/9/8.
 */
public class TimeHelper {

    private static long ONE_DAY = 24*60*60*1000;    //24H
    private static long ONE_HOUR = 60*60*1000;      //1H
    private static long ONE_WHILE = 60*5*1000;      //5MIN
    private static long ONE_MIN = 60*1000;      //5MIN

    public static Date stringToDateTime(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateString);
    }

    public static String getSocialTime(String dateString) {
        try {
            Date date = stringToDateTime(dateString);
            long time = date.getTime();
            long before = new Date().getTime() - time;
            if ( before > 7 * ONE_DAY ) // 7天前不做处理
                return dateString;
            if ( before > ONE_DAY ) {
                return before / ONE_DAY + "天前  " + date.getHours() + ":" + date.getMinutes();
            }
            if ( before <  ONE_WHILE) {
                return "刚刚";
            }
            if ( before > ONE_HOUR) {
                return  before / ONE_HOUR + "小时前";
            }
            return before / ONE_MIN + "分钟前";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateString;
    }
}
