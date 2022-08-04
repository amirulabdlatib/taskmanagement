package com.example.taskmanagement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanagement.model.SharedPrefManager;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.remote.ApiUtils;
import com.example.taskmanagement.remote.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StaffDetailsActivity extends AppCompatActivity {

    private Button zButton;
    private Button logoutButton;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_details);

        Intent intent = getIntent();

        int key = intent.getIntExtra("keyID",1);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //find button id
        zButton = findViewById(R.id.viewtaskbttn);
        logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportLogoutDialogBox();
            }
        });

        zButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openViewTaskActivity();
            }
        });

//////////////////////////////////////////////////////////////////////////////////////////////////////////
        User user =
                SharedPrefManager.getInstance(getApplicationContext()).getUser();

        userService = ApiUtils.getUserService();

        userService.getUser(user.getToken(),key).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d("MyApp:", "Response: " + response.raw().toString());

                User user = response.body();

                TextView txtRole = findViewById(R.id.lblRole);
                TextView txtName = findViewById(R.id.staffName);
                TextView txtId = findViewById(R.id.staffId);
                TextView txtPhone = findViewById(R.id.staffPhone);
                TextView txtAddress = findViewById(R.id.staffAddress);


                txtRole.setText(user.getRole());
                txtName.setText(user.getUsername());
                txtId.setText("Your ID :              "+user.getId());
                txtPhone.setText(user.getPhoneNumber());
                txtAddress.setText(user.getAddress());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(null, "Error connecting",
                        Toast.LENGTH_LONG).show();

            }
        });


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }



    private void openViewTaskActivity() {
        Intent intent = new Intent(this, ViewTask.class);
        startActivity(intent);
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
                startActivity(new Intent(StaffDetailsActivity.this, MainActivity.class));
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