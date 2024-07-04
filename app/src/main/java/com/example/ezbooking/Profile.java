package com.example.ezbooking;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView profileImage;
    EditText editUsername, editEmail;
    Button saveButton, logoutButton;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.profile_image);
        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        databaseHelper = new DatabaseHelper(this);

        // Load user data from database
        loadUserData();

        profileImage.setOnClickListener(v -> chooseImage());

        saveButton.setOnClickListener(v -> {
            updateUserData();
        });

        logoutButton.setOnClickListener(v -> {
            logoutUser();
        });
    }

    // Method to load user data from database
    private void loadUserData() {
        // Assuming you have methods in DatabaseHelper to fetch user data
        // Replace "user@example.com" with actual logged-in user's email
        UserData userData = databaseHelper.getUserData("user@example.com");

        if (userData != null) {
            editUsername.setText(userData.getUsername());
            editEmail.setText(userData.getEmail());
        }
    }

    // Method to update user data in database
    private void updateUserData() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Please enter username");
            editUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Please enter email");
            editEmail.requestFocus();
            return;
        }

        // Update user data in database
        boolean updated = databaseHelper.updateUserData(username, email);

        if (updated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle logout action
    private void logoutUser() {
        // Handle logout logic here, navigate to login page
        Intent intent = new Intent(Profile.this, LoginPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Method to choose image from gallery
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
