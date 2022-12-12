package com.example.team_7_tcss_450.ui.contacts;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactInviteCardBinding;
import com.example.team_7_tcss_450.databinding.FragmentContactRequestCardBinding;
import com.example.team_7_tcss_450.ui.contacts.model.Contact;

import java.util.List;

public class ContactRequestListRecyclerViewAdapter extends RecyclerView.Adapter<ContactRequestListRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mValues;
    private final String mUserEmail;
    private final ContactRequestListRecyclerViewAdapter.OnContactRequestListener mOnContactRequestListener;

    public ContactRequestListRecyclerViewAdapter(List<Contact> items, String email, ContactRequestListRecyclerViewAdapter.OnContactRequestListener onContactRequestListener) {
        mValues = items;
        mUserEmail = email;
        mOnContactRequestListener = onContactRequestListener;
    }

    @NonNull
    @Override
    public ContactRequestListRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_request_card, parent, false);
        return new ContactRequestListRecyclerViewAdapter.ContactViewHolder(view, mOnContactRequestListener);
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

        holder.onCancelRequest(position);
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface OnContactRequestListener {
        void cancelRequest(int position);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        public Contact mItem;
        public View mView;
        public FragmentContactRequestCardBinding binding;
        public ContactRequestListRecyclerViewAdapter.OnContactRequestListener onContactRequestListener;

        public ContactViewHolder(View view, ContactRequestListRecyclerViewAdapter.OnContactRequestListener onContactRequestListener) {
            super(view);
            mView = view;
            binding = FragmentContactRequestCardBinding.bind(view);
            this.onContactRequestListener = onContactRequestListener;
        }

        void onCancelRequest(final int position) {
            binding.cancelContactButton.setOnClickListener(view -> {
                onContactRequestListener.cancelRequest(position);
            });
        }

    }
}