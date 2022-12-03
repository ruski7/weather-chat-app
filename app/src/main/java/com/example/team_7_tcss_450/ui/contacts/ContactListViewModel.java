package com.example.team_7_tcss_450.ui.contacts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.team_7_tcss_450.ui.weather.model.DailyForecast;

import java.util.ArrayList;
import java.util.List;

public class ContactListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Contact>> mContactsList;

    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactsList = new MutableLiveData<>();
        mContactsList.setValue(new ArrayList<>());
        Log.d("CONTACT", "Contact List Model Made!");
    }

    public void addContactListObserver(@NonNull LifecycleOwner owner,
                                             @NonNull Observer<? super List<Contact>> observer) {
        mContactsList.observe(owner, observer);
    }
}
