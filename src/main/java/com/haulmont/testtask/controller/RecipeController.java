package com.haulmont.testtask.controller;

import com.haulmont.testtask.model.dao.RecipeDao;
import com.haulmont.testtask.model.entity.Doctor;
import com.haulmont.testtask.model.entity.Patient;
import com.haulmont.testtask.model.entity.Priority;
import com.haulmont.testtask.model.entity.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeController {
    private RecipeDao recipeDao;

    public RecipeController() {
        recipeDao = new RecipeDao();
    }

    public List<Recipe> getAll(String descriptionFltr, Patient patientFltr, Priority priorityFltr) {
        List<Recipe> recipeList = new ArrayList<>();
        try {
            recipeList = recipeDao.getAll(descriptionFltr, patientFltr, priorityFltr);
        } catch (SQLException e) {
            System.err.println("Error. Get all. " + e);
        }
        return recipeList;
    }

    public List<Recipe> getAll() {
        List<Recipe> recipeList = new ArrayList<>();
        try {
            recipeList = recipeDao.getAll();
        } catch (SQLException e) {
            System.err.println("Error. Get all. " + e);
        }
        return recipeList;
    }

    public Recipe getById(Long id) {
        try {
            return recipeDao.getById(id);
        } catch (SQLException e) {
            System.err.println("Error. Get by id. " + e);
            return null;
        }
    }

    public boolean create(Recipe recipe) {
        try {
            return recipeDao.create(recipe);
        } catch (SQLException e) {
            System.err.println("Error. Create. " + e);
            return false;
        }
    }

    public boolean update(Recipe recipe) {
        try {
            return recipeDao.update(recipe);
        } catch (SQLException e) {
            System.err.println("Error. Update. " + e);
            return false;
        }
    }

    public boolean delete(Long id) {
        try {
            return recipeDao.delete(id);
        } catch (SQLException e) {
            System.err.println("Error. Delete. " + e);
            return false;
        }
    }

    public HashMap<Doctor, Integer> getCountRecipeByDoctor() {
        try {
            return recipeDao.getCountRecipeByDoctor();
        } catch (SQLException e) {
            System.err.println("Error. Get count recipe by doctor. " + e);
            return null;
        }
    }
}
