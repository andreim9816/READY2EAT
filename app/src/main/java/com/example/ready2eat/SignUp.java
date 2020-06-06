package com.example.ready2eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ready2eat.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.io.*;

public class SignUp extends AppCompatActivity
{

    MaterialEditText edtPhone, edtName, edtPassword;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = findViewById(R.id.edtName);
        edtPassword= findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);

        btnSignUp = findViewById(R.id.btnSignUp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Aşteaptă...");
                mDialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if(edtName.getText().length() == 0)
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Introdu un nume!", Toast.LENGTH_SHORT).show();
                        }
                        else if(edtName.getText().length() < 6)
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Introdu un nume de cel putin 6 litere!", Toast.LENGTH_SHORT).show();
                        }
                        else if(edtPassword.getText().length() == 0)
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Introdu o parola!", Toast.LENGTH_SHORT).show();
                        }
                        else if(edtPassword.getText().length() < 6)
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Introdu o parola de cel putin 6 caractere!", Toast.LENGTH_SHORT).show();
                        }
                        else if(edtPhone.getText().length() == 0)
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this, "Introdu un numar de telefon!", Toast.LENGTH_SHORT).show();
                        }
                        else
                            {

                            // Checks if the number is valid
                            try {
                                String phoneNumber = edtPhone.getText().toString();
                                boolean ok = true;
                                if (phoneNumber.length() == 10) {
                                    for (int i = 0; i < 10; i++)
                                        if (phoneNumber.charAt(i) - '0' < 0 || phoneNumber.charAt(i) - '0' > 9) {
                                            ok = false;
                                            break;
                                        }
                                    if (ok)
                                        if (phoneNumber.charAt(0) == '0' && phoneNumber.charAt(1) == '7') // number is valid
                                        {
                                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) // number is already in database
                                            {
                                                mDialog.dismiss();
                                                Toast.makeText(SignUp.this, "Numarul de telefon exista deja!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                mDialog.dismiss();
                                                User user = new User(edtName.getText().toString(), edtPassword.getText().toString());
                                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                                Toast.makeText(SignUp.this, "Inregistrare cu succes!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        } else {
                                            mDialog.dismiss();
                                            Toast.makeText(SignUp.this, "Numarul trebuie sa inceapa cu 07...", Toast.LENGTH_SHORT).show();
                                        }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText(SignUp.this, "Numarul trebuie sa contina doar cifre!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignUp.this, "Numarul trebuie sa aiba 10 cifre!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NullPointerException e) {
                                // Number is not inserted
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Inserati un numar de telefon valabil!", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
        });
    }
}