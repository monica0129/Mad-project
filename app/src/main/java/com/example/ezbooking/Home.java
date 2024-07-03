package com.example.ezbooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recentBookingsRecyclerView;
    private RecentBookingsAdapter adapter;
    private List<Booking> bookingList;

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

        // Initialize booking list and adapter
        bookingList = new ArrayList<>();
        adapter = new RecentBookingsAdapter(bookingList);

        recentBookingsRecyclerView.setAdapter(adapter);

        // Load booking data
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
        // Add sample booking data (replace with real data)
        bookingList.add(new Booking("Hotel 1", "Location 1"));
        bookingList.add(new Booking("Hotel 2", "Location 2"));
        bookingList.add(new Booking("Hotel 3", "Location 3"));

        // Notify adapter of data changes
        adapter.notifyDataSetChanged();
    }
}
