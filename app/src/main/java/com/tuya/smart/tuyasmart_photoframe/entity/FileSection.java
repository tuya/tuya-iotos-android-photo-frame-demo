package com.tuya.smart.tuyasmart_photoframe.entity;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.tuya.smart.android.photoframe.bean.FileInfo;

/**
 * @author vico
 * @date 1/19/21
 */
public class FileSection extends SectionEntity<FileInfo> {

    public FileSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public FileSection(FileInfo fileInfo) {
        super(fileInfo);
    }
}
