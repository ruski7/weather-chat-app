package com.example.team_7_tcss_450.ui.signin;

import android.app.Application;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.example.team_7_tcss_450.io.RequestQueueSingleton;
import com.example.team_7_tcss_450.utils.RequestMaker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> mResponse;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void connect(final String email, final String password) {
        String url = "https://team7-tcss450-project-auth.herokuapp.com/auth";
        Map<String, String> headers = new HashMap<>();
        // add headers <key,value>
        String credentials = email + ":" + password;
        String auth = "Basic "
                + Base64.encodeToString(credentials.getBytes(),
                Base64.NO_WRAP);
        headers.put("Authorization", auth);
        final Request<JSONObject> request = RequestMaker.makeRequest(
                Request.Method.GET,
                url,
                null,
                mResponse::setValue,
                error -> RequestMaker.defaultErrorHandler(error, mResponse),
                headers);
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }


}
