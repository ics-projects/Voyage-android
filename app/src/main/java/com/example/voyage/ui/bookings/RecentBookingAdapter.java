package com.example.voyage.ui.bookings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voyage.R;
import com.example.voyage.data.models.Booking;

import java.util.List;

public class RecentBookingAdapter extends RecyclerView.Adapter<RecentBookingAdapter.ItemViewHolder> {
    private List<Booking> bookings;
    private Context context;

    public RecentBookingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layout = R.layout.recent_booking_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Booking data = bookings.get(i);

        itemViewHolder.destination.setText(data.getDestination());
        itemViewHolder.origin.setText(data.getOrigin());
        itemViewHolder.booked.setText(data.getDateBooked());
        itemViewHolder.paid.setText(data.getAmount());
    }

    @Override
    public int getItemCount() {
        if (bookings == null) {
            return 0;
        }
        return bookings.size();
    }

    public void setData(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView destination;
        TextView origin;
        TextView booked;
        TextView paid;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            destination = itemView.findViewById(R.id.destination);
            origin = itemView.findViewById(R.id.origin);
            booked = itemView.findViewById(R.id.booked);
            paid = itemView.findViewById(R.id.paid);
        }
    }
}
