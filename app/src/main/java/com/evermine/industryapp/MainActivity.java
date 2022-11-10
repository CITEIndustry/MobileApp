package com.evermine.industryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EdgeEffect;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText serverInput;
    private EditText userInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serverInput = findViewById(R.id.serverInput);
        userInput = findViewById(R.id.serverInput);
        passwordInput = findViewById(R.id.passwordInput);


    }
}