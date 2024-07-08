package com.example.ezbooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "ProfileActivity";

    ImageView profileImage;
    EditText editUsername, editEmail;
    Button saveButton, logoutButton;
    DatabaseHelper databaseHelper;
    FirebaseUser currentUser; // Firebase user object
    String username, email; // Variables to hold username and email

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

        // Initialize Firebase Auth and get current user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Check if user is authenticated
        if (currentUser == null) {
            // User not logged in, navigate to login page
            Intent intent = new Intent(Profile.this, LoginPage.class);
            startActivity(intent);
            finish();
            return;
        }

        // Load user data from Firebase Auth and SQLite
        if (currentUser.getEmail() != null) {
            email = currentUser.getEmail();
            loadUserData(email);
        }

        profileImage.setOnClickListener(v -> chooseImage());

        saveButton.setOnClickListener(v -> {
            updateUserData();
        });

        logoutButton.setOnClickListener(v -> {
            logoutUser();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(Profile.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Method to load user data from SQLite
    private void loadUserData(String email) {
        UserData userData = databaseHelper.getUserData(email);

        if (userData != null) {
            username = userData.getUsername();
            editUsername.setText(username);
            editEmail.setText(email);

            // Load profile image
            String encodedImage = databaseHelper.getProfileImage(email);
            if (encodedImage != null) {
                Bitmap bitmap = decodeImage(encodedImage);
                profileImage.setImageBitmap(bitmap);
            }
        } else {
            Log.e(TAG, "User data not found for email: " + email);
        }
    }

    // Method to update user data in Firebase and SQLite
    private void updateUserData() {
        String newUsername = editUsername.getText().toString().trim();
        String newEmail = editEmail.getText().toString().trim();

        if (TextUtils.isEmpty(newUsername)) {
            editUsername.setError("Please enter username");
            editUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newEmail)) {
            editEmail.setError("Please enter email");
            editEmail.requestFocus();
            return;
        }

        // Update Firebase Auth user email (if necessary)
        if (!newEmail.equals(email)) {
            currentUser.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            email = newEmail;
                        } else {
                            Toast.makeText(Profile.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Update SQLite user data
        boolean updated = databaseHelper.updateUserData(username, newEmail);

        // Save profile image
        Bitmap bitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
        String encodedImage = encodeImage(bitmap);
        boolean imageUpdated = databaseHelper.updateProfileImage(email, encodedImage);

        if (updated && imageUpdated) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            // Update local username variable
            username = newUsername;
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle logout action
    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
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

    // Handle result from image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);

                // Save profile image to the database
                String encodedImage = encodeImage(bitmap);
                boolean imageUpdated = databaseHelper.updateProfileImage(email, encodedImage);
                if (!imageUpdated) {
                    Toast.makeText(this, "Failed to save profile image", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Error loading image: " + e.getMessage());
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private Bitmap decodeImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
