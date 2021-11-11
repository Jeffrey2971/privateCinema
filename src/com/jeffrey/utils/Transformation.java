package com.jeffrey.utils;

import java.io.File;
import java.util.ArrayList;

/**
 * @author jeffrey
 * @ClassName: ThumbnailOutPut
 * @Description: 使用 FFmpeg 转换视频格式
 * @date: 2021/10/28 12:03 上午
 * @version:
 * @since JDK 1.8
 */

public class Transformation {

    private static final File WORK_PATH = new File("/usr/local/video/Data");
    private static final ArrayList<String> SUPPORT_TYPE = new ArrayList<>();
    public static final ArrayList<File> LISTS = new ArrayList<>();

    public static void main(String[] args) {

        SUPPORT_TYPE.add("avi");
        SUPPORT_TYPE.add("mkv");

        findVideo(WORK_PATH);

        for (File list : LISTS) {
            System.out.println(list);
        }

        for (File file : LISTS) {
            System.out.println("处理：" + file);
            String name = file.getName();
            String output = file.toString().replace(file.toString().substring(file.toString().lastIndexOf('/') + 1), "") + name.replace(name.substring(name.lastIndexOf('.')), ".mp4");
            try {
                // 这里的命令尾部需要加上 - loglevel quiet 否则会造成大量日志写入 jvm 缓冲区中，当缓冲区满时将会造成阻塞

                String command = "ffmpeg -i '" + file + "' '" + output + "' -loglevel quiet";
                System.out.println(command);
                Runtime.getRuntime().exec(new String[]{"ffmpeg", "-i", file.toString(), output, "-loglevel", "quiet"}).waitFor();

                if (new File(output).exists()) {
                    System.out.println("移除：" + file.delete());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void findVideo(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File item : files) {
                if (item.isDirectory()) {
                    findVideo(item);
                } else {
                    String fileName = item.getName();
                    String type = fileName.substring(fileName.lastIndexOf('.') + 1);
                    if (!"mp4".equalsIgnoreCase(type) && SUPPORT_TYPE.contains(type)) {
                        LISTS.add(item);
                    }
                }
            }
        }
    }

}
