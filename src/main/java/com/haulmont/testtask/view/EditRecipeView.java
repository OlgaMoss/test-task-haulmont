package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.DoctorController;
import com.haulmont.testtask.controller.PatientController;
import com.haulmont.testtask.controller.RecipeController;
import com.haulmont.testtask.model.entity.Doctor;
import com.haulmont.testtask.model.entity.Patient;
import com.haulmont.testtask.model.entity.Priority;
import com.haulmont.testtask.model.entity.Recipe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

class EditRecipeView extends Window {

    private Long idEditValue;
    private TextField descriptionTField;
    private NativeSelect doctorSelect;
    private NativeSelect patientSelect;
    private DateField dateField;
    private TextField durationTField;
    private NativeSelect prioritySelect;

    private boolean isEditable = false;

    EditRecipeView(String name) {
        super(name);
        init();
    }

    EditRecipeView(String name, Recipe recipe) {
        super(name);
        isEditable = true;
        init();
        setValueField(recipe);
    }

    private void init() {
        center();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        setClosable(true);
        setResizable(false);
        setModal(true);

        FormLayout formLayout = new FormLayout();
        formLayout.setSpacing(true);

        descriptionTField = new TextField("Описание");
        descriptionTField.setImmediate(true);
        descriptionTField.setRequired(true);
        descriptionTField.setRequiredError("Поле пустое");
        descriptionTField.addValidator(new StringLengthValidator("Имя должно быть от 2 до 255 символов", 2, 255, false));
        descriptionTField.setSizeFull();
        formLayout.addComponent(descriptionTField);

        DoctorController doctorController = new DoctorController();
        List<Doctor> doctorList = doctorController.getAll();
        if (doctorList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
            close();
        }
        BeanItemContainer<Doctor> doctorContainer = new BeanItemContainer<>(Doctor.class, doctorList);
        doctorSelect = new NativeSelect("Врач", doctorContainer);
        doctorSelect.setRequired(true);
        doctorSelect.setNullSelectionAllowed(false);
        doctorSelect.setValue(doctorSelect.getItemIds().iterator().next());
        doctorSelect.setSizeFull();
        formLayout.addComponent(doctorSelect);

        PatientController patientController = new PatientController();
        List<Patient> patientList = patientController.getAll();
        if (patientList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
            close();
        }
        BeanItemContainer<Patient> patientContainer = new BeanItemContainer<>(Patient.class, patientList);
        patientSelect = new NativeSelect("Пациент", patientContainer);
        patientSelect.setRequired(true);
        patientSelect.setNullSelectionAllowed(false);
        patientSelect.setValue(patientSelect.getItemIds().iterator().next());
        patientSelect.setSizeFull();
        formLayout.addComponent(patientSelect);

        dateField = new DateField("Дата создания");
        dateField.setValue(new Date());
        dateField.setDateFormat("yyyy-MM-dd");
        dateField.setLenient(true);
        dateField.setRequired(true);
        dateField.setSizeFull();
        formLayout.addComponent(dateField);

        durationTField = new TextField("Срок действия");
        durationTField.addValidator(new RegexpValidator("[0-9]+", "Поле должен содержать только цифры"));
        durationTField.setSizeFull();
        formLayout.addComponent(durationTField);

        BeanItemContainer<Priority> container = new BeanItemContainer<>(Priority.class, Arrays.asList(Priority.values()));
        prioritySelect = new NativeSelect("Приоритет", container);
        prioritySelect.setNullSelectionAllowed(false);
        prioritySelect.setValue(prioritySelect.getItemIds().iterator().next());
        prioritySelect.setSizeFull();
        formLayout.addComponent(prioritySelect);

        layout.addComponent(formLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button okButton = new Button("OK");
        okButton.setWidth(60, Unit.PERCENTAGE);
        okButton.setDisableOnClick(true);
        okButton.addClickListener(click -> {
            if (!descriptionTField.isValid() || !durationTField.isValid()) {
                okButton.setEnabled(true);
                return;
            }
            RecipeController recipeController = new RecipeController();

            String description = descriptionTField.getValue();
            Doctor doctor = (Doctor) doctorSelect.getValue();
            Patient patient = (Patient) patientSelect.getValue();
            Date creationDate = dateField.getValue();
            int duration = Integer.parseInt(durationTField.getValue());
            Priority priority = (Priority) prioritySelect.getValue();
            Recipe recipe = isEditable ?
                    new Recipe(idEditValue, description, doctor, patient, creationDate, duration, priority)
                    : new Recipe(description, doctor, patient, creationDate, duration, priority);

            if (isEditable) {
                if (recipeController.update(recipe)) {
                    Notification.show("Рецепт изменен", Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                }
            } else {
                if (recipeController.create(recipe)) {
                    Notification.show("Добавлен новый рецепт", Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
            close();
        });

        Button closeButton = new Button("Отменить");
        closeButton.setWidth(60, Unit.PERCENTAGE);
        closeButton.addClickListener(click -> close());

        buttonLayout.addComponent(okButton);
        buttonLayout.addComponent(closeButton);

        layout.addComponent(buttonLayout);

        layout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
    }

    private void setValueField(Recipe recipe) {
        idEditValue = recipe.getId();
        descriptionTField.setValue(recipe.getDescription());
        doctorSelect.setValue(recipe.getDoctor());
        patientSelect.setValue(recipe.getPatient());
        dateField.setValue(recipe.getCreationDate());
        durationTField.setValue(String.valueOf(recipe.getDuration()));
        prioritySelect.setValue(recipe.getPriority());
    }
}
