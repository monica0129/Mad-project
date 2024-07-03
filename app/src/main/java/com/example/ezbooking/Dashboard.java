package com.example.ezbooking;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Dashboard extends AppCompatActivity {

    CardView chome,csettings,cmovies,chotels,cprofile,ccontact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        chome = findViewById(R.id.card_home);
        csettings = findViewById(R.id.card_settings);
        cmovies = findViewById(R.id.card_movies);
        chotels = findViewById(R.id.card_hotels);
        cprofile = findViewById(R.id.card_profile);
        ccontact = findViewById(R.id.card_contact);


        chome.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Home.class);
            startActivity(intent);
            finish();
        });

        csettings.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this,Settings.class);
            startActivity(intent);
            finish();
        });

        cmovies.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this,Movies.class);
            startActivity(intent);
            finish();
        });

        chotels.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this,Hotels.class);
            startActivity(intent);
            finish();
        });

        cprofile.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this,Profile.class);
            startActivity(intent);
            finish();
        });

        ccontact.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this,Contact.class);
            startActivity(intent);
            finish();
        });


        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to the dashboard activity
                Intent intent = new Intent(Dashboard.this,LoginPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}


