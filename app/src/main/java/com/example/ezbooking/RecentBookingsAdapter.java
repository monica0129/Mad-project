package com.example.ezbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentBookingsAdapter extends RecyclerView.Adapter<RecentBookingsAdapter.BookingViewHolder> {

    private List<Booking> bookingList;

    public RecentBookingsAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.nameTextView.setText(booking.getName());
        holder.locationTextView.setText(booking.getLocation());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView locationTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.booking_name);
            locationTextView = itemView.findViewById(R.id.booking_location);
        }
    }
}