package com.example.team_7_tcss_450.ui.contacts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactListBinding;
import com.example.team_7_tcss_450.databinding.FragmentContactPendingListBinding;
import com.example.team_7_tcss_450.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactPendingListFragment extends Fragment {

    private ContactListViewModel mContactListModel;
    private UserInfoViewModel mUserModel;
    private FragmentContactPendingListBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactPendingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("fragment_made", "OnCreate Called in ContactPendingListFragment");
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(requireActivity());
        mUserModel = provider.get(UserInfoViewModel.class);

        mContactListModel = new ViewModelProvider(requireActivity()).get(ContactListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactPendingListBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactPendingListBinding binding = FragmentContactPendingListBinding.bind(requireView());

        // Set the adapter
        Context context = view.getContext();

        binding.contactPendingList.setLayoutManager(new LinearLayoutManager(context));

        final RecyclerView rv = binding.contactPendingList;
        mContactListModel.addContactListObserver(getViewLifecycleOwner(), (contactsPendingList) -> {
            // while we do observe the contact list from ContactListViewModel,
            // we currently just spawn a list of generated contacts from ContactGenerator.
            // -- replace generated placeholder contacts with real contacts list
//            binding.contactList.setAdapter(new ContactListRecyclerViewAdapter(ContactGenerator.getContactList()));

            // TODO: fix bug when contactList is Empty, there is endless GET calls (only resolved when there is at least one verified contact)
            rv.setAdapter(new ContactListRecyclerViewAdapter(contactsPendingList));
            if (contactsPendingList.isEmpty()) {
                mContactListModel.connectGetContactList(mUserModel.getJWT());}
        });

        // Add "add new contact" icon to top menu bar
        MenuHost menuHost = requireActivity();
        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.contacts_action_menu, menu);
            }
            // Handle the menu selection
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_contact_send_invite: {
                        // TODO: Implement add new contact XML sheet and navigation to said sheet
                        //showAddContactDialog();
                        return true;
                    }
                    case R.id.action_contact_sent_invites: {
                        // TODO: Implement add new contact XML=gation to said sheet
                        System.out.println("sent invites ->");
                        return true;
                    }
                    case R.id.action_contact_pending_invites: {
                        // TODO: Implement add new contact XML sheet and navigation to said sheet
                        System.out.println("pending invites");
                        return true;
                    }

                    default:
                        return false;
                }
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

}