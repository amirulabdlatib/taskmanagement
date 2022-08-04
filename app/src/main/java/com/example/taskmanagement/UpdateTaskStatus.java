package com.example.taskmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.SharedPrefManager;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.remote.ApiUtils;
import com.example.taskmanagement.remote.TaskService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateTaskStatus extends AppCompatActivity {

    private TaskService taskService;
    private Task task;      // store task info
    private EditText txtTaskName;
    private EditText txtTaskDesc;
    private EditText txtStaffName;
    private EditText txtStatus;
    private static TextView tvCreated; // static because need to be accessed by DatePickerFragment
    private static TextView tvDue; // static because need to be accessed by DatePickerFragment
    private Button progressbtn;
    private Button donebtn;

    private static Date createdAt; // static because need to be accessed by DatePickerFragment
    private static Date dueAt; // static because need to be accessed by DatePickerFragment

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task_status);

        // retrieve book id from intent
        // get book id sent by BookListActivity, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("task_id", -1);


        // store context
        context = this;

        // get view objects references
        txtTaskName = findViewById(R.id.txtTaskName2);
        txtTaskDesc = findViewById(R.id.txtTaskDesc2);
        txtStaffName = findViewById(R.id.txtStaffName2);

        txtStatus = findViewById(R.id.txtStatus2);
        txtStatus.setText("Assigned");
        txtStatus.setShowSoftInputOnFocus(true);

        progressbtn = findViewById(R.id.progressbtn);
        donebtn = findViewById(R.id.donebtn);

        txtStatus = findViewById(R.id.txtStatus2);

        progressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("In Progress Clicked", "**********");
                Toast.makeText(context, "In progress button clicked",
                        Toast.LENGTH_LONG).show();
                txtStatus.setText("In Progress");
                txtStatus.setShowSoftInputOnFocus(true);
                progressbtn.setEnabled(false);
            }

        });

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("Done Clicked", "**********");
                Toast.makeText(context, "Done button clicked",
                        Toast.LENGTH_LONG).show();
                txtStatus.setText("Completed");
                txtStatus.setShowSoftInputOnFocus(true);
                progressbtn.setEnabled(false);
                donebtn.setEnabled(false);
            }

        });

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get task service instance
        taskService = ApiUtils.getTaskService();

        // execute the API query. send the token and book id
        taskService.getTask(user.getToken(), id).enqueue(new Callback<Task>() {
                                                             @Override
                                                             public void onResponse(Call<Task> call, Response<Task> response) {
                                                                 // for debug purpose
                                                                 Log.d("MyApp:", "Response: " + response.raw().toString());

                                                                 // get book object from response
                                                                 task = response.body();

                                                                 // set values into forms
                                                                 txtTaskName.setText(task.getTaskName());
                                                                 txtTaskDesc.setText(task.getDescription());
                                                                 txtStaffName.setText(task.getStaffName());
                                                                 txtStatus.setText(task.getStatus());


                                                                 // parse created_at date to date object
                                                                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                 try {
                                                                     // parse created date string to date object
                                                                     createdAt = sdf.parse(task.getAssignedDate());
                                                                 } catch (ParseException e) {
                                                                     e.printStackTrace();
                                                                 }
                                                             }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

            }

    /**
     * Update task info in database when the user click Update Book button
     * @param view
     */
    public void updateTask(View view) {
        // get values in form
        String tasktitle = txtTaskName.getText().toString();
        String description = txtTaskDesc.getText().toString();
        String staff = txtStaffName.getText().toString();
        String status = txtStatus.getText().toString();

        // convert createdAt date to format in DB
        // reference: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   //     String created_at = sdf.format(createdAt);

        // set updated_at to current date and time
      //  String updated_at = sdf.format(new Date());

        // update the book object retrieved in onCreate with the new data. the book object
        // already contains the id
        task.setTaskName(tasktitle);
        task.setDescription(description);
        task.setStaffName(staff);
        task.setStatus(status);
      //  task.setAssignedDate(created_at);

        Log.d("MyApp:", "Task info: " + task.toString());

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // send request to update the task record to the REST API
        TaskService bookService = ApiUtils.getTaskService();
        Call<Task> call = bookService.updateTask(user.getToken(), task);

        Context context = this;
        // execute
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // invalid session?
                if (response.code() == 401)
                    displayAlert("Invalid session. Please re-login");

                // task updated successfully?
                Task updatedTask = response.body();
                if (updatedTask != null) {
                    // display message
                    Toast.makeText(context,
                            updatedTask.getTaskName() + " updated successfully.",
                            Toast.LENGTH_LONG).show();

                    // end this activity and forward user to BookListActivity
                    Intent intent = new Intent(context, ViewTask.class);
                    startActivity(intent);
                    finish();
                } else {
                    displayAlert("Update Task failed.");
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
            }
        });
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
