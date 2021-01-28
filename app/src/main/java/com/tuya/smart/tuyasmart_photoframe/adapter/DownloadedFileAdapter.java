package com.tuya.smart.tuyasmart_photoframe.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tuya.smart.tuyasmart_photoframe.R;
import com.tuya.smart.tuyasmart_photoframe.entity.MediaFile;
import com.tuya.smart.tuyasmart_photoframe.util.TimeUtils;

import java.util.ArrayList;

/**
 * @author vico
 * @date 1/20/21
 */
public class DownloadedFileAdapter extends BaseQuickAdapter<MediaFile, BaseViewHolder> {

    public DownloadedFileAdapter() {
        super(R.layout.item_file, new ArrayList<>());
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MediaFile item) {
        helper.addOnClickListener(R.id.iv_file);
        if (item.getType().equals("video")) {
            helper.setVisible(R.id.video_group, true);
            helper.setText(R.id.tv_duration, TimeUtils.secToTime(item.getDuration()));
            Glide.with(mContext)
                    .load(item.getBitmap())
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .into((ImageView) helper.getView(R.id.iv_file));
        } else {
            helper.setVisible(R.id.video_group, false);
            Glide.with(mContext)
                    .load(item.getPath())
                    .centerCrop()
                    .placeholder(android.R.color.darker_gray)
                    .into((ImageView) helper.getView(R.id.iv_file));
        }
    }
}
