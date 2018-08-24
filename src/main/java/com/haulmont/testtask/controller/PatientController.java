package com.haulmont.testtask.controller;

import com.haulmont.testtask.model.dao.PatientDao;
import com.haulmont.testtask.model.dao.RecipeDao;
import com.haulmont.testtask.model.entity.Patient;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private PatientDao patientDao;

    public PatientController() {
        patientDao = new PatientDao();
    }

    public List<Patient> getAll() {
        List<Patient> patientList = new ArrayList<>();
        try {
            patientList = patientDao.getAll();
        } catch (SQLException e) {
            System.err.println("Error. Get all. " + e);
        }
        return patientList;
    }

    public Patient getById(Long id) {
        try {
            return patientDao.getById(id);
        } catch (SQLException e) {
            System.err.println("Error. Get by id. " + e);
            return null;
        }
    }

    public boolean create(Patient patient) {
        try {
            return patientDao.create(patient);
        } catch (SQLException e) {
            System.err.println("Error. Create. " + e);
            return false;
        }
    }

    public boolean update(Patient patient) {
        try {
            return patientDao.update(patient);
        } catch (SQLException e) {
            System.err.println("Error. Update. " + e);
            return false;
        }
    }

    public String delete(Long id) {
        try {
            RecipeDao recipeDao = new RecipeDao();
            int cntPatient = recipeDao.getCountPeople(id, true);
            if (cntPatient == 0) {
                patientDao.delete(id);
                return "Удаления прошло успешно.";
            } else {
                return "Удалить нельзя. Для пациента " + patientDao.getById(id).toString() + " есть рецепт.";
            }
        } catch (SQLException e) {
            System.err.println("Error. Delete. " + e);
            return "Что-то пошло не так. Перезагрузитесь и попробуйте снова.";
        }
    }
}
