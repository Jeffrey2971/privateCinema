package com.jeffrey.service.impl;

import com.jeffrey.dao.UploadServiceDao;
import com.jeffrey.dao.impl.UploadServiceDaoImpl;
import com.jeffrey.service.UploadService;

public class UploadServiceImpl implements UploadService {

    private static final UploadServiceDao uploadServiceDao = new UploadServiceDaoImpl();

    @Override
    public void addBlackListId(String ip) {
        System.out.println("添加 ip 至黑名单");
        uploadServiceDao.addBlackListId(ip);
    }

    @Override
    public boolean isExistsTable(String ip, String table) {
        return uploadServiceDao.isExistsTable(ip, table);
    }

}
