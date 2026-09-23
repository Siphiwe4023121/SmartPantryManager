package com.example.smartpantrymanager;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.database.Cursor;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText editShoppingItem = findViewById(R.id.editShoppingItem);
        Button btnAddShopping = findViewById(R.id.btnAddShoppingItem);
        LinearLayout shoppingItemsContainer = findViewById(R.id.shoppingItemsContainer);
        Button btnBack = findViewById(R.id.btnBackShopping);

        PantryDatabaseHelper databaseHelper =
                new PantryDatabaseHelper(ShoppingListActivity.this);
        Cursor cursor = databaseHelper.getAllShoppingItems();

        while (cursor.moveToNext()) {

            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            PantryDatabaseHelper.COLUMN_SHOPPING_NAME
                    )
            );

            TextView itemView = new TextView(ShoppingListActivity.this);
            itemView.setText("• " + itemName);
            itemView.setTextSize(18);
            itemView.setPadding(10, 15, 10, 15);

            shoppingItemsContainer.addView(itemView);
        }

        cursor.close();
        btnAddShopping.setOnClickListener(v -> {

            String itemName = editShoppingItem.getText().toString().trim();

            if (!itemName.isEmpty()) {

                boolean added = databaseHelper.addShoppingItem(itemName);

                if (added) {

                    TextView itemView = new TextView(ShoppingListActivity.this);

                    itemView.setText("• " + itemName);
                    itemView.setTextSize(18);
                    itemView.setPadding(10, 15, 10, 15);

                    shoppingItemsContainer.addView(itemView);

                    editShoppingItem.setText("");
                }
            }
        });
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }

}