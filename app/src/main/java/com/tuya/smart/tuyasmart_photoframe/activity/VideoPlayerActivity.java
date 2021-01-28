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

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;
import com.tuya.smart.android.photoframe.TuyaPhotoFrame;
import com.tuya.smart.android.photoframe.api.ISchedulers;
import com.tuya.smart.tuyasmart_photoframe.R;


public class VideoPlayerActivity extends AppCompatActivity {

    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private SimpleExoPlayer player;

    private PlayerView playerView;

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
                        Log.d("VideoPlayerActivity", "taskId: "+taskId);
                        break;
                    case ISchedulers.RUNNING:
                        long progress = intent.getLongExtra(ISchedulers.DOWNLOAD_PROGRESS, -1);
                        long totalSize = intent.getLongExtra(ISchedulers.DOWNLOAD_TOTAL_SIZE, -1);
                        Log.d("VideoPlayerActivity", "progress: " + progress + ", totalSize: " + totalSize);
                        break;
                    case ISchedulers.COMPLETE:
                        String filePath = intent.getStringExtra(ISchedulers.DOWNLOAD_COMPLETE_PATH);
                        initializePlayer(Uri.parse(filePath));
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
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();

        if (intent != null) {
            id = intent.getLongExtra("id", -1);
            local = intent.getBooleanExtra("local", false);
            filePath = intent.getStringExtra("filePath");
        }

        mProgressBar = findViewById(R.id.progress_bar);
        playerView = findViewById(R.id.video_view);

        registerReceiver(mReceiver, new IntentFilter(ISchedulers.PHOTO_FRAME_DOWNLOAD_INFO_ACTION));

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            if (local) {
                mProgressBar.setVisibility(View.GONE);
                initializePlayer(Uri.parse(filePath));
            } else {
                download();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT < 24) || player == null) {
            if (local) {
                mProgressBar.setVisibility(View.GONE);
                initializePlayer(Uri.parse(filePath));
            } else {
                download();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
        TuyaPhotoFrame.newRequest().cancelDownload(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void initializePlayer(Uri uri) {
        if (uri == null) {
            return;
        }
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.prepare();
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void download() {
        TuyaPhotoFrame.newRequest().getDownload(id, getExternalFilesDir("").getPath(), "video");
    }
}