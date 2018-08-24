package com.haulmont.testtask.model.dao;

import com.haulmont.testtask.model.dataSource.DataSourceWrapper;
import com.haulmont.testtask.model.entity.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorDao {
    final static private String QUERY_GET_ALL_DOCTORS = "select doctor_id, name, lastname, patronymic, specialty " +
            "from Doctor";
    final static private String QUERY_GET_DOCTOR_BY_ID = "select doctor_id, name, lastname, patronymic, specialty " +
            "from Doctor where doctor_id = ?";
    final static private String QUERY_CREATE_DOCTOR = "insert into Doctor (name, lastname, patronymic, specialty) " +
            "values (?, ?, ?, ?)";
    final static private String QUERY_UPDATE_DOCTOR = "update Doctor set " +
            "name = ?, lastname = ?, patronymic = ?, specialty = ? where doctor_id = ?";
    final static private String QUERY_DELETE_DOCTOR = "delete from Doctor where doctor_id = ?";


    public List<Doctor> getAll() throws SQLException {
        List<Doctor> doctorList = new ArrayList<>();
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_GET_ALL_DOCTORS)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                Long id = resultSet.getLong("doctor_id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastname");
                String patronymic = resultSet.getString("patronymic");
                String specialty = resultSet.getString("specialty");
                Doctor doctor = new Doctor(id, name, lastName, patronymic, specialty);
                doctorList.add(doctor);
            }
        }
        return doctorList;
    }

    public Doctor getById(Long id) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_GET_DOCTOR_BY_ID)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String name = resultSet.getString("name");
            String lastName = resultSet.getString("lastname");
            String patronymic = resultSet.getString("patronymic");
            String specialty = resultSet.getString("specialty");
            return new Doctor(id, name, lastName, patronymic, specialty);
        }
    }

    public boolean create(Doctor doctor) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_CREATE_DOCTOR)
        ) {
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getPatronymic());
            statement.setString(4, doctor.getSpecialty());
            return statement.executeUpdate() != 0;
        }
    }

    public boolean update(Doctor doctor) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE_DOCTOR)
        ) {
            statement.setString(1, doctor.getName());
            statement.setString(2, doctor.getLastName());
            statement.setString(3, doctor.getPatronymic());
            statement.setString(4, doctor.getSpecialty());
            statement.setLong(5, doctor.getId());
            return statement.executeUpdate() != 0;
        }
    }

    public boolean delete(Long id) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_DELETE_DOCTOR)
        ) {
            statement.setLong(1, id);
            return statement.executeUpdate() != 0;
        }
    }
}
