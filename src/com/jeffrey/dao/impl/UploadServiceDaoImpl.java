package com.jeffrey.dao.impl;

import com.jeffrey.dao.UploadServiceDao;
import com.jeffrey.utils.JdbcUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class UploadServiceDaoImpl implements UploadServiceDao {

    private final QueryRunner queryRunner = new QueryRunner();

    @Override
    public void addBlackListId(String ip) {
        Connection connection = null;
        String sql = "INSERT INTO black_list (ip, time) VALUES (? ,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm-ss");
        try {
            connection = JdbcUtils.getConnection();
            queryRunner.update(connection, sql, ip, sdf.format(new Date(System.currentTimeMillis())));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
    }

    @Override
    public boolean isExistsTable(String ip, String table) {
        Connection connection = null;
        String sql = "SELECT ip FROM " + table + " WHERE ip = ?";
        try {
            connection = JdbcUtils.getConnection();
            return queryRunner.query(connection, sql, new ScalarHandler<>("ip"), ip) != null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.close(connection);
        }
        return false;
    }
}
