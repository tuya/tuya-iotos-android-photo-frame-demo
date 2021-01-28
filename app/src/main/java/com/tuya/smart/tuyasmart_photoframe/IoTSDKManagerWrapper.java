package com.tuya.smart.tuyasmart_photoframe;

import android.content.Context;

import com.tuya.smartai.iot_sdk.IoTSDKManager;

/**
 * @author vico
 * @date 1/21/21
 */
public class IoTSDKManagerWrapper {

    private IoTSDKManager mIoTSDKManager;

    private volatile static IoTSDKManagerWrapper sInstance;

    public IoTSDKManagerWrapper(Context context) {
        mIoTSDKManager = new IoTSDKManager(context);
    }

    public static IoTSDKManagerWrapper getInstance(Context context) {
        if (sInstance == null) {
            synchronized (IoTSDKManagerWrapper.class) {
                if (sInstance == null) {
                    sInstance = new IoTSDKManagerWrapper(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public IoTSDKManager getIoTSDKManager() {
        return mIoTSDKManager;
    }

    public void initSDK(String basePath, String pid, String uuid, String authKey, IoTSDKManager.IoTCallback callback) {
        if (mIoTSDKManager != null) {
            mIoTSDKManager.initSDK(basePath, pid, uuid, authKey, BuildConfig.VERSION_NAME, callback);
        }
    }

    public void reset() {
        if (mIoTSDKManager != null) {
            mIoTSDKManager.reset();
        }
    }

    public void onDestroy() {
        if (mIoTSDKManager != null) {
            mIoTSDKManager.destroy();
            mIoTSDKManager = null;
        }
    }
}
