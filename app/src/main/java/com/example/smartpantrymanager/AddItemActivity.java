package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_QUANTITY = "extra_quantity";
    public static final String EXTRA_UNIT = "extra_unit";
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final String EXTRA_EXPIRY = "extra_expiry";

    private int itemId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        TextView textTitle = findViewById(R.id.textTitle);
        EditText editItemName = findViewById(R.id.editItemName);
        EditText editQuantity = findViewById(R.id.editQuantity);
        Spinner spinnerUnit = findViewById(R.id.spinnerUnit);
        EditText editCategory = findViewById(R.id.editCategory);
        EditText editExpiryDate = findViewById(R.id.editExpiryDate);
        Button btnSaveItem = findViewById(R.id.btnSaveItem);
        Button btnCancel = findViewById(R.id.btnCancel);

        String[] units = new String[]{"pcs", "g", "kg", "ml", "l", "tbsp", "tsp"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        if (getIntent().hasExtra(EXTRA_ITEM_ID)) {
            itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
            if (itemId != -1) {
                isEditMode = true;
                textTitle.setText("Edit Pantry Item");
                btnSaveItem.setText("Update Pantry Item");

                editItemName.setText(getIntent().getStringExtra(EXTRA_NAME));
                double qty = getIntent().getDoubleExtra(EXTRA_QUANTITY, 1.0);
                editQuantity.setText(qty % 1 == 0 ? String.valueOf((int) qty) : String.valueOf(qty));

                String unit = getIntent().getStringExtra(EXTRA_UNIT);
                if (unit != null) {
                    for (int i = 0; i < units.length; i++) {
                        if (units[i].equalsIgnoreCase(unit)) {
                            spinnerUnit.setSelection(i);
                            break;
                        }
                    }
                }

                editCategory.setText(getIntent().getStringExtra(EXTRA_CATEGORY));
                editExpiryDate.setText(getIntent().getStringExtra(EXTRA_EXPIRY));
            }
        }

        editExpiryDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddItemActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        editExpiryDate.setText(selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        btnSaveItem.setOnClickListener(v -> {
            String itemName = editItemName.getText().toString().trim();
            String quantityStr = editQuantity.getText().toString().trim();
            String selectedUnit = spinnerUnit.getSelectedItem() != null ? spinnerUnit.getSelectedItem().toString() : "pcs";
            String category = editCategory.getText().toString().trim();
            String expiryDate = editExpiryDate.getText().toString().trim();

            if (itemName.isEmpty()) {
                editItemName.setError("Please enter an item name");
                editItemName.requestFocus();
                return;
            }

            if (quantityStr.isEmpty()) {
                editQuantity.setError("Please enter a quantity");
                editQuantity.requestFocus();
                return;
            }

            if (category.isEmpty()) {
                editCategory.setError("Please enter a category");
                editCategory.requestFocus();
                return;
            }

            double quantityNumber;
            try {
                quantityNumber = Double.parseDouble(quantityStr);
                if (quantityNumber <= 0) {
                    editQuantity.setError("Quantity must be greater than 0");
                    editQuantity.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                editQuantity.setError("Please enter a valid number");
                editQuantity.requestFocus();
                return;
            }

            PantryDatabaseHelper databaseHelper = new PantryDatabaseHelper(AddItemActivity.this);
            boolean success;

            if (isEditMode) {
                success = databaseHelper.updatePantryItem(itemId, itemName, quantityNumber, selectedUnit, category, expiryDate);
            } else {
                success = databaseHelper.addPantryItem(itemName, quantityNumber, selectedUnit, category, expiryDate);
            }

            if (success) {
                Toast.makeText(AddItemActivity.this,
                        isEditMode ? "Pantry item updated!" : "Pantry item saved!",
                        Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddItemActivity.this,
                        isEditMode ? "Failed to update item." : "Failed to save item.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> finish());
    }
}
