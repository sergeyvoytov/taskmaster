package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateTaskMutation;
import com.amazonaws.amplify.generated.graphql.ListTasksQuery;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.ResultListener;
import com.amplifyframework.storage.result.StorageUploadFileResult;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateTaskInput;

public class AddTask extends AppCompatActivity {
    MyDatabase myDb;
    String statusPicked;
    private AWSAppSyncClient mAWSAppSyncClient;
    String picTask;
    private static final String TAG = "voytov";


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

        Button imgBtn = findViewById(R.id.imgBtn);

        Intent intentThatWeCameFrom = getIntent();
//        Uri data = intentThatWeCameFrom.getData();
        String action = intentThatWeCameFrom.getAction();
        String type = intentThatWeCameFrom.getType();

        Log.i(TAG, "Type of intent " + type);
//        Log.i(TAG, "Uri? " + data.getPath());


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


        ////////

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(chooseFile, 42);
//                chooseFile.setType("*/*");
//                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
//                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
//
//                String src = getPath(uri);    /* Method defined above. */
//                File source = new File(src);
//                String filename = uri.getLastPathSegment();
//                File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CustomFolder/" + filename);
//
//                Uri uri = data.getData();
//                String src = uri.getPath();

            }

        });


        ///////


        addTaskButtonNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText taskName = findViewById(R.id.taskName);
                String taskNameText = taskName.getText().toString();

                EditText editText = findViewById(R.id.editText);
                String editTextNameText = editText.getText().toString();


                runMutation(taskNameText, editTextNameText, statusPicked);

                uploadWithTransferUtility();

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

        CreateTaskInput createTaskInput = CreateTaskInput.builder()
                .title(title)
                .description(description)
                .status(statusPicked)
                .picTask(picTask)
                .build();

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


    public void uploadWithTransferUtility() {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance()))
                        .build();

        File file = new File(getApplicationContext().getFilesDir(), "sample3.txt");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append("Howdy World!");
            writer.close();
        } catch (Exception e) {
            Log.e("voytov", e.getMessage());
        }

        TransferObserver uploadObserver =
                transferUtility.upload(
                        "public/sample3.txt",
                        new File(getApplicationContext().getFilesDir(), "sample3.txt"));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d("voytov", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("voytov", "Bytes Transferred: " + uploadObserver.getBytesTransferred());
        Log.d("voytov", "Bytes Total: " + uploadObserver.getBytesTotal());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        Log.d(TAG, "on activity: " + requestCode+ resultCode+ resultData);




    }


    public String getPath(Uri uri) {

        String path = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(projection[0]);
            path = cursor.getString(column_index);
            cursor.close();
        }

        return ((path == null || path.isEmpty()) ? (uri.getPath()) : path);
    }

}



