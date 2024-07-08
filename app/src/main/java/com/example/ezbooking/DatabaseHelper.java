package com.example.ezbooking;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bookings.db";
    private static final int DATABASE_VERSION = 4; // Incremented the database version

    // Table and column names for booking details
    private static final String TABLE_BOOKINGS = "bookings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GUEST_NAME = "guest_name";
    private static final String COLUMN_GUEST_AGE = "guest_age";
    private static final String COLUMN_ROOM_TYPE = "room_type";
    private static final String COLUMN_NUMBER_OF_PERSONS = "number_of_persons";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";

    // Table and column names for user data
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image"; // New column for profile image

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GUEST_NAME + " TEXT,"
                + COLUMN_GUEST_AGE + " INTEGER,"
                + COLUMN_ROOM_TYPE + " TEXT,"
                + COLUMN_NUMBER_OF_PERSONS + " INTEGER,"
                + COLUMN_PHONE_NUMBER + " TEXT" + ")";
        db.execSQL(CREATE_BOOKINGS_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_PROFILE_IMAGE + " TEXT" + ")"; // Profile image column
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Example upgrade scenarios from oldVersion to newVersion
        switch (oldVersion) {
            case 1:
                // Upgrade logic from version 1 to version 2
                String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                        + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_USERNAME + " TEXT,"
                        + COLUMN_EMAIL + " TEXT,"
                        + COLUMN_PASSWORD + " TEXT" + ")";
                db.execSQL(CREATE_USERS_TABLE);
                // Add any new columns or tables for version 2 if needed
                // No break here, so it falls through to the next case

            case 2:
                String ADD_NEW_COLUMN = "ALTER TABLE " + TABLE_USERS +
                        " ADD COLUMN " + COLUMN_PROFILE_IMAGE + " TEXT"; // Add profile image column
                db.execSQL(ADD_NEW_COLUMN);

            case 3:
                // Additional upgrade logic for future versions
                break;

            default:
                break;
        }
    }

    // Method to add a new user
    public void addUser(String username, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    // Method to get user data
    public UserData getUserData(String userEmail) {
        UserData userData = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME, COLUMN_EMAIL},
                    COLUMN_EMAIL + "=?", new String[]{userEmail},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));

                userData = new UserData(username, email);
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving user data", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return userData;
    }

    // Method to update user data
    public boolean updateUserData(String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();

        return rowsAffected > 0;
    }

    // Method to update profile image
    public boolean updateProfileImage(String email, String encodedImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFILE_IMAGE, encodedImage);

        int rowsAffected = db.update(TABLE_USERS, values, COLUMN_EMAIL + "=?", new String[]{email});
        db.close();

        return rowsAffected > 0;
    }

    // Method to get profile image
    public String getProfileImage(String email) {
        String encodedImage = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_USERS, new String[]{COLUMN_PROFILE_IMAGE},
                    COLUMN_EMAIL + "=?", new String[]{email},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") String image = cursor.getString(cursor.getColumnIndex(COLUMN_PROFILE_IMAGE));
                encodedImage = image;
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving profile image", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return encodedImage;
    }

    // Method to add a new booking
    public void addBooking(Booking booking) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GUEST_NAME, booking.getGuestName());
        values.put(COLUMN_GUEST_AGE, booking.getGuestAge());
        values.put(COLUMN_ROOM_TYPE, booking.getRoomType());
        values.put(COLUMN_NUMBER_OF_PERSONS, booking.getNumberOfPersons());
        values.put(COLUMN_PHONE_NUMBER, booking.getPhoneNumber());

        db.insert(TABLE_BOOKINGS, null, values);
        db.close();
    }

    // Method to get all bookings
    @SuppressLint("Range")
    public List<Booking> getAllBookings() {
        List<Booking> bookingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Booking booking = new Booking();
                    booking.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                    booking.setGuestName(cursor.getString(cursor.getColumnIndex(COLUMN_GUEST_NAME)));
                    booking.setGuestAge(cursor.getInt(cursor.getColumnIndex(COLUMN_GUEST_AGE)));
                    booking.setRoomType(cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_TYPE)));
                    booking.setNumberOfPersons(cursor.getInt(cursor.getColumnIndex(COLUMN_NUMBER_OF_PERSONS)));
                    booking.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER)));
                    bookingList.add(booking);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error while getting bookings from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return bookingList;
    }
}
