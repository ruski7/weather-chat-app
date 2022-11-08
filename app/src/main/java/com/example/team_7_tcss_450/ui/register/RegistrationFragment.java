package com.example.team_7_tcss_450.ui.register;

import static com.example.team_7_tcss_450.utils.PasswordValidator.*;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentRegistrationBinding;
import com.example.team_7_tcss_450.utils.PasswordValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding mBinding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(4)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"))
            .and(checkPwdSpecialChar("."));


    private PasswordValidator mPasswordValidator =
            checkClientPredicate(pwd -> pwd.equals(mBinding.editPasswordVerify.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRegistrationBinding.inflate(inflater); // Edit 2 added!
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

    }

    // Validation and Registration Method Below

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(mBinding.editFirstName.getText().toString().trim()),
                this::validateLast,
                result -> mBinding.editFirstName.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(mBinding.editLastName.getText().toString().trim()),
                this::validateUserName,
                result -> mBinding.editLastName.setError("Please enter a last name."));
    }

    private void validateUserName() {
        mNameValidator.processResult(
                mNameValidator.apply(mBinding.editLastName.getText().toString().trim()),
                this::validateEmail,
                result -> mBinding.editLastName.setError("Please enter a unique User Name."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mBinding.editEmailAddress.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> mBinding.editEmailAddress.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(mBinding.editPasswordVerify.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(mBinding.editPassword.getText().toString().trim()),
                this::validatePassword,
                result -> mBinding.editPassword.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPasswordValidator.processResult(
                mPasswordValidator.apply(mBinding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> mBinding.editPassword.setError("Please enter a valid Password."));
    }

    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                mBinding.editFirstName.getText().toString(),
                mBinding.editLastName.getText().toString(),
                mBinding.editUserName.getText().toString(),
                mBinding.editEmailAddress.getText().toString(),
                mBinding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    private void navigateToLogin() {
//        RegistrationFragmentDirections.ActionRegistrationFragmentToSignInFragment directions =
//                RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment();
//
//        directions.setEmail(mBinding.editEmailAddress.getText().toString());
//        directions.setPassword(mBinding.editPassword.getText().toString());
//
//        Navigation.findNavController(getView()).navigate(directions);
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mBinding.editEmailAddress.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

}