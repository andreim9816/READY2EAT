package com.example.ready2eat.View.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.ready2eat.Common.Common;
import com.example.ready2eat.Interface.ItemClickListener;
import com.example.ready2eat.Model.Request;
import com.example.ready2eat.R;
import com.example.ready2eat.View.OrderDetail;
import com.example.ready2eat.ViewHolder.Admin.AdminOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class AdminOrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, AdminOrderViewHolder> adapter;
    FirebaseDatabase database;
    DatabaseReference requests;

    MaterialSpinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listOrders1);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, AdminOrderViewHolder>(
                Request.class, R.layout.admin_order_layout, AdminOrderViewHolder.class, requests
        ) {
            @Override

            protected void populateViewHolder(AdminOrderViewHolder adminOrderViewHolder, final Request request, int position) {

                adminOrderViewHolder.txtOrderId.setText("Id comanda: " + adapter.getRef(position).getKey());
                adminOrderViewHolder.txtOrderStatus.setText("Status: " + Common.convertCodeToStatus(request.getStatus()));
                adminOrderViewHolder.txtOrderHour.setText("Ora comenzii: " + request.getHour());
                adminOrderViewHolder.txtOrderPhone.setText("Telefon: " + request.getPhone());
                adminOrderViewHolder.txtOrderName.setText("Nume: "+ request.getName());

                adminOrderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClik) {
                            Intent orderDetail = new Intent(AdminOrderStatus.this, OrderDetail.class);
                            Common.currentRequest = request;
                            orderDetail.putExtra("OrderId", adapter.getRef(position).getKey());
                            startActivity(orderDetail);
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
        }
        else
        if(item.getTitle().equals(Common.DELETE))
        {
            deleteCategory(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key)
    {
        requests.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, final Request item)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminOrderStatus.this);
        alertDialog.setTitle("Actualizeaza statusul unei comenzi");
        alertDialog.setMessage("Alege un status:");

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Plasata",  "Se poate prelua", "Preluata");

        alertDialog.setView(view);

        final String localKey = key;

        alertDialog.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                requests.child(localKey).setValue(item);

            }
        });
        alertDialog.setNegativeButton("NU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
}
