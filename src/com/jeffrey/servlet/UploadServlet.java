package com.jeffrey.servlet;

import com.google.gson.Gson;
import com.jeffrey.pojo.Upload;
import com.jeffrey.service.UploadService;
import com.jeffrey.service.impl.UploadServiceImpl;
import com.jeffrey.utils.GetRequestAddress;
import com.jeffrey.utils.UnZip;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UploadServlet extends BaseServlet {

    private static final Gson gson = new Gson();
    protected static final UploadService UPLOAD_SERVICE = new UploadServiceImpl();

    protected void upload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/static/html/upload.html");
    }

    protected void checkFileSize(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int size = (int) Double.parseDouble(req.getParameter("size"));
        long freeSize = new File("/").getFreeSpace() / (1024 * 1024);

        resp.getWriter().write(gson.toJson(
                new Upload(size * 2L > freeSize ? 1 : 0, "文件大小超出服务器存储范围，上传文件原大小：" + size + " MB ，服务器剩余空间：" + freeSize + " MB（以 " + size + " * 2 = " + size * 2 + " 的结果为准）"))
        );
    }

    protected void checkFileName(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File[] list = new File(getServletContext().getRealPath("/static/video")).listFiles();
        ArrayList<String> array = new ArrayList<>();

        if (list != null) {
            for (File item : list) {
                array.add(item.getName());
            }
        }

        resp.getWriter().write(gson.toJson(new Upload(array.contains(req.getParameter("name")) ? 1 : 0, "上传的目录名（压缩包名）已在服务器存在")));
    }

    protected void fileUpload(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (ServletFileUpload.isMultipartContent(req)) {
            FileItemFactory fileItemFactory = new DiskFileItemFactory();
            ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
            try {
                List<FileItem> list = servletFileUpload.parseRequest(req);
                for (FileItem fileItem : list) {
                    if (fileItem.isFormField()) {
                        String fieldName = fileItem.getFieldName();
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
                                    6 伪装压缩包
                                    7 文件名含有特殊符号
                                 */

                                int statusCode = UnZip.unZip(saveTmpFile, saveTmpDir.toString(), StandardCharsets.UTF_8);
                                if (statusCode == 0) {
                                    if (saveTmpDir.renameTo(saveVideoDir)) {
                                        resp.sendRedirect("/player?action=select");
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
                                } else if (statusCode == 6) {
                                    UPLOAD_SERVICE.addBlackListId(GetRequestAddress.getIPAddress(req));
                                    resp.sendRedirect("https://baike.baidu.com/item/%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E7%BD%91%E7%BB%9C%E5%AE%89%E5%85%A8%E6%B3%95/16843044?fromtitle=%E7%BD%91%E7%BB%9C%E5%AE%89%E5%85%A8%E6%B3%95&fromid=12291792&fr=aladdin");
                                } else if (statusCode == 7) {
                                    writer.write(sdf.format(new Date()) + "：请修改包含 # 符号的文件名");
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