package com.example.userb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// TODO: send typed id & password to Server(+1 555-123-4567)
// TODO: finish login page UI
// TODO: if receive `valid` message, jump into the page after login

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_SEND_SMS = 1;
    private EditText nameEditText;
    private EditText idEditText;
    private EditText timeEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: if clicks SendButton, it will send the typed text to server(+1 555-123-4567)
        nameEditText = findViewById(R.id.nameEditText);
        idEditText = findViewById(R.id.idEditText);
        timeEditText = findViewById(R.id.timeEditText);
        sendButton = findViewById(R.id.sendButton);

        // Request SMS permission if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS);
            }
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String identity = idEditText.getText().toString();
                String time = timeEditText.getText().toString();

                if (!name.isEmpty() && !identity.isEmpty() && !time.isEmpty()) {
                    sendSMS(name, identity, time);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter name, id, and time",
                            Toast.LENGTH_SHORT).show();
                }

                // Navigate to the second activity
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

    }

    // Send SMS method
    private void sendSMS(String id, String password, String time) {
        String phoneNumber = "+1 555-123-4567";
        SmsManager smsManager = SmsManager.getDefault();
        String fullText = id + ',' + password + ',' + time;
        smsManager.sendTextMessage(phoneNumber, null, fullText, null, null);

        Toast.makeText(this, "Request Sent", Toast.LENGTH_SHORT).show();
    }
}