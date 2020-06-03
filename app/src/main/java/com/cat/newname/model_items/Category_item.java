package com.cat.newname.model_items;

import java.util.ArrayList;

public class Category_item {
    private int id;
    private String name;
    private ArrayList<Product_item> products;


    public Category_item(int id, String name, ArrayList<Product_item> products) {
        this.id = id;
        this.name = name;
        this.products = products;
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

    public ArrayList<Product_item> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product_item> products) {
        this.products = products;
    }
}
