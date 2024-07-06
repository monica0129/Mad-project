package com.example.ezbooking;

public class Booking {

    private int id;
    private String guestName;
    private int guestAge;
    private String roomType;
    private int numberOfPersons;
    private String phoneNumber;

    public Booking() {
    }

    public Booking(String guestName, int guestAge, String roomType, int numberOfPersons, String phoneNumber) {
        this.guestName = guestName;
        this.guestAge = guestAge;
        this.roomType = roomType;
        this.numberOfPersons = numberOfPersons;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public int getGuestAge() {
        return guestAge;
    }

    public void setGuestAge(int guestAge) {
        this.guestAge = guestAge;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}