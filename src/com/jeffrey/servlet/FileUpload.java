package com.jeffrey.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author jeffrey
 * @ClassName: FileUpload
 * @Description:
 * @date: 2021/9/12 1:28 上午
 * @version:
 * @since JDK 1.8
 */


public class FileUpload extends HttpServlet {

    // 上传文件
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] list = new File(getServletContext().getRealPath("/static/video")).list();
        System.out.println(list);
        req.setAttribute("tree", list);
        req.getRequestDispatcher("/static/jsp/upload.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (ServletFileUpload.isMultipartContent(req)) {
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
            try {
                List<FileItem> list = servletFileUpload.parseRequest(req);
                for (FileItem fileItem : list) {
                    if (!fileItem.isFormField()) {
                        // 文件字段
                        try {
                            System.out.println(getServletContext().getRealPath("/static/video/") + fileItem.getName());
                            fileItem.write(new File(getServletContext().getRealPath("/static/video/") + fileItem.getName()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        // 其他字段
                        resp.getWriter().write("请上传文件！");
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }

        resp.sendRedirect("/select");
    }
}
