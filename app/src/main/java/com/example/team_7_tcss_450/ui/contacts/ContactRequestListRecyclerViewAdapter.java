package com.example.team_7_tcss_450.ui.contacts;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactRequestCardBinding;
import com.example.team_7_tcss_450.ui.contacts.model.Contact;

import java.util.List;

public class ContactRequestListRecyclerViewAdapter extends RecyclerView.Adapter<ContactRequestListRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mValues;
    private final String mUserEmail;

    public ContactRequestListRecyclerViewAdapter(List<Contact> items, String email) {
        mValues = items;
        mUserEmail = email;
    }

    @NonNull
    @Override
    public ContactRequestListRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactRequestListRecyclerViewAdapter.ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Contact item = holder.mItem;
        Resources res = holder.mView.getResources();
        final FragmentContactRequestCardBinding binding = holder.binding;
        final String fullName = item.getFirstName() + " " + item.getLastName();

        binding.textViewContactName.setText(fullName);
        binding.textViewContactUsername.setText(item.getUserName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        public Contact mItem;
        public View mView;
        public FragmentContactRequestCardBinding binding;

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactRequestCardBinding.bind(view);
        }

    }
}