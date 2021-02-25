package com.tuya.smart.tuyasmart_photoframe.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.king.zxing.CaptureActivity;
import com.king.zxing.Intents;
import com.tuya.smart.tuyasmart_photoframe.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SCAN = 0x01;
    private EditText mEditPid;
    private EditText mEditUUid;
    private EditText mEditAuthKey;

    private Button mBtnScan;
    private Button mBtnBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActiveStatus();
        setContentView(R.layout.activity_configuration);

        initView();
        initData();
    }

    private void initView() {
        mEditPid = findViewById(R.id.edit_pid);
        mEditUUid = findViewById(R.id.edit_uuid);
        mEditAuthKey = findViewById(R.id.edit_auth_key);
        mBtnScan = findViewById(R.id.btn_scan);
        mBtnBind = findViewById(R.id.btn_bind);
    }

    private void initData() {
        enableButton();
        onTextChanged(mEditPid);
        onTextChanged(mEditUUid);
        onTextChanged(mEditAuthKey);

        mEditPid.setText(getMetaDataValue("PID"));
        mEditUUid.setText(getMetaDataValue("UUID"));
        mEditAuthKey.setText(getMetaDataValue("AUTH_KEY"));

        mBtnScan.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(ConfigurationActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ConfigurationActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
            } else {
                startScan();
            }
        });

        mBtnBind.setOnClickListener(v -> {
            MainActivity.goToActivity(ConfigurationActivity.this, mEditPid.getText().toString().trim(),
                    mEditUUid.getText().toString().trim(),
                    mEditAuthKey.getText().toString().trim());
            finish();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            boolean hasPermission = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    hasPermission = false;
                    break;
                }
            }

            if (hasPermission) {
                startScan();
            } else {
                Toast.makeText(this, "You must allow all the permissions.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_SCAN) {
                String result = data.getStringExtra(Intents.Scan.RESULT);
                try {
                    JSONObject obj = new JSONObject(result);
                    String pid = obj.optString("PID");
                    String uuid = obj.optString("UUID");
                    String authKey = obj.optString("AUTHKEY");

                    mEditPid.setText(pid);
                    mEditUUid.setText(uuid);
                    mEditAuthKey.setText(authKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    private void enableButton() {
        String pid = mEditPid.getText().toString().trim();
        String uid = mEditUUid.getText().toString().trim();
        String authKey = mEditAuthKey.getText().toString().trim();

        mBtnBind.setEnabled(!TextUtils.isEmpty(pid) && !TextUtils.isEmpty(uid) && !TextUtils.isEmpty(authKey));
    }

    private void onTextChanged(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String getMetaDataValue(String key) {
        String value = "";
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            value = applicationInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    private void getActiveStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("TuyaPhotoFrame", MODE_PRIVATE);
        boolean active = sharedPreferences.getBoolean("active", false);
        if (active) {
            String pid = sharedPreferences.getString("pid", "");
            String uuid = sharedPreferences.getString("uuid", "");
            String authKey = sharedPreferences.getString("authKey", "");
            MainActivity.goToActivity(ConfigurationActivity.this, pid, uuid, authKey);
            finish();
        }
    }
}