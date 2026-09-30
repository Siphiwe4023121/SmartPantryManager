package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView textTitle = findViewById(R.id.textRecipeTitle);
        TextView textCategory = findViewById(R.id.textRecipeCategory);
        TextView textSteps = findViewById(R.id.textPreparationSteps);
        LinearLayout containerIngredients = findViewById(R.id.containerIngredients);
        Button btnBack = findViewById(R.id.btnBackRecipeDetail);

        btnBack.setOnClickListener(v -> finish());

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        PantryDatabaseHelper dbHelper = new PantryDatabaseHelper(this);
        Recipe recipe = dbHelper.getRecipeById(recipeId);

        if (recipe == null) {
            Toast.makeText(this, "Failed to load recipe details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textTitle.setText(recipe.getName());
        textCategory.setText(recipe.getCategory());
        textSteps.setText(recipe.getPreparationSteps());

        containerIngredients.removeAllViews();
        if (recipe.getIngredients() != null) {
            for (RecipeIngredient ing : recipe.getIngredients()) {
                TextView ingView = new TextView(this);
                double qty = ing.getQuantity();
                String qtyStr = (qty % 1 == 0) ? String.valueOf((int) qty) : String.valueOf(qty);

                ingView.setText("• " + ing.getName() + " - " + qtyStr + " " + ing.getUnit());
                ingView.setTextSize(16);
                ingView.setTextColor(0xFF333333);
                ingView.setPadding(0, 8, 0, 8);

                containerIngredients.addView(ingView);
            }
        }
    }
}
