package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.DoctorController;
import com.haulmont.testtask.model.entity.Doctor;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.List;

public class ListOfDoctorsView extends VerticalLayout implements View {
    private DoctorController doctorController;
    private Grid grid;

    public ListOfDoctorsView() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        doctorController = new DoctorController();
        List<Doctor> doctorList = doctorController.getAll();
        init(doctorList);
        if (doctorList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        }
    }

    private void init(List<Doctor> doctorList) {
        BeanItemContainer<Doctor> container = new BeanItemContainer<>(Doctor.class, doctorList);
        grid = new Grid(container);
        grid.setSizeFull();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        grid.setColumnOrder("lastName", "name", "patronymic", "specialty");
        grid.getColumn("lastName").setHeaderCaption("Фамилия");
        grid.getColumn("name").setHeaderCaption("Имя");
        grid.getColumn("patronymic").setHeaderCaption("Отчество");
        grid.getColumn("specialty").setHeaderCaption("Специальность");
        grid.removeColumn("id");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        addComponent(buttonLayout);

        Button addButton = new Button("Добавить");
        addButton.setWidth(70, Unit.PERCENTAGE);
        addButton.addClickListener(click -> {
            EditDoctorView editDoctorView = new EditDoctorView("Добавить врача");
            editDoctorView.setWidth(400, Unit.PIXELS);
            UI.getCurrent().addWindow(editDoctorView);
            editDoctorView.addCloseListener(e -> updateList());

        });
        Button editButton = new Button("Изменить");
        editButton.setWidth(70, Unit.PERCENTAGE);
        editButton.addClickListener(click -> {
                    Grid.MultiSelectionModel selection = (Grid.MultiSelectionModel) grid.getSelectionModel();
                    Object itemId = selection.getSelectedRows().toArray()[0];
                    Long id = (Long) grid.getContainerDataSource().getItem(itemId).getItemProperty("id").getValue();
                    Doctor doctor = doctorController.getById(id);
                    if (doctor == null) {
                        Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
                    }
                    EditDoctorView editDoctorView = new EditDoctorView("Изменить врача", doctor);
                    editDoctorView.setWidth(30, Unit.PERCENTAGE);
                    UI.getCurrent().addWindow(editDoctorView);
                    grid.clearSortOrder();
                    grid.getSelectionModel().reset();
                    click.getButton().setEnabled(false);
                    editDoctorView.addCloseListener(e -> updateList());

                }
        );
        Button deleteButton = new Button("Удалить");
        deleteButton.setWidth(70, Unit.PERCENTAGE);
        deleteButton.addClickListener(click -> {
            Grid.MultiSelectionModel selection = (Grid.MultiSelectionModel) grid.getSelectionModel();
            for (Object itemId : selection.getSelectedRows()) {
                Long id = (Long) grid.getContainerDataSource().getItem(itemId).getItemProperty("id").getValue();
                ConfirmDialog dialog = new ConfirmDialog(clickEvent -> {
                    String msg = doctorController.delete(id);
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

        Button statisticButton = new Button("Показать статистику");
        statisticButton.setWidth(70, Unit.PERCENTAGE);
        statisticButton.addClickListener(click -> {
                    StatisticView statisticView = new StatisticView("Статистика");
                    statisticView.setWidth(400, Unit.PIXELS);
                    UI.getCurrent().addWindow(statisticView);
                }
        );

        buttonLayout.addComponent(addButton);
        buttonLayout.addComponent(editButton);
        buttonLayout.addComponent(deleteButton);
        buttonLayout.addComponent(statisticButton);

        buttonLayout.setComponentAlignment(addButton, Alignment.MIDDLE_CENTER);
        buttonLayout.setComponentAlignment(editButton, Alignment.MIDDLE_CENTER);
        buttonLayout.setComponentAlignment(deleteButton, Alignment.MIDDLE_CENTER);
        buttonLayout.setComponentAlignment(statisticButton, Alignment.MIDDLE_CENTER);

        VerticalLayout listLayout = new VerticalLayout();
        addComponent(listLayout);
        listLayout.addComponent(grid);
    }

    private void updateList() {
        List<Doctor> doctorList = doctorController.getAll();
        if (doctorList == null) {
            Notification.show("Что-то пошло не так. Перезагрузитесь и попробуйте снова.", Notification.Type.HUMANIZED_MESSAGE);
        } else {
            BeanItemContainer<Doctor> bic = (BeanItemContainer<Doctor>) grid.getContainerDataSource();
            bic.removeAllItems();
            bic.addAll(doctorList);
            grid.setContainerDataSource(bic);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    }
}
