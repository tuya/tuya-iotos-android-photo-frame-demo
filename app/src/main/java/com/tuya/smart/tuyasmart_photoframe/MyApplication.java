package com.tuya.smart.tuyasmart_photoframe;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.tuya.smart.android.photoframe.TuyaPhotoFrame;

import java.io.File;


/**
 * @author vico
 * @date 1/14/21
 */
public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            com.tuya.smartai.iot_sdk.Log.init(this, getFilesDir() + File.separator + "tuya_log", 7);
        }

        initTuyaFrameSDK();
        registerLifecycle();
    }


    private void initTuyaFrameSDK() {
        TuyaPhotoFrame.getInstance()
                .setIoTManager(IoTSDKManagerWrapper.getInstance(this).getIoTSDKManager())
                .init(this);
    }

    private void registerLifecycle() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            private int createdCounter = 0;
            private int startedCounter = 0;

            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                createdCounter++;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                startedCounter++;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                startedCounter--;
                if (startedCounter == 0 && !activity.isChangingConfigurations() && !activity.isFinishing()) {
                    Log.d("TuyaSmart_PhotoFrame", "HOME");
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                createdCounter--;
                if (createdCounter == 0 && !activity.isChangingConfigurations()) {
                    Log.d("TuyaSmart_PhotoFrame", "EXIT");
                    com.tuya.smartai.iot_sdk.Log.close();
                }
            }
        });
    }
}
