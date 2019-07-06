package com.example.voyage.ui.bookings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voyage.R;
import com.example.voyage.data.models.RecentBookings;

import java.util.ArrayList;

public class RecentBookingAdapter extends RecyclerView.Adapter<RecentBookingAdapter.ItemViewHolder> {
    private ArrayList<RecentBookings> myData;
    private Context context;

    public RecentBookingAdapter(ArrayList<RecentBookings> myDataset, Context context) {
        this.myData = myDataset;
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
        RecentBookings data = myData.get(i);

        itemViewHolder.destination.setText(data.getDestination());
        itemViewHolder.origin.setText(data.getOrigin());
        itemViewHolder.booked.setText(data.getDateBooked());
        itemViewHolder.paid.setText(data.getSeatPrice());
    }

    @Override
    public int getItemCount() {
        if (myData == null) {
            return 0;
        }
        return myData.size();
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
