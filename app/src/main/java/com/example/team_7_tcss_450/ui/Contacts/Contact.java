package com.example.team_7_tcss_450.ui.Contacts;

public class Contact {

    private final String firstName;
    private final String lastName;
    private final String contactNumb;

    public Contact(String firstName, String lastName, String contactNumb) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumb = contactNumb;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumb() {
        return contactNumb;
    }
}

