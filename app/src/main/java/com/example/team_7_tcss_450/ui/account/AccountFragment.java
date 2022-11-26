package com.example.team_7_tcss_450.ui.account;

import android.app.Activity;
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

import com.example.team_7_tcss_450.MainActivity;
import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentAccountBinding;
import com.example.team_7_tcss_450.databinding.FragmentRegistrationBinding;
import com.example.team_7_tcss_450.ui.signin.SignInViewModel;
import com.example.team_7_tcss_450.utils.Utils;

import java.util.Objects;

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

            //Boolean.TRUE.equals(mAccountModel.getStatus().getValue())
            if(binding.toggleDarkMode.isChecked()) {
                Log.d("Weather App", "Dark Mode (Switch) -> ON");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            } else {
                Log.d("Weather App", "Dark Mode (Switch) -> OFF");
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        });

        // Action Listener - Theme Color Change
        binding.toggleTheme.setOnClickListener(SwitchMaterial -> {
            //Boolean.TRUE.equals(mAccountModel.getStatus().getValue())
            if(binding.toggleTheme.isChecked()) {
                Log.d("Weather App", "Theme (Switch) -> ON");
//                new ViewModelProvider(requireActivity()).get(SignInViewModel.class);
//                Utils.changeToTheme((Activity) getActivity(),Utils.THEME_GREEN);
                getActivity().setTheme(R.style.Theme_ForestGreen);
                mAccountModel.selectState(true);
            } else {
                Log.d("Weather App", "Theme (Switch) -> OFF");
//                  Utils.changeToTheme((Activity) getActivity(),Utils.THEME_BLUE);
                    mAccountModel.selectState(false);
            }

        });

    }

    public AccountFragment(){
        // Required empty public constructor
    }
}