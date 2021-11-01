package com.jeffrey.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author jeffrey
 * @ClassName: SetMimeType
 * @Description:
 * @date: 2021/10/25 10:39 下午
 * @version:
 * @since JDK 1.8
 */


public class SetVideoData extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String arg = req.getParameter("file");
        File file = new File(getServletContext().getRealPath(arg));
        // 视频标题
        req.setAttribute("title", file.getName());
        // 视频播放地址
        req.setAttribute("videoSrc", arg);
        // 视频未播放时展示图片
        req.setAttribute("videoPoster", "");
        // 缩略图列表
        req.setAttribute("previewList", "");
        req.getRequestDispatcher("dynamic/jsp/desktopPluginExample.jsp").forward(req,resp);
    }
}
