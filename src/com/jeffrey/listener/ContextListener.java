package com.jeffrey.listener;

import com.jeffrey.service.ThumbnailService;
import com.jeffrey.service.impl.ThumbnailServiceImpl;
import com.jeffrey.utils.ThumbnailDataManagement;
import com.jeffrey.utils.MonitorVideoModify;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.ArrayList;

public class ContextListener implements ServletContextListener {

    private static final ThumbnailService THUMBNAIL_SERVICE = new ThumbnailServiceImpl();
    private static final ArrayList<File> LISTS = new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {




        // 对表数据进行充重置
        THUMBNAIL_SERVICE.truncateData();

        ServletContext servletContext = sce.getServletContext();

        // 监听目录
        File target = new File(servletContext.getRealPath("/static/video"));

        // 查找监听目录内的所有 mp4 视频
        findVideo(target);

        String thumbnailPath = servletContext.getRealPath("/static/thumbnail/");

        File[] files = new File(thumbnailPath).listFiles();
        if (files != null) {
            for (File item : files) {
                if (item.delete()) {
                    System.out.println("移除文件：" + item);
                }
            }
        }

        ThumbnailDataManagement.createThumbnail(LISTS, thumbnailPath);

        FileAlterationObserver observer = new FileAlterationObserver(target);

        observer.addListener(new MonitorVideoModify(thumbnailPath, servletContext.getRealPath("/")));

        FileAlterationMonitor monitor = new FileAlterationMonitor(5000, observer);

        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    private static void findVideo(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File item : files) {
                if (item.isDirectory()) {
                    findVideo(item);
                } else {
                    if (item.getName().toLowerCase().endsWith("mp4")) {
                        LISTS.add(item);
                    }
                }
            }
        }
    }
}
