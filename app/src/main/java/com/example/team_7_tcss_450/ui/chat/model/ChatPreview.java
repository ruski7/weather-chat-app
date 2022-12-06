package com.example.team_7_tcss_450.ui.chat.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.time.Instant;

public final class ChatPreview implements Comparable<ChatPreview> {

    private String chatName;
    private int chatId;
    private String latestSender = "";
    private String latestMessage = "";
    private String latestSendDate = "";

    public ChatPreview(final String chatName, final String latestSender, final String latestMessage, final String latestDate, final int chatId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.latestSender = latestSender;
        this.latestMessage = latestMessage;
        this.latestSendDate = latestDate;
    }

    /**
     * Constructor used for a newly created chat room
     * @param chatId
     */
    public ChatPreview(final int chatId, final String chatName) {
        this.chatId = chatId;
        this.chatName = chatName;
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

    @Override
    public int compareTo(ChatPreview chatPreview) {
        return (this.latestSendDate).compareTo(chatPreview.latestSendDate);
    }
}
