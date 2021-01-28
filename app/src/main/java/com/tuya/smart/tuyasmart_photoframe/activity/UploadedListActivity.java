package com.tuya.smart.tuyasmart_photoframe.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.tuya.smart.android.photoframe.api.ITuyaResultCallback;
import com.tuya.smart.android.photoframe.bean.FileInfo;
import com.tuya.smart.android.photoframe.bean.PageInfo;
import com.tuya.smart.tuyasmart_photoframe.R;
import com.tuya.smart.tuyasmart_photoframe.adapter.FileSectionAdapter;
import com.tuya.smart.tuyasmart_photoframe.entity.FileSection;
import com.tuya.smart.tuyasmart_photoframe.model.UploadedListModel;
import com.tuya.smart.tuyasmart_photoframe.util.DensityUtil;
import com.tuya.smart.tuyasmart_photoframe.util.ScreenUtils;

public class UploadedListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        ITuyaResultCallback<PageInfo> {

    private static final int LIMIT = 20;
    private UploadedListModel mModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FileSectionAdapter mAdapter;

    private int mType;

    private boolean loadMore = false;

    private int offset = 0;

    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_list);

        width = DensityUtil.dp2px(88);
        height = DensityUtil.dp2px(88);

        initSpinner();
        initSwipeRefresh();
        initRecyclerView();

        mModel = new UploadedListModel();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mType = position;
        loadMore = false;
        offset = 0;

        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        mModel.getUploadedFileList(LIMIT, offset, width, height, mType, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSuccess(PageInfo pageInfo) {
        offset = pageInfo.getOffset();
        if (loadMore) {
            mAdapter.addData(mModel.dealWithData(pageInfo));
            if (pageInfo.isHasNext()) {
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd();
            }
        } else {
            mModel.clearMap();
            mAdapter.setNewData(mModel.dealWithData(pageInfo));
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(String errorCode, String errorMessage) {
        if (loadMore) {
            mAdapter.loadMoreFail();
        }
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(UploadedListActivity.this, "errorCode: " + errorCode + " ,errorMessage: " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setDropDownWidth(DensityUtil.dp2px(100));
        spinner.setDropDownVerticalOffset(DensityUtil.dp2px(60));
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.type_array,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout = findViewById(R.id.swipe_refresh);

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadMore = false;
            offset = 0;
            mModel.getUploadedFileList(LIMIT, offset, width, height, mType, this);
        });
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new FileSectionAdapter();
        int spanCount = ScreenUtils.getScreenWidth(this) / DensityUtil.dp2px(88);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(() -> {
            loadMore = true;
            mModel.getUploadedFileList(LIMIT, offset, width, height, mType, this);
        }, recyclerView);
        recyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FileSection item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                FileInfo fileInfo = item.t;
                Intent intent = new Intent();
                if (fileInfo.getType().equals("image")) {
                    intent.setClass(UploadedListActivity.this, ImageBrowserActivity.class);
                } else {
                    intent.setClass(UploadedListActivity.this, VideoPlayerActivity.class);
                }
                intent.putExtra("id", fileInfo.getId());
                startActivity(intent);
            }
        });
    }
}