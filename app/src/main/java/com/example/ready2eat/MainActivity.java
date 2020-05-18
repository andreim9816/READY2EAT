package com.example.ready2eat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.rey.material.widget.CheckBox;
import com.example.ready2eat.Common.Common;
import com.example.ready2eat.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn, btnSignUp;
    TextView txtSlogan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        txtSlogan = (TextView)findViewById(R.id.txtSlogan);
        Typeface Face = Typeface.createFromAsset(getAssets(), "fonts/NABILLA.ttf");
        txtSlogan.setTypeface(Face);

        //Init Paper
        Paper.init(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(MainActivity.this, SignUp.class);
                startActivity(signUp);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = new Intent(MainActivity.this, SignIn.class);
                startActivity(signIn);

            }
        });

        // Check remember button
        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if(user != null && pwd != null)
            if(!user.isEmpty() && !pwd.isEmpty())
                login(user, pwd);
    }

    private void login(final String phone, final String pwd)
    {
        // copy paste la SignIn. TODO REFACTOR

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please wait...");
        mDialog.show();

        table_user.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Check if user exists in database
                if(dataSnapshot.child(phone).exists())
                {
                    //Get User Information
                    mDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone); // set phone
                    if(user.getPassword().equals(pwd))
                    {
                        if(user.getPhone().equals("0784310009"))
                        {
                            Intent homeAdminIntent = new Intent(MainActivity.this, AdminHome.class);
                            Common.currentUser = user;
                            startActivity(homeAdminIntent);
                            finish();
                        }
                        else
                        {
                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(homeIntent);
                            finish();
                        }
                    }
                    else
                    {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User does not exit in Database", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

}