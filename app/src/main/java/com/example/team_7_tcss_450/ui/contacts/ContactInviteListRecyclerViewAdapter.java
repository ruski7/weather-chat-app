package com.example.team_7_tcss_450.ui.contacts;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactInviteCardBinding;
import com.example.team_7_tcss_450.ui.contacts.model.Contact;

import java.util.List;

public class ContactInviteListRecyclerViewAdapter extends RecyclerView.Adapter<ContactInviteListRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mValues;

    public ContactInviteListRecyclerViewAdapter(List<Contact> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ContactInviteListRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactInviteListRecyclerViewAdapter.ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_invite_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Contact item = holder.mItem;
        Resources res = holder.mView.getResources();
        final FragmentContactInviteCardBinding binding = holder.binding;
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
        public FragmentContactInviteCardBinding binding;

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactInviteCardBinding.bind(view);
        }
    }
}