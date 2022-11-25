package com.example.team_7_tcss_450.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.databinding.FragmentAccountBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private AccountViewModel mAccountModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountModel = new ViewModelProvider(requireActivity())
                .get(AccountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        // Toggles Dark Mode on click (changes view model boolean)
        binding.toggleDarkMode.setOnClickListener(SwitchMaterial -> {
            Log.d("Weather App", "Toggling Dark Mode -> " + binding.toggleDarkMode.isChecked());

            // Sets the State for Toggle that way MainActivity can view and react to changes
            mAccountModel.selectState(binding.toggleDarkMode.isChecked());

            //Boolean.TRUE.equals(mAccountModel.getStatus().getValue())
            if(binding.toggleDarkMode.isChecked()) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d("Weather App", "Fragment Dark Mode -> ON");
            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.d("Weather App", "Fragment Dark Mode -> OFF");
            }
        });

        // Action Listener - Theme Color Change
        binding.toggleTheme.setOnClickListener(SwitchMaterial -> {
            // TODO: Add Theme Actions
        });

    }

    public AccountFragment(){
        // Required empty public constructor
    }
}