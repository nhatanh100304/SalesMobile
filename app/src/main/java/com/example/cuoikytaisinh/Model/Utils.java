package com.example.cuoikytaisinh.Model;

import java.util.ArrayList;

public class Utils {

    // Lịch sử sản phẩm đã xem
    static ArrayList<Furniture> furnitureHistory = new ArrayList<>();

    // Thêm sản phẩm vào lịch sử
    public static void addFurnitureHistory(Furniture furniture) {
        // Kiểm tra xem sản phẩm đã có trong lịch sử chưa
        if (furnitureHistory.indexOf(furniture) == -1) {
            // Nếu chưa có, thêm vào đầu danh sách
            furnitureHistory.add(0, furniture);
        }
    }

    // Lấy danh sách lịch sử sản phẩm
    public static ArrayList<Furniture> getFurnitureHistory() {
        return furnitureHistory;
    }
}
