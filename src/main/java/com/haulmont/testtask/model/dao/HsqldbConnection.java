package com.haulmont.testtask.model.dao;

import org.hsqldb.jdbc.JDBCDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HsqldbConnection {
    final static private String URL = "jdbc:hsqldb:file:data/db";
    final static private String ADD_URL_IF_EXIST = ";ifexists=true";
    final static private String USER = "SA";
    final static private String PASSWORD = "";
    final static private String DB_NAME = "db";

    private JDBCDataSource dataSource;
    private Connection connection;

    private static HsqldbConnection instance;

    public static synchronized HsqldbConnection getInstance() {
        if (instance == null) {
            instance = new HsqldbConnection();
        }
        return instance;
    }

    private HsqldbConnection() {
        init();
    }

    private void init() {
        try {
            dataSource = new JDBCDataSource();
            dataSource.setUser(USER);
            dataSource.setPassword(PASSWORD);
            dataSource.setURL(URL + ADD_URL_IF_EXIST);
            dataSource.setDatabaseName(DB_NAME);
            connection = dataSource.getConnection();
            this.createTables(connection);
            this.insertTables(connection);
        } catch (Exception e) {
            System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }
    }

    private void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE patients_tbl (" +
                "   id_patient NUMERIC IDENTITY PRIMARY KEY," +
                "   name VARCHAR(50) NOT NULL," +
                "   lastname VARCHAR(50) NOT NULL," +
                "   patronymic VARCHAR(50) NOT NULL," +
                "   phone_number VARCHAR(15));");

        statement.executeUpdate("CREATE TABLE doctors_tbl (" +
                "   id_doctor NUMERIC IDENTITY PRIMARY KEY," +
                "   name VARCHAR(50) NOT NULL," +
                "   lastname VARCHAR(50) NOT NULL," +
                "   patronymic VARCHAR(50) NOT NULL," +
                "   specialty VARCHAR(60) NOT NULL);");

        statement.executeUpdate("CREATE TABLE priority_tbl (" +
                "  id_priority NUMERIC IDENTITY PRIMARY KEY," +
                "  priority VARCHAR(6) NOT NULL);");

        statement.executeUpdate("CREATE TABLE recipes_tbl (" +
                "   id_recipe NUMERIC IDENTITY PRIMARY KEY," +
                "   description VARCHAR(255) NOT NULL," +
                "   id_doctor NUMERIC NOT NULL," +
                "   id_patient NUMERIC NOT NULL," +
                "   creation_date DATE NOT NULL," +
                "   duration DATE," +
                "   id_priority NUMERIC," +
                "   FOREIGN KEY (id_doctor) REFERENCES doctors_tbl(id_doctor)," +
                "   FOREIGN KEY (id_patient) REFERENCES patients_tbl(id_patient)," +
                "   FOREIGN KEY (id_priority) REFERENCES priority_tbl(id_priority));");
    }

    private void insertTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO patients_tbl (name, lastname, patronymic, phone_number) VALUES ('Alex0', 'Moss0', 'alexeivich0', '1');");
        statement.executeUpdate("INSERT INTO patients_tbl (name, lastname, patronymic, phone_number) VALUES ('Alex1', 'Moss1', 'alexeivich1', '2');");
        statement.executeUpdate("INSERT INTO patients_tbl (name, lastname, patronymic, phone_number) VALUES ('Alex2', 'Moss2', 'alexeivich2', '3');");
        statement.executeUpdate("INSERT INTO patients_tbl (name, lastname, patronymic, phone_number) VALUES ('Alex3', 'Moss3', 'alexeivich3', '4');");
    }

    public Connection getConnection() {
        return connection;
    }

}
