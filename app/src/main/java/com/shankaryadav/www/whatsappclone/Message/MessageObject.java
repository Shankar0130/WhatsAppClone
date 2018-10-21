package com.shankaryadav.www.whatsappclone.Message;

class MessageObject {
    String messageId, mText, senderId;

    public MessageObject(String messageId, String mText, String senderId) {
        this.messageId = messageId;
        this.mText = mText;
        this.senderId = senderId;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getmText() {
        return mText;
    }

    public String getSenderId() {
        return senderId;
    }
}
