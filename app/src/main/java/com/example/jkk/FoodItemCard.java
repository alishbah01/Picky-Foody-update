package com.example.jkk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;

public class FoodItemCard extends AppCompatActivity {
    private Button Details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_card);
        // Find the button by ID
        Details = findViewById(R.id.details);}


    public void seedetails(View view) {
        startActivity(new Intent(FoodItemCard.this, DetailActivityfood.class));

    }


}