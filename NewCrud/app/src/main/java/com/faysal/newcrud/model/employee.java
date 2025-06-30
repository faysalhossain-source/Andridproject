package com.example.employeecrud.model;

import java.io.Serializable;

public class employee implements Serializable {
    private int id;
    private String name;
    private String email;
    private String designation;

    public employee() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
}
