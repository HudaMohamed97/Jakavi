package com.cat.newname.model_items;

public class Patient_item {
    int id;
    String name, dose, category, hospital, date, productId;

    public Patient_item(int id, String name, String dose, String category, String hospital, String date, String productId) {
        this.id = id;
        this.name = name;
        this.dose = dose;
        this.category = category;
        this.hospital = hospital;
        this.date = date;
        this.productId = productId;
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

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
