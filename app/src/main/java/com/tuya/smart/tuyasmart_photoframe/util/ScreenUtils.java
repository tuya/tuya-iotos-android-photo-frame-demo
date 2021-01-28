package com.tuya.smart.tuyasmart_photoframe.util;

import android.content.Context;
import android.graphics.Point;

/**
 * @author vico
 * @date 1/18/21
 */
public class ScreenUtils {

    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    public static int getScreenWidth(Context context) {
        Point point = new Point();
        context.getDisplay().getRealSize(point);
        return point.x;
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    public static int getScreenHeight(Context context) {
        Point point = new Point();
        context.getDisplay().getRealSize(point);
        return point.y;
    }
}
