package com.example.team_7_tcss_450.ui.contacts.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.io.RequestQueueSingleton;
import com.example.team_7_tcss_450.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mErrorResponse;
    private MutableLiveData<List<Contact>> mContactsList;
    private MutableLiveData<List<Contact>> mContactsInviteList;
    private MutableLiveData<List<Contact>> mContactsRequestList;

    private MutableLiveData<Boolean> mContactsListPending;
    private MutableLiveData<Boolean> mContactsRequestListPending;
    private MutableLiveData<Boolean> mContactsInviteListPending;

    public ContactListViewModel(@NonNull Application application) {
        super(application);

        mErrorResponse = new MutableLiveData<>();
        mErrorResponse.setValue(new JSONObject());

        mContactsList = new MutableLiveData<>();
        mContactsList.setValue(new ArrayList<>());
        mContactsListPending = new MutableLiveData<>();
        mContactsListPending.setValue(Boolean.FALSE);

        mContactsRequestList = new MutableLiveData<>();
        mContactsRequestList.setValue(new ArrayList<>());
        mContactsRequestListPending = new MutableLiveData<>();
        mContactsRequestListPending.setValue(Boolean.FALSE);

        mContactsInviteList = new MutableLiveData<>();
        mContactsInviteList.setValue(new ArrayList<>());
        mContactsInviteListPending = new MutableLiveData<>();
        mContactsInviteListPending.setValue(Boolean.FALSE);
        Log.d("CONTACT", "Contact List Model Made!");
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super List<Contact>> observer) {
        mContactsList.observe(owner, observer);
    }

    public void addContactRequestListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mContactsRequestList.observe(owner, observer);
    }

    public void addContactInviteListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<Contact>> observer) {
        mContactsInviteList.observe(owner, observer);
    }


    public void addErrorResponseObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super JSONObject> observer) {
        mErrorResponse.observe(owner, observer);
    }

    // Gets a list of all contacts associated with the users email (verified)
    public void connectGetContactList(final String jwt, final String email) {
        Log.d("c_connect", "GET CALLED");

        mContactsListPending.setValue(Boolean.TRUE);

        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "?email=" + email;
        System.out.println(url);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResultContacts,
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    // Get a list of all __contacts invites__ that have been sent to our user (inbound)
    public void connectGetContactInviteList(final String jwt, final String email) {
        Log.d("c_connect", "GET CALLED");

        mContactsInviteListPending.setValue(Boolean.TRUE);

        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "invites/?email=" + email;
        System.out.println(url);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResultInvite,
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    // Get a list of all __contacts requests__ that our user has received to other users (outbound)
    public void connectGetContactRequestList(final String jwt, final String email) {
        Log.d("c_connect", "GET CALLED");

        mContactsRequestListPending.setValue(Boolean.TRUE);

        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "requests/?email=" + email;
        System.out.println(url);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResultRequest,
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    // Connects to the backend to accepted an invite to the user if such exists
    public void connectAcceptContact(final String jwt, final String senderEmail, final String receiverEmail, int position) {
        Log.d("c_connect", "PUT CALLED");
        // Generate url for making web service request
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service);
        System.out.println(url);

        JSONObject body = new JSONObject();
        try {
            body.put("sender", senderEmail);
            body.put("receiver", receiverEmail);
        } catch (JSONException e) {
            Log.e("CHAT MODEL", "Failed to parse chat name on connectAddNewChat()");
        }

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                result -> handleAccept(result,position),
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    // Doesn't work, not sure why.
    // Tired localhost and live link on POSTMAN, everything checks out, also on direct DB connection.
    // Deletes Requested Contact!
    public void connectDeleteContact(final String jwt, final String emailA, final String emailB, int position) {
        Log.d("c_connect", "DELETE CALLED");
        // Generate url for making web service request
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service);
        System.out.println(url);

        JSONObject body = new JSONObject();
        try {
            body.put("sender", emailA);
            body.put("receiver", emailB);
        } catch (JSONException e) {
            Log.e("CHAT MODEL", "Failed to parse chat name on connectDeleteContact()");
        }

        System.out.println(body);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                body,
                result -> handleDelete(result, position),
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    // Connects to the user to send a pending contact invite to another user
    public void connectAddContact(final String jwt, final String senderEmail,  final String receiverEmail) {
        Log.d("c_connect", "POST CALLED");
        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "?sender" + senderEmail +"&receiver=" + receiverEmail;
        System.out.println(url);

        JSONObject body = new JSONObject();
        try {
            body.put("sender", senderEmail);
            body.put("receiver", receiverEmail);
        } catch (JSONException e) {
            Log.e("CHAT MODEL", "Failed to parse chat name on connectAddNewChat()");
        }

        System.out.println(body);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                null,
                error -> RequestMaker.defaultErrorHandler(error, mErrorResponse)
        ) {
            // Add user JWT token into request header
            @Override
            public Map<String, String> getHeaders() {
                final Map<String, String> headers = new HashMap<>(1);
                headers.put("authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    // Handles adding items into the contacts list, not to be mixed up with the pending and invite list
    private void handleResultContacts(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            final String ContactKey = getString.apply(R.string.keys_json_contact_list);
            //Try to grab weekly forecast JSON object list
            if (result.has(ContactKey)) {
                JSONArray JSON_Contact_List = result.getJSONArray(ContactKey);
                for (int i = 0; i < JSON_Contact_List.length(); i++) {
                    JSONObject JSON_contact = JSON_Contact_List.getJSONObject(i);

                    //Get args for new Contact object
                    final String userName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_user_name));
                    final String firstName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_first_name));
                    final String lastName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_last_name));
                    final String email = JSON_contact.getString(getString.apply(R.string.keys_json_contact_email));
                    final int memID = JSON_contact.getInt(getString.apply(R.string.keys_json_contact_member_id));

                    Contact newContact = new Contact(
                            userName,
                            firstName,
                            lastName,
                            email,
                            memID
                    );

                    Objects.requireNonNull(mContactsList.getValue()).add(newContact);

                    System.out.println(JSON_Contact_List.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", "No response");
        }
        mContactsList.setValue(mContactsList.getValue());
        mContactsListPending.setValue(Boolean.FALSE);
    }

    // Handles adding items into the pending list, not to be mixed up with the contacts and invite list
    private void handleResultRequest(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            final String ContactKey = getString.apply(R.string.keys_json_contact_list);
            //Try to grab weekly forecast JSON object list
            if (result.has(ContactKey)) {
                JSONArray JSON_Contact_List = result.getJSONArray(ContactKey);
                for (int i = 0; i < JSON_Contact_List.length(); i++) {
                    JSONObject JSON_contact = JSON_Contact_List.getJSONObject(i);

                    //Get args for new Contact object
                    final String userName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_user_name));
                    final String firstName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_first_name));
                    final String lastName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_last_name));
                    final String email = JSON_contact.getString(getString.apply(R.string.keys_json_contact_email));
                    final int memID = JSON_contact.getInt(getString.apply(R.string.keys_json_contact_member_id));

                    Contact newContact = new Contact(
                            userName,
                            firstName,
                            lastName,
                            email,
                            memID
                    );

                    Objects.requireNonNull(mContactsRequestList.getValue()).add(newContact);

                    System.out.println(JSON_Contact_List.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", "No response");
        }
        mContactsRequestList.setValue(mContactsRequestList.getValue());
        mContactsRequestListPending.setValue(Boolean.FALSE);
    }

    // Handles adding items into the sent list, not to be mixed up with the contacts and invite list
    private void handleResultInvite(final JSONObject result) {
        IntFunction<String> getString = getApplication().getResources()::getString;
        try {
            final String ContactKey = getString.apply(R.string.keys_json_contact_list);
            //Try to grab weekly forecast JSON object list
            if (result.has(ContactKey)) {
                JSONArray JSON_Contact_List = result.getJSONArray(ContactKey);
                for (int i = 0; i < JSON_Contact_List.length(); i++) {
                    JSONObject JSON_contact = JSON_Contact_List.getJSONObject(i);

                    //Get args for new Contact object
                    final String userName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_user_name));
                    final String firstName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_first_name));
                    final String lastName = JSON_contact.getString(getString.apply(R.string.keys_json_contact_last_name));
                    final String email = JSON_contact.getString(getString.apply(R.string.keys_json_contact_email));
                    final int memID = JSON_contact.getInt(getString.apply(R.string.keys_json_contact_member_id));

                    //Add new Contact object to add into contact list
                    mContactsInviteList.getValue().add(
                            new Contact(
                                    userName,
                                    firstName,
                                    lastName,
                                    email,
                                    memID
                            )
                    );

                    System.out.println(JSON_Contact_List.get(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", "No response");
        }
        mContactsInviteList.setValue(mContactsInviteList.getValue());

        mContactsInviteListPending.setValue(Boolean.FALSE);
    }

    // Removes Contact from invites list, also adds into verified contact
    private void handleAccept(final JSONObject result, int position){
        Objects.requireNonNull(mContactsList.getValue()).add(Objects.requireNonNull(mContactsInviteList.getValue()).get(position));
        mContactsInviteList.getValue().remove(position);

        mContactsInviteList.setValue(mContactsInviteList.getValue());
        mContactsList.setValue(mContactsList.getValue());
    }

    // Removes Contact from invites list, also adds into verified contact
    private void handleDelete(final JSONObject result, int position){
        Objects.requireNonNull(mContactsInviteList.getValue()).remove(position);
        mContactsInviteList.setValue(mContactsInviteList.getValue());
        mContactsList.setValue(mContactsList.getValue());
    }






    // Helper Methods....

    public Boolean getContactsStatus(){
        return mContactsListPending.getValue();
    }

    public Boolean getInviteStatus(){
        return mContactsInviteListPending.getValue();
    }

    public Boolean getRequestStatus(){
        return mContactsRequestListPending.getValue();
    }



    public boolean isEmptyContactsList() {
        return Objects.requireNonNull(mContactsList.getValue()).size() == 0;
    }

    public boolean isEmptyInviteList() {
        return Objects.requireNonNull(mContactsInviteList.getValue()).size() == 0;
    }

    public boolean isEmptyRequestList() {
        return Objects.requireNonNull(mContactsRequestList.getValue()).size() == 0;
    }



    public List<Contact> getRequestList() {
        return mContactsRequestList.getValue();
    }

    public List<Contact> getInviteList() {
        return mContactsInviteList.getValue();
    }

    public List<Contact> getContactList() {
        return mContactsList.getValue();
    }



}
