package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnViewItems = findViewById(R.id.btnViewPantry);
        Button btnShoppingList = findViewById(R.id.btnShoppingList);

        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
        btnViewItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewPantryActivity.class);
            startActivity(intent);
        });
        btnShoppingList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShoppingListActivity.class);
            startActivity(intent);
        });
    }
}