package com.example.ready2eat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ready2eat.Common.Common;
import com.example.ready2eat.Database.Database;
import com.example.ready2eat.Model.Order;
import com.example.ready2eat.Model.Request;
import com.example.ready2eat.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity
{

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView txtTotalPrice;
    Button btnPlace;

    public List<Order> cart = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace = (Button) findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                showAlertDialog();
                // Create new Request
            }
        });

        loadListFood();
    }
    private void showAlertDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleformat = new SimpleDateFormat("HH:mm");
        calendar.add(Calendar.HOUR_OF_DAY, + 1);

        Date finishTime = calendar.getTime();
        String currentTime = simpleformat.format(finishTime);
        final int minutes = finishTime.getMinutes();
        final int hours = finishTime.getHours();

        alertDialog.setTitle("Ultimul pas!");

        alertDialog.setTitle("Puteti prelua comanda incepand cu ora " + currentTime);
        alertDialog.setMessage("Daca doriti sa preluati comanda mai tarziu, introduceti ora(hh:mm):");

        final EditText edtHour = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        edtHour.setLayoutParams(lp);
        alertDialog.setView(edtHour);
        alertDialog.setIcon(R.drawable.ic_access_time_black_24dp);

        alertDialog.setPositiveButton("CONFIRMĂ ORA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String pickUpTime = edtHour.getText().toString();

                if (pickUpTime.length() != 5)
                    Toast.makeText(Cart.this, "Formatul orei este incorect!", Toast.LENGTH_SHORT).show();
                else {
                    String pickUpHour = pickUpTime.substring(0, 2);
                    String pickUpMinutes = pickUpTime.substring(3, 5);

                    if (Integer.parseInt(pickUpHour) > hours
                            || (Integer.parseInt(pickUpHour) == hours
                            && Integer.parseInt(pickUpMinutes) >= minutes)) {

                        Toast.makeText(Cart.this, "Comanda ta urmează să fie procesată", Toast.LENGTH_SHORT).show();

                        // Create new request
                        Request request = new Request(
                                Common.currentUser.getPhone(),
                                Common.currentUser.getName(),
                                pickUpTime,
                                txtTotalPrice.getText().toString(),
                                cart);

                        //Submit to Firebase
                        //We use System.CurrentMillis
                        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                        //Delete cart
                        new Database(getBaseContext()).cleanCart();
                        Toast.makeText(Cart.this, "Comanda ta urmează să fie procesată",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(Cart.this, "Ora introdusă este incorectă! ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        alertDialog.setNegativeButton("ÎNAPOI LA COȘ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();
    }

    public float calculateTotal(List<Order> cart)
    {
        float total = 0;
        for(Order order: cart)
        {
            total += (1 - (Float.parseFloat(order.getDiscount())/ 100)) *
                    (Float.parseFloat(order.getPrice())) * (Float.parseFloat(order.getQuantity()));
        }

        return total;

    }
    public void loadListFood()
    {
        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart, this);
        recyclerView.setAdapter(adapter);

        cart = new Database(this).getCarts();
        // Calculate total price
        float total = calculateTotal(cart);

        Locale locale = new Locale("ro", "RO");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }
}
