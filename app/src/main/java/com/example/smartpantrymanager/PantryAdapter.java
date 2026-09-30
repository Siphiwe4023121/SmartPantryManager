package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems;

    public PantryAdapter(List<PantryItem> pantryItems) {
        this.pantryItems = pantryItems;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);
        Context context = holder.itemView.getContext();

        holder.textItemName.setText(item.getName());

        double qty = item.getQuantity();
        String qtyFormatted = (qty % 1 == 0) ? String.valueOf((int) qty) : String.valueOf(qty);
        holder.textQuantity.setText("Quantity: " + qtyFormatted + " " + item.getUnit());

        holder.textCategory.setText("Category: " + item.getCategory());

        String expiryStr = item.getExpiryDate();
        holder.textExpiryDate.setText("Expiry Date: " + (expiryStr != null && !expiryStr.isEmpty() ? expiryStr : "N/A"));

        // Reset button states for recycled views
        holder.btnAddToShopping.setText("Add to Shopping List");
        holder.btnAddToShopping.setEnabled(true);

        StringBuilder status = new StringBuilder();

        if (item.getQuantity() <= 2) {
            status.append("⚠ Low Stock");
            holder.btnAddToShopping.setVisibility(View.VISIBLE);
        } else {
            holder.btnAddToShopping.setVisibility(View.GONE);
        }

        holder.btnAddToShopping.setOnClickListener(v -> {
            PantryDatabaseHelper databaseHelper = new PantryDatabaseHelper(v.getContext());
            long result = databaseHelper.addShoppingItem(item.getName() + " (" + qtyFormatted + " " + item.getUnit() + ")");
            if (result != -1) {
                holder.btnAddToShopping.setText("Added to Shopping List");
                holder.btnAddToShopping.setEnabled(false);
                Toast.makeText(v.getContext(), "Added to shopping list!", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences prefs = context.getSharedPreferences("SmartPantryPrefs", Context.MODE_PRIVATE);
        int alertDaysThreshold = prefs.getInt("expiry_alert_days", 7);

        if (expiryStr != null && !expiryStr.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date expiry = dateFormat.parse(expiryStr);
                if (expiry != null) {
                    Calendar todayCal = Calendar.getInstance();
                    todayCal.set(Calendar.HOUR_OF_DAY, 0);
                    todayCal.set(Calendar.MINUTE, 0);
                    todayCal.set(Calendar.SECOND, 0);
                    todayCal.set(Calendar.MILLISECOND, 0);
                    Date today = todayCal.getTime();

                    long difference = expiry.getTime() - today.getTime();
                    long daysUntilExpiry = TimeUnit.MILLISECONDS.toDays(difference);

                    if (daysUntilExpiry < 0) {
                        if (status.length() > 0) status.append("\n");
                        status.append("⚠ EXPIRED");
                    } else if (daysUntilExpiry <= alertDaysThreshold) {
                        if (status.length() > 0) status.append("\n");
                        status.append("⚠ Expiring Soon (").append(daysUntilExpiry).append(" days left)");
                    }
                }
            } catch (ParseException ignored) {
            }
        }

        if (status.length() > 0) {
            holder.textStatus.setText(status.toString());
            holder.textStatus.setVisibility(View.VISIBLE);
        } else {
            holder.textStatus.setText("");
            holder.textStatus.setVisibility(View.GONE);
        }

        holder.btnEditItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddItemActivity.class);
            intent.putExtra(AddItemActivity.EXTRA_ITEM_ID, item.getId());
            intent.putExtra(AddItemActivity.EXTRA_NAME, item.getName());
            intent.putExtra(AddItemActivity.EXTRA_QUANTITY, item.getQuantity());
            intent.putExtra(AddItemActivity.EXTRA_UNIT, item.getUnit());
            intent.putExtra(AddItemActivity.EXTRA_CATEGORY, item.getCategory());
            intent.putExtra(AddItemActivity.EXTRA_EXPIRY, item.getExpiryDate());
            context.startActivity(intent);
        });

        holder.btnDeleteItem.setOnClickListener(view -> {
            PantryDatabaseHelper db = new PantryDatabaseHelper(view.getContext());
            boolean deleted = db.deletePantryItem(item.getId());

            if (deleted) {
                int currentPosition = holder.getBindingAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION && currentPosition < pantryItems.size()) {
                    pantryItems.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                    notifyItemRangeChanged(currentPosition, pantryItems.size() - currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView textItemName;
        TextView textQuantity;
        TextView textCategory;
        TextView textExpiryDate;
        TextView textStatus;

        Button btnAddToShopping;
        Button btnEditItem;
        Button btnDeleteItem;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            textItemName = itemView.findViewById(R.id.textItemName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textCategory = itemView.findViewById(R.id.textCategory);
            textExpiryDate = itemView.findViewById(R.id.textExpiryDate);
            textStatus = itemView.findViewById(R.id.textStatus);

            btnAddToShopping = itemView.findViewById(R.id.btnAddToShopping);
            btnEditItem = itemView.findViewById(R.id.btnEditItem);
            btnDeleteItem = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
