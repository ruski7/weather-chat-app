package com.example.team_7_tcss_450.ui.register;

import static com.example.team_7_tcss_450.utils.PasswordValidator.checkClientPredicate;
import static com.example.team_7_tcss_450.utils.PasswordValidator.checkExcludeWhiteSpace;
import static com.example.team_7_tcss_450.utils.PasswordValidator.checkPwdDigit;
import static com.example.team_7_tcss_450.utils.PasswordValidator.checkPwdLength;
import static com.example.team_7_tcss_450.utils.PasswordValidator.checkPwdLowerCase;
import static com.example.team_7_tcss_450.utils.PasswordValidator.checkPwdSpecialChar;
import static com.example.team_7_tcss_450.utils.PasswordValidator.checkPwdUpperCase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.team_7_tcss_450.databinding.FragmentRegistrationBinding;
import com.example.team_7_tcss_450.utils.PasswordValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private FragmentRegistrationBinding binding;

    private RegistrationViewModel mRegisterModel;

    private final PasswordValidator mNameValidator = checkPwdLength(1);

    private final PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private final PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
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
        mRegisterModel = new ViewModelProvider(requireActivity())
                .get(RegistrationViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistrationBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegisterUser.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLast,
                result -> binding.editFirst.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editLast.setError("Please enter a last name."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editEmail.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editPassword1.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword1.setError("Please enter a valid Password that contains:\n" +
                        "A special character\n" +
                        "A digit\n" +
                        "Is at least 7 characters long\n" +
                        "An uppercase letter\n" +
                        "A lowercase letter"));
    }

    private void verifyAuthWithServer() {
        // Adding in json body args here
        final int argsQuantity = 4;
        final Map<String, String> args = new HashMap<>(argsQuantity);
        args.put("first", binding.editFirst.getText().toString());
        args.put("last", binding.editLast.getText().toString());
        args.put("email", binding.editEmail.getText().toString());
        args.put("password", binding.editPassword1.getText().toString());
        mRegisterModel.connect(args);
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    private void navigateToLogin() {
        RegistrationFragmentDirections.ActionRegistrationFragmentToSignInFragment directions =
                RegistrationFragmentDirections.actionRegistrationFragmentToSignInFragment();
        directions.setEmail(binding.editEmail.getText().toString());
        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(requireView()).navigate(directions);
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
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("REGISTER", "No Response from REGISTER Page");
            Log.d("JSON Response", "No Response");
        }
    }
}