package com.haulmont.testtask.model.dao;

import com.haulmont.testtask.model.dataSource.DataSourceWrapper;
import com.haulmont.testtask.model.entity.Doctor;
import com.haulmont.testtask.model.entity.Patient;
import com.haulmont.testtask.model.entity.Priority;
import com.haulmont.testtask.model.entity.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RecipeDao {
    final static private String QUERY_GET_ALL_RECIPES = "select recipe_id, description, doctor_id, patient_id, creation_date, duration, priority_id " +
            "from Recipe where 1=1 ";
    final static private String QUERY_GET_RECIPE_BY_ID = "select recipe_id, description, doctor_id, patient_id, creation_date, duration, priority_id " +
            "from Recipe where recipe_id = ?";
    final static private String QUERY_CREATE_RECIPE = "insert into Recipe (description, doctor_id, patient_id, creation_date, duration, priority_id) " +
            "values (?, ?, ?, ?, ?, ?)";
    final static private String QUERY_UPDATE_RECIPE = "update Recipe set " +
            "description = ?, doctor_id = ?, patient_id = ?, creation_date = ?, duration = ?, priority_id = ? where recipe_id = ?";
    final static private String QUERY_DELETE_RECIPE = "delete from Recipe where recipe_id = ? ";
    final static private String QUERY_COUNT_RECIPE_BY_DOCTOR = "select Recipe.doctor_id, count(Recipe.doctor_id) as row_count " +
            "from Recipe group by Recipe.doctor_id";
    final static private String QUERY_COUNT_PATIENT = "select count(Recipe.patient_id) as row_count from Recipe where patient_id = ?";
    final static private String QUERY_COUNT_DOCTOR = "select count(Recipe.doctor_id) as row_count from Recipe where doctor_id = ?";
    final static private String ADD_DESCRIPTION_FILTER = "and description like '?%' ";
    final static private String ADD_PATIENT_FILTER = " and patient_id = ";
    final static private String ADD_PRIORITY_FILTER = " and priority_id = ";

    public List<Recipe> getAll() throws SQLException {
        return getAll(null, null, null);
    }

    public List<Recipe> getAll(String descriptionFltr, Patient patientFltr, Priority priorityFltr) throws SQLException {
        String query = QUERY_GET_ALL_RECIPES;
        if (descriptionFltr != null && !descriptionFltr.isEmpty()) {
            query += ADD_DESCRIPTION_FILTER.replace("?", descriptionFltr);
        }
        if (patientFltr != null) {
            query += ADD_PATIENT_FILTER + patientFltr.getId();
        }
        if (priorityFltr != null) {
            query += ADD_PRIORITY_FILTER + priorityFltr.ordinal();
        }
        List<Recipe> recipeList = new ArrayList<>();
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("recipe_id");
                String description = resultSet.getString("description");
                DoctorDao doctorDao = new DoctorDao();
                Doctor doctor = doctorDao.getById(resultSet.getLong("doctor_id"));
                PatientDao patientDao = new PatientDao();
                Patient patient = patientDao.getById(resultSet.getLong("patient_id"));
                Date date = resultSet.getDate("creation_date");
                int duration = resultSet.getInt("duration");
                Priority priority = Priority.getById(resultSet.getLong("priority_id"));

                Recipe recipe = new Recipe(id, description, doctor, patient, date, duration, priority);
                recipeList.add(recipe);
            }
        }
        return recipeList;
    }

    public Recipe getById(Long id) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_GET_RECIPE_BY_ID)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            String description = resultSet.getString("description");
            DoctorDao doctorDao = new DoctorDao();
            Doctor doctor = doctorDao.getById(resultSet.getLong("doctor_id"));
            PatientDao patientDao = new PatientDao();
            Patient patient = patientDao.getById(resultSet.getLong("patient_id"));
            Date date = resultSet.getDate("creation_date");
            int duration = resultSet.getInt("duration");
            Priority priority = Priority.getById(resultSet.getLong("priority_id"));
            return new Recipe(id, description, doctor, patient, date, duration, priority);
        }
    }

    public boolean create(Recipe recipe) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_CREATE_RECIPE)
        ) {
            statement.setString(1, recipe.getDescription());
            statement.setLong(2, recipe.getDoctor().getId());
            statement.setLong(3, recipe.getPatient().getId());
            statement.setDate(4, new java.sql.Date(recipe.getCreationDate().getTime()));
            statement.setInt(5, recipe.getDuration());
            statement.setLong(6, (long) recipe.getPriority().ordinal());
            return statement.executeUpdate() != 0;
        }
    }

    public boolean update(Recipe recipe) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE_RECIPE)
        ) {
            statement.setString(1, recipe.getDescription());
            statement.setLong(2, recipe.getDoctor().getId());
            statement.setLong(3, recipe.getPatient().getId());
            statement.setDate(4, new java.sql.Date(recipe.getCreationDate().getTime()));
            statement.setInt(5, recipe.getDuration());
            statement.setLong(6, (long) recipe.getPriority().ordinal());
            statement.setLong(7, recipe.getId());
            return statement.executeUpdate() != 0;
        }
    }

    public boolean delete(Long id) throws SQLException {
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_DELETE_RECIPE)
        ) {
            statement.setLong(1, id);
            return statement.executeUpdate() != 0;
        }
    }

    public HashMap<Doctor, Integer> getCountRecipeByDoctor() throws SQLException {
        HashMap<Doctor, Integer> countRecipeMap = new HashMap<>();
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(QUERY_COUNT_RECIPE_BY_DOCTOR)
        ) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                DoctorDao doctorDao = new DoctorDao();
                Doctor doctor = doctorDao.getById(resultSet.getLong("doctor_id"));
                Integer count = resultSet.getInt("row_count");
                countRecipeMap.put(doctor, count);
            }
        }
        return countRecipeMap;
    }

    public int getCountPeople(Long id, boolean isPatient) throws SQLException {
        String query = isPatient ? QUERY_COUNT_PATIENT : QUERY_COUNT_DOCTOR;
        try (
                Connection connection = DataSourceWrapper.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("row_count");
        }
    }
}

