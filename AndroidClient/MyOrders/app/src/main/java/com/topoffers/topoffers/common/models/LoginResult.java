package com.topoffers.topoffers.common.models;

import com.orm.SugarRecord;

import java.io.Serializable;

public class LoginResult extends SugarRecord implements Serializable {
    private boolean success;
    private AuthenticationCookie cookie;
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
    private Error error;

    // Sugar need default constructor
    public LoginResult() {
    }

    public LoginResult(boolean success, AuthenticationCookie cookie, String username, String role, String firstName, String lastName, String address, String phone, Error error) {
        this.success = success;
        this.cookie = cookie;
        this.username = username;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phone = phone;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AuthenticationCookie getCookie() {
        return cookie;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
