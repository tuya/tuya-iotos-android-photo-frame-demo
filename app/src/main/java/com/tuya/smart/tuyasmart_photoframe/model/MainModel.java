package com.tuya.smart.tuyasmart_photoframe.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.tuya.smart.tuyasmart_photoframe.IoTSDKManagerWrapper;
import com.tuya.smartai.iot_sdk.IoTSDKManager;

import java.io.File;

/**
 * @author vico
 * @date 1/15/21
 */
public class MainModel {

    private final Context mContext;

    public MainModel(Context context) {
        this.mContext = context;
    }

    public void initIoTSDK(IoTSDKManager.IoTCallback callback, String pid, String uuid, String authKey) {
        if (pid.isEmpty() || uuid.isEmpty() || authKey.isEmpty()) {
            Toast.makeText(mContext, "pid、uuid、authKey is Empty", Toast.LENGTH_SHORT).show();
            return;
        }

        IoTSDKManagerWrapper.getInstance(mContext).initSDK(mContext.getFilesDir() + File.separator + "tuya_iot",
                pid, uuid, authKey, callback);
    }

    public void reset() {
        IoTSDKManagerWrapper.getInstance(mContext).reset();
    }

    public void onDestroy() {
        IoTSDKManagerWrapper.getInstance(mContext).onDestroy();
    }

}
