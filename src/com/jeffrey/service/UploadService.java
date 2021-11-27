package com.jeffrey.service;

public interface UploadService {
    /**
     * 将恶意用户添加进黑名单
     * @param ip 违规 IP 地址
     */
    void addBlackListId(String ip);

    /**
     * 判断给定的 IP 是否在给定的表内
     * @param ip 请求的 IP 地址
     * @param table 指定的表名
     * @return 给定的 IP 是否在指定的表内
     */
    boolean isExistsTable(String ip, String table);
}
