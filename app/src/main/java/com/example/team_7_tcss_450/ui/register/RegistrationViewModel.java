package com.example.team_7_tcss_450.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.utils.RequestMaker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrationViewModel extends AndroidViewModel {

    private final MutableLiveData<JSONObject> mResponse;

    public RegistrationViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    public void connect(final Map<String, String> jsonBodyArgs) {
        String url = getApplication().getResources().getString(R.string.base_url_auth) +
                "auth";
        final Request<JSONObject> request = RequestMaker.makeRequest(
                Request.Method.POST,
                url,
                jsonBodyArgs,
                mResponse::setValue,
                error -> RequestMaker.defaultErrorHandler(error, mResponse));
        Volley.newRequestQueue(getApplication().getApplicationContext()).add(request);
    }
}
