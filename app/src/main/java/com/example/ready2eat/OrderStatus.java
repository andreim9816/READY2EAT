package com.example.ready2eat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.ready2eat.Common.Common;
import com.example.ready2eat.Database.Database;
import com.example.ready2eat.Interface.ItemClickListener;
import com.example.ready2eat.Model.Request;
import com.example.ready2eat.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class, R.layout.order_layout, OrderViewHolder.class, requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int position) {

                orderViewHolder.txtOrderId.setText("Id comanda: " + adapter.getRef(position).getKey());
                orderViewHolder.txtOrderStatus.setText("Status: " + convertCodeToStatus(request.getStatus()));
                orderViewHolder.txtOrderHour.setText("Ora comenzii: " + request.getHour());
                orderViewHolder.txtOrderPhone.setText("Telefon: " + request.getPhone());
                orderViewHolder.txtOrderName.setText("Nume: "+ request.getName());

                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClik) {

                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {

        if(Objects.equals(status, new String("0")))
            return "Plasata";
        else if(Objects.equals(status, new String("1")))
                return "In curs de preparare";
        else
            return "Se poate prelua!";
    }
}
