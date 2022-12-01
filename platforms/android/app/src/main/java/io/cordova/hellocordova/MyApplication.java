package io.cordova.hellocordova;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}