package com.topoffers.topoffers.common.models;

import com.topoffers.data.base.IHaveImageFile;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Product implements Serializable, IHaveImageFile {
    private int id;
    private String title;
    private double price;
    private int quantity;
    private String imageIdentifier;
    private String description;
    private File imageFile;
    private Date dateAdded;
    private String sellerUsername;
    private String sellerFirstName;
    private String sellerLastName;

    public Product(String title, double price, int quantity, String imageIdentifier, String description, File imageFile) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.imageIdentifier = imageIdentifier;
        this.description = description;
        this.imageFile = imageFile;
    }

    public Product(int id, String title, double price, int quantity, String imageIdentifier, String description, Date dateAdded, String sellerUsername, String sellerFirstName, String sellerLastName) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
        this.imageIdentifier = imageIdentifier;
        this.description = description;
        this.dateAdded = dateAdded;
        this.sellerUsername = sellerUsername;
        this.sellerFirstName = sellerFirstName;
        this.sellerLastName = sellerLastName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageIdentifier() {
        return imageIdentifier;
    }

    public void setImageIdentifier(String imageIdentifier) {
        this.imageIdentifier = imageIdentifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getSellerFirstName() {
        return sellerFirstName;
    }

    public void setSellerFirstName(String sellerFirstName) {
        this.sellerFirstName = sellerFirstName;
    }

    public String getSellerLastName() {
        return sellerLastName;
    }

    public void setSellerLastName(String sellerLastName) {
        this.sellerLastName = sellerLastName;
    }
}
