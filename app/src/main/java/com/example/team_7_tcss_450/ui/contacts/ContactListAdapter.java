package com.example.team_7_tcss_450.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.team_7_tcss_450.R;

import java.util.LinkedList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    LayoutInflater mInflator;
    List<Contact> contactList;

    public ContactListAdapter(Context context, List<Contact> contactList) {
        this.mInflator = LayoutInflater.from(context);
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactView = mInflator.inflate(R.layout.fragment_contact, parent, false);
        return new ContactViewHolder(contactView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = this.contactList.get(position);
        holder.textViewContactName.setText(contact.getFirstName());
        holder.textViewContactNumber.setText(contact.getContactNumb());
        holder.imageViewContactImage.setImageResource(contact.getImagePath());
    }

    @Override
    public int getItemCount() {
        return this.contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        ContactListAdapter contactListAdapter;
        ImageView imageViewContactImage;
        TextView textViewContactName;
        TextView textViewContactNumber;

        public ContactViewHolder(@NonNull View itemView,
                                 ContactListAdapter contactListAdapter) {
            super(itemView);
            this.imageViewContactImage = itemView.findViewById(R.id.imageViewContactImage);
            this.textViewContactName = itemView.findViewById(R.id.textViewContactName);
            this.textViewContactNumber = itemView.findViewById(R.id.textViewContactNumber);
            this.contactListAdapter = contactListAdapter;
        }
    }

}