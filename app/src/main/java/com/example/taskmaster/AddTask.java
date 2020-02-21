package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    private AWSAppSyncClient awsAppSyncClient;
    List<Task> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        awsAppSyncClient = AWSAppSyncClient.builder()
                .context(getApplicationContext())
                .awsConfiguration(new AWSConfiguration(getApplicationContext()))
                .build();

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


//                Task newTask = new Task(taskNameText, editTextNameText, statusPicked);
//                myDb.taskDao().save(newTask);

                runTaskCreateMutation(taskNameText, editTextNameText, statusPicked);


                Context context = getApplicationContext();
                CharSequence text = "Submitted!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            public void runTaskCreateMutation(String taskName, String editText, String statusPicked) {

                CreateTaskInput createTaskInput = CreateTaskInput.builder()

                        .title(taskName)
                        .description(editText)
                        .status(statusPicked)
                        .build();
                awsAppSyncClient.mutate(CreateTaskMutation.builder().input(createTaskInput).build())
                        .enqueue(addMutationCallback);
            }


            private GraphQLCall.Callback<CreateTaskMutation.Data> addMutationCallback = new GraphQLCall.Callback<CreateTaskMutation.Data>() {
                @Override
                public void onResponse(@Nonnull Response<CreateTaskMutation.Data> response) {
                    Log.i("voytov", "Added Task");
                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("voytov", e.toString());
                }
            };

            public void getTasks() {
                awsAppSyncClient.query(ListTasksQuery.builder().build())
                        .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                        .enqueue(tasksCallback);
            }

            private GraphQLCall.Callback<ListTasksQuery.Data> tasksCallback = new GraphQLCall.Callback<ListTasksQuery.Data>() {
                @Override
                public void onResponse(@Nonnull Response<ListTasksQuery.Data> response) {
                    Log.i("voytov", response.data().listTasks().items().toString());
                    if (tasks.size() == 0 || response.data().listTasks().items().size() != tasks.size()) {

                        tasks.clear();

                        for (ListTasksQuery.Item item : response.data().listTasks().items()) {
                            Task sample = new Task(item.title(), item.description(), item.status());
                            tasks.add(sample);
                        }

                        Handler handlerForMainThread = new Handler(Looper.getMainLooper()) {
                            @Override
                            public void handleMessage(Message inputMessage) {
//        RecyclerView recyclerView=findViewById(R.id.);
//        recyclerView.getAdapter().notifyDataSetChanged();
                            }
                        };

                        handlerForMainThread.obtainMessage().sendToTarget();
                    }

                }

                @Override
                public void onFailure(@Nonnull ApolloException e) {
                    Log.e("voytov", e.toString());
                }
            };


        });

//https://www.youtube.com/watch?v=FcPUFp8Qrps
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}





