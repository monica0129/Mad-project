package com.example.ezbooking;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Hotels4 extends AppCompatActivity {

    public static final String CHANNEL_ID = "HOTEL_BOOKING_NOTIFICATION_CHANNEL"; // Changed to public and static
    private EditText etGuestName;
    private EditText etGuestAge;
    private Spinner spinnerRoomType;
    private EditText etNumberOfPersons;
    private EditText etPhoneNumber;
    private Button bookNowButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels4);

        etGuestName = findViewById(R.id.etGuestName);
        etGuestAge = findViewById(R.id.etGuestAge);
        spinnerRoomType = findViewById(R.id.spinnerRoomType);
        etNumberOfPersons = findViewById(R.id.etNumberOfPersons);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        bookNowButton = findViewById(R.id.bookNowButton);
        databaseHelper = new DatabaseHelper(this);

        // Set up the spinner with room types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.room_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoomType.setAdapter(adapter);

        // Set click listener for the book button
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookNow();
            }
        });

        // Create notification channel if needed (API 26+)
        createNotificationChannel();
    }

    private void bookNow() {
        String guestName = etGuestName.getText().toString();
        String guestAge = etGuestAge.getText().toString();
        String roomType = spinnerRoomType.getSelectedItem().toString();
        String numberOfPersons = etNumberOfPersons.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if (guestName.isEmpty() || guestAge.isEmpty() || numberOfPersons.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(guestAge);
        int persons = Integer.parseInt(numberOfPersons);

        Booking booking = new Booking(guestName, age, roomType, persons, phoneNumber);
        databaseHelper.addBooking(booking);

        Toast.makeText(this, "Booked successfully!", Toast.LENGTH_SHORT).show();

        // Send booking notification
        sendBookingNotification(guestName);

        // Navigate back to the home page
        Intent intent = new Intent(Hotels4.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Hotel Booking Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for hotel booking confirmation");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendBookingNotification(String guestName) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("EVENT_NAME", "Booking Successful for " + guestName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();

        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }
}
