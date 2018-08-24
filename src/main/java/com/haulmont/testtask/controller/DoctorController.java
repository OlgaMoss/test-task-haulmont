package com.haulmont.testtask.controller;

import com.haulmont.testtask.model.dao.DoctorDao;
import com.haulmont.testtask.model.dao.RecipeDao;
import com.haulmont.testtask.model.entity.Doctor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorController {
    private DoctorDao doctorDao;

    public DoctorController() {
        doctorDao = new DoctorDao();
    }

    public List<Doctor> getAll() {
        List<Doctor> doctorList = new ArrayList<>();
        try {
            doctorList = doctorDao.getAll();
        } catch (SQLException e) {
            System.err.println("Error. Get all. " + e);
        }
        return doctorList;
    }

    public Doctor getById(Long id) {
        try {
            return doctorDao.getById(id);
        } catch (SQLException e) {
            System.err.println("Error. Get by id. " + e);
            return null;
        }
    }

    public boolean create(Doctor doctor) {
        try {
            return doctorDao.create(doctor);
        } catch (SQLException e) {
            System.err.println("Error. Create. " + e);
            return false;
        }
    }

    public boolean update(Doctor doctor) {
        try {
            return doctorDao.update(doctor);
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
                doctorDao.delete(id);
                return "Удаления прошло успешно.";
            } else {
                return "Удалить нельзя. Врачом " + doctorDao.getById(id).toString() + " выписан рецепт.";
            }
        } catch (SQLException e) {
            System.err.println("Error. Delete. " + e);
            return "Что-то пошло не так. Перезагрузитесь и попробуйте снова.";
        }
    }
}
