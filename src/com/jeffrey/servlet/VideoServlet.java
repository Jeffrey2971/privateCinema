package com.jeffrey.servlet;

import com.jeffrey.service.ThumbnailService;
import com.jeffrey.service.impl.ThumbnailServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class VideoServlet extends BaseServlet {

    private static final ThumbnailService THUMBNAIL_SERVICE = new ThumbnailServiceImpl();

    protected void select(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] list = new File(getServletContext().getRealPath("/static/video")).list();
        if (list != null) {
            Arrays.sort(list, String::compareTo);
        }
        req.setAttribute("ver", list);
        req.getRequestDispatcher("/dynamic/jsp/select.jsp").forward(req, resp);
    }

    protected void selectVideoList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String choose = req.getParameter("item");

        if (!"".equals(choose)) {
            String realPath = getServletContext().getRealPath("/static/video/" + choose);
            System.out.println(realPath);

            String[] files = new File(realPath).list();

            if (files != null) {
                List<String> lists = Arrays.asList(files);

                lists.sort(String::compareTo);

                req.setAttribute("ver", choose);
                req.setAttribute("list", lists);
                req.getRequestDispatcher("dynamic/jsp/selectVideoList.jsp").forward(req, resp);
            }
        } else {
            resp.getWriter().write("参数异常");
        }
    }

    protected void videoPlayback(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String arg = req.getParameter("file");
        String name = req.getParameter("name").replace(" ", "");
        File file = new File(getServletContext().getRealPath(arg));
        List<String> thumbnailList = THUMBNAIL_SERVICE.getThumbnailList(name);
        // 视频标题
        req.setAttribute("title", file.getName());
        // 视频播放地址
        req.setAttribute("videoSrc", arg);
        // 视频未播放时展示图片

        req.setAttribute("videoPoster", "");

        // 缩略图列表
        req.setAttribute("previewList", thumbnailList != null ? thumbnailList.toString() : "[]");

        req.getRequestDispatcher("dynamic/jsp/desktopPluginExample.jsp").forward(req, resp);
    }
}
