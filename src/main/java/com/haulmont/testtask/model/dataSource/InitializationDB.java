package com.haulmont.testtask.model.dataSource;

import java.sql.*;

public class InitializationDB {

    final static private String CREATE_PATIENT_TABLE = "CREATE TABLE Patient (" +
            "   patient_id NUMERIC IDENTITY PRIMARY KEY," +
            "   name VARCHAR(50) NOT NULL," +
            "   lastname VARCHAR(50) NOT NULL," +
            "   patronymic VARCHAR(50) NOT NULL," +
            "   phone_number VARCHAR(15));";

    final static private String CREATE_DOCTOR_TABLE = "CREATE TABLE Doctor (" +
            "   doctor_id NUMERIC IDENTITY PRIMARY KEY," +
            "   name VARCHAR(50) NOT NULL," +
            "   lastname VARCHAR(50) NOT NULL," +
            "   patronymic VARCHAR(50) NOT NULL," +
            "   specialty VARCHAR(60) NOT NULL);";

    final static private String CREATE_PRIORITY_TABLE = "CREATE TABLE Priority (" +
            "  priority_id NUMERIC IDENTITY PRIMARY KEY," +
            "  name VARCHAR(6) NOT NULL);";

    final static private String CREATE_RECIPE_TABLE = "CREATE TABLE Recipe (" +
            "   recipe_id NUMERIC IDENTITY PRIMARY KEY," +
            "   description VARCHAR(255) NOT NULL," +
            "   doctor_id NUMERIC NOT NULL," +
            "   patient_id NUMERIC NOT NULL," +
            "   creation_date DATE NOT NULL," +
            "   duration INTEGER," +
            "   priority_id NUMERIC," +
            "   FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id)," +
            "   FOREIGN KEY (patient_id) REFERENCES Patient(patient_id)," +
            "   FOREIGN KEY (priority_id) REFERENCES Priority(priority_id));";

    final static private String INSERT_PATIENT_TABLE = "INSERT INTO Patient (name, lastname, patronymic, phone_number) VALUES " +
            "('Алексей', 'Крылов', 'Александрович', '9673412')," +
            "('Елена', 'Богданова', 'Алексеевна', '89278965423')," +
            "('Лидия', 'Андреева', 'Ивановна', '2435687')," +
            "('Петр', 'Тимофеев', 'Михайлович', '2624060');";

    final static private String INSERT_DOCTOR_TABLE = "INSERT INTO Doctor (name, lastname, patronymic, specialty) VALUES " +
            "('Тамара', 'Дегтярёва', 'Евгеньевна', 'терапевт')," +
            "('Владимир', 'Попов', 'Александрович', 'хирург')," +
            "('Виталий', 'Крягин', 'Петрович', 'окулист')," +
            "('Маргарита', 'Иванова', 'Львовна', 'педиатр');";

    final static private String INSERT_PRIORITY_TABLE = "INSERT INTO Priority (name) VALUES " +
            "('Normal')," +
            "('Cito')," +
            "('Statim');";

    final static private String INSERT_RECIPE_TABLE = "INSERT INTO Recipe (description, doctor_id, patient_id, creation_date, duration, priority_id) VALUES " +
            "('принять таблетки', 0, 1, '2018-08-22', 30, 2)," +
            "('принять капли', 1, 0, '2018-08-21', 7, 0)," +
            "('купить костыли', 1, 1, '2018-08-23', 14, 1);";

    final static private String NAME_TABLE = "PATIENT";

    public void createTables() {
        try {
            queryExecution(CREATE_PATIENT_TABLE);
            queryExecution(CREATE_DOCTOR_TABLE);
            queryExecution(CREATE_PRIORITY_TABLE);
            queryExecution(CREATE_RECIPE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error: Create table. " + e);
        }
    }


    public void insertTables() {
        try {
            queryExecution(INSERT_PATIENT_TABLE);
            queryExecution(INSERT_DOCTOR_TABLE);
            queryExecution(INSERT_PRIORITY_TABLE);
            queryExecution(INSERT_RECIPE_TABLE);
        } catch (SQLException e) {
            System.err.println("Error: Insert table. " + e);
        }
    }

    private void queryExecution(String query) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.executeUpdate();
        }
    }

    public static boolean isExistTable() {
        try (
                Connection connection = DataSourceWrapper.getConnection()
        ) {
            ResultSet resultSet = connection.getMetaData().getTables(null, null, NAME_TABLE, null);
            if (!resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}


