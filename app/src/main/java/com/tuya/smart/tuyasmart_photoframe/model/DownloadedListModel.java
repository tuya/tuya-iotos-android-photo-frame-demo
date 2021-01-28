package com.tuya.smart.tuyasmart_photoframe.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import com.tuya.smart.tuyasmart_photoframe.entity.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author vico
 * @date 1/20/21
 */
public class DownloadedListModel {


    public List<MediaFile> scanAllMediaFiles(Context context) {
        ArrayList<MediaFile> mediaList = new ArrayList<>();
        File dataDir = context.getFilesDir().getParentFile();
        if (dataDir != null) {
            scanMediaFilesUnderSpecificDir(dataDir, true, mediaList);
        }
        File externalDataDir = context.getExternalFilesDir("").getParentFile();
        if (externalDataDir != null) {
            scanMediaFilesUnderSpecificDir(externalDataDir, false, mediaList);
        }
        return mediaList;
    }

    /**
     * Scan all the files under specific directory recursively.
     *
     * @param dir       Base directory to scan.
     * @param internal  Indicates this is internal storage or external storage. True means internal, false means external
     * @param mediaList A media list contains all media files under the specific dir.
     */
    private void scanMediaFilesUnderSpecificDir(File dir, Boolean internal, ArrayList<MediaFile> mediaList) {
        File[] listFiles = dir.listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    scanMediaFilesUnderSpecificDir(file, internal, mediaList);
                } else if (isMediaFile(file)) {
                    if (isVideo(file)) {
                        mediaList.add(getVideoFile(file, internal));
                    } else {
                        mediaList.add(new MediaFile(file.getName(),
                                file.getPath(),
                                internal,
                                new Date(file.lastModified()),
                                "image",
                                null, -1
                        ));
                    }

                }
            }
        }
    }

    private boolean isMediaFile(File file) {
        return file.getName().endsWith(".mp4") || file.getName().endsWith(".jpg");
    }

    private boolean isVideo(File file) {
        return file.getName().endsWith(".mp4");
    }

    private MediaFile getVideoFile(File file, boolean internal) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(file.getPath());
        Bitmap bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long duration = Long.parseLong(time) / 1000;
        return new MediaFile(file.getName(),
                file.getPath(),
                internal,
                new Date(file.lastModified()),
                "video",
                bitmap, duration);
    }
}
