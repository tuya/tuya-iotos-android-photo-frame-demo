package com.tuya.smart.tuyasmart_photoframe.model;

import com.tuya.smart.android.photoframe.TuyaPhotoFrame;
import com.tuya.smart.android.photoframe.api.ITuyaResultCallback;
import com.tuya.smart.android.photoframe.bean.DateInfo;
import com.tuya.smart.android.photoframe.bean.FileInfo;
import com.tuya.smart.android.photoframe.bean.PageInfo;
import com.tuya.smart.tuyasmart_photoframe.entity.FileSection;
import com.tuya.smart.tuyasmart_photoframe.util.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vico
 * @date 1/17/21
 */
public class UploadedListModel {

    private final Map<Long, Long> dateMap = new HashMap<>();

    public void getUploadedFileList(int limit,
                                    int offset,
                                    int width,
                                    int height,
                                    int position,
                                    final ITuyaResultCallback<PageInfo> callback) {

        switch (position) {
            case 0:
                TuyaPhotoFrame.newRequest().getUploadedFileList(limit, offset, width, height, callback);
                break;
            case 1:
                TuyaPhotoFrame.newRequest().getUploadedFileListWithType(limit, offset, width, height, "image", callback);
                break;
            case 2:
                TuyaPhotoFrame.newRequest().getUploadedFileListWithType(limit, offset, width, height, "video", callback);
                break;
            default:
                break;
        }
    }

    public List<FileSection> dealWithData(PageInfo pageInfo) {
        List<FileSection> list = new ArrayList<>();
        for (DateInfo dateInfo : pageInfo.getDatas()) {
            long date = dateInfo.getDate();

            if (dateMap.containsKey(date)) {
                for (FileInfo fileInfo : dateInfo.getList()) {
                    list.add(new FileSection(fileInfo));
                }
            } else {
                dateMap.put(date, date);
                list.add(new FileSection(true, TimeUtils.millis2String(date)));
                for (FileInfo fileInfo : dateInfo.getList()) {
                    list.add(new FileSection(fileInfo));
                }
            }
        }
        return list;
    }

    public void clearMap(){
        dateMap.clear();
    }
}
