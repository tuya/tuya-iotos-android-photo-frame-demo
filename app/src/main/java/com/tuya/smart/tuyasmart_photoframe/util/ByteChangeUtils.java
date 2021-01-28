package com.tuya.smart.tuyasmart_photoframe.util;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * @author vico
 * @date 1/16/21
 */
public class ByteChangeUtils {

    public static String getSize(long size) {
        String result = "";
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        DecimalFormat df = new DecimalFormat("#.00");

        if (size >= gb) {
            result = df.format((float) size / gb) + "GB";
        } else if (size >= mb) {
            result = df.format((float) size / mb) + "MB";
        } else if (size >= kb) {
            result = String.format(Locale.getDefault(), "%.2f", (float) size / kb) + "KB";
        } else {
            result = size + "B";
        }
        return result;
    }
}
