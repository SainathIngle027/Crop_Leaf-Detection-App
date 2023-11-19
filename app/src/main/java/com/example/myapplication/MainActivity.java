package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    private Button navigateButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }


        navigateButton = findViewById(R.id.navigateButton);

        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImgpage();
            }
        });
    }

    private void openImgpage() {
        Intent intent = new Intent(this, Imgpage.class);
        startActivity(intent);
    }

}