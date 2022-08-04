package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanagement.model.ErrorResponse;
import com.example.taskmanagement.model.SharedPrefManager;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.remote.ApiUtils;
import com.example.taskmanagement.remote.UserService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginStaffActivity extends AppCompatActivity {

    private Button dButton;
    EditText edtUsername;
    EditText edtPassword;
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);

        // if the user is already logged in we will directly start
        // the main activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, StaffDetailsActivity.class));
            return;
        }

        dButton = findViewById(R.id.btnLogin2);
        edtUsername = findViewById(R.id.StaffUsername);
        edtPassword = findViewById(R.id.StaffPassword);

        userService = ApiUtils.getUserService();
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get username and password entered by user
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                // validate form, make sure it is not empty
                if (validateLogin(username, password)) {
                    // do login
                    doLogin(username, password);
                }

            }
        });
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().length() == 0) {
            displayToast("Username is required");
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            displayToast("Password is required");
            return false;
        }
        return true;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void doLogin(String username, String password) {
        Call call = userService.login(username, password);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
//////////////////////////////////////////////////////////////////////////////////////////////////////////
                User user = (User) response.body();



//////////////////////////////////////////////////////////////////////////////////////////////////////////

                // received reply from REST API
                if (response.isSuccessful()) {
                    // parse response to POJO
                    if (user.getToken() != null&& !user.getRole().equalsIgnoreCase("founder")) {
                        // successful login. server replies a token value
                        displayToast("Login successful");
                        displayToast("Token: " + user.getToken());

                        // store value in Shared Preferences
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                        // forward user to staff details
                        finish();
                        Intent open = new Intent(getApplicationContext(),StaffDetailsActivity.class);
                        open.putExtra("keyID",user.getId());
                        startActivity(open);

                    }
                }
                else if (response.errorBody() != null){
                    // parse response to POJO
                    String errorResp = null;
                    try {
                        errorResp = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ErrorResponse e = new Gson().fromJson( errorResp, ErrorResponse.class);
                    displayToast(e.getError().getMessage());
                }

            }

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            @Override
            public void onFailure(Call call, Throwable t) {
                displayToast("Error connecting to server.");
                displayToast(t.getMessage());
            }

        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}