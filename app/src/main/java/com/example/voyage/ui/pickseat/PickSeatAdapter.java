package com.example.voyage.ui.pickseat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voyage.R;
import com.example.voyage.data.models.Seat;

import java.util.List;

class PickSeatAdapter extends RecyclerView.Adapter<PickSeatAdapter.ItemViewHolder> {

    private static final String LOG_TAG = PickSeatAdapter.class.getSimpleName();

    private final Context context;
    private SeatRowCollection seatRowCollection;

    PickSeatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PickSeatAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutIdForListItem = R.layout.bus_seat;
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(layoutIdForListItem, viewGroup, false);
        return new PickSeatAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PickSeatAdapter.ItemViewHolder itemViewHolder, int position) {
        String rowNumber = String.valueOf(position + 1);
        itemViewHolder.rowNumberTextView.setText(rowNumber);

        List<Seat> seatRow = seatRowCollection.getRowSeats().get(position);

        for (Seat seat : seatRow) {
            Log.d(LOG_TAG, "Seat id: " + seat.getId());
            int seatModulus = seat.getId() % 4;
            int seatAvailable = seat.getAvailable();
            setSeatDrawable(itemViewHolder, seatModulus, seatAvailable);
        }

    }

    @Override
    public int getItemCount() {
        if (seatRowCollection == null) {
            return 0;
        }
        return seatRowCollection.getRowSeats().size();
    }

    void setSeatRowCollection(SeatRowCollection rowCollection) {
        this.seatRowCollection = rowCollection;
        notifyDataSetChanged();
    }

    private void setSeatDrawable(ItemViewHolder itemViewHolder, int seatModulus, int seatAvailable) {
        Log.d(LOG_TAG, "Seat modulus: " + seatModulus);
        switch (seatModulus) {
            case 0:
                // seat is at the end of the row i.e column d
                if (seatAvailable == 1) {
                    itemViewHolder.columnDImageView.setImageResource(R.drawable.available_img);
                    itemViewHolder.columnDImageView.setContentDescription(
                            context.getString(R.string.available_seat_drawable));
                } else {
                    itemViewHolder.columnDImageView.setImageResource(R.drawable.booked_img);
                    itemViewHolder.columnDImageView.setContentDescription(
                            context.getString(R.string.booked_seat_drawable));
                }
                break;
            case 1:
                // seat is at column a
                if (seatAvailable == 1) {
                    itemViewHolder.columnAImageView.setImageResource(R.drawable.available_img);
                    itemViewHolder.columnAImageView.setContentDescription(
                            context.getString(R.string.available_seat_drawable));
                } else {
                    itemViewHolder.columnAImageView.setImageResource(R.drawable.booked_img);
                    itemViewHolder.columnAImageView.setContentDescription(
                            context.getString(R.string.booked_seat_drawable));
                }
                break;
            case 2:
                // seat is at column b
                if (seatAvailable == 1) {
                    itemViewHolder.columnBImageView.setImageResource(R.drawable.available_img);
                    itemViewHolder.columnBImageView.setContentDescription(
                            context.getString(R.string.available_seat_drawable));
                } else {
                    itemViewHolder.columnBImageView.setImageResource(R.drawable.booked_img);
                    itemViewHolder.columnBImageView.setContentDescription(
                            context.getString(R.string.booked_seat_drawable));
                }
                break;
            case 3:
                // seat is at column c
                if (seatAvailable == 1) {
                    itemViewHolder.columnCImageView.setImageResource(R.drawable.available_img);
                    itemViewHolder.columnCImageView.setContentDescription(
                            context.getString(R.string.available_seat_drawable));
                } else {
                    itemViewHolder.columnCImageView.setImageResource(R.drawable.booked_img);
                    itemViewHolder.columnCImageView.setContentDescription(
                            context.getString(R.string.booked_seat_drawable));
                }
                break;
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView rowNumberTextView;

        ImageView columnAImageView;
        ImageView columnBImageView;
        ImageView columnCImageView;
        ImageView columnDImageView;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rowNumberTextView = itemView.findViewById(R.id.row_number_tv);
            columnAImageView = itemView.findViewById(R.id.column_A_iv);
            columnBImageView = itemView.findViewById(R.id.column_B_iv);
            columnCImageView = itemView.findViewById(R.id.column_C_iv);
            columnDImageView = itemView.findViewById(R.id.column_D_iv);
        }
    }
}
