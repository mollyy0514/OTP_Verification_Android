package com.example.servera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

// TODO: add database.json
// TODO: check whether the user info exist, if exists, trigger RandomNumberGenerator(), and send it to the user phone number
// TODO: add RandomNumberGenerator() which return a `OTPcode`
// TODO: add `ReceiveSMS()` to get `ReceivedCode`
// TODO: if `ReceivedCode` == `OTPcode`, send `valid` message back to the user

public class MainActivity extends AppCompatActivity {

    final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final int PERMISSION_SEND_SMS = 1;
    private static final int PERMISSION_RECEIVE_SMS = 2;
    private DatabaseHelper databaseHelper;
    private TextView receivedSmsTextView;
    String sentCode;
    String receivedInfo;
    String phoneNumber;

    private SmsViewModel smsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_SMS"}
                , REQUEST_CODE_ASK_PERMISSIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
                        PERMISSION_RECEIVE_SMS);
            }
        }

        databaseHelper = new DatabaseHelper(this);

        Toast.makeText(this, "open!", Toast.LENGTH_LONG).show();
        SMSContent content = new SMSContent(new Handler(), this, new SMSContent.OnCallback() {
            @Override
            public void callback(String info) {
                receivedInfo = info;
                if (info.contains(",")) {
                    // SMS format: name + "," + password
                    handleRegistrationMessage(info);
                } else if (isFourDigitNumber(info)) {
                    // SMS format: receivedCode
                    handleVerificationMessage(info);
                }
            }
        });
        this.getContentResolver().registerContentObserver(Uri.parse("content://sms/")
                , true, content);
    }

    private void handleRegistrationMessage(String messageBody) {
        // Extract name and password from the message body
        String[] credentials = messageBody.split(",");
        if (credentials.length == 2) {
            String name = credentials[0].trim();
            String password = credentials[1].trim();

            // Update TextViews with name and password
            TextView nameTextView = findViewById(R.id.nameTextView);
            TextView passwordTextView = findViewById(R.id.passwordTextView);

            nameTextView.setText("Name: " + name);
            passwordTextView.setText("Password: " + password);

            // Check the name and password against the database
            phoneNumber = databaseHelper.getPhoneNumber(name, password);

            if (phoneNumber != null) {
                // Valid registration, send a verification code to the user
                sentCode = generateRandomCode();
                sendVerificationCode(sentCode);
                TextView codeTextView = findViewById(R.id.codeTextView);
                codeTextView.setText("Verification Code: " + sentCode);
            } else {
                // Invalid registration, handle accordingly
                // For example, display an error message or take other actions
                // ...
                sendVerificationCode("0000");
            }
        }
    }

    private void handleVerificationMessage(String messageBody) {
        // Implement actions for verification message
        // Extract the receivedCode from messageBody, compare with the sent code, and so on...
        if (messageBody.equals(sentCode)) {
            sendValid();
        }
        else {
            sendInvalid();
        }
    }

    private String generateRandomCode() {
        // 生成隨機驗證碼
        return String.valueOf((int) (Math.random() * 9000) + 1000);
    }

    private void sendVerificationCode(String verificationCode) {
        // 使用 SmsManager 來發送回覆簡訊
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        String message = "Your verification code is: " + verificationCode;
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private boolean isFourDigitNumber(String input) {
        return input.matches("\\d{4}");
    }

    private void sendValid() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "valid", null, null);
        Toast.makeText(this, "Send Valid", Toast.LENGTH_SHORT).show();
    }

    private void sendInvalid() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "Wrong Verification Code", null, null);
        Toast.makeText(this, "Send Invalid", Toast.LENGTH_SHORT).show();
    }
}