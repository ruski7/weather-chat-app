package com.example.team_7_tcss_450.ui.contacts;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;
import com.example.team_7_tcss_450.databinding.FragmentContactBinding;
import com.example.team_7_tcss_450.databinding.FragmentContactCardBinding;
import com.example.team_7_tcss_450.databinding.FragmentContactListBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mValues;
    private final FragmentManager mFrag;

    public ContactListRecyclerViewAdapter(List<Contact> items, FragmentManager frag) {
        mValues = items;
        mFrag = frag;
    }

    @NonNull
    @Override
    public ContactListRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactListRecyclerViewAdapter.ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        holder.setContact(mValues.get(position));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public Contact mItem;
        public View mView;
        public FragmentContactCardBinding binding;

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);
        }

        void setContact(final Contact contact) {
            mItem = contact;
            binding.textContactName.setText(contact.getUserName());
            String contactFullName = contact.getFirstName() + " " + contact.getLastName();
            binding.textContactName.setText(contactFullName);
        }
    }
}