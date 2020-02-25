package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.SignOutOptions;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //    MyDatabase myDb;


    ///do i need it here at all
    private AWSAppSyncClient awsAppSyncClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//////
        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();
/////
        Button allTasksButton = findViewById(R.id.button2);

        allTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AllTasks.class);
                startActivity(i);

            }
        });

        Button logOutButton = findViewById(R.id.Logout);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AWSMobileClient.getInstance().signOut(SignOutOptions.builder().signOutGlobally(true).build(), new Callback<Void>() {
                    @Override
                    public void onResult(final Void result) {

                        Log.d("voytov", "signed-out");
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(i);


                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("voytov", "sign-out error", e);
                    }
                });

            }
        });


        Button addTaskButton = findViewById(R.id.button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTask.class);
                startActivity(i);
            }
        });
        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Settings.class);
                startActivity(i);
            }
        });


        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
//                        Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                        if (userStateDetails.getUserState().equals(UserState.SIGNED_OUT)) {

                            AWSMobileClient.getInstance().showSignIn(MainActivity.this, new Callback<UserStateDetails>() {
                                @Override
                                public void onResult(UserStateDetails result) {
//                                    Log.d("voytov", "onResult: " + result.getUserState());
//                                    Log.d("voytov", "name: " + AWSMobileClient.getInstance().getUsername());

                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("voytov", "onError: ", e);

                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String helloUser = sharedPreferences.getString("user_name", "default");
        TextView nameUser = findViewById(R.id.helloUser);
//        nameUser.setText("Hi, " + helloUser);
//        nameUser.setText(helloUser + "'s tasks");

        nameUser.setText(AWSMobileClient.getInstance().getUsername() + "'s tasks");


    }
}
