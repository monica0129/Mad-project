package com.example.ezbooking;
public class Booking2 {
    private int id;
    private String guestName;
    private String timeSlot;
    private int numberOfTickets;
    private String mobileNumber;

    // Default constructor
    public Booking2() {
    }

    // Constructor with parameters
    public Booking2(String guestName, String timeSlot, int numberOfTickets, String mobileNumber) {
        this.guestName = guestName;
        this.timeSlot = timeSlot;
        this.numberOfTickets = numberOfTickets;
        this.mobileNumber = mobileNumber;
    }

    // Getters and setters
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


    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
