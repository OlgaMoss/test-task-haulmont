package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.RecipeController;
import com.haulmont.testtask.model.entity.Doctor;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;

import java.util.HashMap;

class StatisticView extends Window {

    StatisticView(String name) {
        super(name);
        center();
        setClosable(true);
        setResizable(false);
        setModal(true);

        RecipeController recipeController = new RecipeController();

        HashMap<Doctor, Integer> doctorHashMap = recipeController.getCountRecipeByDoctor();
        if (doctorHashMap == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
            close();
        }
        IndexedContainer container = new IndexedContainer();
        container.addContainerProperty("doctor", Doctor.class, null);
        container.addContainerProperty("cntRecipe", Integer.class, 0);
        for (HashMap.Entry<Doctor, Integer> entry : doctorHashMap.entrySet()) {
            Object itemId = container.addItem();
            container.getContainerProperty(itemId, "doctor").setValue(entry.getKey());
            container.getContainerProperty(itemId, "cntRecipe").setValue(entry.getValue());
        }
        Grid grid = new Grid();
        grid.setContainerDataSource(container);
        grid.getColumn("doctor").setHeaderCaption("Врач");
        grid.getColumn("cntRecipe").setHeaderCaption("Количество рецептов");
        grid.setSelectionMode(Grid.SelectionMode.NONE);

        setContent(grid);
    }
}
