package com.topoffers.topoffers.common.models;

import java.io.Serializable;
import java.io.StreamCorruptedException;

public class RegisterRequest implements Serializable{
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    private String address;
    private String phone;

    public RegisterRequest(String username, String password, String role, String firstname, String lastname, String address, String phone) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.firstName = firstname;
        this.lastName = lastname;
        this.address = address;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }
}
