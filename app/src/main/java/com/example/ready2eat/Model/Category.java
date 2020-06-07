package com.example.ready2eat.Model;

public class Category
{
    private String Name;
    private String Image;
    private String CategoryId;

    public Category()
    {
        Name = Image = "";
    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
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
  
    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public Category(String name, String image, String categoryId) {
        Name = name;
        Image = image;
        CategoryId = categoryId;
    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }
}
