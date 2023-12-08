package com.assignment.twofactorapp;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RegistrationActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText nameEditText;
    private EditText passwordEditText;

    // Database helper class
    private UserDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new UserDbHelper(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String username = usernameEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validate input
                if (isValidInput(username, name, password)) {
                    // Save user details to the database
                    saveUserData(username, name, password);

                    // Automatically redirect the user to the login page after successful registration
                    startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                    finish(); // Optional: Close the registration activity
                } else {
                    // Show an error message for invalid input
                    Toast.makeText(RegistrationActivity.this, "Invalid input, please check your details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidInput(String username, String name, String password) {
        // Perform validation logic here
        // For simplicity, you can add basic checks like non-empty fields
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password);
    }

    private void saveUserData(String username, String name, String password) {
        // Get a writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object to store the user data
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USERNAME, username);
        values.put(UserContract.UserEntry.COLUMN_NAME, name);
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, password);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(UserContract.UserEntry.TABLE_NAME, null, values);

        // Check if the insertion was successful
        if (newRowId != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error during registration", Toast.LENGTH_SHORT).show();
        }

        // Close the database connection
        db.close();
    }
}
