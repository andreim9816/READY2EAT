package com.example.ready2eat.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ready2eat.Interface.ItemClickListener;
import com.example.ready2eat.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView food_name;
    public ImageView food_image;
    public TextView quantity;
    public TextView food_price;
    public TextView time;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder( View itemView)
    {
        super(itemView);

        food_name = itemView.findViewById(R.id.food_name);
        food_image = itemView.findViewById(R.id.food_image);
        quantity = itemView.findViewById(R.id.quantity);
        food_price = itemView.findViewById(R.id.food_price);
        time = itemView.findViewById(R.id.time);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
