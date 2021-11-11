package com.jeffrey.service;

import java.util.HashMap;
import java.util.List;

public interface ThumbnailService {

    /**
     * 根据传递的 name 属性删除对应的缩略图
     * @param name 文件名
     */
    void delete(String name);

    /**
     * 根据指定的 key 返回缩略图列表
     * @param name 指定文件 key
     * @return 缩略图列表
     */
    List<String> getThumbnailList(String name);

    /**
     * 获取所有 md5
     * @return md5 list
     */
    boolean equalsName(String name);

    /**
     * 清空表中所有数据
     */
    void truncateData();

    /**
     * 服务器初始化时写入的数据
     * @param dataMap key : 写入文件 md5 value : 对应文件缩略图
     * @return 是否添入成功
     */
    boolean initData(HashMap<String, String> dataMap);
}
