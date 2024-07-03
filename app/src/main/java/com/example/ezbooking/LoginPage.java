package com.example.ezbooking;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    EditText editTextUserName, editTextPassword;
    Button buttonLog;
    FirebaseAuth mAuth;
    TextView txtSign;
    ProgressBar progressBar;
    private static final String TAG = "LoginPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);

        editTextUserName = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        buttonLog = findViewById(R.id.signin);
        txtSign = findViewById(R.id.signup);
        progressBar = findViewById(R.id.progressBar);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        buttonLog.setOnClickListener(v -> {
            Log.d(TAG, "Sign in button clicked");

            String username = editTextUserName.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                Toast.makeText(LoginPage.this, "Enter Username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(LoginPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();

                                // Start the HomePage activity
                                Intent intent = new Intent(LoginPage.this, Dashboard.class);
                                startActivity(intent);

                                finish(); // Optional: close the LoginPage activity
                            } else {
                                Toast.makeText(LoginPage.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

        txtSign.setOnClickListener(v -> {
            Log.d(TAG, "Sign up text clicked");

            Intent intent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intent);
            finish();
        });
    }
}