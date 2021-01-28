package com.tuya.smart.tuyasmart_photoframe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.android.photoframe.api.ITuyaResultCallback;
import com.tuya.smart.android.photoframe.bean.Capacity;
import com.tuya.smart.tuyasmart_photoframe.R;
import com.tuya.smart.tuyasmart_photoframe.model.CapacityModel;
import com.tuya.smart.tuyasmart_photoframe.util.ByteChangeUtils;

public class CapacityActivity extends AppCompatActivity {

    private TextView tvTotal;
    private TextView tvUsed;
    private TextView tvImageUsed;
    private TextView tvVideoUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capacity);

        initView();

        CapacityModel mModel = new CapacityModel(this);

        mModel.getCapacity(new ITuyaResultCallback<Capacity>() {
            @Override
            public void onSuccess(Capacity result) {
                tvTotal.setText(getString(R.string.capacity_total, ByteChangeUtils.getSize(result.getTotalCapacity())));
                tvUsed.setText(getString(R.string.capacity_used, ByteChangeUtils.getSize(result.getUsedCapacity())));
                tvImageUsed.setText(getString(R.string.capacity_image_used, ByteChangeUtils.getSize(result.getImageUsedCapacity())));
                tvVideoUsed.setText(getString(R.string.capacity_video_used, ByteChangeUtils.getSize(result.getVideoUsedCapacity())));
            }

            @Override
            public void onError(String errorCode, String errorMessage) {
                Toast.makeText(CapacityActivity.this, "errorCode :" + errorCode + " errorMessage : " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        tvTotal = findViewById(R.id.tv_total);
        tvUsed = findViewById(R.id.tv_used);
        tvImageUsed = findViewById(R.id.tv_image_used);
        tvVideoUsed = findViewById(R.id.tv_video_used);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}