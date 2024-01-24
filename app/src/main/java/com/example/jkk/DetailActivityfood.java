package com.example.jkk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailActivityfood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activityfood);

        Intent intent = getIntent();
        String foodIngredients =  intent.getStringExtra("ingredients");
        String foodDescription =  intent.getStringExtra("FoodDescription");

        TextView description = findViewById(R.id.foodDescriptionTextView);
        description.setText(foodDescription);
        TextView ingredients = findViewById(R.id.foodIngredientTextView);
        ingredients.setText(foodIngredients);

    }

}