package com.example.team_7_tcss_450.ui.contacts;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactCardBinding;
import com.example.team_7_tcss_450.databinding.FragmentContactPendingCardBinding;

import java.util.List;

public class ContactPendingListRecyclerViewAdapter extends RecyclerView.Adapter<ContactPendingListRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mValues;

    public ContactPendingListRecyclerViewAdapter(List<Contact> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ContactPendingListRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactPendingListRecyclerViewAdapter.ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_pending_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        Contact item = holder.mItem;
        Resources res = holder.mView.getResources();
        final FragmentContactPendingCardBinding binding = holder.binding;
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
        public FragmentContactPendingCardBinding binding;

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactPendingCardBinding.bind(view);
        }
    }
}