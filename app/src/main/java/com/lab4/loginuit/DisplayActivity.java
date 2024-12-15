package com.lab4.loginuit;

import static com.lab4.loginuit.R.layout.activity_display;
import static com.lab4.loginuit.R.layout.activity_login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity  extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_display);
        Intent intent = getIntent();
        String username=intent.getStringExtra("username");
        TextView hello=findViewById(R.id.texthello);
        hello.setText("Hello "+username);
    }
}
