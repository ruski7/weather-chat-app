package com.example.team_7_tcss_450.ui.signin;

import static com.example.team_7_tcss_450.utils.PasswordValidator.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentSignInBinding;
import com.example.team_7_tcss_450.utils.PasswordValidator;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding mBinding;

    private SignInViewModel mSignInModel;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        mBinding = FragmentSignInBinding.bind(requireView());

        //On button click, navigate to MainActivity
        mBinding.buttonSignIn.setOnClickListener(button -> {
            //TODO: Add code for Authentication
            onSuccessfulSignIn();
        });

        //On button click, navigate to MainActivity
        mBinding.buttonToRegister.setOnClickListener(button -> {
            //TODO: Add code for Authentication
            Navigation.findNavController(requireView()).navigate(
                    SignInFragmentDirections
                            .actionSignInFragmentToRegistrationFragment()
            );
        });
    }


    public void onSuccessfulSignIn(){

        Navigation.findNavController(requireView()).navigate(
                SignInFragmentDirections
                        .actionSignInFragmentToMainActivity()
        );
    }


}