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

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class AddTask extends AppCompatActivity {
    MyDatabase myDb;
    String statusPicked;
    private AWSAppSyncClient mAWSAppSyncClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mAWSAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

//        myDb = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "tasks").allowMainThreadQueries().build();

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


                runMutation(taskNameText, editTextNameText, statusPicked);

//                Task newTask = new Task(taskNameText, editTextNameText, statusPicked);
//                myDb.taskDao().save(newTask);


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


    // amlify stuff

    public void runMutation(String title, String description, String statusPicked) {

//        Task newTask = new Task(taskNameText, editTextNameText, statusPicked);

        CreateTaskInput createTaskInput = CreateTaskInput.builder().
                title(title).
                description(description).
                status(statusPicked).
                build();

        mAWSAppSyncClient.mutate(CreateTaskMutation.builder().input(createTaskInput).build())
                .enqueue(mutationCallback);
    }

    private GraphQLCall.Callback<CreateTaskMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
            Log.i("voytov", "Added Todo");
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
    };


}

