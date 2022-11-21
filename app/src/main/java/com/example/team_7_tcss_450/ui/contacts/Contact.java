package com.example.team_7_tcss_450.ui.contacts;

public class Contact {
    private final int imagePath;
    private final String firstName;
    private final String contactNumb;

    public Contact(int imagePath, String firstName, String contactNumb) {
        this.imagePath = imagePath;
        this.firstName = firstName;
        this.contactNumb = contactNumb;
    }

    public int getImagePath() {
        return imagePath;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getContactNumb() {
        return contactNumb;
    }
}
