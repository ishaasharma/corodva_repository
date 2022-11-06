package com.example.sample.plugin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import io.cordova.hellocordova.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String package_name = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("new_activity", "layout", package_name));
    }
}