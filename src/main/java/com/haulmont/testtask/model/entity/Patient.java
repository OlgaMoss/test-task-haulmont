package com.haulmont.testtask.model.entity;

public class Patient {
    private Long id;
    private String name;
    private String lastName;
    private String patronymic;
    private String phoneNumber;

    public Patient() {
    }

    public Patient(String name, String lastName, String patronymic, String phoneNumber) {
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
    }

    public Patient(Long id, String name, String lastName, String patronymic, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.phoneNumber = phoneNumber;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return lastName + " " + name.charAt(0) + ". " + patronymic.charAt(0) + ".";
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        return id.equals(((Patient) obj).id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0) + (lastName != null ? lastName.hashCode() : 0)
                + (patronymic != null ? patronymic.hashCode() : 0) + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        return result;
    }
}
