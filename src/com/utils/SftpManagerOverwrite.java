package com.utils;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * @Description: SFTP 传输相关功能，包括文件上传、下载、删除、获取列表等功能
 * @Author: Jeffrey
 */
public abstract class SftpManagerOverwrite {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SftpManagerOverwrite.class);
    public static final String SFTP_PROTOCOL = "sftp";

    /**
     * @param host     服务器地址
     * @param username 用户名
     * @param password 密码
     * @param port     端口
     * @return ChannelSftp
     * @throws Exception Exception
     * @Description: 使用用户名密码登录
     */
    public static ChannelSftp connect(String host, String username, String password, int port) throws Exception {
        Channel channel;
        ChannelSftp sftp = null;
        JSch jsch = new JSch();

        Session session = createSession(jsch, host, username, port);
        // 设置登陆主机的密码
        session.setPassword(password);
        // 设置登陆超时时间
        session.connect(50000);
        LOGGER.info("Session connected to " + host + ".");
        try {
            // 创建sftp通信通道
            channel = session.openChannel(SFTP_PROTOCOL);
            channel.connect(1000);
            LOGGER.info("Channel created to " + host + ".");
            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            LOGGER.error("exception when channel create.", e);
        }
        return sftp;
    }

    /**
     * @param username   用户名
     * @param host       服务器地址
     * @param port       端口号
     * @param privateKey 密匙文件路径
     * @param passphrase 密匙密码
     * @return ChannelSftp
     * @throws Exception Exception
     * @Description: 携带密匙方式登录
     */
    public static ChannelSftp connect(String username, String host, int port, String privateKey, String passphrase) throws Exception {
        Channel channel;
        ChannelSftp sftp = null;
        JSch jsch = new JSch();

        // 设置密钥和密码 ,支持密钥的方式登陆
        if (StringUtils.isNotEmpty(privateKey)) {
            if (StringUtils.isNotEmpty(passphrase)) {
                // 设置带口令的密钥
                jsch.addIdentity(privateKey, passphrase);
            } else {
                // 设置不带口令的密钥
                jsch.addIdentity(privateKey);
            }
        }
        Session session = createSession(jsch, host, username, port);
        // 设置登陆超时时间
        session.connect(15000);
        LOGGER.info("Session connected to " + host + ".");
        try {
            // 创建sftp通信通道
            channel = session.openChannel(SFTP_PROTOCOL);
            channel.connect(1000);
            LOGGER.info("Channel created to " + host + ".");
            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            LOGGER.error("exception when channel create.", e);
        }
        return sftp;
    }

    /**
     * @Description: 将文件上传到服务器
     * @param sftp ChannelSftp
     * @param srcFile 本地文件路径
     * @param dest 目标路径，如果该路径为一个目录那么将文件上传到该目录内，文件名和上传的文件名一致
     */
    public static void upload(ChannelSftp sftp, String srcFile, String dest) {
        try {
            File file = new File(srcFile);
            if (file.isDirectory()) {
                sftp.cd(srcFile);
                for (String fileName : file.list()) {
                    sftp.put(srcFile + SystemUtils.FILE_SEPARATOR + fileName, dest);
                }
            }
            sftp.put(srcFile, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description: 将集合中的本地文件上传到服务器，destPath 必须为一个目录，如目录不存在或不是目录不是则自动创建
     * @param sftp ChannelSftp
     * @param fileList 要上传的文件集合
     * @param destPath 目标服务器目录路径
     * @throws SftpException SftpException
     */
    public static void upload(ChannelSftp sftp, List<String> fileList, String destPath) throws SftpException {
        try {
            sftp.cd(destPath);
        } catch (Exception e) {
            sftp.mkdir(destPath);
        }
        for (String srcFile : fileList) {
            upload(sftp, srcFile, destPath);
        }
    }

    /**
     * @Description: 使用 SFTP 下载文件
     * @param sftp ChannelSftp
     * @param srcPath 目标服务器上的目录地址，必须为目录
     * @param saveFile 本地保存路径，如果为文件夹则保存到该文件内，如不是将创建和服务器目录同名的目录
     * @param srcFile srcPath 目录下的文件名，不能为目录
     */
    public static void download(ChannelSftp sftp, String srcPath, String saveFile, String srcFile) {
        try {
            sftp.cd(srcPath);
            File file = new File(saveFile);
            if (file.isDirectory()) {
                sftp.get(srcFile, new FileOutputStream(file + SystemUtils.FILE_SEPARATOR + srcFile));
            } else {
                sftp.get(srcFile, new FileOutputStream(file));
            }
        } catch (Exception e) {
            LOGGER.error("download file: {} error", srcPath + SystemUtils.FILE_SEPARATOR + srcFile, e);
        }
    }

    /**
     * @Description: 使用 SFTP 下载目标服务器上指定类型的文件，得到的文件名和目标服务器一致
     * @param sftp ChannelSftp
     * @param srcPath 目标服务器路径，必须为目录
     * @param savePath 本地保存目录路径，必须为目录，如不存在或不是目录则自动创建
     * @param fileTypes 包含了文件扩展名 / 后缀名的字符串类型数组
     */
    public static void download(ChannelSftp sftp, String srcPath, String savePath, String... fileTypes) {
        List<String> fileList;
        try {
            sftp.cd(srcPath);
            createDir(savePath);
            if (fileTypes.length == 0) {
                // 列出服务器目录下所有的文件列表
                fileList = listFiles(sftp, srcPath, "*");
                downloadFileList(sftp, srcPath, savePath, fileList);
                return;
            }
            for (String type : fileTypes) {
                fileList = listFiles(sftp, srcPath, "*" + type);
                parseAndUpdateDB(sftp, srcPath, savePath, fileList);
            }
        } catch (Exception e) {
            LOGGER.error("download all file in path = '" + srcPath + "' and type in " + Arrays.asList(fileTypes)
                    + " error", e);
        }

    }

    /**
     * @Description: 使用 SFTP 下载目标服务器上指定目录下所有指定的文件到本地，
     *               若本地存储路径下有文件名和下载的文件名一致将继续下载并覆盖本地文件
     * @param sftp ChannelSftp
     * @param srcPath 服务器目标路径，必须为目录
     * @param savePath 本地存储目录路径，必须为目录
     * @param fileList 需要被下载的文件名集合
     * @throws SftpException SftpException
     */
    public static void downloadFileList(ChannelSftp sftp, String srcPath, String savePath, List<String> fileList) throws SftpException {
        sftp.cd(srcPath);
        for (String srcFile : fileList) {
            LOGGER.info("srcFile: " + srcFile);
            String localPath = savePath + SystemUtils.FILE_SEPARATOR + srcFile;
            sftp.get(srcFile, localPath);
        }
    }

    /**
     * @Description: sftp下载目标服务器上所有指定的文件，若本地存储路径下存在与下载重名的文件, 则忽略(不下载)该文件
     * @param sftp ChannelSftp
     * @param srcPath 目标服务器
     * @param savePath 本地存储目录路径，必须为目录
     * @param fileList 需要被下载的文件名集合
     * @throws SftpException SftpException
     */
    private static void parseAndUpdateDB(ChannelSftp sftp, String srcPath, String savePath, List<String> fileList) throws SftpException {
        sftp.cd(srcPath);
        for (String srcFile : fileList) {
            String localPath = savePath + SystemUtils.FILE_SEPARATOR + srcFile;
            File localFile = new File(localPath);
            // savePath路径下已有文件与下载文件重名, 忽略这个文件
            if (localFile.exists() && localFile.isFile()) {
                continue;
            }

            LOGGER.info("start downloading file: [" + srcFile + "], parseAndUpdate to DB");
            sftp.get(srcFile, localPath);
            //updateDB(localFile);
        }
    }

    /**
     * @Description: 获取目标服务器指定路径下以 regex 格式指定的文件列表
     * @param sftp ChannelSftp
     * @param srcPath 目标服务器目录地址，必须为目录
     * @param regex 正则表达式
     * @return 匹配上的文件列表集合
     * @throws SftpException SftpException
     */
    @SuppressWarnings("unchecked")
    public static List<String> listFiles(ChannelSftp sftp, String srcPath, String regex) throws SftpException {
        List<String> fileList = new ArrayList<>();
        sftp.cd(srcPath); // 如果srcPath不是目录则会抛出异常
        if ("".equals(regex) || regex == null) {
            regex = "*";
        }
        Vector<ChannelSftp.LsEntry> sftpFile = sftp.ls(regex);
        String fileName;
        for (ChannelSftp.LsEntry lsEntry : sftpFile) {
            fileName = lsEntry.getFilename();
            fileList.add(fileName);
        }
        return fileList;
    }

    /**
     * @Description: 删除目标服务器上的指定目录下的指定文件
     * @param dirPath 目标服务器目录，必须为目录
     * @param file 目标服务器目录下的指定文件，必须为文件
     * @param sftp ChannelSftp
     * @throws SftpException SftpException
     */
    public static void delete(String dirPath, String file, ChannelSftp sftp) throws SftpException {
        String now = sftp.pwd();
        sftp.cd(dirPath);
        sftp.rm(file);
        sftp.cd(now);
    }

    /**
     * @Description: 关闭连接
     * @param sftp ChannelSftp
     */
    public static void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                } else if (sftp.isClosed()) {
                    LOGGER.info("sftp is closed already");
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                    System.out.println("目标连接已关闭");
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    /**
     * @Description: 创建会话
     * @param jsch JSch
     * @param host 目标服务器地址
     * @param username 用户名
     * @param port 目标服务器端口
     * @return Session 会话
     * @throws Exception Exception
     */
    private static Session createSession(JSch jsch, String host, String username, int port) throws Exception {
        Session session;
        if (port <= 0) {
            // 连接服务器，采用默认端口
            session = jsch.getSession(username, host);
        } else {
            // 采用指定的端口连接服务器
            session = jsch.getSession(username, host, port);
        }
        // 如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception(host + "session is null");
        }
        // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        return session;
    }

    /**
     * @Description: 创建目录
     * @param savePath 本地路径
     * @return 创建目录之后的 File 对象
     * @throws Exception Exception
     */
    private static File createDir(String savePath) throws Exception {
        File localPath = new File(savePath);
        if (!localPath.exists() && !localPath.isFile()) {
            if (!localPath.mkdir()) {
                throw new Exception(localPath + " directory can not create.");
            }
        }
        return localPath;
    }
}