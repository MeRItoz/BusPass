package com.example.buspass;

public class UserHelperClass {
    String name,email,department,phone,password,address;

    public UserHelperClass(String name, String email, String department, String phone, String password, String address) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.phone = phone;
        this.password = password;
        this.address = address;
    }

    public UserHelperClass() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
