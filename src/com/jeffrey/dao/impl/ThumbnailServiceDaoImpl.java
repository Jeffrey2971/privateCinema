package com.jeffrey.dao.impl;

import com.jeffrey.dao.ThumbnailServiceDao;
import com.jeffrey.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @author jeffrey
 * @ClassName: ThumbnailServiceDaoImpl
 * @Description:
 * @date: 2021/11/8 7:20 下午
 * @version:
 * @since JDK 1.8
 */


public class ThumbnailServiceDaoImpl implements ThumbnailServiceDao {
    private final QueryRunner queryRunner = new QueryRunner();

    @Override
    public List<String> getThumbnailList(String name) {
        String sql = "SELECT img_list FROM thumbnail WHERE `name` = ?";
        Connection connection = null;

        try {
            connection = JdbcUtils.getConnection();
            String imgListStr = queryRunner.query(connection, sql, new ScalarHandler<>("img_list"), name);
            if (imgListStr != null) {
                List<String> lists = Arrays.asList(imgListStr.split(","));
                Collections.sort(lists);
                return lists;

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return null;
    }

    @Override
    public boolean equalsName(String name) {
        String sql = "SELECT `name` FROM thumbnail WHERE `name` = ?";
        Connection connection = null;
        try {
            connection = JdbcUtils.getConnection();
            String queryName = queryRunner.query(connection, sql, new ScalarHandler<>(), name);
            return name.equals(queryName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return false;
    }

    @Override
    public void truncateData() {
        String sql = "TRUNCATE thumbnail";
        Connection connection = null;
        try {
            connection = JdbcUtils.getConnection();
            queryRunner.update(connection, sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public boolean initData(HashMap<String, String> dataMap) {
        String sql = "INSERT INTO thumbnail (`name`, img_list)VALUES(?,?)";
        Connection connection = null;

        try {
            connection = JdbcUtils.getConnection();
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                if (queryRunner.update(connection, sql, entry.getKey(), entry.getValue().replace(" ", "").replace("[", "").replace("]", "")) <= 0) {
                    throw new RuntimeException("插入数据错误，初始化失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return true;
    }

    @Override
    public void delete(String name) {
        String sql = "DELETE FROM thumbnail WHERE name = ?";
        Connection connection = null;
        try {
            connection = JdbcUtils.getConnection();
            queryRunner.update(connection, sql, name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Test
    public void t(){
        String sql = "DELETE FROM thumbnail WHERE img_list = ?";
        Connection connection = null;
        try {
            connection = JdbcUtils.getConnection();
            queryRunner.update(connection, sql, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
    }
}
