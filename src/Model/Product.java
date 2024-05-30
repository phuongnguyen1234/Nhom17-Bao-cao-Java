package Model;

import java.io.*;

public class Product implements Serializable{
    private static final long serialVersionUID = 123456789L;
    private String id, name, color;
    private float price;

    public Product(){
    }

    public Product(String id, String name, String color, float price) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }  
}