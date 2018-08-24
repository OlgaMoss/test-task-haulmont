package com.haulmont.testtask;

import com.haulmont.testtask.model.dataSource.DataSourceWrapper;
import com.haulmont.testtask.model.dataSource.InitializationDB;
import com.haulmont.testtask.view.ListOfDoctorsView;
import com.haulmont.testtask.view.ListOfPatientsView;
import com.haulmont.testtask.view.ListOfRecipesView;
import com.vaadin.annotations.Theme;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;

import static com.haulmont.testtask.model.dataSource.InitializationDB.isExistTable;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        setContent(layout);

        DataSourceWrapper.init();
        InitializationDB initializationDB = new InitializationDB();
        if (!isExistTable()) {
            initializationDB.createTables();
            initializationDB.insertTables();
        }

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setSizeFull();

        String basePath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
        FileResource resource = new FileResource(new File(basePath + "/WEB-INF/images/book.svg"));
        Image image = new Image();
        image.setSource(resource);
        image.setHeight(90, Unit.PERCENTAGE);
        headerLayout.addComponent(image);

        Label title = new Label("Справочник больницы");
        headerLayout.addComponent(title);
        headerLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        layout.addComponent(headerLayout);

        headerLayout.setExpandRatio(image, 0.1f);
        headerLayout.setExpandRatio(title, 0.9f);

        TabSheet tabsheet = new TabSheet();
        layout.addComponent(tabsheet);

        VerticalLayout tab1 = new VerticalLayout();
        tab1.addComponent(new ListOfPatientsView());
        tabsheet.addTab(tab1, "Список пациентов");

        VerticalLayout tab2 = new VerticalLayout();
        tab2.addComponent(new ListOfDoctorsView());
        tabsheet.addTab(tab2, "Список врачей");

        VerticalLayout tab3 = new VerticalLayout();
        tab3.addComponent(new ListOfRecipesView());
        tabsheet.addTab(tab3, "Список рецептов");

        layout.setExpandRatio(headerLayout, 0.10f);
        layout.setExpandRatio(tabsheet, 0.90f);
    }
}