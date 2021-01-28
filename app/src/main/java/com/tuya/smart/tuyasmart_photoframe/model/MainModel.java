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

    public void initIoTSDK(IoTSDKManager.IoTCallback callback) {
        String pid = getMetaDataValue("PID");
        String uuid = getMetaDataValue("UUID");
        String authKey = getMetaDataValue("AUTH_KEY");

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

    private String getMetaDataValue(String key) {
        String value = "";
        try {
            ApplicationInfo applicationInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            value = applicationInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

}
