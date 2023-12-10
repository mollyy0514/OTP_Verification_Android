package com.example.userb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;

public class SecondActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private String receivedCode;  // Variable to store the received code
    private static final int PERMISSION_REQUEST_SEND_SMS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        PinView pinView = findViewById(R.id.pin_view);
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}
                , REQUEST_CODE_ASK_PERMISSIONS);
        SMSContent content = new SMSContent(new Handler(), this, new SMSContent.OnCallback() {
            @Override
            public void callback(String code) {
                pinView.setText(code);
                receivedCode = code;
            }
        });
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/")
                , true, content);

        // Find the button by its ID and set its click listener
        Button sendCodeButton = findViewById(R.id.buttonSendCode);
        sendCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the function to send the code when the button is clicked
                sendCodeToServer();
            }
        });
    }

    private void sendCodeToServer() {
        String phoneNumber = "+1 555-123-4567"; // Replace with the actual phone number of user1
        String message = "Your verification code is: " + receivedCode;

        // Check and request SMS permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_REQUEST_SEND_SMS
            );
        } else {
            // Permission already granted, send SMS
            sendSms(phoneNumber, message);
        }
    }

    private void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "Code sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error sending code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Implement the method to handle button click
    public void onSendCodeButtonClick(View view) {
        // Call the function to send the code when the button is clicked
        sendCodeToServer();
    }
}
