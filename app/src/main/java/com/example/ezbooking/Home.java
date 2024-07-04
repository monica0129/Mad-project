package com.example.ezbooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recentBookingsRecyclerView;
    private RecentBookingsAdapter adapter;
    private List<Booking> bookingList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView movieImageView = findViewById(R.id.movie1);
        ImageView hotelImageView = findViewById(R.id.hotel1);

        movieImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Movies.class);
            startActivity(intent);
            finish();
            Toast.makeText(Home.this, "Movie clicked!", Toast.LENGTH_SHORT).show();
        });

        hotelImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Hotels.class);
            startActivity(intent);
            finish();
            Toast.makeText(Home.this, "Hotel clicked!", Toast.LENGTH_SHORT).show();
        });

        recentBookingsRecyclerView = findViewById(R.id.recent_bookings_recycler_view);
        recentBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Load booking data from the database
        loadBookingData();

        // Handle back button press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Navigate back to the dashboard activity
                Intent intent = new Intent(Home.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadBookingData() {
        bookingList = databaseHelper.getAllBookings();
        adapter = new RecentBookingsAdapter(bookingList);
        recentBookingsRecyclerView.setAdapter(adapter);
    }
}
