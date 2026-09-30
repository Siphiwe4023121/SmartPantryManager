package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout layoutEmptyState;
    private PantryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recyclerView = findViewById(R.id.recyclerViewSuggestedRecipes);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        Button btnBack = findViewById(R.id.btnBackSuggested);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new PantryDatabaseHelper(this);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        List<PantryItem> pantryItems = new ArrayList<>();

        try (Cursor cursor = dbHelper.getAllPantryItems()) {
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
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_EXPIRY));

                pantryItems.add(new PantryItem(id, name, quantity, unit, category, expiry));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Recipe> allRecipes = dbHelper.getAllRecipes();
        List<Recipe> matchedRecipes = RecipeMatcher.getMatchingRecipes(pantryItems, allRecipes);

        if (matchedRecipes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);

            RecipeAdapter adapter = new RecipeAdapter(matchedRecipes);
            recyclerView.setAdapter(adapter);
        }
    }
}
