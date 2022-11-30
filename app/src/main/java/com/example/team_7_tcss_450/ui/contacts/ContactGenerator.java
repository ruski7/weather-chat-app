package com.example.team_7_tcss_450.ui.contacts;

import java.util.Arrays;
import java.util.List;

public class ContactGenerator {
    private static final Contact[] CONTACTS_ARR;
    public static final int COUNT = 20;


    static {
        CONTACTS_ARR = new Contact[COUNT];
        for (int i = 0; i < CONTACTS_ARR.length; i++) {
            CONTACTS_ARR[i] = new Contact("Charles","Bryan","8884442222");
        }
    }

    public static List<Contact> getContactList() {
        return Arrays.asList(CONTACTS_ARR);
    }

    public static Contact[] getCONTACTS() {
        return Arrays.copyOf(CONTACTS_ARR, CONTACTS_ARR.length);
    }

    private ContactGenerator() { }


}

