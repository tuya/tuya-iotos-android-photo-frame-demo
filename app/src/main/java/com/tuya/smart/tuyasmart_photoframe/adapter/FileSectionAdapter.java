package com.tuya.smart.tuyasmart_photoframe.adapter;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tuya.smart.android.photoframe.bean.FileInfo;
import com.tuya.smart.tuyasmart_photoframe.R;
import com.tuya.smart.tuyasmart_photoframe.entity.FileSection;
import com.tuya.smart.tuyasmart_photoframe.util.TimeUtils;

import java.util.ArrayList;

/**
 * @author vico
 * @date 1/19/21
 */
public class FileSectionAdapter extends BaseSectionQuickAdapter<FileSection, BaseViewHolder> {

    public FileSectionAdapter() {
        super(R.layout.item_file, R.layout.item_header, new ArrayList<>());
    }

    @Override
    protected void convertHead(BaseViewHolder helper, FileSection item) {
        helper.setText(R.id.tv_date, item.header);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FileSection item) {
        FileInfo fileInfo = item.t;
        if (fileInfo.getType().equals("video")) {
            helper.setVisible(R.id.video_group, true);
            helper.setText(R.id.tv_duration, TimeUtils.secToTime(fileInfo.getDuration()));
        } else {
            helper.setVisible(R.id.video_group, false);
        }
        helper.addOnClickListener(R.id.iv_file);
        Glide.with(mContext)
                .load(fileInfo.getFileUrl())
                .centerCrop()
                .placeholder(android.R.color.darker_gray)
                .into((ImageView) helper.getView(R.id.iv_file));
    }
}
