package com.example.ready2eat.Model;

public class Order {
    private int ID;
    private String ProductId;
    private String ProductName;
    private String Quantity;
    private String Price;
    private String Discount;
    private String Image;

    public Order(){}



    public Order(String productId, String productName, String quantity, String price, String discount, String image) {
        this.ProductId = productId;
        this.ProductName = productName;
        this.Quantity = quantity;
        this.Price = price;
        this.Discount = discount;
        this.Image = image;
    }

    public Order(int ID, String productId, String productName, String quantity, String price, String discount, String image) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
        Image = image;
    }

    public Order(String quantity, String price, String discount) {
        this.Quantity = quantity;
        this.Price = price;
        this.Discount = discount;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

}
