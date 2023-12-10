package com.example.servera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// TODO: add database.json
// TODO: check whether the user info exist, if exists, trigger RandomNumberGenerator(), and send it to the user phone number
// TODO: add RandomNumberGenerator() which return a `OTPcode`
// TODO: add `ReceiveSMS()` to get `ReceivedCode`
// TODO: if `ReceivedCode` == `OTPcode`, send `valid` message back to the user

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}