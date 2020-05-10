package com.example.ready2eat.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.ready2eat.Cart;
import com.example.ready2eat.Interface.ItemClickListener;
import com.example.ready2eat.Model.Order;
import com.example.ready2eat.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.M)
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnContextClickListener{

    public TextView txt_cart_name, txt_price;
    public ImageView img_cart_count;
    public ImageView cart_image;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView)
    {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_Price);
        img_cart_count = (ImageView)itemView.findViewById(R.id.cart_item_count);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_image);

    }

    @Override
    public boolean onContextClick(View v) {
        return false;
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData = new ArrayList<>();
    private Context context;
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(CartViewHolder holder, final int position) {

        Picasso.get()
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);

        TextDrawable drawable = TextDrawable.builder()
                .buildRect(""+listData.get(position).getQuantity(),  Color.rgb(248, 180, 0));
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("ro", "RO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));

        holder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
