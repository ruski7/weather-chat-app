package com.example.team_7_tcss_450.ui.chat.model;

import java.time.LocalDateTime;
import java.util.Date;

public final class ChatPreview {

    private String chatName;
    private int chatId;
    private String latestSender;
    private String latestMessage;
    private String latestSendDate;

    public ChatPreview(final String chatName, final String latestSender, final String latestMessage, final String latestDate, final int chatId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.latestSender = latestSender;
        this.latestMessage = latestMessage;
        this.latestSendDate = latestDate;
    }

    public String getChatName() {
        return chatName;
    }

    public int getChatId() {
        return chatId;
    }

    public String getLatestSender() {
        return latestSender;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public String getLatestSendDate() {
        return latestSendDate;
    }

    public void setLatestSender(final String sender) {
        this.latestSender = sender;
    }

    private void setLatestSendDate(final String date) {
        this.latestSendDate = date;
    }

    public void setLatestMessage(final String message) {
        this.latestMessage = message;
    }

}
