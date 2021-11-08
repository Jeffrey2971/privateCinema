package com.jeffrey.service.impl;

import com.jeffrey.dao.UploadServiceDao;
import com.jeffrey.dao.impl.UploadServiceDaoImpl;
import com.jeffrey.service.UploadService;

/**
 * @author jeffrey
 * @ClassName: UploadServiceImpl
 * @Description:
 * @date: 2021/11/8 6:06 下午
 * @version:
 * @since JDK 1.8
 */


public class UploadServiceImpl implements UploadService {

    private static final UploadServiceDao uploadServiceDao = new UploadServiceDaoImpl();

    @Override
    public void addBlackListId(String ip) {
        uploadServiceDao.addBlackListId(ip);
    }

    @Override
    public boolean isBlackUser(String ip) {
        return uploadServiceDao.isBlackUser(ip);
    }
}