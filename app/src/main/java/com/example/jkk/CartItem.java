package com.example.jkk;

public class CartItem {
    private String FoodName;
    private String FoodPrice;
    private int totalQuantity;
    private String totalPrice;

    public CartItem() {
        // Required empty public constructor for Firestore
    }

    // Add getters and setters

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        this.FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.FoodPrice = foodPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int quantity) {
        this.totalQuantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
