package com.android.mechanico.FirebaseTablesPojo;

/**
 * Created by anuaki on 4/27/2018.
 */

public class MechanicRegistrationFirebaseTable {

    private String Username;

    private String Email;

    private String MobileNumber;

    private String Password;

    private String LicenseNumber;

    private double LocationLat;

    private double LocationLng;

    private String Location;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getLicenseNumber() {
        return LicenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        LicenseNumber = licenseNumber;
    }

    public double getLocationLat() {
        return LocationLat;
    }

    public void setLocationLat(double locationLat) {
        LocationLat = locationLat;
    }

    public double getLocationLng() {
        return LocationLng;
    }

    public void setLocationLng(double locationLng) {
        LocationLng = locationLng;
    }
}
