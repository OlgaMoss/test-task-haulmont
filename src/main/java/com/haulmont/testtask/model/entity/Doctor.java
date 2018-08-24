package com.haulmont.testtask.model.entity;

public class Doctor {
    private Long id;
    private String name;
    private String lastName;
    private String patronymic;
    private String specialty;

    public Doctor() {
    }

    public Doctor(String name, String lastName, String patronymic, String specialty) {
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.specialty = specialty;
    }

    public Doctor(Long id, String name, String lastName, String patronymic, String specialty) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.specialty = specialty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    @Override
    public String toString() {
        return lastName + " " + name.charAt(0) + ". " + patronymic.charAt(0) + ".";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return id.equals(((Doctor) obj).id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0) + (lastName != null ? lastName.hashCode() : 0)
                + (patronymic != null ? patronymic.hashCode() : 0) + (specialty != null ? specialty.hashCode() : 0);
        return result;
    }
}
