package com.example.team_7_tcss_450.ui.Contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.utils.RequestMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContact;
    private final MutableLiveData<JSONObject> mResponse;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        mContact = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addContactObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Contact>> observer) {
        mContact.observe(owner, observer);
    }

    private void handleSuccess(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray contacts = result.getJSONArray("contacts");
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                int verified = contact.getInt("verified");
                if(verified == 1){
                    String firstName= contact.getString("firstName");
                    String lastName= contact.getString("lastName");
                    String contNumb= contact.getString("userName");

                    Contact entry = new Contact(firstName, lastName, contNumb);
                    temp.add(entry);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mContact.setValue(temp);
    }

    private void handleError() {

    }
    public void connectGet(String jwt) {
        Log.d("f_connect", "GET CALLED");
        // Generate url for making web service request
        // URL USES HARDCODED latitude and longitude args, REPLACE ASAP
        // FURTHERMORE, This uses our TEST ENDPOINT, REPLACE WITH PRODUCTION ENDPOINT BEFORE SPRINT MEET
        final String url = "";

        final Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
    }
}
