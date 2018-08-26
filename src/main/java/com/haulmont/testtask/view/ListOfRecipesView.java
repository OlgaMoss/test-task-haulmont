package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.PatientController;
import com.haulmont.testtask.controller.RecipeController;
import com.haulmont.testtask.model.entity.Patient;
import com.haulmont.testtask.model.entity.Priority;
import com.haulmont.testtask.model.entity.Recipe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.DateRenderer;

import java.util.Arrays;
import java.util.List;

public class ListOfRecipesView extends VerticalLayout implements View {
    private RecipeController recipeController;
    private Grid grid;
    private ComboBox patientSelect;

    public ListOfRecipesView() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        recipeController = new RecipeController();
        List<Recipe> recipeList = recipeController.getAll();
        if (recipeList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        }
        init(recipeList);
    }

    private void init(List<Recipe> recipeList) {
        BeanItemContainer<Recipe> container = new BeanItemContainer<>(Recipe.class, recipeList);
        grid = new Grid(container);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumnOrder("description", "doctor", "patient", "creationDate", "duration", "priority");
        grid.getColumn("description").setHeaderCaption("Описание");
        grid.getColumn("doctor").setHeaderCaption("Врач");
        grid.getColumn("patient").setHeaderCaption("Пациент");
        grid.getColumn("creationDate").setHeaderCaption("Дата создания");
        grid.getColumn("duration").setHeaderCaption("Срок действия [в днях]");
        grid.getColumn("priority").setHeaderCaption("Приоритет");
        grid.removeColumn("id");

        Grid.Column creationDate = grid.getColumn("creationDate");
        creationDate.setRenderer(new DateRenderer("%1$te %1$tB, %1$tY"));

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        Button addButton = new Button("Добавить");
        addButton.setWidth(70, Sizeable.Unit.PERCENTAGE);
        addButton.addClickListener(click -> {
            EditRecipeView editRecipeView = new EditRecipeView("Добавить рецепт");
            editRecipeView.setWidth(400, Sizeable.Unit.PIXELS);
            UI.getCurrent().addWindow(editRecipeView);
            editRecipeView.addCloseListener(e -> updateList());

        });
        Button editButton = new Button("Изменить");
        editButton.setWidth(70, Sizeable.Unit.PERCENTAGE);
        editButton.addClickListener(click -> {
                    Grid.MultiSelectionModel selection = (Grid.MultiSelectionModel) grid.getSelectionModel();
                    Object itemId = selection.getSelectedRows().toArray()[0];
                    Long id = (Long) grid.getContainerDataSource().getItem(itemId).getItemProperty("id").getValue();
                    Recipe recipe = recipeController.getById(id);
                    if (recipe == null) {
                        Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                    } else {
                        EditRecipeView editRecipeView = new EditRecipeView("Изменить рецепт", recipe);
                        editRecipeView.setWidth(30, Sizeable.Unit.PERCENTAGE);
                        UI.getCurrent().addWindow(editRecipeView);
                        grid.clearSortOrder();
                        grid.getSelectionModel().reset();
                        click.getButton().setEnabled(false);
                        editRecipeView.addCloseListener(e -> updateList());
                    }
                }
        );
        Button deleteButton = new Button("Удалить");
        deleteButton.setWidth(70, Sizeable.Unit.PERCENTAGE);
        deleteButton.addClickListener(click -> {
            Grid.MultiSelectionModel selection = (Grid.MultiSelectionModel) grid.getSelectionModel();
            for (Object itemId : selection.getSelectedRows()) {
                Long id = (Long) grid.getContainerDataSource().getItem(itemId).getItemProperty("id").getValue();
                ConfirmDialog dialog = new ConfirmDialog(clickEvent -> {
                    if (recipeController.delete(id)) {
                        updateList();
                        Notification.show("Удаления прошло успешно.", Notification.Type.HUMANIZED_MESSAGE);
                    } else {
                        Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                    }
                });
                getUI().addWindow(dialog);
            }
            grid.getSelectionModel().reset();
            click.getButton().setEnabled(false);
            grid.clearSortOrder();
        });

        deleteButton.setEnabled(grid.getSelectedRows().size() > 0);
        editButton.setEnabled(grid.getSelectedRows().size() == 1);
        grid.addSelectionListener(selection -> {
            deleteButton.setEnabled(grid.getSelectedRows().size() > 0);
            editButton.setEnabled(grid.getSelectedRows().size() == 1);
        });

        buttonLayout.addComponent(addButton);
        buttonLayout.addComponent(editButton);
        buttonLayout.addComponent(deleteButton);

        buttonLayout.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
        buttonLayout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        buttonLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);


        HorizontalLayout filterLayout = new HorizontalLayout();
        filterLayout.setSpacing(true);
        addComponent(filterLayout);

        PatientController patientController = new PatientController();
        List<Patient> patientList = patientController.getAll();
        if (patientList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        }
        BeanItemContainer<Patient> patientContainer = new BeanItemContainer<>(Patient.class, patientList);

        patientSelect = new ComboBox("Пациент", patientContainer);
        patientSelect.setNullSelectionAllowed(true);
        patientSelect.setNullSelectionItemId(null);
        filterLayout.addComponent(patientSelect);

        List<Priority> priorityList = Arrays.asList(Priority.values());
        BeanItemContainer<Priority> containerPriority = new BeanItemContainer<>(Priority.class, priorityList);
        ComboBox prioritySelect = new ComboBox("Приоритет", containerPriority);
        prioritySelect.setNullSelectionAllowed(true);
        prioritySelect.setNullSelectionItemId(null);
        filterLayout.addComponent(prioritySelect);

        TextField descriptionTField = new TextField("Описание");
        filterLayout.addComponent(descriptionTField);


        Button filterButton = new Button("Применить");
        filterButton.setWidth(70, Sizeable.Unit.PERCENTAGE);
        filterButton.addClickListener(click -> {
            String description = descriptionTField.getValue();
            Patient patient = (Patient) patientSelect.getValue();
            Priority priority = (Priority) prioritySelect.getValue();

            List<Recipe> recipeFltrList = recipeController.getAll(description, patient, priority);
            BeanItemContainer<Recipe> bic = (BeanItemContainer<Recipe>) grid.getContainerDataSource();
            bic.removeAllItems();
            bic.addAll(recipeFltrList);
            grid.setContainerDataSource(bic);
        });
        filterLayout.addComponent(filterButton);
        filterLayout.setComponentAlignment(filterButton, Alignment.BOTTOM_CENTER);

        VerticalLayout listLayout = new VerticalLayout();
        addComponent(listLayout);
        listLayout.addComponent(grid);
    }

    public void updateList() {
        List<Recipe> recipeList = recipeController.getAll();
        if (recipeList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            BeanItemContainer<Recipe> container = (BeanItemContainer<Recipe>) grid.getContainerDataSource();
            container.removeAllItems();
            container.addAll(recipeList);
            grid.setContainerDataSource(container);
        }
    }

    public void updatePatientList() {
        PatientController patientController = new PatientController();
        List<Patient> patientList = patientController.getAll();
        if (patientList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            BeanItemContainer<Patient> container = (BeanItemContainer<Patient>) patientSelect.getContainerDataSource();
            container.removeAllItems();
            container.addAll(patientList);
            patientSelect.setContainerDataSource(container);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
