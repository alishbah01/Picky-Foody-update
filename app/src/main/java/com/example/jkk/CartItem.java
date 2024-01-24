package com.example.jkk;

public class CartItem {

    private String foodName;
    private String foodPrice;
    private String totalQuantity;

    private String currentTime;
    private String currentDate;


    private String totalPrice;

    public CartItem() {
        // Required empty public constructor for Firestore
    }

    public CartItem(String foodName, String foodPrice, String totalQuantity, String totalPrice,String currentDate, String currentTime) {
        if (foodName == null || foodName.trim().isEmpty()) {
            this.foodName = "No Name";
        } else {
            this.foodName = foodName.trim();
        }

        if (foodPrice == null || foodPrice.trim().isEmpty()) {
            this.foodPrice = "No Price";
        } else {
            this.foodPrice = foodPrice.trim();
        }

        this.totalQuantity = totalQuantity;

        if (totalPrice == null || totalPrice.trim().isEmpty()) {
            this.totalPrice = "No Total Price";
        } else {
            this.totalPrice = totalPrice.trim();
        }
    }

    // Add getters and setters

    public String getFoodName() {
        return foodName;
    }
    public String getCurrentDate() {
        return currentDate;
    }
    public String getCurrentTime() {
        return currentTime;
    }


    public void setCurrentTime(String currentTime) {
        this.currentDate= currentTime;
    }
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }


    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;


    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
