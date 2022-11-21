package com.example.team_7_tcss_450.ui.account;

import android.app.Application;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.example.team_7_tcss_450.utils.RequestMaker;
import com.google.android.material.switchmaterial.SwitchMaterial;


import java.util.Map;

public class AccountViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> darkMode = new MutableLiveData<Boolean>();
    private final MutableLiveData<SwitchMaterial> darkModeSwitch = new MutableLiveData<SwitchMaterial>();

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    public void selectState(boolean state) {
        darkMode.setValue(state);
    }
    public LiveData<Boolean> getStatus() {
        return darkMode;
    }




}
