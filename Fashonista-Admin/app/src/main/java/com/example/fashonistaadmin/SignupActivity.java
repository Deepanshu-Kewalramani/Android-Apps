package com.example.fashonistaadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener {

    private FirebaseAuth signInAuth;
    private ImageView imageView;
    private TextView appName , signTextView;
    private ConstraintLayout bakcground_Signup;
    private Button signUpButton;
    private EditText email , pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signInAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.userNameSignup);
        pass = (EditText) findViewById(R.id.PasswordSignUp);
        signUpButton = (Button) findViewById(R.id.signinButton);

        pass.setOnKeyListener(this);
        signUpButton.setOnClickListener(this);

        //Not Important views
        appName = (TextView) findViewById(R.id.textViewSignUp);
        signTextView = (TextView) findViewById(R.id.textView2SignUp);
        imageView = (ImageView) findViewById(R.id.imageViewSignup);
        bakcground_Signup = (ConstraintLayout) findViewById(R.id.background_signUp);

        appName.setOnClickListener(this);
        signTextView.setOnClickListener(this);
        imageView.setOnClickListener(this);
        bakcground_Signup.setOnClickListener(this);

    }

    public void logIn(View view){
        Intent intent = new Intent(getApplicationContext() , MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            view = signUpButton;
            onClick(view);
        }

        return false;
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.textViewSignUp || view.getId() == R.id.textView2SignUp || view.getId() == R.id.imageViewSignup || view.getId() == R.id.background_signUp){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(inputMethodManager.isAcceptingText()){
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
            }
        }else if(view.getId() == R.id.signinButton){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(inputMethodManager.isAcceptingText()){
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
            }

            if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(pass.getText().toString())){
                Toast.makeText(getApplicationContext() , "Email and password are required" , Toast.LENGTH_SHORT).show();
            }else{

                final ProgressDialog nDialog = new ProgressDialog(SignupActivity.this);
                nDialog.setMessage("Loading...");
                nDialog.setTitle("Signing Up");
                nDialog.setCancelable(false);
                nDialog.show();

                String em = email.getText().toString();
                String password = pass.getText().toString();
                signInAuth.createUserWithEmailAndPassword(em , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(SignupActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                        Intent loginActivity = new Intent(getApplicationContext(), MainActivity.class);
                        loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(loginActivity);
                        nDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this , e.getMessage() , Toast.LENGTH_LONG).show();
                        Log.i("account not created" , e.getMessage());
                        nDialog.dismiss();
                    }
                });

            }

        }

    }

}