package com.jeffrey.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author jeffrey
 * @ClassName: JdbcUtils
 * @Description:
 * @date: 2021/4/27 5:33 下午
 * @version:
 * @since JDK 1.8
 */
public class JdbcUtils {

    private static DataSource dataSource;

    static {

        InputStream is = null;
        try {

            is  = JdbcUtils.class.getClassLoader().getResourceAsStream("conf/druid-config.properties");

            Properties properties = new Properties();
            // 读取 jdbc.properties属性配置文件
            // 从流中加载数据
            properties.load(is);
            // 创建 数据库连接池
            dataSource = DruidDataSourceFactory.createDataSource(properties);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 获取数据库连接池中的连接
     *
     * @return 如果返回null, 说明获取连接失败<br />有值就是获取连接成功
     */
    public static Connection getConnection() {

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * 关闭连接，放回数据库连接池
     *
     * @param conn 数据库连接
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(getConnection());
    }
}
