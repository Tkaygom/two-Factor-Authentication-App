package com.assignment.twofactorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Locale;

public class TwoFactorAuthActivity extends AppCompatActivity {

    private static final int OTP_LENGTH = 6;
    private static final long COUNTDOWN_IN_MILLIS = 10000; // 10 seconds

    private TextView otpTextView;
    private ProgressBar progressBar;
    private EditText otpEditText;
    private Button submitOtpButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2fa);

        otpTextView = findViewById(R.id.otpTextView);
        progressBar = findViewById(R.id.progressBar);
        otpEditText = findViewById(R.id.otpEditText);
        submitOtpButton = findViewById(R.id.submitOtpButton);

        // Generate a random OTP when the activity is created
        final String generatedOtp = generateRandomOtp();
        otpTextView.setText(getString(R.string.generated_otp, generatedOtp));

        startCountdownTimer();

        submitOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otpEditText.getText().toString();
                if (enteredOtp.length() == OTP_LENGTH && enteredOtp.equals(generatedOtp)) {
                    // Successful 2FA, start dashboard
                    startActivity(new Intent(TwoFactorAuthActivity.this, DashboardActivity.class));
                } else {
                    // Incorrect OTP, show a toast message
                    Toast.makeText(TwoFactorAuthActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(COUNTDOWN_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateCountdownText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // Handle OTP expiration logic here
                // For now, simply show a toast message
                Toast.makeText(TwoFactorAuthActivity.this, "OTP expired", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.start();

        timerRunning = true;
    }

    private void updateCountdownText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        progressBar.setProgress(seconds * 100 / 10);
    }

    private String generateRandomOtp() {
        // Generate a random 6-digit OTP
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerRunning) {
            countDownTimer.cancel();
        }
    }
}
