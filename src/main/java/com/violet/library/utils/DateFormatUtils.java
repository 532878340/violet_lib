package com.violet.library.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Perley on 2015/5/14.
 */
public class DateFormatUtils {

    private static final String MdHm = "MM-dd HH:mm";

    private static final String CHINESE_STANDARD = "yyyy年MM月dd日";

    private static final String MILLS_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String MILLS_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss.SSS";

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(MdHm);
        return format.format(new Date());
    }

    public static String formatTime(String pattern, long millis) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(millis);
    }

    public static String format2Chinese(long millis) {
        SimpleDateFormat format = new SimpleDateFormat(CHINESE_STANDARD);
        return format.format(millis);
    }

    public static String formatStandardDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(time);
    }

    public static String formatStandardTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }

    public static long parse2Millis(String dateStr) {
        long result = 0;
        if (TextUtils.isEmpty(dateStr)) {
            return result;
        }
        SimpleDateFormat format = new SimpleDateFormat(MILLS_FORMAT);
        try {
            Date d = format.parse(dateStr);
            result = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 字符串类型的时间（2016-01-21 16:47:40.0），截取日期和时间
     * @param originalTime
     * @return
     */
    public static String subTime(String originalTime) {
        if(TextUtils.isEmpty(originalTime) || originalTime.length() < 19) {
            return originalTime;
        }
        return originalTime.substring(0, 19);
    }

    /**
     * 字符串类型的时间（2016-01-21 16:47:40.0），截取日期
     * @param originalTime
     * @return
     */
    public static String subDate(String originalTime) {
        if(TextUtils.isEmpty(originalTime) || originalTime.length() < 10) {
            return originalTime;
        }
        return originalTime.substring(0, 10);
    }
}
