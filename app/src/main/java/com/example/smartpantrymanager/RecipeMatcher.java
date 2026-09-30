package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeMatcher {

    public static List<Recipe> getMatchingRecipes(List<PantryItem> pantryItems, List<Recipe> allRecipes) {
        List<Recipe> matchedRecipes = new ArrayList<>();

        Map<String, Map<String, Double>> pantryMap = new HashMap<>();

        for (PantryItem item : pantryItems) {
            String normName = UnitConverter.normalizeName(item.getName());
            String normUnit = UnitConverter.normalizeUnit(item.getUnit());

            if (!pantryMap.containsKey(normName)) {
                pantryMap.put(normName, new HashMap<>());
            }

            Map<String, Double> unitMap = pantryMap.get(normName);
            double currentQty = unitMap.containsKey(normUnit) ? unitMap.get(normUnit) : 0.0;
            unitMap.put(normUnit, currentQty + item.getQuantity());
        }

        for (Recipe recipe : allRecipes) {
            boolean allIngredientsSatisfied = true;

            for (RecipeIngredient ing : recipe.getIngredients()) {
                String reqNormName = UnitConverter.normalizeName(ing.getName());
                String reqNormUnit = UnitConverter.normalizeUnit(ing.getUnit());
                double reqQty = ing.getQuantity();

                if (!pantryMap.containsKey(reqNormName)) {
                    allIngredientsSatisfied = false;
                    break;
                }

                Map<String, Double> availableUnitsMap = pantryMap.get(reqNormName);
                double totalAvailableInReqUnit = 0.0;

                for (Map.Entry<String, Double> entry : availableUnitsMap.entrySet()) {
                    String availableUnit = entry.getKey();
                    double availableQty = entry.getValue();

                    double convertedQty = UnitConverter.convertQuantity(availableQty, availableUnit, reqNormUnit);
                    totalAvailableInReqUnit += convertedQty;
                }

                if (totalAvailableInReqUnit < reqQty) {
                    allIngredientsSatisfied = false;
                    break;
                }
            }

            if (allIngredientsSatisfied && !recipe.getIngredients().isEmpty()) {
                matchedRecipes.add(recipe);
            }
        }

        return matchedRecipes;
    }
}
