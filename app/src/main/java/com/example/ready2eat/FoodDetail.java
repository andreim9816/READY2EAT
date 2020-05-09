package com.example.ready2eat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ready2eat.Database.Database;
import com.example.ready2eat.Model.Food;
import com.example.ready2eat.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.layout.simple_list_item_1;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, quantity, time;
    ListView food_description;
    ImageView food_image;
    Button btnCart;
    ElegantNumberButton numberButton;

    String foodId = "";

    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentFood;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        //justifyListViewHeightBasedOnChildren(food_description);
        //Firebase
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        //Init view
        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (Button) findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// <<<<<<< master
//                 new Database(getBaseContext()).addToCart(new Order(
//                         foodId,
//                         currentFood.getName(),
//                         numberButton.getNumber(),
//                         currentFood.getPrice(),
//                         currentFood.getDiscount(),
//                         currentFood.getImage()
//                 ));

                Toast.makeText(FoodDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
// =======
                if(Integer.parseInt(numberButton.getNumber()) != 0) {
                    new Database(getBaseContext()).addToCart(new Order(
                            foodId,
                            currentFood.getName(),
                            numberButton.getNumber(),
                            currentFood.getPrice(),
                            currentFood.getDiscount(),
                            currentFood.getImage()
                    ));

                    Toast.makeText(FoodDetail.this, "Adaugat in cos", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(FoodDetail.this, "Selecteaza cantitatea mai intai", Toast.LENGTH_SHORT).show();
                }

// >>>>>>> master
            }
        });

        food_description = findViewById(R.id.food_description);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_image = findViewById(R.id.img_food);
        quantity = findViewById(R.id.quantity);
        time = findViewById(R.id.time);
        food_description.setScrollContainer(false);


        //Get FooD Id from Intent
        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");

        }
        if (!foodId.isEmpty()) {
            getDetailFood(foodId);
        }
    }

    private void getDetailFood(String foodId)
    {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //Set Image
                Picasso.get().load(currentFood.getImage())
                        .into(food_image);


                food_price.setText(currentFood.getPrice() + " Lei");        //am afisat in layout cantitate, pret, timp, nume
                food_name.setText(currentFood.getName());
                if(!currentFood.getMenuID().equals("11"))
                    quantity.setText(currentFood.getQuantity() + " g");    //bauturile au deja ml in cantitate
                else quantity.setText(currentFood.getQuantity());
                Log.d("DETALII", currentFood.getQuantity() + " " + currentFood.getName());
                time.setText(currentFood.getTime() + " min");
                String fd = currentFood.getDescription();                   //afisarea ingredientelor tip lista
                ArrayList<String> food_desc_array = new ArrayList<>(Arrays.asList(fd.split("[,|\n]+")));
                for(int i = 0 ; i < food_desc_array.size() ; i++)
                    food_desc_array.set(i, food_desc_array.get(i).trim());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, food_desc_array);
                food_description.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
