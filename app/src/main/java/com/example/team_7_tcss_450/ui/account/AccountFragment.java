package com.example.team_7_tcss_450.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_account, container, false);

        binding = FragmentAccountBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAccountModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        // Toggles Dark Mode on click (changes view model boolean)
        binding.toggleThemeSwitch.setOnClickListener(item -> {
            Log.d("Weather App", "Toggling Dark Mode -> " + binding.toggleThemeSwitch.isChecked());
            mAccountModel.selectState(binding.toggleThemeSwitch.isChecked());
//            mAccountModel.setSwitch(binding.toggleThemeSwitch);
        });

    }

    public AccountFragment(){
        // Required empty public constructor
    }
}