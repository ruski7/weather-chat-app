package com.example.team_7_tcss_450.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentSignInBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }

    private FragmentSignInBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onSuccessfulSignIn(){

        Navigation.findNavController(requireView()).navigate(
                SignInFragmentDirections
                        .actionSignInFragmentToMainActivity()
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object.
        mBinding = FragmentSignInBinding.bind(requireView());

        //On button click, navigate to MainActivity
        mBinding.buttonSignIn.setOnClickListener(button -> {
            //TODO: Add code for Authentication
            onSuccessfulSignIn();
        });

        //On button click, navigate to MainActivity
        mBinding.buttonRegister.setOnClickListener(button -> {
            //TODO: Add code for Authentication
            Navigation.findNavController(requireView()).navigate(
                    SignInFragmentDirections
                            .actionSignInFragmentToRegistrationFragment()
            );
        });
    }


}