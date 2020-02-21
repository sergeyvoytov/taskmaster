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
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String helloUser = sharedPreferences.getString("user_name", "default");
        TextView nameUser = findViewById(R.id.helloUser);
//        nameUser.setText("Hi, " + helloUser);
        nameUser.setText(helloUser + "'s tasks");
    }
}
