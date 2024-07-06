package com.example.ezbooking;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Bookings.db";

    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GUEST_NAME = "guest_name";
    private static final String COLUMN_TIME_SLOT = "time_slot";
    private static final String COLUMN_NUMBER_OF_TICKETS = "number_of_tickets";
    private static final String COLUMN_MOBILE_NUMBER = "mobile_number";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS +
                "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," +
                COLUMN_GUEST_NAME + " TEXT," +
                COLUMN_TIME_SLOT + " TEXT," +
                COLUMN_NUMBER_OF_TICKETS + " INTEGER," +
                COLUMN_MOBILE_NUMBER + " TEXT" +
                ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    public long addBooking(Booking2 booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GUEST_NAME, booking.getGuestName());
        values.put(COLUMN_TIME_SLOT, booking.getTimeSlot());
        values.put(COLUMN_NUMBER_OF_TICKETS, booking.getNumberOfTickets());
        values.put(COLUMN_MOBILE_NUMBER, booking.getMobileNumber());

        // Insert row
        long id = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public List<Booking2> getAllBookings() {
        List<Booking2> bookingList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_BOOKINGS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Booking2 booking = new Booking2();
                booking.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                booking.setGuestName(cursor.getString(cursor.getColumnIndex(COLUMN_GUEST_NAME)));
                booking.setTimeSlot(cursor.getString(cursor.getColumnIndex(COLUMN_TIME_SLOT)));
                booking.setNumberOfTickets(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER_OF_TICKETS)));
                booking.setMobileNumber(cursor.getString(cursor.getColumnIndex(COLUMN_MOBILE_NUMBER)));

                bookingList.add(booking);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return bookingList;
    }
}