package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.ui.contacts.Contact;
import com.example.team_7_tcss_450.ui.contacts.ContactListAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewWordList;

    List<Contact> contactList = new LinkedList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserInfoViewModel.class);

        setContentView(R.layout.activity_main);
        // Make sure the new statements go BELOW setContentView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather,
                R.id.navigation_message, R.id.navigation_Contacts)
                .build();
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.
                setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //static accounts
        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person1", "911-911-9111"));
        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person2", "000-000-0000"));
        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person3", "111-111-1111"));
        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person4", "222-222-2222"));
        contactList.add(new Contact(R.drawable.ic_account_black_24dp, "Person5", "333-333-3333"));

        recyclerViewWordList = findViewById(R.id.recyclerViewWordList);
        recyclerViewWordList.setAdapter(new ContactListAdapter(this, contactList));
        recyclerViewWordList.setLayoutManager(new LinearLayoutManager(this));
    }
}