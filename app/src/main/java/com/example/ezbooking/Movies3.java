package com.example.ezbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Movies3 extends AppCompatActivity {

    private EditText etGuestName;
    private Spinner spinnerTimeSlot;
    private EditText etNumberOfTickets;
    private EditText etPhoneNumber;
    private Button bookNowButton;
    private DBHelper DBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies3);

        etGuestName = findViewById(R.id.etGuestName);
        spinnerTimeSlot = findViewById(R.id.spinnerTimeSlot);
        etNumberOfTickets = findViewById(R.id.etNumberOfTickets);
        etPhoneNumber = findViewById(R.id.etMobileNumber);
        bookNowButton = findViewById(R.id.bookNowButton);
        DBHelper = new DBHelper(this);

        // Set up the spinner with room types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_slots, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimeSlot.setAdapter(adapter);

        // Set click listener for the book button
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookNow();
            }
        });
    }

    private void bookNow() {
        String guestName = etGuestName.getText().toString();
        String timeSlot = spinnerTimeSlot.getSelectedItem().toString();
        String numberOfTickets = etNumberOfTickets.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if (guestName.isEmpty()  || timeSlot.isEmpty() || numberOfTickets.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int tickets = Integer.parseInt(numberOfTickets);

        Booking2 booking = new Booking2(guestName, timeSlot, tickets, phoneNumber);
        DBHelper.addBooking(booking);

        Toast.makeText(this, "Booked successfully!", Toast.LENGTH_SHORT).show();

        // Navigate back to the home page
        Intent intent = new Intent(Movies3.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
