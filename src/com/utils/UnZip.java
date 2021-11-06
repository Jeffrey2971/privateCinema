package com.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author jeffrey
 * @ClassName: UnZip
 * @Description:
 * @date: 2021/10/31 3:08 下午
 * @version:
 * @since JDK 1.8
 */


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
                    return 5;
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
