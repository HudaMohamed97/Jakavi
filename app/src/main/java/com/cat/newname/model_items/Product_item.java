package com.cat.newname.model_items;

import java.util.ArrayList;

public class Product_item {
    private int id;
    private String name;
    private ArrayList<Dose_item> doses;

    public Product_item(int id, String name, ArrayList<Dose_item> doses) {
        this.id = id;
        this.name = name;
        this.doses = doses;
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

    public ArrayList<Dose_item> getDoses() {
        return doses;
    }

    public void setDoses(ArrayList<Dose_item> doses) {
        this.doses = doses;
    }
}
