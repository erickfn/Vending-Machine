package com.erickfloresnava.vendormachine.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by erickfloresnava on 1/11/16.
 */
public class ProductBean implements Serializable{

    private int id;
    private String title;
    private double price;
    private int availability;
    private String img;

    public ProductBean(int id, String title, double price, int availability, String img) {
        this.id = id;
        this.availability = availability;
        this.title = title;
        this.price = price;
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public void sellItem() {
        -- this.availability;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
