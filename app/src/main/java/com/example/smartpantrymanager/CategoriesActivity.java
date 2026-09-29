package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.database.Cursor;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnBack = findViewById(R.id.btnBackCategories);
        Button btnDairy = findViewById(R.id.btnDairy);
        Button btnMeat = findViewById(R.id.btnMeat);
        Button btnFruits = findViewById(R.id.btnFruits);
        Button btnVegetables = findViewById(R.id.btnVegetables);
        Button btnGrains = findViewById(R.id.btnGrains);
        Button btnSnacks = findViewById(R.id.btnSnacks);
        Button btnOther = findViewById(R.id.btnOther);
        LinearLayout categoryItemsContainer = findViewById(R.id.categoryItemsContainer);

        PantryDatabaseHelper databaseHelper =
                new PantryDatabaseHelper(CategoriesActivity.this);

        btnDairy.setOnClickListener(view ->
                showCategoryItems("Dairy", categoryItemsContainer, databaseHelper));

        btnMeat.setOnClickListener(view ->
                showCategoryItems("Meat", categoryItemsContainer, databaseHelper));

        btnFruits.setOnClickListener(view ->
                showCategoryItems("Fruits", categoryItemsContainer, databaseHelper));

        btnVegetables.setOnClickListener(view ->
                showCategoryItems("Vegetables", categoryItemsContainer, databaseHelper));

        btnGrains.setOnClickListener(view ->
                showCategoryItems("Grains", categoryItemsContainer, databaseHelper));

        btnSnacks.setOnClickListener(view ->
                showCategoryItems("Snacks", categoryItemsContainer, databaseHelper));

        btnOther.setOnClickListener(view ->
                showCategoryItems("Other", categoryItemsContainer, databaseHelper));

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void showCategoryItems(String selectedCategory,
                                   LinearLayout categoryItemsContainer,
                                   PantryDatabaseHelper databaseHelper) {

        categoryItemsContainer.removeAllViews();

        Cursor cursor = databaseHelper.getAllPantryItems();

        while (cursor.moveToNext()) {

            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_NAME)
            );

            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_CATEGORY)
            );

            if (category.equalsIgnoreCase(selectedCategory)) {

                TextView itemView = new TextView(this);
                itemView.setText("• " + itemName);
                itemView.setTextSize(18);
                itemView.setPadding(10, 15, 10, 15);

                categoryItemsContainer.addView(itemView);
            }
        }

        cursor.close();
    }
}