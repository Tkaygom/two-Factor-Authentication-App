// DashboardActivity.java
package com.assignment.twofactorapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Additional logic for the dashboard can be added here
        // For now, the layout only displays a welcome message

        // Add this line to display the welcome message
        //TextView welcomeTextView = findViewById(R.id.welcomeMessage);

        System.out.print ("Welcome to 6130 project");
    }
}
