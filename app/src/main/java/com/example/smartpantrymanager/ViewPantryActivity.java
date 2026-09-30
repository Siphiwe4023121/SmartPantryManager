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