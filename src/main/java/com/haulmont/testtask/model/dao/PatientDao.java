package com.haulmont.testtask.model.dao;

import com.haulmont.testtask.model.dataSource.DataSourceWrapper;
import com.haulmont.testtask.model.entity.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientDao {

    final static private String QUERY_GET_ALL_PATIENTS = "select patient_id, name, lastname, patronymic, phone_number " +
            "from Patient";
    final static private String QUERY_GET_PATIENT_BY_ID = "select patient_id, name, lastname, patronymic, phone_number " +
            "from Patient where patient_id = ?";
    final static private String QUERY_CREATE_PATIENT = "insert into Patient (name, lastname, patronymic, phone_number) " +
            "values (?, ?, ?, ?)";
    final static private String QUERY_UPDATE_PATIENT = "update Patient set " +
            "name = ?, lastname = ?, patronymic = ?, phone_number = ? where patient_id = ?";
    final static private String QUERY_DELETE_PATIENT = "delete from Patient where patient_id = ?";

    public List<Patient> getAll() throws SQLException {
        List<Patient> patientList = new ArrayList<>();
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_GET_ALL_PATIENTS)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("patient_id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastname");
                String patronymic = resultSet.getString("patronymic");
                String phoneNumber = resultSet.getString("phone_number");
                Patient patient = new Patient(id, name, lastName, patronymic, phoneNumber);
                patientList.add(patient);
            }
        }
        return patientList;
    }

    public Patient getById(Long id) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_GET_PATIENT_BY_ID)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            String lastName = resultSet.getString("lastname");
            String patronymic = resultSet.getString("patronymic");
            String phoneNumber = resultSet.getString("phone_number");

            return new Patient(id, name, lastName, patronymic, phoneNumber);
        }
    }

    public boolean create(Patient patient) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_CREATE_PATIENT)
        ) {
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getPhoneNumber());
            return statement.executeUpdate() != 0;
        }
    }

    public boolean update(Patient patient) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE_PATIENT)
        ) {
            statement.setString(1, patient.getName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getPatronymic());
            statement.setString(4, patient.getPhoneNumber());
            statement.setLong(5, patient.getId());
            return statement.executeUpdate() != 0;
        }
    }

    public boolean delete(Long id) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_DELETE_PATIENT)
        ) {
            statement.setLong(1, id);
            return statement.executeUpdate() != 0;
        }
    }
}
