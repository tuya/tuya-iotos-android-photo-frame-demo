package com.tuya.smart.tuyasmart_photoframe.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.tuya.smart.android.photoframe.TuyaPhotoFrame;
import com.tuya.smart.android.photoframe.api.ISchedulers;
import com.tuya.smart.tuyasmart_photoframe.R;


public class ImageBrowserActivity extends AppCompatActivity {

    private PhotoView mPhotoView;

    private ProgressBar mProgressBar;

    private boolean local;
    private String filePath;
    private long id;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ISchedulers.PHOTO_FRAME_DOWNLOAD_INFO_ACTION)) {
                int state = intent.getIntExtra(ISchedulers.DOWNLOAD_STATE, -1);
                switch (state) {
                    case ISchedulers.START:
                        int taskId = intent.getIntExtra(ISchedulers.DOWNLOAD_TASK_ID, -1);
                        Log.d("ImageBrowserActivity", "taskId: "+taskId);
                        break;
                    case ISchedulers.RUNNING:
                        long progress = intent.getLongExtra(ISchedulers.DOWNLOAD_PROGRESS, -1);
                        long totalSize = intent.getLongExtra(ISchedulers.DOWNLOAD_TOTAL_SIZE, -1);
                        Log.d("ImageBrowserActivity", "progress: " + progress + ", totalSize: " + totalSize);
                        break;
                    case ISchedulers.COMPLETE:
                        String filePath = intent.getStringExtra(ISchedulers.DOWNLOAD_COMPLETE_PATH);
                        setPhotoView(filePath);
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    case ISchedulers.ERROR:
                        String errorCode = intent.getStringExtra(ISchedulers.DOWNLOAD_ERROR_CODE);
                        String errorMessage = intent.getStringExtra(ISchedulers.DOWNLOAD_ERROR_MESSAGE);
                        Toast.makeText(context, "errorCode: " + errorCode + " ,errorMessage: " + errorMessage, Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);

        Intent intent = getIntent();

        if (intent != null) {
            id = intent.getLongExtra("id", -1);
            local = intent.getBooleanExtra("local", false);
            filePath = intent.getStringExtra("filePath");
        }

        mPhotoView = findViewById(R.id.photo_view);
        mProgressBar = findViewById(R.id.progress_bar);

        registerReceiver(mReceiver, new IntentFilter(ISchedulers.PHOTO_FRAME_DOWNLOAD_INFO_ACTION));

        if (local) {
            mProgressBar.setVisibility(View.GONE);
            setPhotoView(filePath);
        } else {
            TuyaPhotoFrame.newRequest().getDownload(id, getExternalFilesDir("").getPath(), "image");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TuyaPhotoFrame.newRequest().cancelDownload(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }


    private void setPhotoView(String filePath) {
        Uri uri = Uri.parse(filePath);
        if (uri != null) {
            mPhotoView.setImageURI(uri);
        }
        mPhotoView.setOnPhotoTapListener((view1, x, y) -> finish());
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        mPhotoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}