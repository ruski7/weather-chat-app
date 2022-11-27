package com.example.team_7_tcss_450.ui.contacts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentHomeBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;

import java.util.LinkedList;
import java.util.List;


public class ContactFragment extends Fragment {
//    private RecyclerView recyclerViewWordList;
//
//    List<Contact> contactList = new LinkedList<Contact>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel userInfoModel = new ViewModelProvider(requireActivity())
                .get(UserInfoViewModel.class);

        FragmentHomeBinding.bind(requireView()).editHome.setText(
                getResources().getString(R.string.home_greeting, userInfoModel.getEmail()));
    }
//    //static accounts
//        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person1", "911-911-9111"));
//        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person2", "000-000-0000"));
//        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person3", "111-111-1111"));
//        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person4", "222-222-2222"));
//        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person5", "333-333-3333"));
//
//        recyclerViewWordList = findViewById(R.id.recyclerViewWordList);
//        recyclerViewWordList.setAdapter(new ContactListAdapter(this, contactList));
//        recyclerViewWordList.setLayoutManager(new LinearLayoutManager(this));
}

