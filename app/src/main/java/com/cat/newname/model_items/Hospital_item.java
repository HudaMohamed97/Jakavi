package com.cat.newname.model_items;

import java.util.ArrayList;

public class Hospital_item {
    private int id;
    private String name;
    private ArrayList<Doctor_item> doctors;

    public Hospital_item(int id, String name, ArrayList<Doctor_item> doctors) {
        this.id = id;
        this.name = name;
        this.doctors = doctors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Doctor_item> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor_item> doctors) {
        this.doctors = doctors;
    }
}
