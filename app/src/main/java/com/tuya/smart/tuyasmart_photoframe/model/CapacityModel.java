package com.tuya.smart.tuyasmart_photoframe.model;

import android.content.Context;

import com.tuya.smart.android.photoframe.TuyaPhotoFrame;
import com.tuya.smart.android.photoframe.api.ITuyaResultCallback;
import com.tuya.smart.android.photoframe.bean.Capacity;
import com.tuya.smart.tuyasmart_photoframe.IoTSDKManagerWrapper;


/**
 * @author vico
 * @date 1/16/21
 */
public class CapacityModel {

    private final Context mContext;


    public CapacityModel(Context context) {
        mContext = context;
    }

    public void getCapacity(final ITuyaResultCallback<Capacity> callback) {
        String deviceId = IoTSDKManagerWrapper.getInstance(mContext).getIoTSDKManager().getDeviceId();
        if (deviceId == null || deviceId.isEmpty()) {
            return;
        }


        TuyaPhotoFrame.newRequest().getCapacity(deviceId, callback);
    }
}
