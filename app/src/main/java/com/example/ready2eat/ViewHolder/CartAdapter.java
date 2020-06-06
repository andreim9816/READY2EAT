package com.example.ready2eat.ViewHolder;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ready2eat.Cart;
import com.example.ready2eat.Database.Database;
import com.example.ready2eat.Interface.ItemClickListener;
import com.example.ready2eat.Model.Order;
import com.example.ready2eat.R;
import com.example.ready2eat.Cart;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.M)
class CartViewHolder extends RecyclerView.ViewHolder implements View.OnContextClickListener
{
    public TextView txt_cart_name, txt_price;
    public ElegantNumberButton btn_quantity;
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
        btn_quantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_image);
    }
    @Override
    public boolean onContextClick(View v) {
        return false;
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>
{
    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart)
    {
        this.listData = listData;
        this.cart = cart;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final CartViewHolder holder, final int position)
    {

        Picasso.get()
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(holder.cart_image);

//        TextDrawable drawable = TextDrawable.builder()
//                .buildRect(""+listData.get(position).getQuantity(),  Color.rgb(248, 180, 0));
//        holder.img_cart_count.setImageDrawable(drawable);

        holder.btn_quantity.setNumber(listData.get(position).getQuantity());
        holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Order order = listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                if(newValue == 0)
                {
                    int id = order.getID();
                    new Database(cart).removeFromCart(id);
                    cart.loadListFood();

                }
                else
                {
                    new Database(cart).updateCart(order);
                }

                // Calculate total price
                float total = 0;
                List<Order> orders = new Database(cart).getCarts();

                for(Order item: orders)
                {
                    total += (Float.parseFloat(item.getPrice())) * (Integer.parseInt(item.getQuantity()));

                }
                Locale locale = new Locale("ro", "RO");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                holder.txt_price.setText(fmt.format(Float.parseFloat(order.getPrice()) * Integer.parseInt(order.getQuantity())));
                cart.txtTotalPrice.setText(fmt.format(total));


            }
        });

        Locale locale = new Locale("ro", "RO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        float price = (Float.parseFloat(listData.get(position).getPrice())) * (Integer.parseInt(listData.get(position).getQuantity()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
