package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private int id;
    private String name;
    private String category;
    private String preparationSteps;
    private List<RecipeIngredient> ingredients;

    public Recipe(int id, String name, String category, String preparationSteps) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.preparationSteps = preparationSteps;
        this.ingredients = new ArrayList<>();
    }

    public Recipe(int id, String name, String category, String preparationSteps, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.preparationSteps = preparationSteps;
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getPreparationSteps() {
        return preparationSteps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }
}
