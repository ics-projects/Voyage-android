package com.example.voyage.ui.trips;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.voyage.R;
import com.example.voyage.data.models.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class AvailableBusAdapter extends RecyclerView.Adapter<AvailableBusAdapter.ItemViewHolder> {

    private static final String LOG_TAG = AvailableBusAdapter.class.getSimpleName();
    private Context context;
    private List<Trip> trips;

    AvailableBusAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdForListItem = R.layout.trip_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder viewHolder, int position) {
        Log.d(LOG_TAG, "Adapter length: " + trips.get(0).getArrivalTime());
        Trip trip = trips.get(position);

        String departureTime = trip.getDepartureTime();
        String arrivalTime = trip.getArrivalTime();

        Date formattedDepartureTime;
        Date formattedArrivalTime;
        String durationString = "";

        SimpleDateFormat originalTimeFormat =
                new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
        SimpleDateFormat screenTimeFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        try {
            formattedDepartureTime = originalTimeFormat.parse(departureTime);
            formattedArrivalTime = originalTimeFormat.parse(arrivalTime);

            String finalFormatDeparture = screenTimeFormat.format(formattedDepartureTime);
            String finalFormatArrival = screenTimeFormat.format(formattedArrivalTime);

            durationString = finalFormatDeparture.concat(" - ").concat(finalFormatArrival);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String price = String.valueOf(trip.getFirstClassPrice());

        viewHolder.timeTextView.setText(durationString);
        viewHolder.priceTextView.setText(price);
    }

    @Override
    public int getItemCount() {
        if (trips == null) {
            return 0;
        }
        return trips.size();
    }

    void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView priceTextView;
        private TextView timeTextView;

        ItemViewHolder(View view) {
            super(view);
            timeTextView = view.findViewById(R.id.time_text_view);
            priceTextView = view.findViewById(R.id.seat_price);
        }
    }
}
