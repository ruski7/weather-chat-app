package com.example.team_7_tcss_450.ui.contacts;

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
import com.example.team_7_tcss_450.ui.chat.model.ChatPreview;
import com.example.team_7_tcss_450.ui.weather.model.DailyForecast;
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

    public ContactListViewModel(@NonNull Application application) {
        super(application);

        mErrorResponse = new MutableLiveData<>();
        mErrorResponse.setValue(new JSONObject());

        mContactsList = new MutableLiveData<>();
        mContactsList.setValue(new ArrayList<>());
        Log.d("CONTACT", "Contact List Model Made!");
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super List<Contact>> observer) {
        mContactsList.observe(owner, observer);
    }

    public void addErrorResponseObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super JSONObject> observer) {
        mErrorResponse.observe(owner, observer);
    }

    public void connectGetContactList(final String jwt) {
        Log.d("c_connect", "GET CALLED");
        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "?email=test@test.test";
        System.out.println(url);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
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

    private void handleResult(final JSONObject result) {
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
                    mContactsList.getValue().add(
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
        mContactsList.setValue(mContactsList.getValue());
    }

    public void connectContactAccept(final String jwt) {
        Log.d("c_connect", "PUT CALLED");
        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "?sender=test@test.test&receiver=test1@test.com";
        System.out.println(url);

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
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


    public void connectAddContact(final String receiverEmail, final String jwt) {
        Log.d("c_connect", "POST CALLED");
        // Generate url for making web service request
        // URL USES HARDCODED email args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = getApplication().getResources().getString(R.string.base_url_contact_service) +
                "?sender=test@test.test&receiver=" + receiverEmail;
        System.out.println(url);
        final String senderEmail = "test@test.test";

        JSONObject body = new JSONObject();
        try {
            body.put("sender", senderEmail);
            body.put("receiver", receiverEmail);
        } catch (JSONException e) {
            Log.e("CHAT MODEL", "Failed to parse chat name on connectAddNewChat()");
        }


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
}
