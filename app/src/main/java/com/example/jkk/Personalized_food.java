package com.example.jkk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Personalized_food extends AppCompatActivity {

    private Spinner ingredientSpinner;
    private Button addIngredientButton;
    private TextView selectedIngredientsList;

    private List<String> selectedIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalized_food);

        ingredientSpinner = findViewById(R.id.ingredientSpinner);
        addIngredientButton = findViewById(R.id.addIngredientButton);
        selectedIngredientsList = findViewById(R.id.selectedIngredientsList);

        selectedIngredients = new ArrayList<>();

        // Populate spinner with options
        List<String> ingredients = Arrays.asList("Rice", "Noodle", "Lentils", "Vegetable");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ingredients);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ingredientSpinner.setAdapter(adapter);

        // Set listener for addIngredientButton
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedIngredient = ingredientSpinner.getSelectedItem().toString();
                switch (selectedIngredient) {
                    case "Rice":
                        showRiceOptionsDialog();
                        break;
                    case "Noodle":
                        showNoodleOptionsDialog();
                        break;
                    case "Lentils":
                        showLentilsOptionsDialog();
                        break;
                    case "Vegetable":
                        showVegetableOptionsDialog();
                        break;
                    default:
                        addToSelectedIngredients(selectedIngredient);
                        break;
                }
            }
        });

        // Set long click listener for selectedIngredientsList to remove ingredient
        selectedIngredientsList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showRemoveIngredientDialog(selectedIngredientsList.getText().toString());
                return true;
            }
        });
    }

    private void showRiceOptionsDialog() {
        final String[] riceOptions = {"BASMATI", "Brown Rice", "PK 386 Rice"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Rice");
        builder.setItems(riceOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToSelectedIngredients(riceOptions[which]);
            }
        });
        builder.show();
    }

    private void showNoodleOptionsDialog() {
        final String[] noodleOptions = {"Bulak Ramen Noodle", "Egg Noodles", "Ramen Noodles", "Rice Stick Noodles", "Wheat Flour Noodles"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Noodles");
        builder.setItems(noodleOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToSelectedIngredients(noodleOptions[which]);
            }
        });
        builder.show();
    }

    private void showLentilsOptionsDialog() {
        final String[] lentilsOptions = {"Red Beans", "White Beans", "Split Dal Chana", "Orid Dal", "Split Masoor Dal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Lentils");
        builder.setItems(lentilsOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToSelectedIngredients(lentilsOptions[which]);
            }
        });
        builder.show();
    }

    private void showVegetableOptionsDialog() {
        final String[] vegetables = {"Carrot", "Broccoli", "Spinach", "Tomato", "Onion"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Vegetable");
        builder.setItems(vegetables, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToSelectedIngredients(vegetables[which]);
            }
        });
        builder.show();
    }

    private void addToSelectedIngredients(String ingredient) {
        selectedIngredients.add(ingredient);
        updateSelectedIngredientsTextView();
    }

    private void updateSelectedIngredientsTextView() {
        StringBuilder builder = new StringBuilder();
        for (String ingredient : selectedIngredients) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(ingredient);
        }
        selectedIngredientsList.setText(builder.toString());
    }

    private void showRemoveIngredientDialog(String s) {
        final CharSequence[] items = selectedIngredients.toArray(new CharSequence[selectedIngredients.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remove Ingredient");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                confirmRemoveIngredientDialog(items[item].toString());
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void confirmRemoveIngredientDialog(final String ingredient) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Removal");
        builder.setMessage("Are you sure you want to remove " + ingredient + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                selectedIngredients.remove(ingredient);
                updateSelectedIngredientsTextView();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }


}
