package com.example.buspass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation topanim,botanim;
    ImageView busLogo;
    TextView busHeading,busText;

    private static int SPLASH_SCREEN=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botanim= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        busLogo=findViewById(R.id.busLogo);
        busHeading=findViewById(R.id.busHeading);
        busText=findViewById(R.id.busText);

        busLogo.setAnimation(topanim);
        busHeading.setAnimation(botanim);
        busText.setAnimation(botanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent =new Intent(MainActivity.this,UserselectActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}