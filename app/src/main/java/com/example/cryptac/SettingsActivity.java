package com.example.cryptac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    BottomNavigationView bottom_navigation;
    private Button about;
    private Button logout;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences(verifyOTPActivity.PREFS_NAME,0);
        String mobile = sharedPreferences.getString("mobile","");
        txt = findViewById(R.id.mobile);
        txt.setText("+91 "+mobile);

        //logout code
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(verifyOTPActivity.PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("hasLoggedIn",false);
                editor.putString("mobile","");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), SendOTPActivity.class);
                startActivity(intent);
            }
        });

        //about us code
        about = findViewById(R.id.aboutUs);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setSelectedItemId(R.id.page_3);
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.page_1:
                        startActivity(new Intent(getApplicationContext(), CalculateActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_2:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.page_3:
                        return true;
                }
                return false;
            }
        });
    }
}