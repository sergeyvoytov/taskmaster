package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

public class AddTask extends AppCompatActivity {
    MyDatabase myDb;
    String statusPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        myDb = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "tasks").allowMainThreadQueries().build();


        Button addTaskButtonNewActivity = findViewById(R.id.addSingleTask);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("voytov", "user picked a new status");
                // figure out which one they picked
//                String statusPicked = "something";
                if (checkedId == R.id.radioNew) {
                    // they picked new
                    statusPicked = "new";
                } else if (checkedId == R.id.radioAssigned) {
                    // they picked assigned
                    statusPicked = "assigned";
                } else if (checkedId == R.id.radioProgress) {
                    // they picked in progress
                    statusPicked = "in progress";
                } else if (checkedId == R.id.radioComplete) {
                    // they picked complete
                    statusPicked = "complete";
                }
            }
        });

        addTaskButtonNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText taskName = findViewById(R.id.taskName);
                String taskNameText = taskName.getText().toString();

                EditText editText = findViewById(R.id.editText);
                String editTextNameText = editText.getText().toString();


                Task newTask = new Task(taskNameText, editTextNameText, statusPicked);
                myDb.taskDao().

                        save(newTask);


                Context context = getApplicationContext();
                CharSequence text = "Submitted!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


        });

//https://www.youtube.com/watch?v=FcPUFp8Qrps
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}


