package com.jeffrey.service;

public interface UploadService {
    /**
     * 将恶意用户添加进黑名单
     * @param ip 违规 IP 地址
     */
    void addBlackListId(String ip);

    /**
     * 根据给定的 ip 判断是否为违规 IP
     * @param ip 请求 IP 地址
     * @return 是否违规
     */
    boolean isBlackUser(String ip);
}
