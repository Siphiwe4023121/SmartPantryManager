package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        EditText editItemName = findViewById(R.id.editItemName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        EditText editCategory = findViewById(R.id.editCategory);
        EditText editExpiryDate = findViewById(R.id.editExpiryDate);

        editExpiryDate.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddItemActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        String selectedDate =
                                String.format("%02d/%02d/%04d",
                                        selectedDay,
                                        selectedMonth + 1,
                                        selectedYear);

                        editExpiryDate.setText(selectedDate);
                    },
                    year,
                    month,
                    day
            );

            datePickerDialog.show();
        });

        Button btnSaveItem = findViewById(R.id.btnSaveItem);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnSaveItem.setOnClickListener(v -> {

            String itemName = editItemName.getText().toString().trim();
            String quantity = editQuantity.getText().toString().trim();
            String category = editCategory.getText().toString().trim();
            String expiryDate = editExpiryDate.getText().toString().trim();

            if (itemName.isEmpty()) {
                editItemName.setError("Please enter an item name");
                editItemName.requestFocus();
                return;
            }

            if (quantity.isEmpty()) {
                editQuantity.setError("Please enter a quantity");
                editQuantity.requestFocus();
                return;
            }

            if (category.isEmpty()) {
                editCategory.setError("Please enter a category");
                editCategory.requestFocus();
                return;
            }

            if (expiryDate.isEmpty()) {
                editExpiryDate.setError("Please enter an expiry date");
                editExpiryDate.requestFocus();
                return;
            }

            int quantityNumber = Integer.parseInt(quantity);

            PantryDatabaseHelper databaseHelper =
                    new PantryDatabaseHelper(AddItemActivity.this);

            boolean isInserted = databaseHelper.addPantryItem(
                    itemName,
                    quantityNumber,
                    category,
                    expiryDate
            );

            if (isInserted) {
                Toast.makeText(
                        AddItemActivity.this,
                        "Pantry item saved successfully!",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            } else {
                Toast.makeText(
                        AddItemActivity.this,
                        "Failed to save pantry item.",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }
}