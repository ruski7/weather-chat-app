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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.team_7_tcss_450.ui.weather.model.DailyForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactsList;
    private final MutableLiveData<JSONObject> mResponse;

    /**
     * Contact list view model constructor.
     * @param application
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactsList = new MutableLiveData<>(new ArrayList<>());
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        Log.d("CONTACT", "Contact List Model Made!");
    }

    /**
     * Contact list view model observer.
     * @param owner
     * @param observer
     */
    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super List<Contact>> observer) {
        mContactsList.observe(owner, observer);
    }

    /**
     * Connection to webservice endpoint to get list of contacts.
     * @param jwt
     */
    public void connectGet(String jwt) {
        // insert heroku link for string url
        String url = "";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Verification", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Successful connection to webservice handle.
     * @param result
     */
    private void handleSuccess(final JSONObject result) {
        ArrayList<Contact> temp = new ArrayList<>();
        try {
            JSONArray list = result.getJSONArray("Contacts");
            for (int i = 0; i < list.length(); i++) {
                JSONObject contact = list.getJSONObject(i);
                int verified = contact.getInt("Verified");
                if(verified == 1){
                    String email = contact.getString("Email");
                    String firstName = contact.getString("First Name");
                    String lastName = contact.getString("Last Name");
                    String username = contact.getString("Username");
                    int memberID = contact.getInt("ID");

                    Contact entry = new Contact(email, firstName, lastName, username, memberID);
                    temp.add(entry);
                }
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ContactListViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mContactsList.setValue(temp);
    }

    /**
     * Error connection to webservice handle.
     * @param error
     */
    private void handleError(final VolleyError error) {
        Log.e("ERROR WITH CONNECTION", "NO CONTACTS!");
    }
}
