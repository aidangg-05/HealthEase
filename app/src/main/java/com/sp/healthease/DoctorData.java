package com.sp.healthease;

import java.io.Serializable;

public class DoctorData implements Serializable {
    private String email;
    private String password;
    private String field;
    private String clinic;
    private String fullName;
    private String telegram;

    // Constructor
    public DoctorData(String email, String password, String field, String clinic, String fullName, String telegram) {
        this.email = email;
        this.password = password;
        this.field = field;
        this.clinic = clinic;
        this.fullName = fullName;
        this.telegram = telegram;
    }

    // Getter and Setter methods for each field
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String age) {
        this.telegram = age;
    }
}
