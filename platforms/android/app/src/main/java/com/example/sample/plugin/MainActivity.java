package com.example.sample.plugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import io.cordova.hellocordova.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent intent=new Intent(MainActivity.this);
//        startActivity(intent);
        String package_name = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("new_activity", "layout", package_name));
    }
}