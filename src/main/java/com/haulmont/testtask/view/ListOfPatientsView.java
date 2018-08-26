package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.PatientController;
import com.haulmont.testtask.model.entity.Patient;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;

public class ListOfPatientsView extends VerticalLayout implements View {

    private PatientController patientController;
    private Grid grid;

    public ListOfPatientsView() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);

        patientController = new PatientController();
        List<Patient> patientList = patientController.getAll();
        init(patientList);
        if (patientList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void init(List<Patient> patientList) {
        BeanItemContainer<Patient> container = new BeanItemContainer<>(Patient.class, patientList);
        grid = new Grid(container);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumnOrder("lastName", "name", "patronymic", "phoneNumber");
        grid.getColumn("lastName").setHeaderCaption("Фамилия");
        grid.getColumn("name").setHeaderCaption("Имя");
        grid.getColumn("patronymic").setHeaderCaption("Отчество");
        grid.getColumn("phoneNumber").setHeaderCaption("Телефон");
        grid.removeColumn("id");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        Button addButton = new Button("Добавить");
        addButton.setWidth(70, Unit.PERCENTAGE);
        addButton.addClickListener(click -> {
            EditPatientView editPatientView = new EditPatientView("Добавить пациента");
            editPatientView.setWidth(400, Unit.PIXELS);
            UI.getCurrent().addWindow(editPatientView);
            editPatientView.addCloseListener(e -> updateList());

        });
        Button editButton = new Button("Изменить");
        editButton.setWidth(70, Unit.PERCENTAGE);
        editButton.addClickListener(click -> {
                    Grid.MultiSelectionModel selection = (Grid.MultiSelectionModel) grid.getSelectionModel();
                    Object itemId = selection.getSelectedRows().toArray()[0];
                    Long id = (Long) grid.getContainerDataSource().getItem(itemId).getItemProperty("id").getValue();
                    Patient patient = patientController.getById(id);
                    if (patient == null) {
                        Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                    } else {
                        EditPatientView editPatientView = new EditPatientView("Изменить пациента", patient);
                        editPatientView.setWidth(30, Unit.PERCENTAGE);
                        UI.getCurrent().addWindow(editPatientView);
                        grid.clearSortOrder();
                        grid.getSelectionModel().reset();
                        click.getButton().setEnabled(false);
                        editPatientView.addCloseListener(e -> updateList());
                    }
                }
        );
        Button deleteButton = new Button("Удалить");
        deleteButton.setWidth(70, Unit.PERCENTAGE);
        deleteButton.addClickListener(click -> {
            Grid.MultiSelectionModel selection = (Grid.MultiSelectionModel) grid.getSelectionModel();
            for (Object itemId : selection.getSelectedRows()) {
                Long id = (Long) grid.getContainerDataSource().getItem(itemId).getItemProperty("id").getValue();
                ConfirmDialog dialog = new ConfirmDialog(clickEvent -> {
                    String msg = patientController.delete(id);
                    updateList();
                    Notification.show(msg, Notification.Type.HUMANIZED_MESSAGE);
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

        VerticalLayout listLayout = new VerticalLayout();
        addComponent(listLayout);
        listLayout.addComponent(grid);
    }

    public void updateList() {
        List<Patient> patientList = patientController.getAll();
        if (patientList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            BeanItemContainer<Patient> container = (BeanItemContainer<Patient>) grid.getContainerDataSource();
            container.removeAllItems();
            container.addAll(patientList);
            grid.setContainerDataSource(container);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
