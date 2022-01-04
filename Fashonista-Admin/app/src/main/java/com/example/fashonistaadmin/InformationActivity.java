package com.example.fashonistaadmin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class InformationActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mReff;
    RecyclerView recyclerView;
    ProductAdapter adapter;
    ArrayList<String> nodeNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        nodeNames = new ArrayList<String>();

        Handler handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(InformationActivity.this);
        progressDialog.setTitle("Loading Clothes");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        } ,4000);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        mReff = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Product> product = new FirebaseRecyclerOptions.Builder<Product>().setQuery(mReff , Product.class).build();
        adapter = new ProductAdapter(product);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_options, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){

            case R.id.upload:{

                Intent uploadActivityIntent = new Intent(getApplicationContext() , UploadActivity.class);
                startActivity(uploadActivityIntent);
                return true;
            }


            case R.id.delete: {

                AlertDialog.Builder alertDialogueBox = new AlertDialog.Builder(InformationActivity.this);
                alertDialogueBox.setTitle("Warning!");
                alertDialogueBox.setMessage("Are you sure you want to delete your account?");
                alertDialogueBox.setCancelable(false);
                alertDialogueBox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.delete();
                        Intent signupIntent = new Intent(getApplicationContext() , SignupActivity.class);
                        startActivity(signupIntent);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogueBox.create();
                alertDialog.show();

                return true;
            }

            case R.id.signout: {

                mAuth.signOut();
                Intent loginPageintent = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(loginPageintent);
                return true;
            }

            default: return false;
        }

    }

    public void onCardClick(View view){

        final int i = (int) view.getTag();

        final Intent cardDataintent = new Intent(getApplicationContext() , CardDataActivity.class);

        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                nodeNames.clear();
                for(DataSnapshot ds : snapshot.getChildren()){

                    String name = ds.getKey();
                    nodeNames.add(name);
                }

                String nodeName = nodeNames.get(i);

                cardDataintent.putExtra("name" , nodeName);
                startActivity(cardDataintent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void uploadButton(View view){

        Intent uploadActivityIntent = new Intent(getApplicationContext() , UploadActivity.class);
        startActivity(uploadActivityIntent);

    }
}