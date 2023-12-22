package com.example.userb;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class ThirdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Retrieve the login status from the intent
        String loginStatus = getIntent().getStringExtra("LOGIN_STATUS");
        // Use the loginStatus variable to set the text of the TextView
        TextView statusTextView = findViewById(R.id.statusTextView);
        statusTextView.setText(loginStatus);
    }
}
