package com.example.ready2eat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ready2eat.Common.Common;
import com.example.ready2eat.Interface.ItemClickListener;
import com.example.ready2eat.Model.Category;
import com.example.ready2eat.ViewHolder.AdminMenuViewHolder;
import com.example.ready2eat.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.UUID;

public class AdminHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    FirebaseDatabase database;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, AdminMenuViewHolder> adapter;

    TextView txtFullName;

    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    //Add new menu layout
    MaterialEditText edtName;
    Button btnUpload, btnSelect;

    DrawerLayout drawer;
    Category newCategory;
    Uri saveUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(    - am inlocuit cu ce e mai jos ca sa functioneze - Laura
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Set name for user
        final View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        //Load Menu
        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);

        loadMenu();
    }

    private void showDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminHome.this);
        alertDialog.setTitle("Adauga o noua categorie");
        alertDialog.setMessage("Va rog adaugati informatiile complete!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        // Event for Buttons
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //Let user select images from Gallery and save URI
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadImage();
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shortcut_playlist_add_24dp);

        //Set button
        alertDialog.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

                if(newCategory != null)
                {
                    category.push().setValue(newCategory);
                    Snackbar.make(drawer,"Categoria " + newCategory.getName() + " a fost adaugata!" , Snackbar.LENGTH_SHORT ).show();
                }
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

    private void uploadImage()
    {
        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Se incarca...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName );
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(AdminHome.this, "Incarcare cu succes!", Toast.LENGTH_SHORT ).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            newCategory = new Category(edtName.getText().toString(),uri.toString() );
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    mDialog.dismiss();
                    Toast.makeText(AdminHome.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Se incarca: " + progress + " %");
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            saveUri = data.getData();
            btnSelect.setText("Imagine selectata!");
        }
    }

    private void chooseImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecteaza imaginea"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, AdminMenuViewHolder>(Category.class, R.layout.menu_item, AdminMenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(AdminMenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.imageView);
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //send Category ID and start new activity
                        Intent adminFoodList = new Intent(AdminHome.this, AdminFoodList.class);
                        adminFoodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(adminFoodList);
                    }
                });
                final Category clickItem = model;

              viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get CategoryId and send to new Activity
                        Intent adminfoodList = new Intent(AdminHome.this, AdminFoodList.class);
                        //Because CategoryId is key, we just get key of this item
                        adminfoodList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(adminfoodList);

                        //Toast.makeText(Home.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        recyclerMenu.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.nav_menu) {
            //handle the camera action
        }
        else if(id == R.id.nav_cart) {
            Intent cartIntent = new Intent(AdminHome.this, Cart.class);
            startActivity(cartIntent);
        }
        else if(id == R.id.nav_orders) {
            Intent orderIntent = new Intent(AdminHome.this, OrderStatus.class);
            startActivity(orderIntent);
        }
        else if(id == R.id.nav_log_out) {
            Intent signIn = new Intent(AdminHome.this, SignIn.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Update / Delete option


    @Override
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
        category.child(key).removeValue();
        Toast.makeText(this, "Categorie stearsa", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final Category item)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AdminHome.this);
        alertDialog.setTitle("Actualizeaza categoria aleasa");
        alertDialog.setMessage("Va rog adaugati informatiile complete!");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_menu_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnSelect = add_menu_layout.findViewById(R.id.btnSelect);
        btnUpload = add_menu_layout.findViewById(R.id.btnUpload);

        // Set default name
        edtName.setText(item.getName());

        // Event for Buttons
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //Let user select images from Gallery and save URI
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.ic_shortcut_playlist_add_24dp);

        //Set button
        alertDialog.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                item.setName(edtName.getText().toString());
                category.child(key).setValue(item);
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
    private void changeImage(final Category item)
    {
        if (saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Se incarca...");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName );
            imageFolder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(AdminHome.this, "Incarcare cu succes!", Toast.LENGTH_SHORT ).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    mDialog.dismiss();
                    Toast.makeText(AdminHome.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot)
                {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Se incarca: " + progress + " %");
                }
            });

        }
    }
}
