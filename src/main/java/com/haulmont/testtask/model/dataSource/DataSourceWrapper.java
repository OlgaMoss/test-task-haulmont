package com.haulmont.testtask.model.dataSource;

import org.hsqldb.jdbc.JDBCDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class DataSourceWrapper {
    final static private String URL = "jdbc:hsqldb:file:data/db";
    final static private String USER = "SA";
    final static private String PASSWORD = "";
    final static private String DB_NAME = "data/db";

    private static JDBCDataSource dataSource;

    public static void init() {
        try {
            dataSource = new JDBCDataSource();
            dataSource.setUser(USER);
            dataSource.setPassword(PASSWORD);
            dataSource.setURL(URL);
            dataSource.setDatabaseName(DB_NAME);
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
