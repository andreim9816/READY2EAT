package com.example.ready2eat.Model;

public class Food
{
    private String Name, Image, Description, Price, Discount, MenuID;
    private String Quantity;
    private String Time;

    public Food() {
    }

    public Food(String name, String image, String description, String price, String discount, String menuID, String quantity, String cookingTime) {
        this.Name = name;
        this.Image = image;
        this.Description = description;
        this.Price = price;
        this.Discount = discount;
        this.MenuID = menuID;
        this.Quantity = quantity;
        this.Time = cookingTime;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        this.Quantity = quantity;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
