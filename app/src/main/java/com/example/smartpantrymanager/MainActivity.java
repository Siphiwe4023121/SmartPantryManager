package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnViewItems = findViewById(R.id.btnViewPantry);
        Button btnSuggestedRecipes = findViewById(R.id.btnSuggestedRecipes);
        Button btnShoppingList = findViewById(R.id.btnShoppingList);
        Button btnCategories = findViewById(R.id.btnCategories);
        Button btnSettings = findViewById(R.id.btnSettings);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);

        btnAddItem.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddItemActivity.class)));
        btnViewItems.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ViewPantryActivity.class)));
        btnSuggestedRecipes.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class)));
        btnShoppingList.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ShoppingListActivity.class)));
        btnCategories.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CategoriesActivity.class)));
        btnSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                startActivity(new Intent(MainActivity.this, ViewPantryActivity.class));
                return true;
            } else if (id == R.id.nav_recipes) {
                startActivity(new Intent(MainActivity.this, SuggestedRecipesActivity.class));
                return true;
            } else if (id == R.id.nav_shopping) {
                startActivity(new Intent(MainActivity.this, ShoppingListActivity.class));
                return true;
            } else if (id == R.id.nav_categories) {
                startActivity(new Intent(MainActivity.this, CategoriesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }
}
