package com.example.taskmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button sButton;   //staff login button
    private Button lButton;   //leader login button

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sButton = findViewById(R.id.button);
        lButton = findViewById(R.id.button2);


        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginStaffActivity();
            }
        });

        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openLoginLeaderActivity();
            }
        });

    }

    private void openLoginLeaderActivity() {
        Intent intent = new Intent(this, LoginFounder.class);
        startActivity(intent);

    }

    public void openLoginStaffActivity(){
        Intent intent = new Intent(this,LoginStaffActivity.class);
        startActivity(intent);
    }


}