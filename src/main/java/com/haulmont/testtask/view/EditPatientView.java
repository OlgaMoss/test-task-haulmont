package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.PatientController;
import com.haulmont.testtask.model.entity.Patient;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

class EditPatientView extends Window {

    private Long idEditValue;
    private TextField nameTField;
    private TextField lastNameTField;
    private TextField patronymicTField;
    private TextField phoneNumberTField;

    private boolean isEditable = false;

    EditPatientView(String name) {
        super(name);
        init();
    }

    EditPatientView(String name, Patient patient) {
        super(name);
        isEditable = true;
        init();
        setValueField(patient);
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

        lastNameTField = new TextField("Фамилия");
        lastNameTField.setRequired(true);
        lastNameTField.setRequiredError("Поле пустое");
        lastNameTField.addValidator(new StringLengthValidator("Фамилия должно быть от 3 до 50 символов", 3, 50, false));
        lastNameTField.setSizeFull();
        formLayout.addComponent(lastNameTField);

        nameTField = new TextField("Имя");
        nameTField.setRequired(true);
        nameTField.setRequiredError("Поле пустое");
        nameTField.addValidator(new StringLengthValidator("Имя должно быть от 3 до 50 символов", 3, 50, false));
        nameTField.setSizeFull();
        formLayout.addComponent(nameTField);

        patronymicTField = new TextField("Отчество");
        patronymicTField.setRequired(true);
        patronymicTField.setRequiredError("Поле пустое");
        patronymicTField.addValidator(new StringLengthValidator("Отчество должно быть от 3 до 50 символов", 3, 50, false));
        patronymicTField.setSizeFull();
        formLayout.addComponent(patronymicTField);

        phoneNumberTField = new TextField("Телефон");
        phoneNumberTField.addValidator(new StringLengthValidator("Телефон должно быть от 0 до 15 символов", 0, 15, false));
        phoneNumberTField.addValidator(new RegexpValidator("[0-9]+", "Телефон должен содержать только цифры"));
        phoneNumberTField.setSizeFull();
        formLayout.addComponent(phoneNumberTField);

        layout.addComponent(formLayout);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);

        Button okButton = new Button("OK");
        okButton.setWidth(30, Unit.PERCENTAGE);
        okButton.setDisableOnClick(true);
        okButton.addClickListener(click -> {
            if (!nameTField.isValid() || !lastNameTField.isValid() || !patronymicTField.isValid() || !phoneNumberTField.isValid()) {
                okButton.setEnabled(true);
                return;
            }
            PatientController patientController = new PatientController();
            Patient patient = isEditable ?
                    new Patient(idEditValue, nameTField.getValue(), lastNameTField.getValue(), patronymicTField.getValue(), phoneNumberTField.getValue())
                    : new Patient(nameTField.getValue(), lastNameTField.getValue(), patronymicTField.getValue(), phoneNumberTField.getValue());
            if (isEditable) {
                if (patientController.update(patient)) {
                    Notification.show("Пациент изменен", Notification.Type.HUMANIZED_MESSAGE);
                } else {
                    Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                }
            } else {
                if (patientController.create(patient)) {
                    Notification.show("Добавлен новый пациент", Notification.Type.HUMANIZED_MESSAGE);
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

    private void setValueField(Patient patient) {
        idEditValue = patient.getId();
        nameTField.setValue(patient.getName());
        lastNameTField.setValue(patient.getLastName());
        patronymicTField.setValue(patient.getPatronymic());
        phoneNumberTField.setValue(patient.getPhoneNumber());
    }
}
