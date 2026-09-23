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

                TextView itemView = new TextView(ViewPantryActivity.this);

                itemView.setText(
                        name +
                                "\nQuantity: " + quantity +
                                "\nCategory: " + category +
                                "\nExpiry Date: " + expiryDate
                );

                itemView.setTextSize(18);
                itemView.setTextColor(Color.BLACK);
                itemView.setPadding(20, 20, 20, 30);

                pantryItemsContainer.addView(itemView);

                Button deleteButton = new Button(ViewPantryActivity.this);
                deleteButton.setText("Delete");

                deleteButton.setOnClickListener(v -> {

                    boolean deleted = databaseHelper.deletePantryItem(id);

                    if (deleted) {
                        pantryItemsContainer.removeView(itemView);
                        pantryItemsContainer.removeView(deleteButton);
                    }

                });

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