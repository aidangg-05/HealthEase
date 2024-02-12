package com.sp.healthease;

import java.io.Serializable;

public class AppointmentData implements Serializable {
    private String doctorName;
    private String clinicName;
    private String appointmentDate;
    private String appointmentTime;
    private String patientName;

    // Constructor
    public AppointmentData(String doctorName, String clinicName, String appointmentDate, String appointmentTime, String patientName) {
        this.doctorName = doctorName;
        this.clinicName = clinicName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.patientName = patientName;
    }

    // Getter and Setter methods for each field
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
