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
            itemViewHolder.bind(seat);
        }

    }

    @Override
    public int getItemCount() {
        if (seatRowCollection == null) {
            return 0;
        }
        return seatRowCollection.getRowSeats().size();
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

        public void bind(Seat seat) {
            int seatModulus = seat.getId() % 4;

            ImageView currentImage = null;
            switch (seatModulus) {
                case 0:
                    currentImage = columnDImageView;
                    break;
                case 1:
                    currentImage = columnAImageView;
                    break;
                case 2:
                    currentImage = columnBImageView;
                    break;
                case 3:
                    currentImage = columnCImageView;
                    break;
            }

            setSeatDrawable(currentImage, seat, getAdapterPosition());
        }

        private void setSeatDrawable(ImageView imageView, Seat seat, int position) {
            if (seat.getAvailable() == 1) {
                seat.setSeatAvailable(true);
                imageView.setImageResource(R.drawable.available_img);
            } else {
                seat.setSeatAvailable(false);
                imageView.setImageResource(R.drawable.booked_img);
            }

            imageView.setOnClickListener(v -> {
                Log.d(LOG_TAG, "Selected seat id: " + seat.getId() +
                        "\nSeat is available: " + seat.isAvailable());
                seat.setSeatAvailable(!seat.isAvailable());

                if (seat.isAvailable()) {
                    imageView.setImageResource(R.drawable.available_img);
                } else {
                    imageView.setImageResource(R.drawable.your_seat_img);
                }
            });
        }
    }

    void setSeatRowCollection(SeatRowCollection rowCollection) {
        this.seatRowCollection = rowCollection;
        notifyDataSetChanged();
    }


    public interface ItemClickListener {
        void onItemClickListener(int seatId);
    }
}
