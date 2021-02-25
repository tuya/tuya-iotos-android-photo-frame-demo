package com.tuya.smart.tuyasmart_photoframe.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tuya.smart.tuyasmart_photoframe.R;
import com.tuya.smart.tuyasmart_photoframe.model.MainModel;
import com.tuya.smart.tuyasmart_photoframe.util.QrCodeUtils;
import com.tuya.smartai.iot_sdk.DPEvent;
import com.tuya.smartai.iot_sdk.IoTSDKManager;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements IoTSDKManager.IoTCallback {

    private LinearLayout qrCodeLayout;
    private ImageView ivQrCode;
    private ProgressBar progressBar;
    private LinearLayout menuLayout;

    private MainModel mModel;

    private String pid = "";
    private String uuid = "";
    private String authKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initArgs();
        initView();
        mModel = new MainModel(this);
        mModel.initIoTSDK(this, pid, uuid, authKey);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mModel.onDestroy();
    }

    private void initArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            pid = getIntent().getStringExtra("pid");
            uuid = getIntent().getStringExtra("uuid");
            authKey = getIntent().getStringExtra("authKey");
        }
    }

    private void initView() {
        qrCodeLayout = findViewById(R.id.qrCode_layout);
        ivQrCode = findViewById(R.id.iv_qrCode);
        progressBar = findViewById(R.id.progress_bar);
        menuLayout = findViewById(R.id.menu_layout);

        findViewById(R.id.btn_reset).setOnClickListener(v -> new AlertDialog.Builder(this)
                .setMessage(R.string.reset_tip)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    mModel.reset();
                    progressBar.setVisibility(View.VISIBLE);
                    menuLayout.setVisibility(View.GONE);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {

                }).show());

        findViewById(R.id.btn_capacity).setOnClickListener(v -> startActivity(new Intent(this, CapacityActivity.class)));

        findViewById(R.id.btn_uploaded_list).setOnClickListener(v -> startActivity(new Intent(this, UploadedListActivity.class)));

        findViewById(R.id.btn_downloaded_list).setOnClickListener(v -> startActivity(new Intent(this, DownloadedListActivity.class)));
    }

    private void restartApp() {
        saveStatus(false);
        Intent intent = new Intent();
        intent.setClass(this, ConfigurationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    public static void goToActivity(Context context, String pid, String uuid, String authKey) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("pid", pid);
        intent.putExtra("uuid", uuid);
        intent.putExtra("authKey", authKey);
        context.startActivity(intent);
    }

    @Override
    public void onDpEvent(DPEvent event) {

    }

    @Override
    public void onReset() {
        runOnUiThread(this::restartApp);
    }

    @Override
    public void onShorturl(String urlJson) {
        if (urlJson == null || urlJson.isEmpty()) {
            return;
        }

        try {
            JSONObject obj = new JSONObject(urlJson);
            String url = obj.getString("shortUrl");
            Bitmap bitmap = QrCodeUtils.createQRCode(url, 200);

            runOnUiThread(() -> {
                if (bitmap != null) {
                    ivQrCode.setImageBitmap(bitmap);
                }
                qrCodeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActive() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.GONE);
            menuLayout.setVisibility(View.VISIBLE);
            qrCodeLayout.setVisibility(View.GONE);
            saveStatus(true);
        });
    }

    @Override
    public void onFirstActive() {
    }

    @Override
    public void onMQTTStatusChanged(int status) {
    }

    @Override
    public void onMqttMsg(int protocol, JSONObject msgObj) {
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.exit_tip, Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveStatus(boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences("TuyaPhotoFrame", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (flag) {
            edit.putBoolean("active", true);
            edit.putString("pid", pid);
            edit.putString("uuid", uuid);
            edit.putString("authKey", authKey);
        } else {
            edit.clear();
        }
        edit.commit();
    }
}