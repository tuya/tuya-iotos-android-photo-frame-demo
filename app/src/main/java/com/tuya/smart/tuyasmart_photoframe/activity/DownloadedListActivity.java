package com.tuya.smart.tuyasmart_photoframe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.tuya.smart.tuyasmart_photoframe.R;
import com.tuya.smart.tuyasmart_photoframe.adapter.DownloadedFileAdapter;
import com.tuya.smart.tuyasmart_photoframe.entity.MediaFile;
import com.tuya.smart.tuyasmart_photoframe.model.DownloadedListModel;
import com.tuya.smart.tuyasmart_photoframe.util.DensityUtil;
import com.tuya.smart.tuyasmart_photoframe.util.ScreenUtils;

import java.util.List;

public class DownloadedListActivity extends AppCompatActivity {

    private DownloadedListModel mModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_list);

        mModel = new DownloadedListModel();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                int spanCount = ScreenUtils.getScreenWidth(DownloadedListActivity.this) / DensityUtil.dp2px(88);
                DownloadedFileAdapter adapter = new DownloadedFileAdapter();
                recyclerView.setLayoutManager(new GridLayoutManager(DownloadedListActivity.this, spanCount));
                recyclerView.setAdapter(adapter);
                loadData(adapter);
                return false;
            }
        });
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MediaFile item = (MediaFile) adapter.getItem(position);
                if (item == null) {
                    return;
                }
                Intent intent = new Intent();
                if (item.getType().equals("image")) {
                    intent.setClass(DownloadedListActivity.this, ImageBrowserActivity.class);
                } else {
                    intent.setClass(DownloadedListActivity.this, VideoPlayerActivity.class);
                }
                intent.putExtra("filePath", item.getPath());
                intent.putExtra("local", true);
                startActivity(intent);
            }
        });
    }

    private void loadData(DownloadedFileAdapter adapter) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                List<MediaFile> mediaList = mModel.scanAllMediaFiles(DownloadedListActivity.this);
                runOnUiThread(() -> adapter.setNewData(mediaList));
            }
        };
        thread.start();
    }
}