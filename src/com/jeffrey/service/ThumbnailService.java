package com.jeffrey.service;

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
     * 插入单条数据，该 Object[] 类型的数组只有两个人值，obj[0] 文件名 obj[1] 缩略图列表
     * @param obj obj[]
     */
    void addData(Object[] obj);

}
