package com.example.taskmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.example.taskmanagement.adapter.TaskAdapter;
import com.example.taskmanagement.model.DeleteResponse;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.SharedPrefManager;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.remote.ApiUtils;
import com.example.taskmanagement.remote.TaskService;



public class TaskStatusActivity extends AppCompatActivity {

    TaskService taskService;
    Context context;
    RecyclerView taskList;
    TaskAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_status);
        context = this; // get current activity context

        // get reference to the RecyclerView taskList
        taskList = findViewById(R.id.taskList);

        //register for context menu
        registerForContextMenu(taskList);

        // update listview
        updateListView();


    }

    /**
     * Fetch data for ListView
     */
    private void updateListView() {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get task service instance
        taskService = ApiUtils.getTaskService();

        // execute the call. send the user token when sending the query
        taskService.getAllTasks(user.getToken()).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // token is not valid/expired
                if (response.code() == 401) {
                    displayAlert("Session Invalid");
                }

                // Get list of task object from response
                List<Task> tasks = response.body();

                // initialize adapter
                adapter = new TaskAdapter(context, tasks);

                // set adapter to the RecyclerView
                taskList.setAdapter(adapter);

                // set layout to recycler view
                taskList.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(taskList.getContext(),
                        DividerItemDecoration.VERTICAL);
                taskList.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Task selectedTask = adapter.getSelectedItem();
        Log.d("MyApp", "selected "+selectedTask.toString());
        switch (item.getItemId()) {
            case R.id.menu_details://should match the id in the context menu file
                doViewDetails(selectedTask);
                break;
            case R.id.menu_delete://should match the id in the context menu file
                doDeleteTask(selectedTask);
                break;


        }
        return super.onContextItemSelected(item);
    }

    private void doViewDetails(Task selectedTask) {
        Log.d("MyApp:", "viewing details "+selectedTask.toString());
        Intent intent = new Intent(context, TaskDetailActivity.class);
        intent.putExtra("task_id", selectedTask.getTaskID());
        startActivity(intent);
    }

    /**
     * Delete task record. Called by contextual menu "Delete"
     * @param selectedTask - task selected by user
     */
    private void doDeleteTask(Task selectedTask) {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // prepare REST API call
        TaskService taskService = ApiUtils.getTaskService();
        Call<DeleteResponse> call = taskService.deleteTask(user.getToken(), selectedTask.getTaskID());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("task successfully deleted");
                    // update data in list view
                    updateListView();
                } else {
                    displayAlert("task failed to delete");
                    Log.e("MyApp:", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
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