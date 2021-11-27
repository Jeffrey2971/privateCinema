package com.jeffrey.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author jeffrey
 * @ClassName: util
 * @Description:
 * @date: 2020/11/30 9:49 下午
 * @version:
 * @since JDK 1.8
 */
public class RequestUtils {

    /**
     * 向指定的地址发送 POST 请求并带着 Data 数据
     *
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, String data) {
        InputStream is = null;
        try {
            URL urlObj = new URL(url);
            URLConnection urlConnection = urlObj.openConnection();
            // 如需发送数据必须设置为可发送数据状态，默认不发送
            urlConnection.setDoOutput(true);
            // 获取输出流
            OutputStream ops = urlConnection.getOutputStream();
            ops.write(data.getBytes());
            ops.close();
            // 获取输入流读取数据
            is = urlConnection.getInputStream();

            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));

            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * @description: 向指定的地址发送 GET 请求
     * @author: jeffrey
     * @date: 2020/11/30
     * @return: java.lang.String
     * @params: url
     */
    public static String get(String url) {
        try {
            URL urlObj = new URL(url);
            // 开链接
            URLConnection urlConnection = urlObj.openConnection();
            InputStream is = urlConnection.getInputStream();

            byte[] b = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = is.read(b)) != -1) {
                sb.append(new String(b, 0, len));

            }

            return sb.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param picUrl   http 图片链接
     * @param savePath 保存地址
     * @return 是否下载成功
     * @Description: 根据提供的链接及保存地址下载图片
     */
    public static boolean get(String picUrl, File savePath) {

        InputStream inputStream = null;
        try {
            URL url1 = new URL(picUrl);
            URLConnection uc = url1.openConnection();
            inputStream = uc.getInputStream();

            FileOutputStream out = new FileOutputStream(savePath);
            int j = 0;
            while ((j = inputStream.read()) != -1) {
                out.write(j);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return savePath.exists();


    }


}
