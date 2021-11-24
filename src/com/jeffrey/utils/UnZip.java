package com.jeffrey.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/*
    0 解压成功
    1 压缩包内含有其他压缩包
    2 编码异常
    3 其他异常
    4 含有文件夹
    5 含有非 mp4
    6 伪造压缩文件
 */
public class UnZip {

    private static final int BUFFER_SIZE = 2 * 1024;

    public static int unZip(File srcFile, String destDirPath, Charset charset) throws RuntimeException {
        if (!CheckZip.isArchiveFile(srcFile)) {
            return 6;
        }
        long start = System.currentTimeMillis();
        // 开始解压
        ZipFile zipFile = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            zipFile = new ZipFile(srcFile, charset);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                File unZipPathName = new File(entry.getName());

                if (unZipPathName.isDirectory()) {
                    return 4;
                } else if (unZipPathName.getName().contains("zip")) {
                    return 1;
                } else if (!unZipPathName.getName().contains(".mp4")) {
                    System.out.println(unZipPathName.getName());
                    return 5;
                } else if (unZipPathName.getName().contains("#")) {
                    return 7;
                }

                System.out.println("解压：" + unZipPathName);
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    if (!dir.mkdirs()) {
                        return 3;
                    }
                } else {
                    File targetFile = new File(destDirPath + File.separator + entry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        if (!targetFile.getParentFile().mkdirs()) {
                            return 3;
                        }
                    }
                    if (!targetFile.createNewFile()) {
                        return 3;
                    }
                    is = zipFile.getInputStream(entry);
                    fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[BUFFER_SIZE];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("解压完成，耗时：" + (end - start) + " ms");

            // 删除 MacOS 下压缩包附带的无用文件
            File[] deleteFiles = new File(destDirPath).listFiles(File::isDirectory);
            if (deleteFiles != null) {

                for (File deleteFile : deleteFiles) {
                    File[] files = deleteFile.listFiles();
                    if (files != null) {
                        for (File item : files) {
                            if (item.delete()) {
                                System.out.println("移除 MacOS 压缩包附带无用文件：" + item.getName());
                            }
                        }
                    }
                }

                for (File item : deleteFiles) {
                    if (item.delete()) {
                        System.out.println("移除空目录：" + item.getName());
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {

                return 2;
            }
            e.printStackTrace();
            return 3;
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
