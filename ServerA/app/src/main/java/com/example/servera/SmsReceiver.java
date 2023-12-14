package com.example.servera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    String requestName = "";
    String requestPw = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // Extract SMS messages from the intent
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu : pdus) {

                        SmsMessage smsMessage = getIncomingMessage(pdu, bundle);
                        String messageBody = smsMessage.getMessageBody();
                        String senderPhoneNumber = intent.getStringExtra("senderPhoneNumber");

                        // Determine the type of SMS and broadcast it
                        if (messageBody.contains(",")) {
                            // SMS format: name + "," + password
                            broadcastMessage1(context, "registration", messageBody);
                        } else if (isFourDigitNumber(messageBody)) {
                            // SMS format: receivedCode
                            broadcastMessage2(context, "verification", messageBody, senderPhoneNumber);
                        }
                    }
                }
            }
        }
    }

    private SmsMessage getIncomingMessage(Object pdu, Bundle bundle) {
        SmsMessage currentSMS;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String format = bundle.getString("format");
            currentSMS = SmsMessage.createFromPdu((byte[]) pdu, format);
        } else {
            currentSMS = SmsMessage.createFromPdu((byte[]) pdu);
        }
        return currentSMS;
    }

    private boolean isFourDigitNumber(String input) {
        return input.matches("\\d{4}");
    }

    private void broadcastMessage1(Context context, String messageType, String messageBody) {
        Intent broadcastIntent = new Intent("sms-received");
        broadcastIntent.putExtra("messageType", messageType);
        broadcastIntent.putExtra("messageBody", messageBody);
        context.sendBroadcast(broadcastIntent);
    }
    private void broadcastMessage2(Context context, String messageType, String messageBody, String senderPhoneNumber) {
        Intent broadcastIntent = new Intent("sms-received");
        broadcastIntent.putExtra("messageType", messageType);
        broadcastIntent.putExtra("messageBody", messageBody);
        broadcastIntent.putExtra("senderPhoneNumber", senderPhoneNumber);
        context.sendBroadcast(broadcastIntent);
    }
}
