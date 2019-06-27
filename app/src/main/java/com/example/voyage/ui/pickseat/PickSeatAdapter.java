package com.example.voyage.ui.pickseat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.voyage.R;
import com.example.voyage.data.models.Seat;

import java.util.List;

class PickSeatAdapter extends RecyclerView.Adapter<PickSeatAdapter.ItemViewHolder> {

    private final Context context;
    private List<Seat> seats;

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
        Seat seat = seats.get(position);

        String rowNumber = String.valueOf(position + 1);
        itemViewHolder.rowNumberTextView.setText(rowNumber);

        int seatId = seat.getId();
        int seatAvailable = seat.getAvailable();
        int seatModulus = seatId % 4;

        switch (seatModulus) {
            case 0:
                // means seat is at the end of the row i.e column d
                if (seatAvailable == 1) {
                    itemViewHolder.columnDImageView.setImageResource(R.drawable.available_img);
                } else {
                    itemViewHolder.columnDImageView.setImageResource(R.drawable.booked_img);
                }
                break;
            case 1:
                // means seat is at column a
                if (seatAvailable == 1) {
                    itemViewHolder.columnAImageView.setImageResource(R.drawable.available_img);
                } else {
                    itemViewHolder.columnAImageView.setImageResource(R.drawable.booked_img);
                }
                break;
            case 2:
                // means seat is at column b
                if (seatAvailable == 1) {
                    itemViewHolder.columnBImageView.setImageResource(R.drawable.available_img);
                } else {
                    itemViewHolder.columnBImageView.setImageResource(R.drawable.booked_img);
                }
                break;
            case 3:
                // means seat is at column c
                if (seatAvailable == 1) {
                    itemViewHolder.columnCImageView.setImageResource(R.drawable.available_img);
                } else {
                    itemViewHolder.columnCImageView.setImageResource(R.drawable.booked_img);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (seats == null) {
            return 0;
        }
        return seats.size();
    }

    void setSeats(List<Seat> seats) {
        this.seats = seats;
        notifyDataSetChanged();
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
