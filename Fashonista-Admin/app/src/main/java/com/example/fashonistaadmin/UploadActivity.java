package com.example.fashonistaadmin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView dressImage;
    private Button getImageButton , uploadSuitButton;
    private EditText suitName , price , description;
    private TextView nameTextView , priceTextView , descriptionTextView;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private Uri selectedImage;
    private String childName;
    private StorageReference mstorage;  //Declare this variable if using in function other than uploadImage or else app will crash
    private ImageData imageData;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        dressImage = (ImageView) findViewById(R.id.dressImageView);
        getImageButton = (Button) findViewById(R.id.getImageButton);
        suitName = (EditText) findViewById(R.id.suitnameTextView);
        price = (EditText) findViewById(R.id.suitPriceNumberTextView);
        description = (EditText) findViewById(R.id.descriptionTextView);
        uploadSuitButton = (Button) findViewById(R.id.uploadImageButton);

        nameTextView = (TextView) findViewById(R.id.textView8);
        priceTextView = (TextView) findViewById(R.id.textView9);
        descriptionTextView = (TextView) findViewById(R.id.textView10);
        scrollView = (ScrollView) findViewById(R.id.scrollView2);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        dressImage.setOnClickListener(this);
        nameTextView.setOnClickListener(this);
        priceTextView.setOnClickListener(this);
        descriptionTextView.setOnClickListener(this);
        scrollView.setOnClickListener(this);
        linearLayout.setOnClickListener(this);


    }

    public void get_image(View view){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1);
            Toast.makeText(UploadActivity.this , "Change Permission in settings" , Toast.LENGTH_SHORT).show();

        }else{
            getPhoto();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){

            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getPhoto();

            }

        }
    }


    public void getPhoto(){

        Intent getImageIntent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImageIntent , 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){

            selectedImage = data.getData();

            try{

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , selectedImage);
                dressImage.setImageBitmap(bitmap);

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void uploadImage(View view){

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(inputMethodManager.isAcceptingText()){

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);

        }

        final String name = suitName.getText().toString();
        final String suitPrice = price.getText().toString();
        final String suitDescription = description.getText().toString();

        if(selectedImage != null){

            if(TextUtils.isEmpty(name) && TextUtils.isEmpty(suitPrice) && TextUtils.isEmpty(suitDescription)){

                Toast.makeText(this, "Fill the details Properly", Toast.LENGTH_SHORT).show();

            }else{

                imageData = new ImageData();

                childName = name+".jpg";
                mstorage = FirebaseStorage.getInstance().getReference().child(childName);
                final DatabaseReference myReff = FirebaseDatabase.getInstance().getReference();


                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading Image");
                progressDialog.show();

                dressImage.setDrawingCacheEnabled(true);
                dressImage.buildDrawingCache();
                Bitmap bitmap =((BitmapDrawable) dressImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mstorage.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressDialog.dismiss();
                        Task<Uri> downloadUrl = mstorage.getDownloadUrl();
                        downloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageReference = uri.toString();
                                imageData.setClothName(name);
                                imageData.setPrice(suitPrice);
                                imageData.setDescription(suitDescription);
                                imageData.setImageUrl(imageReference);
                                myReff.push().setValue(imageData);
                                Toast.makeText(UploadActivity.this , "Data Uploaded Successfully!" , Toast.LENGTH_SHORT).show();
                                Intent mainPageIntent = new Intent(getApplicationContext() , InformationActivity.class);
                                startActivity(mainPageIntent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, "Unable To upload data Try again later", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(UploadActivity.this, "Failed To upload data\nPlease try again later", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        double progress = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());

                        progressDialog.setMessage("Uploaded " + progress + " %");

                    }
                });

            }

        }else{
            Toast.makeText(this, "Select an Image", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.linearLayout || view.getId() == R.id.textView8 ||  view.getId() == R.id.textView9 || view.getId() == R.id.textView10 || view.getId() == R.id.dressImageView){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            if(inputMethodManager.isAcceptingText()){
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
            }

        }

    }

}