package com.assignment.twofactorapp;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String enteredUsername = usernameEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                // Validate input
                if (isValidInput(enteredUsername, enteredPassword)) {
                    // Check if the entered credentials match the stored data in the SQLite database
                    if (checkCredentials(enteredUsername, enteredPassword)) {
                        // Successful login, navigate to the 2FA page
                        startActivity(new Intent(LoginActivity.this, TwoFactorAuthActivity.class));
                        finish(); // Optional: Close the login activity
                    } else {
                        // Incorrect credentials, show an error message
                        Log.d("LoginActivity", "Invalid username or password");
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Show an error message for invalid input
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidInput(String username, String password) {
        // Perform validation logic here
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

    private boolean checkCredentials(String enteredUsername, String enteredPassword) {
        // Retrieve stored user data from the SQLite database using UserDbHelper
        UserDbHelper dbHelper = new UserDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Perform a database query to check if the entered credentials match stored data
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                null,
                UserContract.UserEntry.COLUMN_USERNAME + " = ? AND " +
                        UserContract.UserEntry.COLUMN_PASSWORD + " = ?",
                new String[]{enteredUsername, enteredPassword},
                null,
                null,
                null
        );

        // Check if the query returned any rows
        boolean credentialsMatch = cursor.getCount() > 0;

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return credentialsMatch;
    }
}
