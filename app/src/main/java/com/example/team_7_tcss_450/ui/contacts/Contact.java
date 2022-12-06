package com.example.team_7_tcss_450.ui.contacts;


public class Contact {

    private final String mUserName;
    private final String mFirstName;
    private final String mLastName;
    private final String mEmail;
    private final int memberID;

    /**
     * Represents a contact.
     * @param userName
     * @param firstName
     * @param lastName
     * @param email
     * @param memID
     */
    public Contact(String userName, String firstName, String lastName, String email, int memID) {
        this.mUserName = userName;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail = email;
        this.memberID = memID;
    }

    /**
     * Gets contact username.
     * @return username.
     */
    public String getUserName() { return mUserName; }

    /**
     * Gets contact first name.
     * @return first name.
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Gets contact last name.
     * @return last name.
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Gets contact email.
     * @return email.
     */
    public String getEmail() { return mEmail; }

    /**
     * Gets contact member id.
     * @return member id.
     */
    public int getMemberID() {
        return memberID;
    }
}

