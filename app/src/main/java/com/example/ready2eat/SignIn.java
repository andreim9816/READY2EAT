package com.example.ready2eat;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ready2eat.Common.Common;
import com.example.ready2eat.Model.Admin;
import com.example.ready2eat.Model.Person;
import com.example.ready2eat.Model.User;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;


public class SignIn extends AppCompatActivity {

    EditText edtPhone, edtPassword;
    Button btnSignIn;
    com.rey.material.widget.CheckBox ckbRemember; //! Not CheckBox type, but that one!

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        btnSignIn = findViewById(R.id.btnSignIn);
        ckbRemember = findViewById(R.id.ckbRemember);

        // Init Paper
        Paper.init(this);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        final DatabaseReference table_admin = database.getReference("Admin");


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 //functie de isConnectedToInternet
                if(ckbRemember.isChecked())
                {
                    Paper.book().write(Common.USER_KEY, edtPhone.getText().toString());
                    Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());
                }

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Va rugam sa asteptati...");
                mDialog.show();



                table_user.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        //Check if user exists in database
                        if (!TextUtils.isEmpty(edtPhone.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString())) {

                            if (dataSnapshot.child(edtPhone.getText().toString()).exists())
                            {
                                //Get User Information
                                mDialog.dismiss();
                                Person user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                user.setPhone(edtPhone.getText().toString()); // set phone
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, "Parola gresita", Toast.LENGTH_SHORT).show();
                                }
                            } else
                                {
                                table_admin.addValueEventListener(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {
                                        //Check if user exists in database
                                        if (!TextUtils.isEmpty(edtPhone.getText().toString()) && !TextUtils.isEmpty(edtPassword.getText().toString()))
                                        {

                                            if (dataSnapshot.child(edtPhone.getText().toString()).exists())
                                            {
                                                //Get User Information
                                                mDialog.dismiss();
                                                Person admin = dataSnapshot.child(edtPhone.getText().toString()).getValue(Admin.class);
                                                admin.setPhone(edtPhone.getText().toString()); // set phone
                                                if (admin.getPassword().equals(edtPassword.getText().toString()))
                                                {
                                                    Intent homeAdminIntent = new Intent(SignIn.this, AdminHome.class);
                                                    Common.currentUser = admin;
                                                    startActivity(homeAdminIntent);
                                                    finish();
                                                }
                                                else
                                                    {
                                                        mDialog.dismiss();
                                                        Toast.makeText(SignIn.this, "Parola gresita", Toast.LENGTH_SHORT).show();
                                                    }
                                            }
                                            else
                                                {
                                                  mDialog.dismiss();
                                                  Toast.makeText(SignIn.this, "Numar de telefon sau parola incorecte", Toast.LENGTH_SHORT).show();
                                                }
                                        }
                                        else
                                            {
                                                mDialog.dismiss();
                                                Toast.makeText(SignIn.this, "Introdu mai intai numar si parola", Toast.LENGTH_SHORT).show();
                                             }
                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Introdu mai intai numar si parola", Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                });


            }
        });
    }


}
