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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

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
            email = editTextEmail.getText().toString().trim();
            password = editTextNewPassword.getText().toString().trim();
            username = editTextUsername.getText().toString().trim();

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

            // Create user with Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterPage.this, task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Store user details in SQLite
                                db.addUser(username, email, password);
                                Toast.makeText(RegisterPage.this, "Registration successful.", Toast.LENGTH_SHORT).show();

                                // Redirect to Profile activity and pass data
                                Intent intent = new Intent(RegisterPage.this, Profile.class);
                                intent.putExtra("username", username);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            // If registration fails, display a message to the user.
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
            Intent intent = new Intent(RegisterPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });
    }

    private void showToast(String message) {
        Toast.makeText(RegisterPage.this, message, Toast.LENGTH_SHORT).show();
    }
}
