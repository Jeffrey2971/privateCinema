package com.jeffrey.dao.impl;

import com.jeffrey.dao.ThumbnailServiceDao;
import com.jeffrey.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

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
    public void data(Object[] obj) {
        String sql = "INSERT INTO thumbnail (`name`, img_list)VALUES(?,?)";
        Connection connection = null;

        try {
            connection = JdbcUtils.getConnection();
            queryRunner.update(connection, sql, obj[0], obj[1].toString().replace(" ", "").replace("[", "").replace("]", ""));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
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
}
