package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.SharedPrefManager;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.remote.ApiUtils;
import com.example.taskmanagement.remote.TaskService;



public class TaskDetailActivity extends AppCompatActivity {

    TaskService taskService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // get book id sent by TaskListActivity, -1 if not found
        Intent intent = getIntent();
        int id = intent.getIntExtra("task_id", -1);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get task service instance
        taskService = ApiUtils.getTaskService();

        // execute the API query. send the token and task id
        taskService.getTask(user.getToken(), id).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // get book object from response
                Task task = response.body();

                // get references to the view elements
                TextView taskName = findViewById(R.id.taskName);
                TextView taskStatus = findViewById(R.id.taskStatus);
                TextView staffName = findViewById(R.id.staffName);
                TextView assignedDate = findViewById(R.id.assignedDate);
                TextView dueDate = findViewById(R.id.dueDate);
                TextView taskDesc = findViewById(R.id.taskDesc);

                // set values
                taskName.setText(task.getTaskName());
                taskStatus.setText(task.getStatus());
                staffName.setText(task.getStaffName());
                assignedDate.setText(task.getAssignedDate());
                dueDate.setText(task.getTaskDue());
                taskDesc.setText(task.getDescription());
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });

    }
}