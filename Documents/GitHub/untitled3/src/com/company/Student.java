package com.company;
// Класс Student
public class Student {
    // private переменные с типом String (строка текста)
    private String firstName;
    private String middleName;
    private String lastName;
    // Тот самый ФИО, который я назвал fullName
    private String fullName;

    // Получить первое имя, просто имя или то, которым человека называют обычно
    public String getFirstName() {
        return firstName;
    }

    // Установить FirstName
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // Получить имя, которое обычно стоит в середине (возможно, отчество)
    public String getMiddleName() {
        return middleName;
    }

    // Установить MiddleName
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    // Получить имя, которые стоит обычно в конце (возможно, фамилия)
    public String getLastName() {
        return lastName;
    }

    // Установить LastName
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Получить полное имя (fullName), хотя в ДЗ надо было получить ФИО
    public String getFullName() {
        this.fullName = this.firstName + " " + this.middleName + " " + this.lastName;
        return fullName;
    }
}
