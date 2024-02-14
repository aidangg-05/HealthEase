package com.sp.healthease;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorData implements Parcelable {
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

    // Parcelable implementation
    protected DoctorData(Parcel in) {
        email = in.readString();
        password = in.readString();
        field = in.readString();
        clinic = in.readString();
        fullName = in.readString();
        telegram = in.readString();
    }

    public static final Creator<DoctorData> CREATOR = new Creator<DoctorData>() {
        @Override
        public DoctorData createFromParcel(Parcel in) {
            return new DoctorData(in);
        }

        @Override
        public DoctorData[] newArray(int size) {
            return new DoctorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(field);
        dest.writeString(clinic);
        dest.writeString(fullName);
        dest.writeString(telegram);
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

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }
}
