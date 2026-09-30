package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ViewPantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPantry;
    private ArrayList<PantryItem> pantryItems;
    private PantryAdapter pantryAdapter;
    private PantryDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pantry);

        // Connect RecyclerView
        recyclerViewPantry =
                findViewById(R.id.recyclerViewPantry);

        recyclerViewPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // Create the list
        pantryItems = new ArrayList<>();

        // Connect to database
        databaseHelper =
                new PantryDatabaseHelper(this);

        // Load pantry items
        loadPantryItems();

        // Connect list to RecyclerView
        pantryAdapter =
                new PantryAdapter(pantryItems);

        recyclerViewPantry.setAdapter(pantryAdapter);

        // Back button
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadPantryItems() {

        pantryItems.clear();

        Cursor cursor =
                databaseHelper.getAllPantryItems();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            PantryDatabaseHelper.COLUMN_ID
                    )
            );

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            PantryDatabaseHelper.COLUMN_NAME
                    )
            );

            int quantity = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                            PantryDatabaseHelper.COLUMN_QUANTITY
                    )
            );

            String category = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            PantryDatabaseHelper.COLUMN_CATEGORY
                    )
            );

            String expiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            PantryDatabaseHelper.COLUMN_EXPIRY
                    )
            );

            PantryItem item = new PantryItem(
                    id,
                    name,
                    quantity,
                    category,
                    expiryDate
            );

            pantryItems.add(item);
        }

        cursor.close();
    }
}