package com.sp.healthease;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentData implements Parcelable {
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

    // Parcelable implementation
    protected AppointmentData(Parcel in) {
        doctorName = in.readString();
        clinicName = in.readString();
        appointmentDate = in.readString();
        appointmentTime = in.readString();
        patientName = in.readString();
    }

    public static final Creator<AppointmentData> CREATOR = new Creator<AppointmentData>() {
        @Override
        public AppointmentData createFromParcel(Parcel in) {
            return new AppointmentData(in);
        }

        @Override
        public AppointmentData[] newArray(int size) {
            return new AppointmentData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctorName);
        dest.writeString(clinicName);
        dest.writeString(appointmentDate);
        dest.writeString(appointmentTime);
        dest.writeString(patientName);
    }
}
