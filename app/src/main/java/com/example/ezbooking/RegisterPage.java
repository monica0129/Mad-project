package com.example.ezbooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterPage extends AppCompatActivity {
    AppCompatEditText editTextUsername, editTextEmail, editTextNewPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    DatabaseHelper db;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_page);

        // Initialize Firebase Auth and SQLite Database Helper
        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHelper(this);

        editTextEmail = findViewById(R.id.email1);
        editTextNewPassword = findViewById(R.id.rpassword);
        editTextUsername = findViewById(R.id.username);
        buttonReg = findViewById(R.id.signUp);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.sign_in);

        buttonReg.setOnClickListener(v -> {
            String email, password, username;
            progressBar.setVisibility(View.VISIBLE);
            email = String.valueOf(editTextEmail.getText());
            password = String.valueOf(editTextNewPassword.getText());
            username = String.valueOf(editTextUsername.getText());

            if (TextUtils.isEmpty(email)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterPage.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterPage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(username)) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RegisterPage.this, "Enter Username", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Store user details in SQLite with a null profile image path initially
                            db.addUser(username, email, password);
                            Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterPage.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                showToast("Weak Password.");
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                showToast("Invalid Email.");
                            } catch (FirebaseAuthUserCollisionException e) {
                                showToast("User Already Exists.");
                            } catch (Exception e) {
                                showToast("Authentication failed.");
                            }
                        }
                    });
        });

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(RegisterPage.this, message, Toast.LENGTH_SHORT).show());
    }
}
