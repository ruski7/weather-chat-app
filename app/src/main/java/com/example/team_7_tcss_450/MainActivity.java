package com.example.team_7_tcss_450;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.team_7_tcss_450.databinding.ActivityMainBinding;
import com.example.team_7_tcss_450.model.NewMessageCountViewModel;
import com.example.team_7_tcss_450.model.UserInfoViewModel;
import com.example.team_7_tcss_450.services.PushReceiver;
import com.example.team_7_tcss_450.ui.chat.ChatMessage;
import com.example.team_7_tcss_450.ui.chat.ChatViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    AppBarConfiguration mAppBarConfiguration;

    private MainPushMessageReceiver mPushMessageReceiver;
    private ActivityMainBinding binding;
    private NewMessageCountViewModel mNewMessageModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserInfoViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setContentView(R.layout.activity_main);
        // Make sure the new statements go BELOW setContentView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_weather,
                R.id.navigation_message, R.id.navigation_account)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_message) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });

        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = binding.navView.getOrCreateBadge(R.id.navigation_message);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });
    }

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {

        private ChatViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(ChatViewModel.class);

        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();

            if (intent.hasExtra("chatMessage")) {

                ChatMessage cm = (ChatMessage) intent.getSerializableExtra("chatMessage");

                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.navigation_message) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
    }

}