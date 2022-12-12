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
    private final OnContactInviteListener mOnContactInviteListener;

    public ContactInviteListRecyclerViewAdapter(List<Contact> items, OnContactInviteListener onContactInviteListener) {
        mValues = items;
        mOnContactInviteListener = onContactInviteListener;
    }

    @NonNull
    @Override
    public ContactInviteListRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_invite_card, parent, false);
        return new ContactViewHolder(view, mOnContactInviteListener);
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

        holder.acceptInvite(position);
        holder.rejectInvite(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface OnContactInviteListener {
        void onAcceptInvite(int position);
        void onRejectInvite(int position);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        public OnContactInviteListener onContactInviteListener;
        public Contact mItem;
        public View mView;
        public FragmentContactInviteCardBinding binding;

        public ContactViewHolder(View view, OnContactInviteListener onContactInviteListener) {
            super(view);
            mView = view;
            binding = FragmentContactInviteCardBinding.bind(view);
            this.onContactInviteListener = onContactInviteListener;
        }

        void acceptInvite(final int position) {
            binding.acceptContactButton.setOnClickListener(view -> {
                onContactInviteListener.onAcceptInvite(position);
            });
        }

        void rejectInvite(final int position) {
            binding.rejectContactButton.setOnClickListener(view -> {
                onContactInviteListener.onRejectInvite(position);
            });
        }
    }
}