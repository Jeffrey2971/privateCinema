package com.jeffrey.servlet;

import com.utils.UnZip;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author jeffrey
 * @ClassName: GetData
 * @Description:
 * @date: 2021/9/11 4:11 下午
 * @version:
 * @since JDK 1.8
 */

public class UploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("static/html/upload.html");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1、判断上传的表单中是否携带文件
        if (ServletFileUpload.isMultipartContent(req)) {
            // 新建 FileItemFactory 工厂实现类
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            // 新建 ServletFileUpload 实例
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                // 解析请求
                List<FileItem> list = servletFileUpload.parseRequest(req);
                for (FileItem fileItem : list) {
                    // 判断表单项是否为普通类型
                    if (fileItem.isFormField()) {
                        // 获取字段中的 name 属性值
                        String fieldName = fileItem.getFieldName();
                        // 获取 value 属性值
                        String string = fileItem.getString("UTF-8");
                        System.out.println(fieldName + " = " + string);
                    } else {
                        req.setCharacterEncoding("UTF-8");
                        resp.setContentType("text/html;charset=UTF-8");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String fieldName = fileItem.getFieldName();
                        try {
                            PrintWriter writer = resp.getWriter();

                            String dirName = fileItem.getName().replace(".zip", "");

                            File tmp = new File(getServletContext().getRealPath("/static/tmp"));
                            File video = new File(getServletContext().getRealPath("/static/video"));

                            File saveTmpFile = new File(tmp + "/" + fileItem.getName());
                            File saveTmpDir = new File(tmp + "/" + dirName);
                            File saveVideoDir = new File(video + "/" + dirName);

                            fileItem.write(saveTmpFile);

                            if (saveVideoDir.mkdir() && saveTmpDir.mkdir()) {
                                /*
                                    0 解压成功
                                    1 压缩包内含有其他压缩包
                                    2 编码异常
                                    3 其他异常
                                    4 含有文件夹
                                    5 含有非 mp4
                                 */

                                int statusCode = UnZip.unZip(saveTmpFile, saveTmpDir.toString(), StandardCharsets.UTF_8);
                                if (statusCode == 0) {
                                    if (saveTmpDir.renameTo(saveVideoDir)) {
                                        resp.sendRedirect("/select");
                                    } else {
                                        writer.write(sdf.format(new Date()) + "：解压过程中出现异常");
                                    }
                                } else if (statusCode == 1) {
                                    writer.write(sdf.format(new Date()) + "：压缩包内含有其他压缩包，请检查后重试");


                                } else if (statusCode == 2) {
                                    writer.write(sdf.format(new Date()) + "：压缩包请使用 UTF-8 编码格式");


                                } else if (statusCode == 3) {
                                    writer.write(sdf.format(new Date()) + "：解压过程中出现异常");


                                } else if (statusCode == 4) {
                                    writer.write(sdf.format(new Date()) + "：压缩包内含有文件夹");


                                } else if (statusCode == 5) {
                                    writer.write(sdf.format(new Date()) + "：压缩包内含有非 MPEG-4 格式视频");


                                }
                                writer.flush();
                                if (statusCode != 0) {
                                    System.out.println(saveVideoDir);
                                    System.out.println(saveTmpDir);
                                    System.out.println("移除：" + saveVideoDir.delete());
                                    System.out.println("移除：" + saveTmpDir.delete());
                                }
                                System.out.println("移除：" + saveTmpFile.delete());
                            }
                            return;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
    }
}