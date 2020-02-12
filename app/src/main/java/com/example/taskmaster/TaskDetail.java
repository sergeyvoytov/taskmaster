package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

//        String testSting = getIntent().getStringExtra("task_key");
//        Log.i("Voytov", testSting);
        String taskString = getIntent().getStringExtra("task_key");
        TextView taskTextView = findViewById(R.id.textView6);
        taskTextView.setText(taskString);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
