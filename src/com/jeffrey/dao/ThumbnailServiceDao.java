package com.jeffrey.dao;

import java.util.List;

public interface ThumbnailServiceDao {
    /**
     * 获取所有 md5
     * @return md5 list
     */
    boolean equalsName(String name);

    /**
     * 根据传递的 key 返回该 key 对应的缩略图列表
     * @param name 文件 key
     * @return 该 key 的缩略图列表
     */
    List<String> getThumbnailList(String name);

    /**
     * 清空表中所有数据
     */
    void truncateData();

    void data(Object[] obj);

    /**
     * 根据传递的 name 属性删除对应的缩略图
     * @param name 文件名
     */
    void delete(String name);
}
