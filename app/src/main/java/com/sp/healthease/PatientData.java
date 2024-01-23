package com.sp.healthease;

import java.io.Serializable;

public class PatientData implements Serializable{
    private String fullName; // New field for full name
    private String email;
    private String password;
    private String age;
    private String telegram;
    private String blood_group;
    private String medical_history;

    // Constructor
    public PatientData(String fullName, String email, String password, String age, String telegram, String blood_group, String medical_history) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.telegram = telegram;
        this.blood_group = blood_group;
        this.medical_history = medical_history;
    }

    // Getter and Setter methods for each field
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getBloodGroup() {
        return blood_group;
    }

    public void setBloodGroup(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getMedicalHistory() {
        return medical_history;
    }

    public void setMedicalHistory(String medical_history) {
        this.medical_history = medical_history;
    }
}
