    package com.example.fashonistaadmin;

    import android.app.AlertDialog;
    import android.app.ProgressDialog;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.net.ConnectivityManager;
    import android.net.NetworkInfo;
    import android.os.Bundle;
    import android.provider.Settings;
    import android.text.TextUtils;
    import android.view.KeyEvent;
    import android.view.View;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.constraintlayout.widget.ConstraintLayout;

    public class MainActivity extends AppCompatActivity implements View.OnClickListener , View.OnKeyListener {

        private TextView loginText , appNameText;
        private EditText userName , password;
        private Button loginButton;
        private ImageView imageView;
        private ConstraintLayout background;
        private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = (EditText) findViewById(R.id.userNameSignup);
        password = (EditText) findViewById(R.id.PasswordSignUp);
        loginButton = (Button) findViewById(R.id.signinButton);
        loginButton.setOnClickListener(this);
        password.setOnKeyListener(this);

        //not so important Views
        loginText = (TextView) findViewById(R.id.textView2SignUp);
        appNameText = (TextView) findViewById(R.id.textViewSignUp);
        imageView = (ImageView) findViewById(R.id.imageViewSignup);
        background = (ConstraintLayout) findViewById(R.id.background);

        loginText.setOnClickListener(this);
        appNameText.setOnClickListener(this);
        imageView.setOnClickListener(this);
        background.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }


        @Override
        protected void onStart() {
            super.onStart();
            isConnected(MainActivity.this);
        }

        public boolean isConnected(Context context){

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo internetInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_ETHERNET);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

            if(internetInfo != null && internetInfo.isConnected() || mobileInfo != null && mobileInfo.isConnected() || wifiInfo != null && wifiInfo.isConnected()){
                return true;
            }else{
                showDialogueBox();
                return false;
            }

        }

        private void showDialogueBox(){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Connect to Internet")
                    .setTitle("Internet Connectivity")
                    .setCancelable(false)
                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }

    public void Signup(View view){
        Intent intent = new Intent(getApplicationContext() , SignupActivity.class);
        startActivity(intent);
    }

        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){

            view = loginButton;
            onClick(view);

        }

            return false;
        }

        @Override
        public void onClick(View view) {

            if(view.getId() == R.id.textViewSignUp || view.getId() == R.id.textView2SignUp || view.getId() == R.id.imageViewSignup || view.getId() == R.id.background){

                InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager.isAcceptingText()){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
                }
            }else if (view.getId() == R.id.signinButton){


                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(inputMethodManager.isAcceptingText()){
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken() , 0);
                }

                if(TextUtils.isEmpty(userName.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(this , "Email and Password are required" , Toast.LENGTH_SHORT).show();
                }else{

                    final ProgressDialog nDialog = new ProgressDialog(MainActivity.this);
                    nDialog.setMessage("Loading...");
                    nDialog.setTitle("Logging in");
                    nDialog.setCancelable(false);
                    nDialog.show();

                    String email = userName.getText().toString();
                    String pass = password.getText().toString();

                    mAuth.signInWithEmailAndPassword(email , pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Intent buyPageIntent = new Intent(getApplicationContext() , InformationActivity.class);
                                buyPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(buyPageIntent);
                                nDialog.dismiss();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this , e.getMessage() , Toast.LENGTH_SHORT).show();
                            nDialog.dismiss();
                        }
                    });
                }
            }

        }

    }