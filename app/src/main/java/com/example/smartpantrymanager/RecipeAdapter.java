package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipeList;

    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        Context context = holder.itemView.getContext();

        holder.textRecipeName.setText(recipe.getName());
        holder.textRecipeCategory.setText(recipe.getCategory());
        int ingCount = recipe.getIngredients() != null ? recipe.getIngredients().size() : 0;
        holder.textIngredientCount.setText(ingCount + " Ingredients");

        View.OnClickListener listener = v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            context.startActivity(intent);
        };

        holder.btnViewRecipe.setOnClickListener(listener);
        holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView textRecipeName;
        TextView textRecipeCategory;
        TextView textIngredientCount;
        Button btnViewRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipeName = itemView.findViewById(R.id.textRecipeName);
            textRecipeCategory = itemView.findViewById(R.id.textRecipeCategory);
            textIngredientCount = itemView.findViewById(R.id.textIngredientCount);
            btnViewRecipe = itemView.findViewById(R.id.btnViewRecipe);
        }
    }
}
