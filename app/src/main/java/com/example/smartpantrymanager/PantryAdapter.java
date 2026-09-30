package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final List<PantryItem> pantryItems;
    private PantryDatabaseHelper databaseHelper;

    public PantryAdapter(List<PantryItem> pantryItems) {
        this.pantryItems = pantryItems;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position) {

        PantryItem item = pantryItems.get(position);

        holder.textItemName.setText(item.getName());
        holder.textQuantity.setText(
                "Quantity: " + item.getQuantity()
        );
        holder.textCategory.setText(
                "Category: " + item.getCategory()
        );
        holder.textExpiryDate.setText(
                "Expiry Date: " + item.getExpiryDate()
        );
        String status = "";

        if (item.getQuantity() <= 2) {

            status = "⚠ Low Stock";

            holder.btnAddToShopping.setVisibility(View.VISIBLE);

        } else {

            holder.btnAddToShopping.setVisibility(View.GONE);
        }

        holder.btnAddToShopping.setOnClickListener(v -> {

            PantryDatabaseHelper databaseHelper =
                    new PantryDatabaseHelper(v.getContext());

            long result =
                    databaseHelper.addShoppingItem(item.getName());

            if (result != -1) {
                holder.btnAddToShopping.setText("Added to Shopping List");
                holder.btnAddToShopping.setEnabled(false);
            }
        });


        SimpleDateFormat dateFormat =
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {

            Date expiry = dateFormat.parse(item.getExpiryDate());
            Date today = new Date();

            if (expiry != null) {

                long difference =
                        expiry.getTime() - today.getTime();

                long daysUntilExpiry =
                        TimeUnit.MILLISECONDS.toDays(difference);

                if (daysUntilExpiry < 0) {

                    status += "\n⚠ EXPIRED";

                } else if (daysUntilExpiry <= 7) {

                    status += "\n⚠ Expiring Soon ("
                            + daysUntilExpiry
                            + " days left)";
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!status.isEmpty()) {

            holder.textStatus.setText(status.trim());
            holder.textStatus.setVisibility(View.VISIBLE);

        } else {

            holder.textStatus.setText("");
            holder.textStatus.setVisibility(View.GONE);
        }
        holder.btnDeleteItem.setOnClickListener(view -> {

            PantryDatabaseHelper db =
                    new PantryDatabaseHelper(view.getContext());

            boolean deleted = db.deletePantryItem(item.getId());

            if (deleted) {

                int currentPosition = holder.getBindingAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    pantryItems.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

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

            textItemName =
                    itemView.findViewById(R.id.textItemName);

            textQuantity =
                    itemView.findViewById(R.id.textQuantity);

            textCategory =
                    itemView.findViewById(R.id.textCategory);

            textExpiryDate =
                    itemView.findViewById(R.id.textExpiryDate);

            textStatus =
                    itemView.findViewById(R.id.textStatus);

            btnAddToShopping =
                    itemView.findViewById(R.id.btnAddToShopping);

            btnEditItem =
                    itemView.findViewById(R.id.btnEditItem);

            btnDeleteItem =
                    itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
