package com.example.jkk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jkk.CartItem;
import com.example.jkk.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public CartAdapter(List<CartItem> cartItemList, Context context) {
        this.context = context;
        this.cartItemList = cartItemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);

        holder.foodNameTextView.setText("Food Name:\t"+cartItem.getFoodName());

        // Format price-related information
//        String formattedPrice = String.format("$ %.2f", cartItem.getFoodPrice());
        holder.foodPriceTextView.setText("Food Price:\t"+cartItem.getFoodPrice());

        holder.quantityTextView.setText("Quantity:\t"+cartItem.getTotalQuantity());

        // Format total price information
        holder.totalPriceTextView.setText("Total Price:\t"+cartItem.getTotalPrice());

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        TextView foodPriceTextView;
        TextView quantityTextView;
        TextView totalPriceTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.foodNameTextView);
            foodPriceTextView = itemView.findViewById(R.id.foodPriceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
        }
    }
}
