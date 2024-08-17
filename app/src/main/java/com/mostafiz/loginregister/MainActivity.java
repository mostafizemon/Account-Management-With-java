package com.mostafiz.loginregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SharedPreferences here
        sharedPreferences = getSharedPreferences("information", MODE_PRIVATE);
        edit = sharedPreferences.edit();

        String email = sharedPreferences.getString("email", "");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (email.length() > 0) {
                    Intent mainIntent = new Intent(MainActivity.this, HomeActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                    MainActivity.this.finish();
                }
            }
        }, 3000);
    }
}
