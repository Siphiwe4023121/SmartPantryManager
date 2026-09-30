package com.example.smartpantrymanager;

import java.util.Locale;

public class UnitConverter {

    public static String normalizeName(String name) {
        if (name == null) return "";
        String s = name.trim().toLowerCase(Locale.ROOT);

        if (s.endsWith("tomatoes")) {
            return s.substring(0, s.length() - 2); // tomato
        } else if (s.endsWith("potatoes")) {
            return s.substring(0, s.length() - 2); // potato
        } else if (s.endsWith("berries")) {
            return s.substring(0, s.length() - 3) + "y"; // berry
        } else if (s.endsWith("es") && s.length() > 4 && !s.endsWith("cheese") && !s.endsWith("sauce")) {
            return s.substring(0, s.length() - 2);
        } else if (s.endsWith("s") && s.length() > 3 && !s.endsWith("ss") && !s.endsWith("us") && !s.endsWith("is") && !s.endsWith("rice")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String normalizeUnit(String unit) {
        if (unit == null) return "pcs";
        String u = unit.trim().toLowerCase(Locale.ROOT);

        switch (u) {
            case "g":
            case "gram":
            case "grams":
                return "g";
            case "kg":
            case "kgs":
            case "kilogram":
            case "kilograms":
                return "kg";
            case "ml":
            case "mls":
            case "milliliter":
            case "milliliters":
                return "ml";
            case "l":
            case "liter":
            case "liters":
            case "litre":
            case "litres":
                return "l";
            case "tbsp":
            case "tbsps":
            case "tablespoon":
            case "tablespoons":
                return "tbsp";
            case "tsp":
            case "tsps":
            case "teaspoon":
            case "teaspoons":
                return "tsp";
            case "piece":
            case "pieces":
            case "pcs":
            case "item":
            case "items":
            case "unit":
            case "units":
            default:
                return "pcs";
        }
    }

    public static double convertQuantityToBase(double quantity, String unit) {
        String normUnit = normalizeUnit(unit);
        switch (normUnit) {
            case "kg":
                return quantity * 1000.0; // convert to g
            case "g":
                return quantity;
            case "l":
                return quantity * 1000.0; // convert to ml
            case "tbsp":
                return quantity * 15.0; // convert to ml
            case "tsp":
                return quantity * 5.0; // convert to ml
            case "ml":
                return quantity;
            case "pcs":
            default:
                return quantity;
        }
    }

    public static String getUnitCategory(String unit) {
        String normUnit = normalizeUnit(unit);
        switch (normUnit) {
            case "g":
            case "kg":
                return "mass";
            case "ml":
            case "l":
            case "tbsp":
            case "tsp":
                return "volume";
            case "pcs":
            default:
                return "count";
        }
    }

    public static boolean areUnitsCompatible(String unit1, String unit2) {
        return getUnitCategory(unit1).equalsIgnoreCase(getUnitCategory(unit2));
    }

    public static double convertQuantity(double quantity, String fromUnit, String toUnit) {
        String fromCat = getUnitCategory(fromUnit);
        String toCat = getUnitCategory(toUnit);

        if (!fromCat.equalsIgnoreCase(toCat)) {
            return quantity; // Incompatible units: direct comparison
        }

        double baseQty = convertQuantityToBase(quantity, fromUnit);
        String normToUnit = normalizeUnit(toUnit);

        switch (normToUnit) {
            case "kg":
                return baseQty / 1000.0;
            case "g":
                return baseQty;
            case "l":
                return baseQty / 1000.0;
            case "tbsp":
                return baseQty / 15.0;
            case "tsp":
                return baseQty / 5.0;
            case "ml":
                return baseQty;
            case "pcs":
            default:
                return baseQty;
        }
    }
}
