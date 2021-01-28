package com.tuya.smart.tuyasmart_photoframe.util;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vico
 * @date 1/18/21
 */
public final class TimeUtils {

    private static final ThreadLocal<Map<String, SimpleDateFormat>> SDF_THREAD_LOCAL
            = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            return new HashMap<>();
        }
    };

    private static SimpleDateFormat getDefaultFormat() {
        return getSafeDateFormat("yyyy.MM.dd");
    }

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getSafeDateFormat(String pattern) {
        Map<String, SimpleDateFormat> sdfMap = SDF_THREAD_LOCAL.get();
        //noinspection ConstantConditions
        SimpleDateFormat simpleDateFormat = sdfMap.get(pattern);
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(pattern);
            sdfMap.put(pattern, simpleDateFormat);
        }
        return simpleDateFormat;
    }

    private TimeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String millis2String(long millis) {
        return getDefaultFormat().format(new Date(millis));
    }

    public static String secToTime(long duration) {
        String timeStr;
        long hour = 0;
        long minute = 0;
        long second = 0;
        if (duration <= 0) {
            return "00:00";
        } else {
            minute = duration / 60;
            if (minute < 60) {
                second = duration % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) return "99:59:59";
                minute = minute % 60;
                second = duration - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + i;
        } else {
            retStr = String.valueOf(i);
        }
        return retStr;
    }
}
