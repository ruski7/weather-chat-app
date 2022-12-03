package com.example.team_7_tcss_450.ui.contacts;


public class Contact {

    private final String firstName;
    private final String lastName;
    private final String contactNumber;

    public Contact(String firstName, String lastName, String contactNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactNumber = contactNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }
}

