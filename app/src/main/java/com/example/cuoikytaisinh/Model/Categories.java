package com.example.cuoikytaisinh.Model;

import java.util.ArrayList;

public class Categories {

    private String name;
    private ArrayList<Furniture> furnitureList;
    private String imagePath; // Lưu đường dẫn ảnh dưới dạng String

    // Constructor
    public Categories(String name, ArrayList<Furniture> furnitureList, String imagePath) {
        this.name = name;
        this.furnitureList = furnitureList != null ? furnitureList : new ArrayList<>(); // Đảm bảo danh sách không null
        this.imagePath = imagePath; // Lưu đường dẫn ảnh dưới dạng String
    }

    // Getter cho tên danh mục
    public String getName() {
        return name;
    }

    // Getter cho danh sách đồ nội thất
    public ArrayList<Furniture> getFurnitureList() {
        return furnitureList;
    }

    // Setter cho danh sách đồ nội thất (nếu cần thay đổi sau này)
    public void setFurnitureList(ArrayList<Furniture> furnitureList) {
        this.furnitureList = furnitureList;
    }

    // Getter cho đường dẫn ảnh đại diện của danh mục
    public String getImagePath() {
        return imagePath;
    }

    // Setter cho đường dẫn ảnh đại diện của danh mục
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
