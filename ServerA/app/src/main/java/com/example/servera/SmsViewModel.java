package com.example.servera;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SmsViewModel extends ViewModel {

    private final MutableLiveData<MessageData> messageLiveData = new MutableLiveData<>();

    public void processSms(String messageBody, String senderPhoneNumber) {
        if (messageBody.contains(",")) {
            // SMS format: name + "," + password
            RegistrationMessage(messageBody);
        } else if (isFourDigitNumber(messageBody)) {
            // SMS format: receivedCode
            VerificationMessage(messageBody, senderPhoneNumber);
        }
    }

    private void RegistrationMessage(String messageBody) {
        // Process the registration message
        MessageData messageData = new MessageData("registration", messageBody);
        messageLiveData.setValue(messageData);
    }

    private void VerificationMessage(String messageBody, String senderPhoneNumber) {
        // Process the verification message
        MessageData messageData = new MessageData("verification", messageBody, senderPhoneNumber);
        messageLiveData.setValue(messageData);
    }

    public MutableLiveData<MessageData> getMessageLiveData() {
        return messageLiveData;
    }

    private boolean isFourDigitNumber(String messageBody) {
        // Implement your logic to check if the messageBody is a four-digit number
        // For example, you can use a regular expression or other validation logic
        return messageBody != null && messageBody.matches("\\d{4}");
    }

    public static class MessageData {

        private final String messageType;
        private final String messageBody;
        private final String senderPhoneNumber;

        public MessageData(String messageType, String messageBody) {
            this.messageType = messageType;
            this.messageBody = messageBody;
            this.senderPhoneNumber = null;
        }

        public MessageData(String messageType, String messageBody, String senderPhoneNumber) {
            this.messageType = messageType;
            this.messageBody = messageBody;
            this.senderPhoneNumber = senderPhoneNumber;
        }

        public String getMessageType() {
            return messageType;
        }

        public String getMessageBody() {
            return messageBody;
        }

        public String getSenderPhoneNumber() {
            return senderPhoneNumber;
        }
    }

}
