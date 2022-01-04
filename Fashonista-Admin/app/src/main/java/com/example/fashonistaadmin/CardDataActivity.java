package com.example.fashonistaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class CardDataActivity extends AppCompatActivity {


    ImageView clothImage;
    TextView name , priceTextView , descriptionTextView;
    DatabaseReference mReff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_data);

        Intent intent = getIntent();
        String nodeName = intent.getStringExtra("name");

        mReff = FirebaseDatabase.getInstance().getReference().child(nodeName);

        clothImage = (ImageView) findViewById(R.id.productImage);
        name = (TextView) findViewById(R.id.productName);
        priceTextView = (TextView) findViewById(R.id.productPrice);
        descriptionTextView = (TextView) findViewById(R.id.productDescription);

        mReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String clothName , price , description , imageUrl;

                clothName = snapshot.child("clothName").getValue(String.class);
                price = snapshot.child("price").getValue(String.class);
                description = snapshot.child("description").getValue(String.class);
                imageUrl = snapshot.child("imageUrl").getValue(String.class);

                name.setText(clothName);
                priceTextView.setText(price);
                descriptionTextView.setText(description);
                Picasso.get().load(imageUrl).into(clothImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}