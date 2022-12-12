package com.example.team_7_tcss_450.ui.contacts.model;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactBinding;
import com.example.team_7_tcss_450.databinding.FragmentContactListBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    private UserInfoViewModel mUserModel;
    private ContactListViewModel mContactListModel;

    private String email;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mContactListModel = new ViewModelProvider(requireActivity()).get(ContactListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ContactFragmentArgs args = ContactFragmentArgs.fromBundle(getArguments());
        FragmentContactBinding binding = FragmentContactBinding.bind(getView());

        final String fullName = args.getContact().getFirstName() + " " + args.getContact().getLastName();
        final String userName = args.getContact().getUserName();
        email = args.getContact().getEmail(); // nasty workaround but we rushing lol
        final String emailLabel = "Email: " + email;
        final int memberID = args.getContact().getMemberID();
        final String memberID_String = "MemberID: " + memberID;


        System.out.println(fullName);
        binding.contactFullName.setText(fullName);
        binding.contactUserName.setText(userName);
        binding.contactEmail.setText(emailLabel);
        binding.contactMemberID.setText(memberID_String);

        binding.deleteContactButton.setOnClickListener(this::deleteContact);
    }

    private void deleteContact(View view) {
        mContactListModel.connectDeleteContact(mUserModel.getJWT(), mUserModel.getEmail(), email);
    }

}