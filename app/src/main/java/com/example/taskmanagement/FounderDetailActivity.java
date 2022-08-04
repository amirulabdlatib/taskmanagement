package com.example.taskmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.taskmanagement.model.SharedPrefManager;

public class FounderDetailActivity extends AppCompatActivity implements View.OnClickListener{


    private Button statusButton;   //staff login button
    private Button createButton;   //leader login button
    private Button reportButton;   //leader login button
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_founder_detail);

        statusButton = findViewById(R.id.button3);
        createButton = findViewById(R.id.button4);
        logoutButton = findViewById(R.id.logout);


        statusButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

    }



    public void onGroupItemClick(MenuItem menuitem) {
        Intent intent = new Intent(this, NewTaskList.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.button3:
                openActivity(1);
                break;
            case R.id.button4:
                openActivity(2);
                break;


            case R.id.logout:
                reportLogoutDialogBox();
            break;
        }
    }

    public void openActivity(int key){

        switch (key){
            case 1:
                Intent intent = new Intent(this, TaskStatusActivity.class);
                startActivity(intent);
                break;

            case 2:
                Intent open = new Intent(this,CreateTaskActivity.class);
                startActivity(open);
                break;

        }

    }


    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    /** * Displaying dialog box confirmation for logout */
    private void reportLogoutDialogBox() {
        // prepare a dialog box confirmation with yes or no
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("Logout?");
        builder2.setMessage("Are you sure you want to logout?");

        // prepare action listener for each button
        builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                startActivity(new Intent(FounderDetailActivity.this, MainActivity.class));
                Toast.makeText(getApplicationContext(),"You have successfully logged out.", Toast.LENGTH_SHORT).show();
            }
        });
        builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancel.", Toast.LENGTH_SHORT).show();
            }
        });

        // create the alert dialog and show to the user
        AlertDialog alert = builder2.create();
        alert.show();
    }

}