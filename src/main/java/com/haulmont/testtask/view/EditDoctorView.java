package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.DoctorController;
import com.haulmont.testtask.model.entity.Doctor;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

class EditDoctorView extends Window {

    private Long idEditValue;
    private TextField nameTField;
    private TextField lastNameTField;
    private TextField patronymicTField;
    private TextField specialtyTField;

    private boolean isEditable = false;

    EditDoctorView(String name) {
        super(name);
        init();
    }

    EditDoctorView(String name, Doctor doctor) {
        super(name);
        isEditable = true;
        init();
        setValueField(doctor);
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

        nameTField = new TextField("Имя");
        nameTField.setRequired(true);
        nameTField.setSizeFull();
        nameTField.setRequiredError("Поле пустое");
        nameTField.addValidator(new StringLengthValidator("Имя должно быть от 3 до 50 символов", 3, 50, false));
        formLayout.addComponent(nameTField);

        lastNameTField = new TextField("Фамилия");
        lastNameTField.setRequired(true);
        lastNameTField.setRequiredError("Поле пустое");
        lastNameTField.addValidator(new StringLengthValidator("Фамилия должно быть от 3 до 50 символов", 3, 50, false));
        lastNameTField.setSizeFull();
        formLayout.addComponent(lastNameTField);

        patronymicTField = new TextField("Отчество");
        patronymicTField.setRequired(true);
        patronymicTField.setRequiredError("Поле пустое");
        patronymicTField.addValidator(new StringLengthValidator("Отчество должно быть от 3 до 50 символов", 3, 50, false));
        patronymicTField.setSizeFull();
        formLayout.addComponent(patronymicTField);

        specialtyTField = new TextField("Специальность");
        specialtyTField.addValidator(new StringLengthValidator("Специальность должно быть от 1 до 60 символов", 1, 60, false));
        specialtyTField.setRequired(true);
        specialtyTField.setRequiredError("Поле пустое");
        specialtyTField.setSizeFull();
        formLayout.addComponent(specialtyTField);

        layout.addComponent(formLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button okButton = new Button("OK");
        okButton.setWidth(30, Unit.PERCENTAGE);
        okButton.setDisableOnClick(true);
        okButton.addClickListener(click -> {
            if (!nameTField.isValid() || !lastNameTField.isValid() || !patronymicTField.isValid() || !specialtyTField.isValid()) {
                okButton.setEnabled(true);
                return;
            }
            DoctorController doctorController = new DoctorController();
            Doctor patient = isEditable ?
                    new Doctor(idEditValue, nameTField.getValue(), lastNameTField.getValue(), patronymicTField.getValue(), specialtyTField.getValue())
                    : new Doctor(nameTField.getValue(), lastNameTField.getValue(), patronymicTField.getValue(), specialtyTField.getValue());
            if (isEditable) {
                if (doctorController.update(patient)) {
                    Notification.show("Врач изменен", Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                }
            } else {
                if (doctorController.create(patient)) {
                    Notification.show("Добавлен новый врач", Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                }
            }
            close();

        });

        Button closeButton = new Button("Отменить");
        closeButton.setWidth(30, Unit.PERCENTAGE);
        closeButton.addClickListener(click -> close());

        buttonLayout.addComponent(okButton);
        buttonLayout.addComponent(closeButton);

        layout.addComponent(buttonLayout);

        layout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);
    }

    private void setValueField(Doctor doctor) {
        idEditValue = doctor.getId();
        nameTField.setValue(doctor.getName());
        lastNameTField.setValue(doctor.getLastName());
        patronymicTField.setValue(doctor.getPatronymic());
        specialtyTField.setValue(doctor.getSpecialty());
    }
}
