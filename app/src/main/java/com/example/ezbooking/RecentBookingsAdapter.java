package com.example.ezbooking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecentBookingsAdapter extends RecyclerView.Adapter<RecentBookingsAdapter.ViewHolder> {

    private List<Booking> bookingList;

    public RecentBookingsAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.guestNameTextView.setText(booking.getGuestName());
        holder.roomTypeTextView.setText(booking.getRoomType());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView guestNameTextView;
        public TextView roomTypeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            guestNameTextView = itemView.findViewById(R.id.guest_name);
            roomTypeTextView = itemView.findViewById(R.id.room_type);
        }
    }
}
