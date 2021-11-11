package com.jeffrey.utils;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MonitorVideoModify extends FileAlterationListenerAdaptor {

    private final String thumbnailPath;
    private final String realPath;

    public MonitorVideoModify(String thumbnailPath, String realPath) {
        this.thumbnailPath = thumbnailPath;
        this.realPath = realPath;
    }

    // 文件删除
    @Override
    public void onFileDelete(File file) {
        System.out.println("[删除文件]：" + file.getName());
        String name = file.getName();
        ThumbnailDataManagement.deleteData(name, realPath);
    }

    // 文件创建
    @Override
    public void onFileCreate(File file) {
        if (!ThumbnailDataManagement.equalsName(file.getName().replace(" ", "")) && file.getName().endsWith("mp4")) {
            System.out.println("[新增文件]：" + file.getName());
            ThumbnailDataManagement.createThumbnail(Collections.singletonList(file), this.thumbnailPath);
            ThumbnailDataManagement.createThumbnail(Arrays.asList(Objects.requireNonNull(file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith("mp4");
                }
            }))), this.thumbnailPath);
        }
    }

    // 文件修改
    @Override
    public void onFileChange(File file) {
        System.out.println("[修改文件]:" + file.getName());
        onFileDelete(file);
        onFileCreate(file);
    }

    // 目录创建
    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("[新建目录]:" + directory.getAbsolutePath());
        File[] createFiles = directory.listFiles();
        if (createFiles != null) {
            List<File> files = Arrays.asList(createFiles);
            ThumbnailDataManagement.createThumbnail(files, this.thumbnailPath);
        }
    }

    // 检测开始
    @Override
    public void onStart(FileAlterationObserver observer) {
        File[] directory = observer.getDirectory().listFiles();
        if (directory != null) {
            for (File item : directory) {
                if (item.isDirectory()) {
                    if (Objects.requireNonNull(item.list()).length <= 0) {
                        if (item.delete()) {
                            System.out.println("移除空目录：" + item.getName());
                        }
                    }
                }
            }
        }
        super.onStart(observer);
    }

    // 检测结束
    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }
}
