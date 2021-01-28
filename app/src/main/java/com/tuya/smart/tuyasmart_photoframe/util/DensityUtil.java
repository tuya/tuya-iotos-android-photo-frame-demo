package com.tuya.smart.tuyasmart_photoframe.util;

import android.content.res.Resources;

/**
 * @author vico
 * @date 1/17/21
 */
public class DensityUtil {


    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }


    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

}
