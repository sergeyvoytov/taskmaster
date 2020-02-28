package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        String taskString = getIntent().getStringExtra("task_key");
        TextView taskTextView = findViewById(R.id.textView6);
        taskTextView.setText(taskString);

        String taskDescription = getIntent().getStringExtra("task_desc");
        TextView taskTextView3 = findViewById(R.id.textView8);
        taskTextView3.setText(taskDescription);


        String statusBar = getIntent().getStringExtra("task_state");
        TextView statusBarView = findViewById(R.id.statusBar);
        statusBarView.setText(statusBar);

        String imageUrl = getIntent().getStringExtra("task_url");

        ImageView imageViewall = findViewById(R.id.picAll);
//        imageViewall.setI(statusBar);

        Picasso.get().load(imageUrl).into(imageViewall);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }
}
