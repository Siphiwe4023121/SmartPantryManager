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

        recyclerViewPantry = findViewById(R.id.recyclerViewPantry);
        recyclerViewPantry.setLayoutManager(new LinearLayoutManager(this));

        pantryItems = new ArrayList<>();
        databaseHelper = new PantryDatabaseHelper(this);

        pantryAdapter = new PantryAdapter(pantryItems);
        recyclerViewPantry.setAdapter(pantryAdapter);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void loadPantryItems() {
        pantryItems.clear();

        try (Cursor cursor = databaseHelper.getAllPantryItems()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_NAME));
                double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_QUANTITY));

                String unit = "pcs";
                int unitIdx = cursor.getColumnIndex(PantryDatabaseHelper.COLUMN_UNIT);
                if (unitIdx != -1) {
                    unit = cursor.getString(unitIdx);
                }

                String category = cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_CATEGORY));
                String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_EXPIRY));

                PantryItem item = new PantryItem(id, name, quantity, unit, category, expiryDate);
                pantryItems.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pantryAdapter != null) {
            pantryAdapter.notifyDataSetChanged();
        }
    }
}
