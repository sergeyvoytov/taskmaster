package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;

public class AllTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

//https://www.youtube.com/watch?v=FcPUFp8Qrps
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
