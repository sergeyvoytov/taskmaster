package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
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

import java.util.List;

public class MainActivity extends AppCompatActivity{

//    MyDatabase myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

//        final Button taskOneDetailButton = findViewById(R.id.task1);
//
//        taskOneDetailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, TaskDetail.class);
//                i.putExtra("task_key", taskOneDetailButton.getText());
//                Log.i("Voytov", taskOneDetailButton.getText().toString());
//                startActivity(i);
//            }
//        });
//
//        final Button taskTwoDetailButton = findViewById(R.id.task2);
//
//
//        taskTwoDetailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, TaskDetail.class);
//                i.putExtra("task_key", taskTwoDetailButton.getText());
//                startActivity(i);
//            }
//        });
//        final Button taskThreeDetailButton = findViewById(R.id.task3);
//
//        taskThreeDetailButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, TaskDetail.class);
//                i.putExtra("task_key", taskThreeDetailButton.getText());
//                startActivity(i);
//            }
//        });

        /// out putting user on the main page
//        String helloUser = getIntent().getStringExtra("user_name");
//        String userHi = sharedPreferences.getString("user_name", "default");
//        if(helloUser != null){
//            TextView userTextView = findViewById(R.id.helloUser);
//            userTextView.setText("Hi, " + helloUser);
//        }
//        final LinearLayout linearLayout = findViewById(R.id.fragment_click);
//
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(MainActivity.this, TaskDetail.class);
//                i.putExtra("task_key", linearLayout.getId());
//                startActivity(i);
//            }
//        });


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
