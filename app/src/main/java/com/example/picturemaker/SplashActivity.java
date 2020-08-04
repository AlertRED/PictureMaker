package com.example.picturemaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.picturemaker.storage.FirebaseDB;
import com.example.picturemaker.storage.Storage;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Storage.getInstance(this);
        FirebaseDB.login(this, this::finish);
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.finish();
    }
}