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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.SharedPrefManager;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.remote.ApiUtils;
import com.example.taskmanagement.remote.TaskService;


public class CreateTaskActivity extends AppCompatActivity {

    private EditText txtTaskName;
    private EditText txtTaskDesc;
    private EditText txtStaffName;
    private EditText txtStatus;
    private static TextView tvCreated; // static because need to be accessed by DatePickerFragment
    private static TextView tvDue; // static because need to be accessed by DatePickerFragment

    private static Date createdAt; // static because need to be accessed by DatePickerFragment
    private static Date dueAt; // static because need to be accessed by DatePickerFragment


    private Context context;

    /**
     * Date picker fragment class
     * Reference: https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            // create a date object from selected year, month and day
            dueAt = new GregorianCalendar(year, month, day).getTime();
            // display in the label beside the button with specific date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tvDue.setText(sdf.format(dueAt));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // store context
        context = this;

        // get view objects references
        txtTaskName = findViewById(R.id.txtTaskName);
        txtTaskDesc = findViewById(R.id.txtTaskDesc);
        txtStaffName = findViewById(R.id.txtStaffName);

        txtStatus = findViewById(R.id.txtStatus);
        txtStatus.setText("Assigned");
        txtStatus.setShowSoftInputOnFocus(true);

        tvCreated = findViewById(R.id.tvCreated);
        tvDue = findViewById(R.id.tvDue);
        // set default createdAt value, get current date
        createdAt = new Date();
        dueAt = new Date();
        // display in the label beside the button with specific date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tvCreated.setText( sdf.format(createdAt) );
        tvDue.setText(sdf.format(dueAt));
    }

    /**
     * Called when pick date button is clicked. Display a date picker dialog
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }



    /**
     * Called when Add Task button is clicked
     * @param v
     */
    public void addNewTask(View v) {
        // get values in form
        String TaskName = txtTaskName.getText().toString();
        String description = txtTaskDesc.getText().toString();
        String staffName = txtStaffName.getText().toString();
        String status = txtStatus.getText().toString();


        // convert createdAt date to format in DB
        // reference: https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String created_at = sdf.format(createdAt);
        String due_at = sdf.format(dueAt);


        // create a Task object
        // set id to 0, it will be automatically generated by the db later

        Task t = new Task(0,TaskName,description,created_at,due_at,staffName,status);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // send request to add new book to the REST API
        TaskService taskService = ApiUtils.getTaskService();
        Call<Task> call = taskService.addTask(user.getToken(), t);

        // execute
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {

                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // invalid session?
                if (response.code() == 401)
                    displayAlert("Invalid session. Please re-login");

                // book added successfully?
                Task addedTask = response.body();
                if (addedTask != null) {
                    // display message
                    Toast.makeText(context,
                            addedTask.getTaskName() + " added successfully.",
                            Toast.LENGTH_LONG).show();

                    // end this activity and forward user to FounderActivity
                    Intent intent = new Intent(context, FounderDetailActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    displayAlert("Add New Task failed.");
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