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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class ContextListener implements ServletContextListener {

    private static final ThumbnailService THUMBNAIL_SERVICE = new ThumbnailServiceImpl();
    private static final ArrayList<File> LISTS = new ArrayList<>();
    private static boolean initData;
    private static boolean monitorFolder;

    static {
        InputStream is = null;
        try {
            is = ContextListener.class.getClassLoader().getResourceAsStream("conf/root.properties");
            Properties prop = new Properties();
            prop.load(is);
            initData = Boolean.parseBoolean(prop.getProperty("initData"));
            monitorFolder = Boolean.parseBoolean(prop.getProperty("monitorFolder"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext servletContext = sce.getServletContext();
        // 监听目录
        File target = new File(servletContext.getRealPath("/static/video"));
        // 缩略图存放目录
        String thumbnailPath = servletContext.getRealPath("/static/thumbnail/");

        new Thread(() -> {

            if (initData) {

                // 对表数据进行充重置
                THUMBNAIL_SERVICE.truncateData();

                // 查找监听目录内的所有 mp4 视频
                findVideo(target);


                File[] files = new File(thumbnailPath).listFiles();
                if (files != null) {
                    for (File item : files) {
                        if (item.delete()) {
                            System.out.println("移除文件：" + item);
                        }
                    }
                }

                ThumbnailDataManagement.createThumbnail(LISTS, thumbnailPath);
            }

            if (monitorFolder) {
                FileAlterationObserver observer = new FileAlterationObserver(target);

                observer.addListener(new MonitorVideoModify(thumbnailPath, servletContext.getRealPath("/")));

                FileAlterationMonitor monitor = new FileAlterationMonitor(5000, observer);

                try {
                    monitor.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
