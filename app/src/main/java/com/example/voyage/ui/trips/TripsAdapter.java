package com.example.voyage.ui.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voyage.R;
import com.example.voyage.data.models.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ItemViewHolder> {

    private static final String LOG_TAG = TripsAdapter.class.getSimpleName();

    final private ItemClickListener itemClickListener;

    private Context context;
    private List<Trip> trips;

    TripsAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
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

    public interface ItemClickListener {
        void onItemClickListener(int tripId, int pickPoint, int dropPoint, int busId);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView priceTextView;
        private TextView timeTextView;

        ItemViewHolder(View view) {
            super(view);

            timeTextView = view.findViewById(R.id.time_text_view);
            priceTextView = view.findViewById(R.id.seat_price);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Trip trip = trips.get(getAdapterPosition());
            int tripId = trip.getId();
            int pickPointId = trip.getOriginStage().getId();
            int dropPointId = trip.getDestinationStage().getId();
            int busId = trip.getBusId();

            itemClickListener.onItemClickListener(tripId, pickPointId, dropPointId, busId);
        }
    }
}
