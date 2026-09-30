package com.example.smartpantrymanager;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class PantryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_EXPIRY = "expiry_date";

    public static final String TABLE_SHOPPING = "shopping_items";
    public static final String COLUMN_SHOPPING_ID = "shopping_id";
    public static final String COLUMN_SHOPPING_NAME = "shopping_name";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_CATEGORY = "category";
    public static final String COLUMN_RECIPE_PREP = "prep_steps";

    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COLUMN_ING_ID = "id";
    public static final String COLUMN_ING_RECIPE_ID = "recipe_id";
    public static final String COLUMN_ING_NAME = "name";
    public static final String COLUMN_ING_QUANTITY = "quantity";
    public static final String COLUMN_ING_UNIT = "unit";

    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_QUANTITY + " REAL NOT NULL, " +
                COLUMN_UNIT + " TEXT NOT NULL, " +
                COLUMN_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_EXPIRY + " TEXT)";
        db.execSQL(createPantryTable);

        String createShoppingTable = "CREATE TABLE " + TABLE_SHOPPING + " (" +
                COLUMN_SHOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SHOPPING_NAME + " TEXT NOT NULL)";
        db.execSQL(createShoppingTable);

        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                COLUMN_RECIPE_CATEGORY + " TEXT NOT NULL, " +
                COLUMN_RECIPE_PREP + " TEXT NOT NULL)";
        db.execSQL(createRecipesTable);

        String createIngredientsTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COLUMN_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ING_RECIPE_ID + " INTEGER NOT NULL, " +
                COLUMN_ING_NAME + " TEXT NOT NULL, " +
                COLUMN_ING_QUANTITY + " REAL NOT NULL, " +
                COLUMN_ING_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_ING_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "))";
        db.execSQL(createIngredientsTable);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            try {
                db.execSQL("ALTER TABLE " + TABLE_PANTRY + " ADD COLUMN " + COLUMN_UNIT + " TEXT NOT NULL DEFAULT 'pcs'");
            } catch (Exception ignored) {
            }

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " (" +
                    COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_RECIPE_CATEGORY + " TEXT NOT NULL, " +
                    COLUMN_RECIPE_PREP + " TEXT NOT NULL)");

            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_INGREDIENTS + " (" +
                    COLUMN_ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ING_RECIPE_ID + " INTEGER NOT NULL, " +
                    COLUMN_ING_NAME + " TEXT NOT NULL, " +
                    COLUMN_ING_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_ING_UNIT + " TEXT NOT NULL)");

            seedRecipes(db);
        }
    }

    private void seedRecipes(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_RECIPES, null);
        if (cursor.moveToFirst() && cursor.getInt(0) > 0) {
            cursor.close();
            return;
        }
        cursor.close();

        addSeedRecipe(db, "Tomato Omelette", "Breakfast",
                "1. Whisk eggs with milk and a pinch of salt.\n2. Heat oil in a pan over medium heat.\n3. Add diced tomatoes and sauté for 1 minute.\n4. Pour in eggs and cook until fluffy and set.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Egg", 2, "pcs"),
                        new RecipeIngredient("Tomato", 1, "pcs"),
                        new RecipeIngredient("Milk", 50, "ml"),
                        new RecipeIngredient("Oil", 1, "tbsp"),
                        new RecipeIngredient("Salt", 1, "tsp")
                });

        addSeedRecipe(db, "Banana Pancakes", "Breakfast",
                "1. Mash bananas in a bowl.\n2. Mix in flour, milk, egg, and sugar to form a smooth batter.\n3. Heat oil/butter on a skillet.\n4. Pour batter to make small pancakes; cook until golden on both sides.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Flour", 200, "g"),
                        new RecipeIngredient("Milk", 250, "ml"),
                        new RecipeIngredient("Egg", 1, "pcs"),
                        new RecipeIngredient("Banana", 2, "pcs"),
                        new RecipeIngredient("Sugar", 2, "tbsp")
                });

        addSeedRecipe(db, "Vegetable Stir Fry", "Dinner",
                "1. Chop broccoli and carrots into bite-sized pieces.\n2. Heat oil in a wok or large pan.\n3. Stir fry vegetables for 5-7 minutes.\n4. Add soy sauce and serve hot over cooked rice.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Broccoli", 200, "g"),
                        new RecipeIngredient("Carrot", 2, "pcs"),
                        new RecipeIngredient("Rice", 250, "g"),
                        new RecipeIngredient("Soy Sauce", 2, "tbsp"),
                        new RecipeIngredient("Oil", 2, "tbsp")
                });

        addSeedRecipe(db, "Chicken Pasta", "Dinner",
                "1. Boil pasta according to package instructions and drain.\n2. Cook diced chicken in oil until browned.\n3. Add minced garlic and tomato sauce; simmer for 5 minutes.\n4. Toss pasta with sauce and top with melted cheese.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Pasta", 300, "g"),
                        new RecipeIngredient("Chicken", 250, "g"),
                        new RecipeIngredient("Tomato Sauce", 200, "ml"),
                        new RecipeIngredient("Cheese", 100, "g"),
                        new RecipeIngredient("Garlic", 2, "pcs")
                });

        addSeedRecipe(db, "Garlic Bread", "Snacks",
                "1. Mix softened butter with minced garlic.\n2. Spread butter evenly over slices of bread.\n3. Sprinkle grated cheese on top.\n4. Toast in oven at 200°C for 8-10 minutes until golden.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Bread", 4, "pcs"),
                        new RecipeIngredient("Garlic", 3, "pcs"),
                        new RecipeIngredient("Butter", 50, "g"),
                        new RecipeIngredient("Cheese", 50, "g")
                });

        addSeedRecipe(db, "Fruit Salad", "Dessert",
                "1. Wash and dice apples, bananas, and oranges.\n2. Combine all diced fruits in a large salad bowl.\n3. Drizzle fresh yogurt over the top and gently mix.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Apple", 2, "pcs"),
                        new RecipeIngredient("Banana", 2, "pcs"),
                        new RecipeIngredient("Orange", 2, "pcs"),
                        new RecipeIngredient("Yogurt", 150, "ml")
                });

        addSeedRecipe(db, "Spaghetti Bolognese", "Dinner",
                "1. Sauté chopped onion and garlic in oil.\n2. Add minced beef and cook until browned.\n3. Pour in tomato sauce and simmer for 15 minutes.\n4. Serve sauce hot over boiled spaghetti.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Spaghetti", 250, "g"),
                        new RecipeIngredient("Minced Beef", 300, "g"),
                        new RecipeIngredient("Onion", 1, "pcs"),
                        new RecipeIngredient("Garlic", 2, "pcs"),
                        new RecipeIngredient("Tomato Sauce", 250, "ml")
                });

        addSeedRecipe(db, "Grilled Cheese Sandwich", "Lunch",
                "1. Butter one side of each slice of bread.\n2. Place cheese between unbuttered sides.\n3. Grill sandwich on skillet until bread is golden and cheese is melted.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Bread", 2, "pcs"),
                        new RecipeIngredient("Cheese", 2, "pcs"),
                        new RecipeIngredient("Butter", 20, "g")
                });

        addSeedRecipe(db, "French Toast", "Breakfast",
                "1. Whisk egg, milk, sugar, and cinnamon in a shallow bowl.\n2. Dip bread slices to coat thoroughly.\n3. Cook on a buttered skillet until golden brown on both sides.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Bread", 4, "pcs"),
                        new RecipeIngredient("Egg", 2, "pcs"),
                        new RecipeIngredient("Milk", 100, "ml"),
                        new RecipeIngredient("Sugar", 1, "tbsp"),
                        new RecipeIngredient("Cinnamon", 1, "tsp")
                });

        addSeedRecipe(db, "Oatmeal Bowl", "Breakfast",
                "1. Cook oats in milk over medium heat for 5 minutes.\n2. Pour into a bowl.\n3. Slice banana on top and drizzle with honey.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Oats", 100, "g"),
                        new RecipeIngredient("Milk", 250, "ml"),
                        new RecipeIngredient("Banana", 1, "pcs"),
                        new RecipeIngredient("Honey", 1, "tbsp")
                });

        addSeedRecipe(db, "Chicken Salad", "Lunch",
                "1. Shred cooked chicken breast.\n2. Chop lettuce, tomato, and cucumber.\n3. Toss chicken and fresh veggies together.\n4. Drizzle with olive oil and serve cold.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Chicken", 200, "g"),
                        new RecipeIngredient("Lettuce", 100, "g"),
                        new RecipeIngredient("Tomato", 2, "pcs"),
                        new RecipeIngredient("Cucumber", 1, "pcs"),
                        new RecipeIngredient("Olive Oil", 1, "tbsp")
                });

        addSeedRecipe(db, "Mashed Potatoes", "Side",
                "1. Peel and boil potatoes in salted water until soft.\n2. Drain water thoroughly.\n3. Mash potatoes with butter, milk, and salt until creamy.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Potato", 4, "pcs"),
                        new RecipeIngredient("Butter", 30, "g"),
                        new RecipeIngredient("Milk", 50, "ml"),
                        new RecipeIngredient("Salt", 1, "tsp")
                });

        addSeedRecipe(db, "Scrambled Eggs", "Breakfast",
                "1. Whisk eggs with milk and salt.\n2. Melt butter in a non-stick pan over low heat.\n3. Pour in eggs and gently stir until softly set.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Egg", 3, "pcs"),
                        new RecipeIngredient("Butter", 15, "g"),
                        new RecipeIngredient("Milk", 30, "ml"),
                        new RecipeIngredient("Salt", 1, "tsp")
                });

        addSeedRecipe(db, "Rice and Beans", "Lunch",
                "1. Sauté chopped onion and minced garlic in oil.\n2. Add cooked beans and simmer for 5 minutes.\n3. Serve warm alongside cooked white rice.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Rice", 200, "g"),
                        new RecipeIngredient("Beans", 200, "g"),
                        new RecipeIngredient("Onion", 1, "pcs"),
                        new RecipeIngredient("Garlic", 1, "pcs"),
                        new RecipeIngredient("Oil", 1, "tbsp")
                });

        addSeedRecipe(db, "Guacamole Dip", "Snacks",
                "1. Mash avocados in a bowl.\n2. Mix in finely diced onion, tomato, lemon juice, and salt.\n3. Serve with tortilla chips.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Avocado", 2, "pcs"),
                        new RecipeIngredient("Onion", 1, "pcs"),
                        new RecipeIngredient("Tomato", 1, "pcs"),
                        new RecipeIngredient("Lemon Juice", 1, "tbsp"),
                        new RecipeIngredient("Salt", 1, "tsp")
                });

        addSeedRecipe(db, "Beef Taco", "Dinner",
                "1. Cook minced beef in a pan until browned.\n2. Fill taco shells with cooked beef.\n3. Top with shredded lettuce, diced tomato, and grated cheese.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Minced Beef", 250, "g"),
                        new RecipeIngredient("Taco Shells", 4, "pcs"),
                        new RecipeIngredient("Lettuce", 50, "g"),
                        new RecipeIngredient("Cheese", 50, "g"),
                        new RecipeIngredient("Tomato", 1, "pcs")
                });

        addSeedRecipe(db, "Strawberry Banana Smoothie", "Snacks",
                "1. Place milk, sliced banana, strawberries, and honey into a blender.\n2. Blend on high until completely smooth.\n3. Pour into glasses and serve fresh.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Milk", 300, "ml"),
                        new RecipeIngredient("Banana", 1, "pcs"),
                        new RecipeIngredient("Strawberry", 100, "g"),
                        new RecipeIngredient("Honey", 1, "tbsp")
                });

        addSeedRecipe(db, "Mushroom Soup", "Dinner",
                "1. Sauté sliced mushrooms and onions in melted butter.\n2. Sprinkle flour and stir for 1 minute.\n3. Slowly add milk while whisking and simmer until creamy.",
                new RecipeIngredient[]{
                        new RecipeIngredient("Mushroom", 250, "g"),
                        new RecipeIngredient("Butter", 30, "g"),
                        new RecipeIngredient("Onion", 1, "pcs"),
                        new RecipeIngredient("Milk", 400, "ml"),
                        new RecipeIngredient("Flour", 2, "tbsp")
                });
    }

    private void addSeedRecipe(SQLiteDatabase db, String name, String category, String prep, RecipeIngredient[] ingredients) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_NAME, name);
        values.put(COLUMN_RECIPE_CATEGORY, category);
        values.put(COLUMN_RECIPE_PREP, prep);

        long recipeId = db.insert(TABLE_RECIPES, null, values);
        if (recipeId != -1) {
            for (RecipeIngredient ing : ingredients) {
                ContentValues ingValues = new ContentValues();
                ingValues.put(COLUMN_ING_RECIPE_ID, recipeId);
                ingValues.put(COLUMN_ING_NAME, ing.getName());
                ingValues.put(COLUMN_ING_QUANTITY, ing.getQuantity());
                ingValues.put(COLUMN_ING_UNIT, ing.getUnit());
                db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);
            }
        }
    }

    public boolean addPantryItem(String name, double quantity, String unit, String category, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit != null ? unit : "pcs");
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY, expiryDate != null ? expiryDate : "");

        long result = db.insert(TABLE_PANTRY, null, values);
        return result != -1;
    }

    public boolean addPantryItem(String name, int quantity, String category, String expiryDate) {
        return addPantryItem(name, (double) quantity, "pcs", category, expiryDate);
    }

    public boolean updatePantryItem(int id, String name, double quantity, String unit, String category, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit != null ? unit : "pcs");
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_EXPIRY, expiryDate != null ? expiryDate : "");

        int result = db.update(TABLE_PANTRY, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY, null);
    }

    public Cursor getPantryItemsByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PANTRY, null, COLUMN_CATEGORY + " = ?", new String[]{category}, null, null, null);
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PANTRY, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public long addShoppingItem(String itemName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOPPING_NAME, itemName);
        return db.insert(TABLE_SHOPPING, null, values);
    }

    public Cursor getAllShoppingItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SHOPPING, null);
    }

    public boolean deleteShoppingItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_SHOPPING, COLUMN_SHOPPING_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_CATEGORY));
            String prep = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_PREP));

            Recipe recipe = new Recipe(id, name, category, prep);
            recipe.setIngredients(getRecipeIngredients(id));
            recipes.add(recipe);
        }
        cursor.close();
        return recipes;
    }

    public List<RecipeIngredient> getRecipeIngredients(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COLUMN_ING_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)}, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ING_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ING_NAME));
            double quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_ING_QUANTITY));
            String unit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ING_UNIT));

            ingredients.add(new RecipeIngredient(id, recipeId, name, quantity, unit));
        }
        cursor.close();
        return ingredients;
    }

    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)}, null, null, null);

        Recipe recipe = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_NAME));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_CATEGORY));
            String prep = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RECIPE_PREP));

            recipe = new Recipe(id, name, category, prep);
            recipe.setIngredients(getRecipeIngredients(id));
        }
        cursor.close();
        return recipe;
    }
}
