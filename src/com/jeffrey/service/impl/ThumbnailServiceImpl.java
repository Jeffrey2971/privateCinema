package com.jeffrey.service.impl;

import com.jeffrey.dao.ThumbnailServiceDao;
import com.jeffrey.dao.impl.ThumbnailServiceDaoImpl;
import com.jeffrey.service.ThumbnailService;
import java.util.HashMap;
import java.util.List;

public class ThumbnailServiceImpl implements ThumbnailService {

    private static final ThumbnailServiceDao THUMBNAIL_SERVICE_DAO = new ThumbnailServiceDaoImpl();

    @Override
    public void delete(String name) {
        System.out.println("移除数据表对应缩略图");
        THUMBNAIL_SERVICE_DAO.delete(name);
    }

    @Override
    public List<String> getThumbnailList(String name) {
        return THUMBNAIL_SERVICE_DAO.getThumbnailList(name);
    }

    @Override
    public boolean equalsName(String name) {
        System.out.println("检查新增文件是否存在");
        return THUMBNAIL_SERVICE_DAO.equalsName(name);

    }

    @Override
    public void truncateData() {
        System.out.println("清除数据表");
        THUMBNAIL_SERVICE_DAO.truncateData();
    }

    @Override
    public boolean initData(HashMap<String, String> dataMap) {
        System.out.println("批量插入数据");
        return THUMBNAIL_SERVICE_DAO.initData(dataMap);
    }


}
