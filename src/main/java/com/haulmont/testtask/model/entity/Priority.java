package com.haulmont.testtask.model.entity;

public enum Priority {

    NORMAL("Нормальный"),
    CITO("Срочный"),
    STATIM("Немедленный");

    private String name;

    Priority(String s) {
        this.name = s;
    }

    public String getName() {
        return this.name;
    }

    public static Priority getById(Long id) {
        switch (id.toString()) {
            case "0":
                return NORMAL;
            case "1":
                return CITO;
            case "2":
                return STATIM;
            default:
                return NORMAL;
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
