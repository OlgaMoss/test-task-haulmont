package com.haulmont.testtask.view;

import com.vaadin.ui.*;

class ConfirmDialog extends Window {
    private Button okButton;

    ConfirmDialog(Button.ClickListener listener) {
        init();
        okButton.addClickListener(event -> {
            listener.buttonClick(event);
            close();
        });
    }

    private void init() {
        setModal(true);
        setResizable(false);
        setClosable(false);
        setWidth(20, Unit.PERCENTAGE);
        center();

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        Label label = new Label("Вы действительно хотите удалить данную запись?");
        layout.addComponent(label);
        okButton = new Button("ОК");
        okButton.setDisableOnClick(true);
        buttonLayout.addComponent(okButton);

        Button cancelButton = new Button("Отмена");
        cancelButton.setDisableOnClick(true);
        buttonLayout.addComponent(cancelButton);
        cancelButton.addClickListener(event -> close());

        layout.addComponent(buttonLayout);
        layout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_CENTER);

        setContent(layout);

    }
}
