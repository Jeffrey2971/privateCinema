package com.jeffrey.utils;

import com.jeffrey.service.ThumbnailService;
import com.jeffrey.service.impl.ThumbnailServiceImpl;

import java.io.*;
import java.util.*;
import java.util.List;

public class ThumbnailDataManagement {

    private static final ThumbnailService THUMBNAIL_SERVICE = new ThumbnailServiceImpl();

    /**
     * 在收到文件监视器的调用后，根据传递的删除文件，删除指定文件的缩略图
     *
     * @param fileName 被删除文件的文件名（此时的文件已被删除）
     * @param realPath 服务器工作绝对路径
     */
    public static void deleteData(String fileName, String realPath) {
        try {
            List<String> thumbnailList = THUMBNAIL_SERVICE.getThumbnailList(fileName.replace(" ", ""));
            for (String file : thumbnailList) {
                if (new File(realPath + "/" + file.replace("\"", "")).delete()) {
                    System.out.println("移除缩略图：" + file);
                }
            }

            THUMBNAIL_SERVICE.delete(fileName.replace(" ", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据传递的视频文件列表创建每个视频对应的缩略图
     *
     * @param lists         视频列表
     * @param thumbnailPath /static/thumbnailPath
     */
    public static void createThumbnail(List<File> lists, String thumbnailPath) {

        // 用于存放写入到数据库中的数据，obj[0] 文件名（去掉空格），obj[1] value 为该视频的缩略图列表
        Object[] data = new Object[2];

        // 忽略文件，在每次存放值到 data 后加入，防止 data 数据重复，前端获取缩略图重复
        ArrayList<String> ignoreFile = new ArrayList<>();

        // 遍历找到的视频，计算出 md5 以及对应的缩略图列表
        for (File item : lists) {
            try {
                File targetFolder = new File(thumbnailPath);
                if (targetFolder.mkdir() || targetFolder.exists()) {
                    System.out.println("正在导出文件的缩略图：" + item.getName());

                    Runtime.getRuntime().exec(new String[]{"ffmpeg", "-i", item.toString(), "-vsync", "vfr", "-vf", "select=isnan(prev_selected_t)+gte(t-prev_selected_t\\,1),scale=160:90,tile=10x10", "-qscale:v", "3", "" + targetFolder + "/" + getDiffTimeMillis(System.currentTimeMillis()) + "%03d.jpg", "-loglevel", "quiet"}).waitFor();

                    String[] files = targetFolder.list((dir, name) -> name.endsWith("jpg") && !ignoreFile.contains(name));
                    if (files != null) {
                        for (int i = 0; i < files.length; i++) {
                            ignoreFile.add(files[i]);
                            files[i] = "\"/static/thumbnail/" + files[i] + "\"";
                        }
                        data[0] = item.getName().replace(" ", "");
                        data[1] = Arrays.toString(files);

                        THUMBNAIL_SERVICE.addData(data);
                    }
                } else {
                    throw new RuntimeException("创建存储缩略图目录失败，初始化失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 传递一个时间戳返回一个不一样的时间戳
     *
     * @param time 时间戳
     * @return 时间戳
     */
    private static long getDiffTimeMillis(long time) {
        while (true) {
            long now = System.currentTimeMillis();
            if (time != now) {
                return now;
            }
        }
    }

    /**
     * 用于避免在添加文件时和目录重复
     *
     * @param name 添加的文件名
     * @return 在数据库是否存在
     */
    public static boolean equalsName(String name) {
        return THUMBNAIL_SERVICE.equalsName(name);
    }
}
