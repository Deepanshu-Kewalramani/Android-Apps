package com.example.fashonistaadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Animation topAnimation , bottomAnimation;
    ImageView appImage;
    TextView appName , appMoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        topAnimation = AnimationUtils.loadAnimation(this , R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this , R.anim.bottom_animation);

        appImage = (ImageView) findViewById(R.id.appLogoImageView);
        appName = (TextView) findViewById(R.id.appNameTextView);
        appMoto = (TextView) findViewById(R.id.appMotoTextView);

        appImage.setAnimation(topAnimation);
        appName.setAnimation(bottomAnimation);
        appMoto.setAnimation(bottomAnimation);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginPageIntent = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(loginPageIntent);
                finish();

            }
        } , 3000);

    }
}