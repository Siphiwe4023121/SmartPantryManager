package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.database.Cursor;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

public class ViewPantryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_pantry);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        LinearLayout pantryItemsContainer =
                findViewById(R.id.pantryItemsContainer);

        PantryDatabaseHelper databaseHelper =
                new PantryDatabaseHelper(ViewPantryActivity.this);
        Cursor cursor = databaseHelper.getAllPantryItems();

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_ID));

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_NAME));

                int quantity = cursor.getInt(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_QUANTITY));

                String category = cursor.getString(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_CATEGORY));

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COLUMN_EXPIRY));

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                long daysUntilExpiry = 999;

                try {
                    Date expiry = dateFormat.parse(expiryDate);
                    Date today = new Date();

                    if (expiry != null) {
                        long difference = expiry.getTime() - today.getTime();
                        daysUntilExpiry = TimeUnit.MILLISECONDS.toDays(difference);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                TextView itemView = new TextView(ViewPantryActivity.this);

                String stockStatus = "";

                if (quantity <= 2) {
                    stockStatus = "\n⚠ Low Stock";
                }

                String expiryStatus = "";

                if (daysUntilExpiry < 0) {
                    expiryStatus = "\n⚠ EXPIRED";
                    itemView.setTextColor(Color.RED);
                } else if (daysUntilExpiry <= 7) {
                    expiryStatus = "\n⚠ Expiring Soon (" + daysUntilExpiry + " days left)";
                    itemView.setTextColor(Color.rgb(255, 140, 0));
                } else {
                    itemView.setTextColor(Color.BLACK);
                }

                itemView.setText(
                        name +
                                "\nQuantity: " + quantity +
                                "\nCategory: " + category +
                                "\nExpiry Date: " + expiryDate +
                                stockStatus +
                                expiryStatus
                );

                itemView.setTextSize(18);
                itemView.setPadding(20, 20, 20, 30);

                Button shoppingButton = new Button(ViewPantryActivity.this);
                shoppingButton.setText("Add to Shopping List");

                shoppingButton.setOnClickListener(view -> {
                    long result = databaseHelper.addShoppingItem(name);

                    if (result != -1) {
                        shoppingButton.setText("Added to Shopping List");
                        shoppingButton.setEnabled(false);
                    }
                });

                Button editButton = new Button(ViewPantryActivity.this);
                editButton.setText("Edit");

                editButton.setOnClickListener(view -> {

                    LinearLayout editLayout = new LinearLayout(ViewPantryActivity.this);
                    editLayout.setOrientation(LinearLayout.VERTICAL);
                    editLayout.setPadding(40, 20, 40, 20);

                    EditText editName = new EditText(ViewPantryActivity.this);
                    editName.setHint("Item Name");
                    editName.setText(name);

                    EditText editQuantity = new EditText(ViewPantryActivity.this);
                    editQuantity.setHint("Quantity");
                    editQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editQuantity.setText(String.valueOf(quantity));

                    EditText editCategory = new EditText(ViewPantryActivity.this);
                    editCategory.setHint("Category");
                    editCategory.setText(category);

                    EditText editExpiry = new EditText(ViewPantryActivity.this);
                    editExpiry.setHint("Expiry Date (dd/MM/yyyy)");
                    editExpiry.setText(expiryDate);

                    editLayout.addView(editName);
                    editLayout.addView(editQuantity);
                    editLayout.addView(editCategory);
                    editLayout.addView(editExpiry);

                    new AlertDialog.Builder(ViewPantryActivity.this)
                            .setTitle("Edit Pantry Item")
                            .setView(editLayout)
                            .setPositiveButton("Save", (dialog, which) -> {

                                String newName = editName.getText().toString().trim();
                                String quantityText = editQuantity.getText().toString().trim();
                                String newCategory = editCategory.getText().toString().trim();
                                String newExpiry = editExpiry.getText().toString().trim();

                                if (!newName.isEmpty() && !quantityText.isEmpty()
                                        && !newCategory.isEmpty() && !newExpiry.isEmpty()) {

                                    int newQuantity;

                                    try {
                                        newQuantity = Integer.parseInt(quantityText);

                                        if (newQuantity <= 0) {
                                            Toast.makeText(
                                                    ViewPantryActivity.this,
                                                    "Quantity must be greater than 0",
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                            return;
                                        }

                                    } catch (NumberFormatException e) {
                                        Toast.makeText(
                                                ViewPantryActivity.this,
                                                "Please enter a valid quantity",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        return;
                                    }

                                    SimpleDateFormat validationFormat =
                                            new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                                    validationFormat.setLenient(false);

                                    try {
                                        validationFormat.parse(newExpiry);
                                    } catch (ParseException e) {
                                        Toast.makeText(
                                                ViewPantryActivity.this,
                                                "Please enter a valid date (dd/MM/yyyy)",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        return;
                                    }

                                    boolean updated = databaseHelper.updatePantryItem(
                                            id,
                                            newName,
                                            newQuantity,
                                            newCategory,
                                            newExpiry
                                    );

                                    if (updated) {
                                        Toast.makeText(
                                                ViewPantryActivity.this,
                                                "Item updated successfully",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        recreate();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                });

                Button deleteButton = new Button(ViewPantryActivity.this);
                deleteButton.setText("Delete");

                deleteButton.setOnClickListener(view -> {

                    boolean deleted = databaseHelper.deletePantryItem(id);

                    if (deleted) {
                        pantryItemsContainer.removeView(itemView);
                        pantryItemsContainer.removeView(shoppingButton);
                        pantryItemsContainer.removeView(deleteButton);
                    }
                });

                pantryItemsContainer.addView(itemView);

                if (quantity <= 2) {
                    pantryItemsContainer.addView(shoppingButton);
                }
                pantryItemsContainer.addView(editButton);
                pantryItemsContainer.addView(deleteButton);
            }

        }

        cursor.close();
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
}